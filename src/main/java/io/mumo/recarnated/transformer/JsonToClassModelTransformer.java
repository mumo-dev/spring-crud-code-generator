package io.mumo.recarnated.transformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mumo.recarnated.exceptions.InvalidJsonException;
import io.mumo.recarnated.transformer.field.FieldModel;
import io.mumo.recarnated.transformer.field.FieldModelCreator;
import io.mumo.recarnated.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonToClassModelTransformer {

    private final ObjectMapper objectMapper;
    private final ClassTypeConverter classTypeConverter;
    @Setter
    private  FieldModelCreator fieldModelCreator;

    public ClassModelMetadata getClassMetaData(String json) {
        Map<String, Object> map = convertJsonToMap(json);
        Map<String, Object> metadataMap = (Map<String, Object>) map.getOrDefault("_metadata", new HashMap<>());
        ClassModelMetadata metaData = objectMapper.convertValue(metadataMap, ClassModelMetadata.class);
        return metaData;
    }
    public List<ClassModel> transform(String json) {
        Map<String, Object> map = convertJsonToMap(json);
        Set<String> imports = new HashSet<>();
        List<FieldModel> fieldModels = new ArrayList<>();
        List<ClassModel> classModels = new ArrayList<>();

        Map<String, Object> metadataMap = (Map<String, Object>) map.getOrDefault("_metadata", new HashMap<>());
        ClassModelMetadata metaData = objectMapper.convertValue(metadataMap, ClassModelMetadata.class);
        for (String key : map.keySet()) {
            if (key.startsWith("_")) {
                continue;
            }
            Object value = map.get(key);
            if (value instanceof String) {
                //add to fieldList
                ClassTypeConverter.MapperResult mapper = classTypeConverter.convert((String) value);
                fieldModels.add(fieldModelCreator.createFieldModel(key, mapper.typeName, metaData));
                if (mapper.importClass != null) {
                    imports.add(mapper.importClass);
                }
            } else {
                // we need to create a classModel for this object
                Map<String, Object> fieldHashMap = (Map<String, Object>) value;
                List<ClassModel> classModelList = transform(key, metaData.getPackageName(), fieldHashMap);

                // using key  get class using className and packageName
                Map<String, Object> fieldMetadataMap = (Map<String, Object>) fieldHashMap.getOrDefault("_metadata", new HashMap<>());
                ClassModelMetadata fieldMetaData = objectMapper.convertValue(fieldMetadataMap, ClassModelMetadata.class);
                if(fieldMetaData.getPackageName() == null) {
                    fieldMetaData.setPackageName(metaData.getPackageName());
                }

                String className = getClassName(fieldMetaData, key);
                String packageName = getPackageName(fieldMetaData);
                fieldModels.add(fieldModelCreator.createFieldModel(key, className, metaData));
                // add class import if packageNames are different
                if (!packageName.equals(metaData.getPackageName())) {
                    imports.add(packageName + "." + className);
                }
                classModels.addAll(classModelList);
            }
        }

        ClassModel baseModel = createClassModel(fieldModels, new ArrayList<>(imports),
                new ArrayList<>(), null, metaData);

        classModels.add(baseModel);
        return classModels;
    }

    public List<ClassModel> transform(String fieldName, String packageName, Map<String, Object> map) {
        Set<String> imports = new HashSet<>();
        List<FieldModel> fieldModels = new ArrayList<>();
        List<ClassModel> classModelList = new ArrayList<>();

        Map<String, Object> metadataMap = (Map<String, Object>) map.getOrDefault("_metadata", new HashMap<>());
        ClassModelMetadata metaData = objectMapper.convertValue(metadataMap, ClassModelMetadata.class);
        if(metaData.getPackageName() == null) {
            metaData.setPackageName(packageName);
        }else {
            packageName = metaData.getPackageName();
        }

        for (String key : map.keySet()) {
            if (key.startsWith("_")) {
                continue;
            }

            Object value = map.get(key);
            if (value instanceof String) {
                //add to fieldList
                ClassTypeConverter.MapperResult mapper = classTypeConverter.convert((String) value);
               // log.debug(value + "::" + mapper);
                fieldModels.add(fieldModelCreator.createFieldModel(key, mapper.typeName, metaData));
                if (mapper.importClass != null) {
                    imports.add(mapper.importClass);
                }
            } else {
                // TODO: populate metadata
                classModelList.addAll(transform(key, packageName, (Map<String, Object>) value));

                // we need to create a classModel for this object
                Map<String, Object> fieldHashMap = (Map<String, Object>) value;

                // using key
                // get class using className and packageName
                Map<String, Object> fieldMetadataMap = (Map<String, Object>) fieldHashMap.getOrDefault("_metadata", new HashMap<>());
                ClassModelMetadata fieldMetaData = objectMapper.convertValue(fieldMetadataMap, ClassModelMetadata.class);
                if(fieldMetaData.getPackageName() == null) {
                    fieldMetaData.setPackageName(metaData.getPackageName());
                }
                String className = getClassName(fieldMetaData, key);
                String fieldPackageName = getPackageName(fieldMetaData);
                fieldModels.add(fieldModelCreator.createFieldModel(key, className, metaData));
                // add class import if packageNames are different
                if (!fieldPackageName.equals(metaData.getPackageName())) {
                    imports.add(fieldPackageName + "." + className);
                }

            }
        }

        ClassModel baseModel = createClassModel(fieldModels, new ArrayList<>(imports),
                new ArrayList<>(), fieldName, metaData);

        classModelList.add(baseModel);
        return classModelList;
    }


    private ClassModel createClassModel(
            List<FieldModel> fields,
            List<String> imports,
            List<String> annotations,
            String fieldName,
            ClassModelMetadata metadata) {

        ClassModel classModel = new ClassModel();
        classModel.setClassName(getClassName(metadata, fieldName));
        classModel.setPackageName(getPackageName(metadata));
        classModel.setFields(fields);
        classModel.setImports(imports);
        classModel.setAnnotations(annotations);
        return classModel;
    }

    private String getClassName(ClassModelMetadata metadata, String fieldName) {
        String className;
        if (metadata == null || metadata.getClassName() == null) {
            // get the singular form if plural e.g images==> image, categories=>category and turn it to titleCase
            className = StringUtil.titleCase(StringUtil.singular(fieldName));
        } else {
            className = metadata.getClassName();
        }
        return className;
    }

    private String getPackageName(ClassModelMetadata metadata) {
        String packageName = "main";
        if (metadata != null && metadata.getPackageName() != null) {
            packageName = metadata.getPackageName();
        }
        return packageName.toLowerCase();
    }


    public Map<String, Object> convertJsonToMap(String json) {
        Map<String, Object> map;
        try {
            map = objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonException(e.getMessage());
        }
        return map;
    }


}

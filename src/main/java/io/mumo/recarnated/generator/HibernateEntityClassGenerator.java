package io.mumo.recarnated.generator;

import io.mumo.recarnated.freemarker.TemplateManager;
import io.mumo.recarnated.transformer.ClassModel;
import io.mumo.recarnated.transformer.ClassModelMetadata;
import io.mumo.recarnated.transformer.JsonToClassModelTransformer;
import io.mumo.recarnated.transformer.field.FieldModelCreator;
import io.mumo.recarnated.transformer.field.HibernateEntityFieldModelCreator;
import io.mumo.recarnated.util.StringUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HibernateEntityClassGenerator {
    private final JsonToClassModelTransformer jsonToClassModelTransformer;
    private final TemplateManager templateManager;

    private FieldModelCreator fieldModelCreator;


    public HibernateEntityClassGenerator(
            @Qualifier("hibernateEntityFieldModelCreator") HibernateEntityFieldModelCreator fieldModelCreator,
            JsonToClassModelTransformer jsonToClassModelTransformer,
            TemplateManager templateManager) {
        this.jsonToClassModelTransformer = jsonToClassModelTransformer;
        this.templateManager = templateManager;
        this.fieldModelCreator = fieldModelCreator;
    }

    public String generate(String json) {
        jsonToClassModelTransformer.setFieldModelCreator(fieldModelCreator);
        List<ClassModel> classModels = jsonToClassModelTransformer.transform(json);
        StringBuilder output = new StringBuilder("");

        for (ClassModel classModel : classModels) {
            ClassModelMetadata metadata = jsonToClassModelTransformer.getClassMetaData(json);
            String tableName = getTableName(classModel, metadata);
            classModel.addAnnotation("@Entity");
            classModel.addAnnotation("@Table(name= \"" + tableName + "\")" );
            classModel.addImport("javax.persistence.*");
            String generatedClass = templateManager.processTemplate("class", classModel);
            output.append("\n\n");
            output.append("//------------- BEGIN CLASS ------------\n");
            output.append(generatedClass);
            output.append("//------------- END CLASS ------------\n");
        }
        output.append("\n\n");
        return output.toString();
    }

    private String getTableName(ClassModel classModel, ClassModelMetadata metadata) {
        if (metadata.getTableName() == null) {
            return StringUtil.snakeCase(classModel.getClassName());
        }
        return  metadata.getTableName();
    }

}

package io.mumo.recarnated.transformer.field;

import io.mumo.recarnated.transformer.ClassModelMetadata;
import io.mumo.recarnated.util.StringUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Qualifier("hibernateEntityFieldModelCreator")
@Component
public class HibernateEntityFieldModelCreator implements FieldModelCreator {
    @Override
    public FieldModel createFieldModel(String fieldName, String classType, ClassModelMetadata metadata) {
        FieldModel field = new FieldModel();
        field.setName(StringUtil.camelCase(fieldName));
        List<String> annotations =  new ArrayList<>();
        if(isIdField(fieldName, metadata.getIdField())) {
            annotations.addAll(getIdAnnotations());
        }
        String columnAnnotation = "@Column(name =\"" + fieldName + "\")";
        annotations.add(columnAnnotation);
        field.setType(classType);
        field.setAnnotations(annotations);
        return field;
    }

    private List<String> getIdAnnotations() {
       return List.of(
               "@Id",
               "@GeneratedValue(strategy = GenerationType.IDENTITY)"
       );
    }

    private boolean isIdField(String fieldName, String idField) {
        if(idField == null) return false;
        return StringUtil.snakeCase(fieldName).equals(StringUtil.snakeCase(idField));
    }
}

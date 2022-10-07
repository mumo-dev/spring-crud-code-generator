package io.mumo.recarnated.transformer.field;

import io.mumo.recarnated.transformer.ClassModelMetadata;
import io.mumo.recarnated.util.StringUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Qualifier("pojoFieldModelCreator")
@Component
public class PojoFieldModelCreator implements FieldModelCreator {

    @Override
    public FieldModel createFieldModel(String fieldName, String classType, ClassModelMetadata metadata) {
        FieldModel field = new FieldModel();
        field.setName(StringUtil.camelCase(fieldName));
        field.setType(classType);
        field.setAnnotations(new ArrayList<>());
        return field;
    }
}

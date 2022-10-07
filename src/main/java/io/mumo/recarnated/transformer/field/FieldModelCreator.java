package io.mumo.recarnated.transformer.field;

import io.mumo.recarnated.transformer.ClassModelMetadata;

public interface FieldModelCreator {
   FieldModel createFieldModel(String fieldName, String classType, ClassModelMetadata metadata);
}

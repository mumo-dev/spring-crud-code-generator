package io.mumo.recarnated.transformer;

import io.mumo.recarnated.transformer.field.FieldModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassModel {
    private String packageName;
    private String className;
    private List<String> imports = new ArrayList<>();
    private List<String> annotations = new ArrayList<>();
    private List<FieldModel> fields = new ArrayList<>();

    public void addImport(String importClass) {
        imports.add(importClass);
    }

    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }
    public void addFieldModel(FieldModel fieldModel) {
        fields.add(fieldModel);
    }
}

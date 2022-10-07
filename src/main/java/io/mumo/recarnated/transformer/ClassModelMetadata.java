package io.mumo.recarnated.transformer;

import lombok.Data;

@Data
public class ClassModelMetadata {
    private String className;
    private String packageName;
    private String tableName;
    private String idField;
}

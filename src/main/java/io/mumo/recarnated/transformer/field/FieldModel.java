package io.mumo.recarnated.transformer.field;

import lombok.Data;

import java.util.List;

@Data
public class FieldModel {
    private String name;
    private String type;
    private List<String> annotations;
}

package io.mumo.recarnated;

import lombok.Data;

@Data
public class CrudRequest {
    private String fields;
    private CrudMetaData metadata;
}

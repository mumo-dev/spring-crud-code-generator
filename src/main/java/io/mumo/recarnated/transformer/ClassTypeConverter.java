package io.mumo.recarnated.transformer;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ClassTypeConverter {

    /**
     * custom types
     * --------------
     * string
     * integer
     * long
     * float
     * boolean
     * double
     * date
     * datetime
     * array::type
     * list::type
     * set::type
     * map::type,type
     * -----------
     * type can be primitive show above or user defined class
     */


    public MapperResult convert(String customType) {
        String type = customType.toLowerCase().trim();
        if (isPrimitive(type)) {
            return primitiveClassMapper(type);
        }
        if (isDate(type)) {
            return dateClassMapper(type);
        }

        if (isCollection(type)) {
            return collectionClassMapper(type);
        }
        // TODO, enhance to handle user-defined classes
        throw new RuntimeException("type cannot be converted" + customType);
    }

    private MapperResult primitiveClassMapper(String primitive) {
        String type = primitiveDataTypeMapping().get(primitive);
        return new MapperResult(type);
    }

    private MapperResult dateClassMapper(String dateType) {
        String type = dateDataTypeMapping().get(dateType);
        String importClass = importMapping().get(type);
        return new MapperResult(type, importClass);
    }

    /**
     * * array::type
     * * list::type
     * * set::type
     * * map::type,type
     */
    private MapperResult collectionClassMapper(String collectionType) {
        String collectionName = collectionType.split("::")[0];
        String types = collectionType.split("::")[1];
        if (collectionName.toLowerCase().trim().equals("array")) {
            String type = getTypeMapping(types.trim());
            return new MapperResult(type);
        }
        if (collectionName.toLowerCase().trim().equals("list")) {
            String innerType = getTypeMapping(types.trim());
            String type = "List<" + innerType + ">";
            String importClass = importMapping().get("List");
            return new MapperResult(type, importClass);
        }

        if (collectionName.toLowerCase().trim().equals("set")) {
            String innerType = getTypeMapping(types.trim());
            String type = "Set<" + innerType + ">";
            String importClass = importMapping().get("Set");
            return new MapperResult(type, importClass);
        }

        if (collectionName.toLowerCase().trim().equals("map")) {
            String innerType1 = getTypeMapping(types.split(",")[0].trim());
            String innerType2 = getTypeMapping(types.split(",")[1].trim());
            String type = "Map<" + innerType1 + "," + innerType2 + ">";
            String importClass = importMapping().get("Map");
            return new MapperResult(type, importClass);
        }

        return null;
    }

    private String getTypeMapping(String type) {
        return convert(type).typeName;
    }


    private Map<String, String> primitiveDataTypeMapping() {
        Map<String, String> mappings = new HashMap<>();
        mappings.put("string", "String");
        mappings.put("integer", "Integer");
        mappings.put("long", "Long");
        mappings.put("double", "Double");
        mappings.put("float", "Float");
        mappings.put("boolean", "Boolean");
        return mappings;
    }

    private Map<String, String> dateDataTypeMapping() {
        Map<String, String> mappings = new HashMap<>();
        mappings.put("date", "Date");
        mappings.put("datetime", "Date");
        mappings.put("instant", "Instant");
        return mappings;
    }

    private Map<String, String> importMapping() {
        Map<String, String> mappings = new HashMap<>();
        mappings.put("Date", "java.util.*");
        mappings.put("Instant", "java.time.*");
        mappings.put("List", "java.util.*");
        mappings.put("Set", "java.util.*");
        mappings.put("Map", "java.util.*");
        return mappings;
    }

    private boolean isPrimitive(String dataType) {
        return primitiveDataTypes().contains(dataType.toLowerCase());
    }

    private boolean isDate(String dataType) {
        return dateDataTypes().contains(dataType.toLowerCase());
    }

    private boolean isCollection(String dataType) {
        return collectionDataTypes().contains(dataType.split("::")[0]);
    }

    private List<String> primitiveDataTypes() {
        return Arrays.asList("string", "integer", "double", "float", "boolean", "long");
    }

    private List<String> dateDataTypes() {
        return Arrays.asList("date", "datetime", "instant");
    }

    private List<String> collectionDataTypes() {
        return Arrays.asList("array", "list", "set", "map");
    }

    public class MapperResult {
        public String typeName;
        public String importClass;

        public MapperResult(String typeName) {
            this.typeName = typeName;
        }

        public MapperResult(String typeName, String importClass) {
            this.typeName = typeName;
            this.importClass = importClass;
        }

        @Override
        public String toString() {
            return "MapperResult{" +
                    "typeName='" + typeName + '\'' +
                    ", importClass='" + importClass + '\'' +
                    '}';
        }
    }
}

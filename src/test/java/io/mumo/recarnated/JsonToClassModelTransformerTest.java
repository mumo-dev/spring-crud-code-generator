package io.mumo.recarnated;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mumo.recarnated.transformer.ClassModel;
import io.mumo.recarnated.transformer.ClassTypeConverter;
import io.mumo.recarnated.transformer.JsonToClassModelTransformer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

@Slf4j
class JsonToClassModelTransformerTest {

     String exampleJson() {
        return """
                {
                      "name": "string",
                      "id": "integer",
                      "products": "list::string",
                      "images":{
                          "id":"string"
                      },
                      "_metadata": {
                        "packageName": "io.cellulant.productmanager",
                        "className": "Category"
                      }
                }
                """;
    }

    ObjectMapper objectMapper = new ObjectMapper();
    ClassTypeConverter converter = new ClassTypeConverter();
    JsonToClassModelTransformer transformer = new JsonToClassModelTransformer(objectMapper, converter);

    @SneakyThrows
    @Test
    void transformTest() {
        List<ClassModel> classModelList = transformer.transform(exampleJson());
        log.debug(objectMapper.writeValueAsString(classModelList));
    }

    @Test
    void convertJsonToMapTest() {
        Map<String, Object> map = transformer.convertJsonToMap(exampleJson());
        log.debug(map.toString());
    }


}

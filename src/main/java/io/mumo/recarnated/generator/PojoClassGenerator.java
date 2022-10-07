package io.mumo.recarnated.generator;

import io.mumo.recarnated.freemarker.TemplateManager;
import io.mumo.recarnated.transformer.ClassModel;
import io.mumo.recarnated.transformer.JsonToClassModelTransformer;
import io.mumo.recarnated.transformer.field.FieldModelCreator;
import io.mumo.recarnated.transformer.field.PojoFieldModelCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PojoClassGenerator {
    private final JsonToClassModelTransformer jsonToClassModelTransformer;
    private final TemplateManager templateManager;
    private final FieldModelCreator fieldModelCreator;
    public PojoClassGenerator(
            @Qualifier("pojoFieldModelCreator") PojoFieldModelCreator fieldModelCreator,
            JsonToClassModelTransformer jsonToClassModelTransformer,
            TemplateManager templateManager) {

        this.jsonToClassModelTransformer = jsonToClassModelTransformer;
        this.templateManager = templateManager;
        this.fieldModelCreator = fieldModelCreator;

    }

    public String generate(String json) {
        // TODO: remove this - code smell
        jsonToClassModelTransformer.setFieldModelCreator(fieldModelCreator);
        List<ClassModel> classModels = jsonToClassModelTransformer.transform(json);
        StringBuilder output = new StringBuilder("");

        for (ClassModel classModel : classModels) {
            String generatedClass = templateManager.processTemplate("class", classModel);
            output.append("\n\n");
            output.append("//------------- BEGIN CLASS ------------\n");
            output.append(generatedClass);
            output.append("//------------- END CLASS ------------\n");
        }
        output.append("\n\n");
        return output.toString();
    }


}

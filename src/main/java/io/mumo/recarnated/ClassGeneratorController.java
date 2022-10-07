package io.mumo.recarnated;

import io.mumo.recarnated.generator.HibernateEntityClassGenerator;
import io.mumo.recarnated.generator.PojoClassGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/class-generator")
public class ClassGeneratorController {
    private final PojoClassGenerator pojoClassGenerator;
    private final HibernateEntityClassGenerator hibernateEntityClassGenerator;

    @PostMapping(value = "/pojo",produces = {MediaType.TEXT_PLAIN_VALUE})
    public String generatePojoClasses(@RequestBody String body){
        return pojoClassGenerator.generate(body);
    }

    @PostMapping(value = "/hibernate-entity",produces = {MediaType.TEXT_PLAIN_VALUE})
    public String generateHibernateModel(@RequestBody String body){
        return hibernateEntityClassGenerator.generate(body);
    }

}

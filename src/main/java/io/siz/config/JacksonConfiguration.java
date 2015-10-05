package io.siz.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.siz.domain.util.CustomDateTimeDeserializer;
import io.siz.domain.util.CustomDateTimeSerializer;
import io.siz.domain.util.CustomLocalDateSerializer;
import io.siz.domain.util.ISO8601LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import java.io.IOException;
import javax.inject.Inject;
import org.bson.types.ObjectId;

@Configuration
public class JacksonConfiguration {
    
    @Inject
    private ObjectMapper mapper;

    @Bean
    public JodaModule jacksonJodaModule() {
        JodaModule module = new JodaModule();
        module.addSerializer(DateTime.class, new CustomDateTimeSerializer());
        module.addDeserializer(DateTime.class, new CustomDateTimeDeserializer());
        module.addSerializer(LocalDate.class, new CustomLocalDateSerializer());
        module.addDeserializer(LocalDate.class, new ISO8601LocalDateDeserializer());
        return module;
    }

//    @Bean
//    public SimpleModule objectIdModule() {
//        final SimpleModule simpleModule = new SimpleModule("ObjectIdModule");
//        simpleModule.addSerializer(new ObjectIdSerializer());
//        
//        mapper.registerModule(simpleModule);
//        
//        return simpleModule;
//    }

//    public Jdk8Module jacksonJdk8Module() {
//        Jdk8Module module = new Jdk8Module();
//        return module;
//    }
}

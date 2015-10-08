package io.siz.config;

import com.mongodb.Mongo;
import java.util.Arrays;
import java.util.Date;
import org.mongeez.Mongeez;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import javax.inject.Inject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import static io.siz.config.Constants.*;

@Configuration
@EnableMongoRepositories("io.siz.repository")
@Import(value = MongoAutoConfiguration.class)
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfiguration extends AbstractMongoConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Inject
    private Mongo mongo;

    @Inject
    private MongoProperties mongoProperties;

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    protected String getDatabaseName() {
        return mongoProperties.getDatabase();
    }

    @Override
    public Mongo mongo() throws Exception {
        return mongo;
    }

    @Override
    public CustomConversions customConversions() {
        return new CustomConversions(Arrays.asList(
                /**
                 * On a en base pas mal de champs de type mongo NumberLong qui sont en fait des dates. On créé ici le
                 * convertisseur custom qui va nous sauver.
                 */
                new Converter<Long, Date>() {

                    @Override
                    public Date convert(Long source) {
                        log.trace("converting long to date: {} -> {}", source, new Date());
                        return new Date(source);
                    }
                })
        );
    }

    @Bean
    @Profile({"!" + SPRING_PROFILE_FAST, SPRING_PROFILE_MIGRATION})
    public Mongeez mongeez() {
        log.debug("Configuring Mongeez for migrations because profile is activated");
        Mongeez mongeez = new Mongeez();
        mongeez.setFile(new ClassPathResource("/config/mongeez/master.xml"));
        mongeez.setMongo(mongo);
        mongeez.setDbName(mongoProperties.getDatabase());
        mongeez.process();
        return mongeez;
    }

    @Bean
    @Profile(SPRING_PROFILE_FIXTURES)
    public Mongeez mongeez_fixtures() {
        log.debug("Configuring Mongeez for fixtures");
        Mongeez mongeez = new Mongeez();
        mongeez.setFile(new ClassPathResource("/config/mongeez/fixtures/master.xml"));
        mongeez.setMongo(mongo);
        mongeez.setDbName(mongoProperties.getDatabase());
        mongeez.process();
        return mongeez;
    }
}

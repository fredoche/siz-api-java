package io.siz.domain.siz.story;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Source {

    private Integer duration;
    private String type;
    @Field("id")
    private String id;

}

package io.siz.domain.siz.story;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Source {

    private Integer duration;
    private String type;
    private String id;

}

package io.siz.domain.siz.story;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Picture {

    @JsonSerialize(converter = AddHttpToHrefConverter.class)
    private String href;

}

package io.siz.domain.siz.story;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.siz.domain.siz.story.AddHttpToHrefConverter;
import lombok.Data;

/**
 *
 * @author fred
 */
@Data
public class VideoFormat {

    private String type;
    @JsonSerialize(converter = AddHttpToHrefConverter.class)
    private String href;
}

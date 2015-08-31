package io.siz.web.rest.dto.siz;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.siz.domain.siz.Story;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author fred
 */
@Data
@NoArgsConstructor
public class StoryWrapperDTO {

    @JsonProperty("stories") // keep typo because its used in the interface.
    private Story story;

    public StoryWrapperDTO(Story story) {
        this.story = story;
    }

}

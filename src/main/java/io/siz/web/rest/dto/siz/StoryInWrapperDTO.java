package io.siz.web.rest.dto.siz;

import io.siz.web.rest.dto.siz.story.StoryDTO;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author fred
 */
@Data
@NoArgsConstructor
public class StoryInWrapperDTO {

    private List<StoryDTO> stories;

    public StoryInWrapperDTO(List<StoryDTO> stories) {
        this.stories = stories;
    }
}

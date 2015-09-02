package io.siz.web.rest.dto.siz.story;

import io.siz.domain.siz.story.Source;
import java.util.List;
import lombok.Data;

/**
 * émule la case class boxes: List[NewBox], source: Source, title: String, tags:
 * List[String]
 *
 * @author fred
 */
@Data
public class StoryDTO {

    private List<BoxDTO> boxes;
    private Source source;
    private String title;
    private List<String> tags;
}

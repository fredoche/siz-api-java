package io.siz.web.rest.dto.siz;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.siz.domain.siz.Event;
import io.siz.domain.siz.SizToken;
import io.siz.domain.siz.SizUser;
import io.siz.domain.siz.Story;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe fourre tout pour respecter l'ancienne api scala #fml
 *
 * @author fred
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TopLevelDto {

    public TopLevelDto(SizUser user) {
        this.user = user;
    }

    public TopLevelDto(SizToken token) {
        this.token = token;
    }

    public TopLevelDto(List<Story> stories) {
        this.stories = stories;
    }

    public TopLevelDto(SizErrorDTO error) {
        this.errors = Collections.singletonList(error);
    }

    public TopLevelDto(Story story) {
        this.stories = Collections.singletonList(story);
    }

    private Map<String, String> links;
    private List<IdAndStateDTO> emails;
    private List<UsernameDTO> usernames;
    private SizUser user;
    private SizToken token;
    private List<Story> stories;
    private List<SizErrorDTO> errors;
    private List<Event> events;
}

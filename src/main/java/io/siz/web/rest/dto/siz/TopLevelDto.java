package io.siz.web.rest.dto.siz;

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
public class TopLevelDto {

    public TopLevelDto(SizUser user) {
        this.user = user;
    }

    public TopLevelDto(SizToken token) {
        this.token = token;
    }
    
    public TopLevelDto(SizErrorDTO error) {
        this.errors = Collections.singletonList(error);
    }
    

    private Map<String, String> links;
    private List<EmailDTO> emails;
    private List<UsernameDTO> usernames;
    private SizUser user;
    private SizToken token;
    private List<Story> stories;
    private List<SizErrorDTO> errors;
    private List<Event> events;
}

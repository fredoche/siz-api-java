package io.siz.web.rest.dto.siz;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.siz.domain.siz.SizToken;

/**
 * Classe pour créer un json correspondant à { "tokens": { "id": "xxx",
 * "viewerProfileId": "xxx", "storyIdToShow": "xxx", "href": "/tokens/xxx" } }
 *
 * @author fred
 */
public class TokenWrapperDTO {

    @JsonProperty("tokens") // keep typo because its used in the interface.
    private SizToken token;

    public TokenWrapperDTO(SizToken token) {
        this.token = token;
    }

    public SizToken getToken() {
        return token;
    }

    public void setToken(SizToken token) {
        this.token = token;
    }
}

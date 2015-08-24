package io.siz.web.rest.dto.siz;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.siz.domain.siz.Token;

/**
 * Classe pour créer un json correspondant à { "tokens": { "id": "xxx",
 * "viewerProfileId": "xxx", "storyIdToShow": "xxx", "href": "/tokens/xxx" } }
 *
 * @author fred
 */
public class TokenWrapperDTO {

    @JsonProperty("tokens") // keep typo because its used in the interface.
    private Token token;

    public TokenWrapperDTO(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}

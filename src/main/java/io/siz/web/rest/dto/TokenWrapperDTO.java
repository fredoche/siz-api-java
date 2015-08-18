package io.siz.web.rest.dto;

import java.util.Optional;

/**
 * Classe pour créer un json correspondant à { "tokens": { "id": "xxx",
 * "viewerProfileId": "xxx", "storyIdToShow": "xxx", "href": "/tokens/xxx" } }
 *
 * @author fred
 */
public class TokenWrapperDTO {

    private Token tokens;

    private class Token {

        private String id;
        private String viewerProfileId;
        private Optional<String> storyIdToShow;
        private Optional<String> href;

    }

}

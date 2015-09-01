package io.siz.repository.siz.eventhandlers;

import io.siz.domain.siz.Story;
import io.siz.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Component;

/**
 *
 * @author fred
 */
@Component
@RepositoryEventHandler(Story.class)
public class StoryRepositoryEventHandler {

    Logger log = LoggerFactory.getLogger(StoryRepositoryEventHandler.class);

    /**
     * On va sécuriser les suppressions d'histoire par défaut
     */
    @HandleBeforeDelete
    @PostAuthorize("hasRole('ROLE_ADMIN')")
    public void secureDeleteByDefault(Story s) {
        log.info("Attempt to delete story {} by user {}", s.getId(), SecurityUtils.getCurrentLogin());
    }
}

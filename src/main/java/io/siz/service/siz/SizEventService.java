package io.siz.service.siz;

import com.mongodb.WriteResult;
import io.siz.domain.siz.Event;
import io.siz.domain.siz.Story;
import io.siz.domain.siz.ViewerProfile;
import io.siz.exception.SizException;
import io.siz.repository.siz.EventRepository;
import io.siz.repository.siz.ViewerProfileRepository;
import io.siz.security.SecurityUtils;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author fred
 */
@Service
public class SizEventService {

    private final Logger log = LoggerFactory.getLogger(SizEventService.class);

    @Inject
    private EventRepository eventRepository;

    @Inject
    private ViewerProfileRepository viewerProfileRepository;

    /**
     * Créé un évènement. Au passage, créé le viewerprofile implicitement
     * associé au token.
     */
    public Event create(Event event) {
        /**
         * si on a updaté zero profil, c'est qu'un user nouveau vient de liker
         * et qu'il faut lui créer un nouveau viewerprofile. L'id du
         * viewerprofile est égal à celui du token.
         */
        final ViewerProfile vp = viewerProfileRepository.findById(event.getViewerProfileId())
                .orElseGet(() -> {
                    ViewerProfile newProfile = new ViewerProfile();
                    newProfile.setId(event.getViewerProfileId());
                    return viewerProfileRepository.insert(newProfile);
                });

        event.setViewerProfileId(vp.getId());

        WriteResult updateFromEvent = viewerProfileRepository.updateFromEvent(event);
        if (updateFromEvent.getN() != 1) {
            throw new SizException("unable to update profile");
        }
        return eventRepository.insert(event);
    }
}

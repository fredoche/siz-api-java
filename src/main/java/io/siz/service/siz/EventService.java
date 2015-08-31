package io.siz.service.siz;

import com.mongodb.WriteResult;
import io.siz.domain.siz.Event;
import io.siz.domain.siz.Story;
import io.siz.domain.siz.SizToken;
import io.siz.repository.siz.EventRepository;
import io.siz.repository.siz.ViewerProfileRepository;
import io.siz.security.SecurityUtils;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author fred
 */
@Service
public class EventService {

    private final Logger log = LoggerFactory.getLogger(EventService.class);

    @Inject
    private EventRepository eventRepository;

    @Inject
    private ViewerProfileRepository viewerProfileRepository;

    public Event create(Event event, Story story, String remoteAddr) {
        event.getViewerProfile().setId(SecurityUtils.getCurrentLogin());
        WriteResult updateFromEvent = viewerProfileRepository.updateFromEvent(event);
        if (updateFromEvent.getN() != 1) {
            /**
             * si on a updaté zero profil, c'est qu'un user nouveau vient de
             * liker et qu'il faut lui créer un nouveau viewerprofile.
             */

//            SecurityContextHolder.getContext().getAuthentication();
            // equivalent de Event(newEvent.storyId, newEvent._type, tags, viewerProfileId, BSONObjectID.generate.stringify, ip = ip)
            event.setIp(remoteAddr);

//            event.setViewerProfile(((Token) authentication).getViewerProfileId());
            return eventRepository.insert(event);
        } else {
            /**
             * sinon c'est bon on retourne juste l'event.
             */
            return event;
//            log.error(
//                    "wrong number of udpated profiles: Updated {} for viewerprofile id {}",
//                    updateFromEvent.getN(),
//                    event.getViewerProfile().getId());
        }
    }
}

package io.siz.service.siz;

import io.siz.domain.siz.Event;
import io.siz.domain.siz.Story;
import io.siz.repository.siz.EventRepository;
import javax.inject.Inject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 *
 * @author fred
 */
@Service
public class EventService {

    @Inject
    private EventRepository eventRepository;

    public void create(Event event, Story story, Authentication authentication, String remoteAddr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

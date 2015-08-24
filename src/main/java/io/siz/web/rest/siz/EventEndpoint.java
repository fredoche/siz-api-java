package io.siz.web.rest.siz;

import com.codahale.metrics.annotation.Timed;
import io.siz.domain.siz.Event;
import io.siz.domain.siz.Story;
import io.siz.exception.SizException;
import io.siz.repository.siz.StoryRepository;
import io.siz.service.siz.EventService;
import io.siz.web.rest.dto.siz.EventWrapperDTO;
import java.util.Optional;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

/**
 *
 * @author fred
 */
@RestController
public class EventEndpoint {

    @Inject
    private StoryRepository storyDao;

    @Inject
    private EventService eventService;

    @RequestMapping(value = "/events",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured(value = "ROLE_USER")
    public EventWrapperDTO create(EventWrapperDTO submittedEventDto, HttpServletRequest request) throws SizException {
        final Optional<Story> s = Optional.of(storyDao.findOne(submittedEventDto.getEvent().getStoryId()));
        return s
                .map(story -> {
                    Event event = new Event();
                    eventService.create(
                            event,
                            story, SecurityContextHolder.getContext().getAuthentication(),
                            request.getRemoteAddr());
                    event.setStoryId(story.getId());
                    final EventWrapperDTO eventWrapperDTO = new EventWrapperDTO(event);
                    return eventWrapperDTO;
                })
                .orElseThrow(() -> new SizException());
    }
}

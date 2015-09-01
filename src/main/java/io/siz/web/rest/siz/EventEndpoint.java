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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("hasRole('ROLE_USER')")
    public EventWrapperDTO create(@RequestBody EventWrapperDTO submittedEventDto, HttpServletRequest request) throws SizException {
        final Event event = submittedEventDto.getEvent();
        final Optional<Story> s = Optional.ofNullable(storyDao.findOne(event.getStoryId()));
        return s.map(story -> {
            eventService.create(
                    event,
                    story,
                    request.getRemoteAddr());
            event.setStoryId(story.getId());
            final EventWrapperDTO eventWrapperDTO = new EventWrapperDTO(event);
            return eventWrapperDTO;
        })
                .orElseThrow(() -> new SizException());
    }
}

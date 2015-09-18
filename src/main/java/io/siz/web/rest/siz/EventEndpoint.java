package io.siz.web.rest.siz;

import com.codahale.metrics.annotation.Timed;
import io.siz.domain.siz.Event;
import io.siz.exception.SizException;
import io.siz.repository.siz.StoryRepository;
import io.siz.service.siz.SizEventService;
import io.siz.service.siz.SizTokenService;
import io.siz.web.rest.dto.siz.EventWrapperDTO;
import java.util.Optional;
import javax.inject.Inject;
import org.apache.catalina.security.SecurityUtil;
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
    private SizEventService eventService;

    @Inject
    private SizTokenService sizTokenService;

    @RequestMapping(value = "/events",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("hasRole('ROLE_USER')")
    public EventWrapperDTO create(@RequestBody EventWrapperDTO submittedEventDto) {
        final Event event = submittedEventDto.getEvent();
        return Optional.ofNullable(storyDao.findOne(event.getStoryId()))
                .map(story -> {
                    event.setTags(story.getTags());
                    event.setViewerProfileId(sizTokenService.getCurrentToken().getViewerProfileId());
                    eventService.create(event);
                    return new EventWrapperDTO(event);
                })
                .orElseThrow(SizException::new);
    }
}

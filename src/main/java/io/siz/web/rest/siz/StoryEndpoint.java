package io.siz.web.rest.siz;

import com.codahale.metrics.annotation.Timed;
import io.siz.domain.siz.Story;
import io.siz.exception.SizException;
import io.siz.repository.siz.StoryRepository;
import io.siz.web.rest.dto.siz.StoryWrapperDTO;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author fred
 */
@RestController
@RequestMapping("/stories")
public class StoryEndpoint {

    @Inject
    private StoryRepository storyRepository;

    @RequestMapping(
            value = "/{storyId}",
            method = RequestMethod.GET)
    @Timed
    @PreAuthorize("hasRole('ROLE_USER')")
    public StoryWrapperDTO getById(@PathVariable String storyId) throws SizException {
        final Optional<Story> s = Optional.ofNullable(storyRepository.findOne(storyId));
        return s.map(story -> {
            return new StoryWrapperDTO(story);
        })
                .orElseThrow(() -> new SizException());
    }
}

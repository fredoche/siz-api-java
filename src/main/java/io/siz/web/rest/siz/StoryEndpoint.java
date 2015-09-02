package io.siz.web.rest.siz;

import com.codahale.metrics.annotation.Timed;
import io.siz.domain.siz.Story;
import io.siz.domain.siz.story.Box;
import io.siz.exception.SizException;
import io.siz.repository.siz.StoryRepository;
import io.siz.web.rest.dto.siz.StoryInWrapperDTO;
import io.siz.web.rest.dto.siz.StoryOutWrapperDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Timed
    @RequestMapping(
            value = "/{storyId}",
            method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public StoryOutWrapperDTO getById(@PathVariable String storyId) throws SizException {
        final Optional<Story> s = Optional.ofNullable(storyRepository.findOne(storyId));
        return s.map(story -> {
            return new StoryOutWrapperDTO(story);
        })
                .orElseThrow(() -> new SizException());
    }

    @Timed
    @RequestMapping(method = RequestMethod.POST)
    public StoryOutWrapperDTO createStory(@RequestBody StoryInWrapperDTO wrapper) throws SizException {
        // TODO utiliser mapstruct eventuellement, plutot que ce mapping boring.
        return wrapper.getStories().stream()
                .map(storyDto -> {
                    final Story story = new Story();
                    story.setSource(storyDto.getSource());
                    story.setTitle(storyDto.getTitle());
                    story.setTags(storyDto.getTags());

                    List<Box> boxes = storyDto.getBoxes().stream()
                    .map(boxDto -> {
                        final Box box = new Box();
                        box.setStart(boxDto.getStart());
                        box.setStop(boxDto.getStop());
                        return box;
                    }
                    ).collect(Collectors.toList());

                    story.setBoxes(boxes);
                    return story;
                })
                .map(s -> storyRepository.insert(s))
                .findFirst()
                .map(StoryOutWrapperDTO::new)
                .orElseThrow(SizException::new);
    }
}

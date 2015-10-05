package io.siz.web.rest.siz;

import com.codahale.metrics.annotation.Timed;
import io.siz.domain.siz.SizToken;
import io.siz.domain.siz.Story;
import io.siz.domain.siz.story.Box;
import io.siz.exception.SizException;
import io.siz.repository.siz.StoryRepository;
import io.siz.repository.siz.ViewerProfileRepository;
import io.siz.service.siz.SizStoryService;
import io.siz.service.siz.SizTokenService;
import io.siz.service.siz.ViewerProfileService;
import io.siz.web.rest.dto.siz.SizErrorDTO;
import io.siz.web.rest.dto.siz.StoryFilterBy;
import io.siz.web.rest.dto.siz.StoryInWrapperDTO;
import io.siz.web.rest.dto.siz.StoryOutWrapperDTO;
import io.siz.web.rest.dto.siz.TopLevelDto;
import io.siz.web.rest.dto.siz.converters.StoryFilterByEnumConverter;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author fred
 */
@RestController
public class StoryEndpoint {

    @Inject
    private StoryRepository storyRepository;

    @Inject
    private SizStoryService storyService;

    @Inject
    private ViewerProfileService viewerProfileService;

    @Inject
    private ViewerProfileRepository viewerProfileRepository;

    @Inject
    private SizTokenService sizTokenService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(StoryFilterBy.class, new StoryFilterByEnumConverter());
    }

    @Timed
    @RequestMapping(
            value = "/stories/{storyId}",
            method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public StoryOutWrapperDTO getById(@PathVariable String storyId) throws SizException {
        final Optional<Story> s = Optional.ofNullable(storyRepository.findOne(storyId));
        return s.map(story -> {
            return new StoryOutWrapperDTO(story);
        })
                .orElseThrow(SizException::new);
    }

    /**
     *
     * @param limit
     * @param orderBy
     * @param filterBy
     * @param slug
     * @param sinceId
     * @param lastSkippedId
     * @return
     */
    @Timed
    @RequestMapping(
            value = "/stories",
            method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public TopLevelDto getStories(
            @RequestParam(defaultValue = "12") Integer limit,
            @RequestParam(defaultValue = "creationDate") String orderBy,
            @RequestParam(defaultValue = "recommends") StoryFilterBy filterBy,
            @RequestParam Optional<String> slug,
            @RequestParam Optional<String> sinceId,
            @RequestParam Optional<String> lastSkippedId) {

        if (slug.isPresent()) {
            return slug
                    .flatMap(storyService::getBySlug)
                    .map(TopLevelDto::new)
                    .orElse(new TopLevelDto(new SizErrorDTO("slug pas trouvé")));
        } else {
            SizToken token = sizTokenService.getCurrentToken();

            /**
             * on GARDE les ids entre since et last: donc le predicat est true si s.date est apres since et avant last.
             * a,b,c,sinceId],d,e,f[,lastSkipId,g,h,i
             */
            final Predicate<? super Story> storyBetweenPredicate = s -> {
                return sinceId
                        .map(storyRepository::findOne)
                        .map(Story::getCreationDate)
                        .map(
                                date -> s.getCreationDate().after(date))
                        .orElse(true)
                        && lastSkippedId
                        .map(storyRepository::findOne)
                        .map(Story::getCreationDate)
                        .map(
                                date -> s.getCreationDate().before(date))
                        .orElse(true);

            };

            Stream<Story> stories = viewerProfileRepository.findById(token.getViewerProfileId())
                    .map(profile -> {
                        switch (filterBy) {
                            default:
                            case RECOMMENDS:
                                return viewerProfileService.findRecommends(token, profile, orderBy);
                            case LIKES:

                                /**
                                 * renvoie les stories aimées présentes dans le profil du visiteur. On conserve la façon
                                 * moisie de faire la pagination de l'ancienne api, mais en moins déglingué quand même.
                                 */
                                return viewerProfileService.findLikes(profile)
                                .filter(storyBetweenPredicate);
                        }
                    })
                    .orElseGet(() -> {
                        switch (filterBy) {
                            /**
                             * Si le mec a pas de profil on lui envoie des histoire recommandées.
                             */
                            default:
                            case RECOMMENDS:
                                return viewerProfileService.findRecommends(token, orderBy);
                            case LIKES:
                                /**
                                 * le mec a pas de profil mais demande ses likes. C'est vide pour le moment.
                                 */
                                return Stream.empty();
                        }
                    });
            return new TopLevelDto(
                    stories
                    .distinct()
                    .limit(limit)
                    .collect(Collectors.toList()));
        }
    }

    @Timed
    @RequestMapping(
            value = "/stories",
            method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_USER')")
    public StoryOutWrapperDTO createStory(@RequestBody StoryInWrapperDTO wrapper) {
        Story s = wrapper.getStories().stream()
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
                .findFirst()
                .orElseThrow(SizException::new);

        storyService.startConversion(s);
        return new StoryOutWrapperDTO(s);
    }
}

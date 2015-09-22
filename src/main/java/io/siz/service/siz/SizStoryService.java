package io.siz.service.siz;

import io.siz.domain.siz.Event;
import io.siz.domain.siz.EventType;
import io.siz.domain.siz.Story;
import io.siz.repository.siz.EventRepository;
import io.siz.repository.siz.StoryRepository;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SizStoryService {

    @Inject
    private StoryRepository storyRepository;

    @Inject
    private EventRepository eventRepository;

    /**
     * Renvoie le slug et au passage associe l'ip du visiteur à son token pour
     * renvoyer le même slug lors de la prochaine visite.
     *
     * @param slug
     * @return
     */
    // DO NOT CACHE: utilise des threadlocal.
    public Optional<Story> getBySlug(String slug) {
        return storyRepository
                .getBySlug(slug)
                .map(story -> {
//                    SizToken token = (SizToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    final String remoteAddr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest()
                    .getRemoteAddr();

                    Event e = new Event();
                    e.setStoryId(story.getId());
                    e.setType(EventType.ANONYMOUS_VIEW);
                    e.setTags(story.getTags());
//                    e.setViewerProfileId(token.getViewerProfileId());
                    e.setIp(remoteAddr);

                    eventRepository.insert(e);
                    return story;
                });
    }
}

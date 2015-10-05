package io.siz.service.siz;

import io.siz.domain.siz.Counter;
import io.siz.domain.siz.Event;
import io.siz.domain.siz.EventType;
import io.siz.domain.siz.Story;
import io.siz.repository.siz.EventRepository;
import io.siz.repository.siz.StoryRepository;
import io.siz.repository.siz.impl.CounterRepository;
import java.text.Normalizer;
import java.util.Optional;
import javax.inject.Inject;
import org.bson.types.ObjectId;
import org.springframework.integration.annotation.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SizStoryService {

    @Inject
    private StoryRepository storyRepository;

    @Inject
    private EventRepository eventRepository;

    @Inject
    private CounterRepository counterRepository;

    /**
     * Renvoie le slug et au passage associe l'ip du visiteur à son token pour renvoyer le même slug lors de la
     * prochaine visite.
     *
     * @param slug
     * @return
     */
    // DO NOT CACHE: utilise des threadlocal.
    public Optional<Story> getBySlug(String slug) {
        return storyRepository
                .getBySlug(slug)
                .map(story -> {
                    final String remoteAddr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest()
                    .getRemoteAddr();

                    Event e = new Event();
                    e.setStoryId(story.getId());
                    e.setType(EventType.ANONYMOUS_VIEW);
                    e.setTags(story.getTags());
                    e.setIp(remoteAddr);

                    eventRepository.insert(e);
                    return story;
                });
    }

    /**
     * enregistre la story dans la bdd et au passage notifie le systeme de dl video qu'il a du boulot via l'annotation.
     * Est asynchrone car de toute façon car de tte façon renvoie void. Voir TODO.
     *
     * @param s
     * @return
     */
    @Publisher(channel = "videostripChannel")
    public Story startConversion(Story s) {
        final Counter nextSequence = counterRepository.getNextSequence("slug");

        String sluggedTitle = Normalizer.normalize(s.getTitle(), Normalizer.Form.NFD)
                .replaceAll("[^\\w\\s-]", "") // Remove all non-word, non-space or non-dash characters
                .replace('-', ' ') // Replace dashes with spaces
                .trim() // Trim leading/trailing whitespace (including what used to be leading/trailing dashes)
                .replaceAll("\\s+", "-") // Replace whitespace (including newlines and repetitions) with single dashes
                .toLowerCase(); // Lowercase the final results

        s.setSlug(sluggedTitle + "-" + nextSequence.getSeq());

        s.setId(new ObjectId());

        /**
         * la sauvegarde est déléguée au script python de conversion video qui va choisir l'id, etc. TODO: changer ça:
         * le script doit récupérer une story depuis la bdd et uniquement faire la conversion, pas dupliquer la logique
         * qui consiste à faire une sauvegarde en bdd.
         */
//        final Story savedStory = storyRepository.insert(s);
        return s;
    }
}

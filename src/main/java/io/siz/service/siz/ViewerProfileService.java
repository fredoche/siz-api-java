package io.siz.service.siz;

import io.siz.domain.siz.SizToken;
import io.siz.domain.siz.Story;
import io.siz.domain.siz.ViewerProfile;
import io.siz.repository.siz.SizTokenRepository;
import io.siz.repository.siz.StoryRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author fred
 */
@Service
public class ViewerProfileService {

    private final static int DISLIKE_TAG_SCORE = -5;

    @Inject
    private StoryRepository storyRepository;

    @Inject
    private SizTokenRepository sizTokenRepository;

    public Stream<Story> findLikes(ViewerProfile profile) {

        return profile.getLikedStories().stream()
                .map(storyRepository::findOne)
                .sorted(Comparator.comparing(Story::getLastModifiedDate));
    }

    /**
     * Renvoie les recommendations. Pour le moment, les recommendations
     * fonctionnent comme celà: Les utilisateurs like ou unlikent des vidéos, ce
     * qui modifie le score des tags associés à leur user viewerprofile. Les
     * tags avec un score supérieurs à -5 sont exclus de la requete. De plus,
     * les stories déja vues sont également enlevées.
     *
     * @param token pour eventuellemnt pousser une story donnée.
     * @param profile pour connaitre les story likées ou pas.
     * @param orderBy une propriété de story pour le tri descendant.
     * @return Un stream de stories.
     */
    public Stream<Story> findRecommends(SizToken token, ViewerProfile profile, String orderBy) {

        final List<String> alreadySeenStories = Stream.concat(
                profile.getLikedStories().stream(),
                profile.getNopeStoryIds().stream())
                .collect(Collectors.toList());

        final List<String> dislikedTags = profile.getTagsWeights().entrySet().stream()
                .filter((entry) -> entry.getValue() <= DISLIKE_TAG_SCORE)
                .map((entry) -> entry.getKey())
                .collect(Collectors.toList());

        return Stream.concat(
                /**
                 * on renvoie un stream contenant éventuellement la story
                 * recommandée par ip:
                 */
                getPushedStories(token),
                storyRepository.findByIdNotInAndTagsNotIn(
                        alreadySeenStories,
                        dislikedTags,
                        new Sort(Sort.Direction.DESC/* correspong à mongo -1 dans l'ancienne api*/, orderBy)));
    }

    /**
     * renvoie les stories à pousser spécifiquement pour diverses raisons, par
     * exemple parce que le token courant a une vidéo associée.
     *
     * @param token
     * @return
     */
    private Stream<Story> getPushedStories(SizToken token) {
        return Optional.ofNullable(token.getStoryIdToShow())
                .map(storyRepository::findOne)
                .map(story -> {
                    token.setStoryIdToShow(null);
                    sizTokenRepository.save(token);
                    return story;
                })
                .map(Stream::of)
                .orElse(Stream.empty());
    }

    public Stream<Story> findRecommends(SizToken token, String orderBy) {
        return findRecommends(token, new ViewerProfile(), orderBy);
    }
}

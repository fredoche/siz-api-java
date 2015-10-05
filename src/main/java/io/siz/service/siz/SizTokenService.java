package io.siz.service.siz;

import io.siz.domain.siz.EventType;
import io.siz.domain.siz.SizToken;
import io.siz.repository.siz.EventRepository;
import io.siz.repository.siz.SizTokenRepository;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SizTokenService {

    private static final Logger log = LoggerFactory.getLogger(SizTokenService.class);

    private final Integer GENERATED_ID_SIZE = 64;

    @Inject
    private SizTokenRepository sizTokenRepository;

    @Inject
    private EventRepository eventRepository;
    /**
     * classe pour gérénrer les id mongo
     */
    private final SecureRandom random = new SecureRandom();

    public SizToken createToken() {
        SizToken token = new SizToken();
        token.setId(
                random.ints(0, 9).limit(GENERATED_ID_SIZE).boxed().map(String::valueOf).collect(Collectors.joining("")));
        token.setViewerProfileId(
                random.ints(0, 9).limit(GENERATED_ID_SIZE).boxed().map(String::valueOf).collect(Collectors.joining("")));
        sizTokenRepository.insert(token);

        log.info("Creating new token.");

        final String remoteAddr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getRemoteAddr();

        return eventRepository.findByIpAndType(remoteAddr, EventType.ANONYMOUS_VIEW)
                .map(event -> {
                    log.info("Found a video associated with this ip. Associating the video to the token");
                    token.setStoryIdToShow(event.getStoryId().toString());
                    return token;
                })
                .orElse(token);
    }

    public SizToken getCurrentToken() {
        return (SizToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

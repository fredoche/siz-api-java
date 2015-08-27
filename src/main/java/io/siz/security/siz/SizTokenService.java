package io.siz.security.siz;

import io.siz.domain.siz.SizToken;
import io.siz.repository.siz.SizTokenRepository;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class SizTokenService {

    private final Integer GENERATED_ID_SIZE = 64;

    @Inject
    private SizTokenRepository sizTokenRepository;
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
        return token;
    }

}

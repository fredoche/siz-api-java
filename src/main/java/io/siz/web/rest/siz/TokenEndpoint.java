package io.siz.web.rest.siz;

import com.codahale.metrics.annotation.Timed;
import io.siz.web.rest.dto.siz.TokenWrapperDTO;
import javax.inject.Inject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.siz.domain.siz.SizToken;
import io.siz.security.siz.SizTokenService;

/**
 *
 * @author fred
 */
@RestController
public class TokenEndpoint {

    @Inject
    private SizTokenService tokenProvider;

    @RequestMapping(value = "/tokens",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public TokenWrapperDTO create() {
        final SizToken newToken = tokenProvider.createToken();
        return new TokenWrapperDTO(newToken);
    }
}

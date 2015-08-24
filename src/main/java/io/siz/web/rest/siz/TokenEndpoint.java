package io.siz.web.rest.siz;

import com.codahale.metrics.annotation.Timed;
import io.siz.web.rest.dto.siz.TokenWrapperDTO;
import javax.inject.Inject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.siz.domain.siz.Token;
import io.siz.security.anonymous.AnonymousUser;
import io.siz.security.xauth.TokenProvider;

/**
 *
 * @author fred
 */
@RestController
public class TokenEndpoint {

    @Inject
    private TokenProvider tokenProvider;

    @RequestMapping(value = "/tokens",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public TokenWrapperDTO create() {
        AnonymousUser anonymousUser = new AnonymousUser();
        io.siz.security.xauth.Token xauthtoken = tokenProvider.createToken(anonymousUser);
        Token sizToken = new Token();
        sizToken.setId(xauthtoken.getToken());
        return new TokenWrapperDTO(sizToken);
    }
}

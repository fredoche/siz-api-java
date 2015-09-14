package io.siz.web.rest.siz;

import com.codahale.metrics.annotation.Timed;
import io.siz.web.rest.dto.siz.TokenWrapperDTO;
import javax.inject.Inject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.siz.domain.siz.SizToken;
import io.siz.service.siz.SizTokenService;
import io.siz.service.siz.SizUserService;
import io.siz.web.rest.dto.siz.SizErrorDTO;
import io.siz.web.rest.dto.siz.SizUserWrapperDTO;
import io.siz.web.rest.dto.siz.TopLevelDto;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author fred
 */
@RestController
public class TokenEndpoint {

    @Inject
    private SizTokenService sizTokenService;

    @Inject
    private SizUserService sizUserService;

    @Inject
    private MessageSource messageSource;

    @RequestMapping(value = "/tokens",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public TokenWrapperDTO create() {
        final SizToken newToken = sizTokenService.createToken();
        return new TokenWrapperDTO(newToken);
    }

    /**
     * enregistre un login associé à un token. Un seul login par token est
     * autorisé. Si le token est déja associé à un user on renvoie une erreur de
     * sécurité.
     *
     * @param unusedString est pas utilisé mais on le pose là pour le matching
     * d'url.
     * @param wrapper
     * @return
     */
    @RequestMapping(value = "/tokens/{unusedString}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    /**
     * cette clause spécifie que le token n'est pas déja associé à un user.
     */
    @PreAuthorize("principal.userId == null")
    public TopLevelDto login(@PathVariable String unusedString, @RequestBody SizUserWrapperDTO wrapper) {
        Locale locale = LocaleContextHolder.getLocale();
        return wrapper.getUsers().stream()
                .findFirst()
                .flatMap(sizUserService::loginUser)
                .map(user -> {
                    TopLevelDto dto = new TopLevelDto();
                    dto.setUser(user);
                    dto.setToken((SizToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                    return dto;
                }
                )
                // on alors on laisse l'exception aller dans le ExceptionTranslator
                .orElseGet(() -> new TopLevelDto(new SizErrorDTO(
                                        messageSource.getMessage(
                                                "token.invalid.login",
                                                new Object[]{unusedString},
                                                "invalid login",
                                                locale))));
    }
}

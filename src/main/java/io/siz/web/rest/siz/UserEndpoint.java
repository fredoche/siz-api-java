package io.siz.web.rest.siz;

import com.codahale.metrics.annotation.Timed;
import io.siz.domain.siz.SizUser;
import io.siz.service.siz.SizTokenService;
import io.siz.service.siz.SizUserService;
import io.siz.web.rest.dto.siz.SizErrorDTO;
import io.siz.web.rest.dto.siz.SizUserWrapperDTO;
import io.siz.web.rest.dto.siz.TopLevelDto;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/users")
public class UserEndpoint {

    @Inject
    private SizUserService sizUserService;

    @Inject
    private SizTokenService sizTokenService;

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("hasRole('ROLE_USER')")
    public TopLevelDto create(@RequestBody SizUserWrapperDTO wrapper, HttpServletRequest request) {
        return wrapper.getUser()
                .map(userDTO -> {
                    final SizUser newUser = sizUserService.create(userDTO);
                    return new TopLevelDto(
                            sizTokenService.getCurrentToken(),
                            newUser);
                }).orElse(new TopLevelDto(new SizErrorDTO("provide a user")));
    }

    /**
     * recupere un user par Id en théorie mais UserId est ignoré car pas utile: le User est associé a son token de toute
     * façon.
     *
     * @param userId nécessaire pour matcher l'ancienne api mais pas utilisé
     * @return
     */
    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("hasRole('ROLE_USER')")
    public SizUser get(@PathVariable String userId) {
        return sizUserService.getUser(sizTokenService.getCurrentToken().getUserId());
    }
}

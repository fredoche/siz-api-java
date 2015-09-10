package io.siz.web.rest.siz;

import com.codahale.metrics.annotation.Timed;
import io.siz.domain.siz.SizUser;
import io.siz.exception.SizException;
import io.siz.repository.siz.SizTokenRepository;
import io.siz.service.siz.SizUserService;
import io.siz.web.rest.dto.siz.SizUserWrapperDTO;
import io.siz.web.rest.dto.siz.TopLevelDto;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
    private SizTokenRepository sizTokenRepository;

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("hasRole('ROLE_USER')")
    public TopLevelDto create(@RequestBody SizUserWrapperDTO wrapper, HttpServletRequest request) throws SizException {
        return wrapper.getUsers().stream()
                .findFirst()
                .map(userDTO -> {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    return sizUserService.create(userDTO, ((User) authentication.getPrincipal()).getUsername())
                    .map(TopLevelDto::new);
                })
                .orElseThrow(SizException::new)
                .get();
    }

    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("hasRole('ROLE_USER')")
    public SizUser get(@PathVariable String userId) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String tokenString = ((User) authentication.getPrincipal()).getUsername();
        
        sizTokenRepository.findById(tokenString);
        
        return sizUserService.getUser(userId);
    }
}

package io.siz.web.rest.siz;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Maps;
import io.siz.repository.siz.SizUserRepository;
import io.siz.web.rest.dto.siz.IdAndStateDTO;
import io.siz.web.rest.dto.siz.SizErrorDTO;
import io.siz.web.rest.dto.siz.TopLevelDto;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author fred
 */
@RestController
public class CheckEndpoint {

    @Inject
    private SizUserRepository sizUserRepository;

    @RequestMapping(value = "/emails/{email}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity findByEmail(@RequestParam String email) {
        return sizUserRepository
                .findByEmail(email)
                .map(user
                        -> new ResponseEntity(
                                Maps.immutableEntry("emails",
                                        new IdAndStateDTO(user.getEmail(), "registered")), HttpStatus.OK))
                .orElse(new ResponseEntity(
                                new TopLevelDto(new SizErrorDTO(null, "Email not found", null)), HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/usernames/{property}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity findByUsername(@RequestParam String username) {
        return sizUserRepository
                .findByUsername(username)
                .map(user
                        -> new ResponseEntity(
                                Maps.immutableEntry("usernames",
                                        new IdAndStateDTO(user.getUsername(), "registered")), HttpStatus.OK))
                .orElse(new ResponseEntity(
                                new TopLevelDto(new SizErrorDTO(null, "Username not found", null)), HttpStatus.NOT_FOUND));
    }
}

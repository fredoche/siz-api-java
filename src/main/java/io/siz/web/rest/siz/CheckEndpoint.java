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
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * on doit pouvoir récupérer un user par email. La route est protégée pour éviter un brute force de tous les emails
     * possibles et ne fonctionne que si on demande son propre user ou si on est admin.
     *
     * @param email
     * @return
     */
    @RequestMapping(value = "/emails/{email:.*}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity findByEmail(@PathVariable String email) {
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
            method = RequestMethod.GET,
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

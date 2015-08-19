package io.siz.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.siz.web.rest.dto.TokenWrapperDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author fred
 */
@RestController
@RequestMapping("/tokens")
public class TokenEndpoint {

    @RequestMapping(value = "/new",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public TokenWrapperDTO create() {
        return new TokenWrapperDTO();
    }
}

package io.siz.jersey;

import io.siz.web.rest.dto.TokenWrapperDTO;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import org.springframework.stereotype.Component;

/**
 *
 * @author fred
 */
@Component
@Path("/tokens")
public class TokenEndpoint {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public TokenWrapperDTO create() {
        return new TokenWrapperDTO();
    }
}

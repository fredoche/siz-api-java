package io.siz.web.rest.dto.siz;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Data;

/**
 *
 * @author fred
 */
@Data
public class SizUserWrapperDTO {

    @JsonProperty("users") // keep typo because its used in the interface.
    private Optional<SizUserDTO> user;
}

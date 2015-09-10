package io.siz.web.rest.dto.siz;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author fred
 */
@Data
public class SizUserDTO {

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Size(max = 50)
    @Pattern(regexp = "[0-9a-zA-Z.]{2,20}")
    private String username;

    @Pattern(regexp = "[0-9a-z]{64}")
    private String password;

    @Pattern(regexp = "[^;\t\n]{1,1024}")
    private String facebookToken;

}

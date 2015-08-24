package io.siz.security.anonymous;

import io.siz.security.AuthoritiesConstants;
import io.siz.service.util.RandomUtil;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author fred
 */
public class AnonymousUser extends User {

    public AnonymousUser() {
        super(
                "anonymous",
                RandomUtil.generatePassword(),
                Collections.singleton(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS)));
    }
}

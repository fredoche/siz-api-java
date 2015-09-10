package io.siz.repository.siz;

import io.siz.domain.siz.SizUser;
import io.siz.repository.secure.SecureMongoRepository;
import java.util.Optional;

/**
 *
 * @author fred
 */
public interface SizUserRepository extends SecureMongoRepository<SizUser, String> {

    public Optional<SizUser> findByUsername(String username);

    public Optional<SizUser> findByEmail(String username);

    public Optional<SizUser> findFacebookUserId(String facebookUserId);

}

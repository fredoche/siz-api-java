package io.siz.repository.siz;

import io.siz.domain.siz.SizToken;
import io.siz.repository.secure.SecureMongoRepository;
import java.util.Optional;

/**
 *
 * @author fred
 */
//@RepositoryRestResource(exported = false)
public interface SizTokenRepository extends SecureMongoRepository<SizToken, String> {

    public Optional<SizToken> findById(String id);
}

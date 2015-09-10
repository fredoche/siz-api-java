package io.siz.repository.siz;

import io.siz.domain.siz.SizToken;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author fred
 */
@RepositoryRestResource(exported = false)
public interface SizTokenRepository extends MongoRepository<SizToken, String> {

    @Cacheable
    public Optional<SizToken> findById(String id);
}

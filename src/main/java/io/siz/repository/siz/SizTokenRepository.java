package io.siz.repository.siz;

import io.siz.domain.siz.SizToken;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author fred
 */
public interface SizTokenRepository extends MongoRepository<SizToken, String> {

    public Optional<SizToken> findById(String id);
}

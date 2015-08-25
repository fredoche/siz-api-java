package io.siz.repository.siz;

import io.siz.domain.siz.ViewerProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author fred
 */
public interface ViewerProfileRepository extends MongoRepository<ViewerProfile, String>, ViewerProfileRepositoryCustom {
}

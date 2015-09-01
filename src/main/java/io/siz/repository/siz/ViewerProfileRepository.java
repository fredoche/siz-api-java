package io.siz.repository.siz;

import io.siz.domain.siz.ViewerProfile;
import io.siz.repository.secure.SecureMongoRepository;

/**
 *
 * @author fred
 */
public interface ViewerProfileRepository extends SecureMongoRepository<ViewerProfile, String>, ViewerProfileRepositoryCustom {
}

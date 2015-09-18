package io.siz.repository.siz;

import io.siz.domain.siz.ViewerProfile;
import io.siz.repository.secure.SecureMongoRepository;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author fred
 */
public interface ViewerProfileRepository extends SecureMongoRepository<ViewerProfile, String>, ViewerProfileRepositoryCustom {

    /**
     * On ne peut hydrater que on propre profil (sauf admin)
     *
     * @param id
     * @return
     */
    @PreAuthorize("#id == null or principal.viewerProfileId == #id or hasRole('ROLE_ADMIN')")
    Optional<ViewerProfile> findById(String id);

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public <S extends ViewerProfile> S insert(S entity);

}

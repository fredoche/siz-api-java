package io.siz.repository.siz;

import io.siz.domain.siz.SizUser;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;

/**
 *
 * @author fred
 */
@RepositoryRestResource(exported = false)
public interface SizUserRepository extends MongoRepository<SizUser, String> {

    final static String COMMON_SECURITY = "hasRole('ROLE_ADMIN') or (returnObject.isPresent() and returnObject.get().get";

    @PostAuthorize(COMMON_SECURITY + "Username().equals( #username))")
    public Optional<SizUser> findByUsername(String username);

    @PostAuthorize(COMMON_SECURITY + "Email().equals( #email))")
    public Optional<SizUser> findByEmail(String email);

    @PostAuthorize(COMMON_SECURITY + "FacebookUserId().equals( #facebookUserId))")
    public Optional<SizUser> findByFacebookUserId(String facebookUserId);
}

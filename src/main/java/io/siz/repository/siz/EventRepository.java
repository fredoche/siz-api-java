package io.siz.repository.siz;

import io.siz.domain.siz.Event;
import io.siz.repository.secure.SecureMongoRepository;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author fred
 */
public interface EventRepository extends SecureMongoRepository<Event, String> {

    @Override
    @PreAuthorize("hasRole('ROLE_USER')") // todo rajouter ACL cf http://docs.spring.io/spring-framework/docs/current/spring-framework-reference/html/expressions.html#expressions-collection-selection
    public <S extends Event> S insert(S entity);

    @Override
    @PreAuthorize("hasRole('ROLE_USER')") // todo rajouter ACL
    public <S extends Event> List<S> insert(Iterable<S> entities);

}

package io.siz.repository.siz;

import io.siz.domain.siz.Event;
import io.siz.domain.siz.EventType;
import io.siz.repository.secure.SecureMongoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author fred
 */
public interface EventRepository extends SecureMongoRepository<Event, String> {

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public <S extends Event> S insert(S entity);

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public <S extends Event> List<S> insert(Iterable<S> entities);

    @PreAuthorize("hasRole('ROLE_USER')")
    public Optional<Event> findByIpAndEventType(String remoteAddr, EventType type);
}

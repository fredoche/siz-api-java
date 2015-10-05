package io.siz.repository.siz;

import io.siz.domain.siz.Event;
import io.siz.domain.siz.EventType;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author fred
 */
@RepositoryRestResource(exported = false)
public interface EventRepository extends MongoRepository<Event, ObjectId> {

    @Override
    public <S extends Event> S insert(S entity);

    @Override
    public <S extends Event> List<S> insert(Iterable<S> entities);

    public Optional<Event> findByIpAndType(String remoteAddr, EventType type);
}

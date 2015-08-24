package io.siz.repository.siz;

import io.siz.domain.siz.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author fred
 */
public interface EventRepository extends MongoRepository<Event, String> {

}

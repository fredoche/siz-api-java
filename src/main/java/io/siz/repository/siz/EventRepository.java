package io.siz.repository.siz;

import io.siz.domain.siz.Event;
import io.siz.repository.secure.SecureMongoRepository;

/**
 *
 * @author fred
 */
public interface EventRepository extends SecureMongoRepository<Event, String> {

}

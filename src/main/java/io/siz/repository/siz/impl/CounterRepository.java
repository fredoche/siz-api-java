package io.siz.repository.siz.impl;

import io.siz.domain.siz.Counter;
import javax.inject.Inject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.stereotype.Repository;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Update;

/**
 *
 * @author fred
 */
@Repository
public class CounterRepository {

    @Inject
    private MongoTemplate template;

    public Counter getNextSequence(String counter) {
        Query q = query(where("id").is(counter));
        Update u = new Update().inc("seq", 1);
        return template.findAndModify(q, u, Counter.class);
    }
}

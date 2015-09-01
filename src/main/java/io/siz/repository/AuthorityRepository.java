package io.siz.repository;

import io.siz.domain.Authority;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Spring Data MongoDB repository for the Authority entity.
 */
@RepositoryRestResource(exported = false)
public interface AuthorityRepository extends MongoRepository<Authority, String> {
}

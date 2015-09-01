package io.siz.repository;

import io.siz.domain.PersistentAuditEvent;
import org.joda.time.LocalDateTime;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Spring Data MongoDB repository for the PersistentAuditEvent entity.
 */
@RepositoryRestResource(exported = false)
public interface PersistenceAuditEventRepository extends MongoRepository<PersistentAuditEvent, String> {

    List<PersistentAuditEvent> findByPrincipal(String principal);

    List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(String principal, LocalDateTime after);

    List<PersistentAuditEvent> findAllByAuditEventDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
}

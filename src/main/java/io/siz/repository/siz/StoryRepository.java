package io.siz.repository.siz;

import io.siz.domain.siz.Story;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Spring Data MongoDB repository for the Story entity.
 */
@PreAuthorize("hasRole('ROLE_USER')")
public interface StoryRepository extends MongoRepository<Story, String> {

    public Optional<Story> findBySlug(String slug);
}

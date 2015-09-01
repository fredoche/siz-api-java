package io.siz.repository.siz;

import io.siz.domain.siz.Story;

import io.siz.repository.secure.SecureMongoRepository;

import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Spring Data MongoDB repository for the Story entity.
 */
public interface StoryRepository extends SecureMongoRepository<Story, String> {

    @PreAuthorize("hasRole('ROLE_USER')")
    public Optional<Story> findBySlug(String slug);
}

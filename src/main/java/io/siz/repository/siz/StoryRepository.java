package io.siz.repository.siz;

import io.siz.domain.siz.Story;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Story entity.
 */
public interface StoryRepository extends MongoRepository<Story, String> {

    public Optional<Story> findBySlug(String slug);
}

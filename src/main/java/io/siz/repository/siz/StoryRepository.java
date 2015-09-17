package io.siz.repository.siz;

import io.siz.domain.siz.Story;

import io.siz.repository.secure.SecureMongoRepository;
import java.util.List;

import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Spring Data MongoDB repository for the Story entity.
 * TODO mettre les annotations de cache.
 */
public interface StoryRepository extends SecureMongoRepository<Story, String> {

    @PreAuthorize("hasRole('ROLE_USER')")
    public Stream<Story> findAll(Sort sort, Pageable pageable);

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public Story findOne(String id);

    @PreAuthorize("hasRole('ROLE_USER')")
    public Optional<Story> findBySlug(String slug);

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public Iterable<Story> findAll(Iterable<String> ids);

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<Story> findAll(Pageable pageable);

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Story> findAll();

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Story> findAll(Sort sort);

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public <S extends Story> S insert(S entity);

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public <S extends Story> List<S> insert(Iterable<S> entities);

    @PreAuthorize("hasRole('ROLE_USER')")
    public Optional<Story> getBySlug(String slug);

    @PreAuthorize("hasRole('ROLE_USER')")
    public Stream<Story> findAllIn(List<String> ids, Page page, Sort sort);

    /**
     * http://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongodb.repositories.queries
     * @param alreadySeenStories
     * @param dislikedTags
     * @return 
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    public Stream<Story> findAllWhereIdNotInAndTagsContainingNot(List<String> alreadySeenStories, List<String> dislikedTags, Sort sort);
}

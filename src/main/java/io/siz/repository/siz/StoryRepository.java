package io.siz.repository.siz;

import io.siz.domain.siz.Story;

import io.siz.repository.secure.SecureMongoRepository;
import java.util.List;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Spring Data MongoDB repository for the Story entity.
 */
public interface StoryRepository extends SecureMongoRepository<Story, String> {

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
}

package io.siz.repository.secure;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author fred
 */
@NoRepositoryBean
public interface SecureMongoRepository<T, ID extends Serializable> extends MongoRepository<T, ID> {

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public <S extends T> List<S> insert(Iterable<S> entities);

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public <S extends T> S insert(S entity);

    /**
     * {@inheritDoc}
     */
    @Override
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<T> findAll(Sort sort);

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<T> findAll();

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public <S extends T> List<S> save(Iterable<S> entites);

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<T> findAll(Pageable pageable);

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteAll();

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(Iterable<? extends T> entities);

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(T entity);

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(ID id);

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public long count();

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Iterable<T> findAll(Iterable<ID> ids);

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean exists(ID id);

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public T findOne(ID id);

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public <S extends T> S save(S entity);

}

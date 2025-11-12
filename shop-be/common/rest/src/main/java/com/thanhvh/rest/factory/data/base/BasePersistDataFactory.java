/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.factory.data.base;

import com.thanhvh.exception.ErrorCode;
import com.thanhvh.exception.InvalidException;
import com.thanhvh.rest.config.CacheFactoryConfig;
import com.thanhvh.rest.factory.data.IPersistDataFactory;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.filter.ISearchFilter;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BasePagingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Persist data factory
 *
 * @param <I> Id model
 * @param <T> Info model
 * @param <D> Detail model
 * @param <A> id entity
 * @param <E> Entity
 * @param <R> repository
 */
@Slf4j
@SuppressWarnings("unchecked")
@Transactional
public abstract class BasePersistDataFactory<I, T extends IBaseData<I>, D extends T, A, E, R extends ListCrudRepository<E, A> & ListPagingAndSortingRepository<E, A>>
        extends BaseDataFactory<I, T, D>
        implements IPersistDataFactory<T, D, E> {

    /**
     * R
     */
    protected final R repository;

    /**
     * @param repository repository
     */
    protected BasePersistDataFactory(R repository) {
        this.repository = repository;
    }

    @Override
    protected D preCreate(D detail) throws InvalidException {
        if (existByDetail(detail)) {
            throw new InvalidException(conflict());
        }
        return detail;
    }

    @Override
    protected D aroundCreate(D detail) throws InvalidException {
        E entity = repository.save(createConvertToEntity(detail));
        detail = convertToDetail(entity);
        postCreate(entity, detail);
        return detail;
    }

    /**
     * Post create
     *
     * @param entity entity
     * @param detail detail model
     * @throws InvalidException invalid
     */
    protected void postCreate(E entity, D detail) throws InvalidException {
    }

    @Override
    protected D aroundUpdate(I id, D detail) throws InvalidException {
        E oldEntity = getEntity(id, detail).orElseThrow(() -> new InvalidException(notFound()));
        updateConvertToEntity(oldEntity, detail);
        E entity = repository.save(oldEntity);
        detail = convertToDetail(entity);
        postUpdate(entity, detail);
        return detail;
    }

    /**
     * Post update
     *
     * @param entity entity
     * @param detail detail model
     * @throws InvalidException invalid
     */
    protected void postUpdate(E entity, D detail) throws InvalidException {
    }

    @Override
    public boolean existByDetail(D detail) throws InvalidException {
        if (detail.getId() != null) {
            return repository.existsById(convertId(detail.getId()));
        }
        return false;
    }

    @Override
    public <F extends IFilter> boolean existByFilter(I id, F filter) throws InvalidException {
        return repository.existsById(convertId(id));
    }

    @Override
    protected <F extends IFilter> void aroundDelete(I id, F filter) throws InvalidException {
        try {
            repository.deleteById(convertId(id));
        } catch (Exception e) {
            throw new InvalidException(badRequest());
        }
    }

    @Override
    protected <F extends IFilter> D aroundGetDetail(I id, F filter) throws InvalidException {
        E entity = getEntity(id, filter).orElseThrow(() -> new InvalidException(notFound()));
        D detail = convertToDetail(entity);
        postDetail(entity, detail);
        return detail;
    }

    @Override
    protected <F extends IFilter> BasePagingResponse<T> aroundGetPage(F filter, Integer number, Integer size) throws InvalidException {
        Page<E> pageEntity = pageQuery(filter, number, size);
        return BasePagingResponse.<T>builder()
                .content(convertList(pageEntity.getContent()))
                .pageNumber(pageEntity.getNumber())
                .pageSize(pageEntity.getSize())
                .totalElements(pageEntity.getTotalElements())
                .build();
    }

    @Override
    protected <F extends IFilter> BasePagingResponse<D> aroundGetPageDetail(F filter, Integer number, Integer size) throws InvalidException {
        Page<E> pageEntity = pageQuery(filter, number, size);
        return BasePagingResponse.<D>builder()
                .content(convertListDetail(pageEntity.getContent()))
                .pageNumber(pageEntity.getNumber())
                .pageSize(pageEntity.getSize())
                .totalElements(pageEntity.getTotalElements())
                .build();
    }

    @Override
    protected <F extends IFilter> List<T> aroundGetList(F filter) throws InvalidException {
        return convertList(listQuery(filter));
    }

    @Override
    protected List<T> aroundGetList() throws InvalidException {
        return convertList(listQuery());
    }

    @Override
    protected <F extends IFilter> List<D> aroundGetListDetail(F filter) throws InvalidException {
        return convertListDetail(listQuery(filter));
    }

    @Override
    protected List<D> aroundGetListDetail() throws InvalidException {
        return convertListDetail(listQuery());
    }


    /**
     * Using repository query
     *
     * @param filter filter
     * @param <F>    filter type
     * @return list entity
     * @throws InvalidException invalid
     */
    protected <F extends IFilter> Iterable<E> listQuery(F filter) throws InvalidException {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new InvalidException(ErrorCode.SERVER_ERROR, e);
        }
    }

    /**
     * Using repository find all
     *
     * @return list entity
     * @throws InvalidException invalid
     */
    protected Iterable<E> listQuery() throws InvalidException {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new InvalidException(ErrorCode.SERVER_ERROR, e);
        }
    }

    /**
     * Using repository query {@link Page}
     *
     * @param filter filter
     * @param number number
     * @param size   size
     * @param <F>    filter type
     * @return {@link Page}
     * @throws InvalidException invalid
     */
    protected <F extends IFilter> Page<E> pageQuery(F filter, Integer number, Integer size) throws InvalidException {
        try {
            return repository.findAll(PageRequest.of(number, size));
        } catch (Exception e) {
            throw new InvalidException(ErrorCode.SERVER_ERROR, e);
        }
    }

    @Override
    protected <F extends ISearchFilter> BasePagingResponse<T> aroundSearchInfo(F search, Integer number, Integer size) throws InvalidException {
        Page<E> pageEntity = searchQuery(search, number, size);
        return BasePagingResponse.<T>builder()
                .content(convertList(pageEntity.getContent()))
                .pageNumber(pageEntity.getNumber())
                .pageSize(pageEntity.getSize())
                .totalElements(pageEntity.getTotalElements())
                .build();
    }

    @Override
    protected <F extends ISearchFilter> BasePagingResponse<D> aroundSearchDetail(F search, Integer number, Integer size) throws InvalidException {
        Page<E> pageEntity = searchQuery(search, number, size);
        return BasePagingResponse.<D>builder()
                .content(convertListDetail(pageEntity.getContent()))
                .pageNumber(pageEntity.getNumber())
                .pageSize(pageEntity.getSize())
                .totalElements(pageEntity.getTotalElements())
                .build();
    }

    /**
     * searchPageQuery
     *
     * @param <F>    type of SearchFilter
     * @param search search object
     * @param number number
     * @param size   size
     * @return Page info
     * @throws InvalidException InvalidException
     */
    protected <F extends ISearchFilter> Page<E> searchQuery(F search, Integer number, Integer size) throws InvalidException {
        try {
            return repository.findAll(PageRequest.of(number, size));
        } catch (Exception e) {
            throw new InvalidException(ErrorCode.SERVER_ERROR, e);
        }
    }

    /**
     * Get entity
     *
     * @param id     id
     * @param detail detail
     * @return entity
     * @throws InvalidException invalid
     */
    protected Optional<E> getEntity(I id, D detail) throws InvalidException {
        return repository.findById(convertId(id));
    }

    /**
     * Get entity by filter
     *
     * @param id     id
     * @param filter filter
     * @param <F>    filter type
     * @return entity
     * @throws InvalidException invalid
     */
    protected <F extends IFilter> Optional<E> getEntity(I id, F filter) throws InvalidException {
        return repository.findById(convertId(id));
    }

    /**
     * Convert Model id to Entity id
     *
     * @param id model id
     * @return entity id
     * @throws InvalidException invalid
     */
    protected A convertId(I id) throws InvalidException {
        try {
            return (A) id;
        } catch (ClassCastException e) {
            throw new InvalidException(ErrorCode.CAN_NOT_CAST_ID_ERROR);
        }
    }

    /**
     * Post get detail model
     *
     * @param entity entity
     * @param detail detail
     * @throws InvalidException invalid
     */
    protected void postDetail(E entity, D detail) throws InvalidException {
    }

    /**
     * Post get info model
     *
     * @param entity entity
     * @param info   detail
     * @throws InvalidException invalid
     */
    protected void postInfo(E entity, T info) throws InvalidException {
    }

    @Override
    protected <C extends CacheFactoryConfig<D>> @lombok.NonNull C cacheFactoryDetailConfig() {
        return (C) new CacheFactoryConfig<D>() {
            @Override
            @NonNull
            public Class<? extends D> objectClass() {
                return getDetailClass();
            }
        };
    }

    /**
     * Convert list entity to list info
     *
     * @param entities list entity
     * @return list info
     * @throws InvalidException invalid
     */
    protected List<T> convertList(Iterable<E> entities) throws InvalidException {
        List<T> response = new ArrayList<>();
        for (E entity : entities) {
            response.add(convertToInfo(entity));
        }
        return response;
    }

    /**
     * Convert list entity to list detail
     *
     * @param entities list entity
     * @return list info
     * @throws InvalidException invalid
     */
    protected List<D> convertListDetail(Iterable<E> entities) throws InvalidException {
        List<D> response = new ArrayList<>();
        for (E entity : entities) {
            response.add(convertToDetail(entity));
        }
        return response;
    }

    @Override
    public List<D> createModels(List<D> models) throws InvalidException {
        List<D> result = new ArrayList<>();
        if (models != null) {
            for (D model : models) {
                result.add(createModel(model));
            }
        }
        return result;
    }

    @Override
    protected <F extends IFilter> T aroundGetInfo(I id, F filter) throws InvalidException {
        E entity = getEntity(id, filter).orElseThrow(() -> new InvalidException(notFound()));
        T info = convertToInfo(entity);
        postInfo(entity, info);
        return info;
    }
}

package com.thanhvh.rest.factory.data.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thanhvh.cache.CreateCacheException;
import com.thanhvh.cache.ITimeRedisCacheManager;
import com.thanhvh.exception.ErrorCode;
import com.thanhvh.exception.IErrorCode;
import com.thanhvh.exception.InvalidException;
import com.thanhvh.rest.config.CacheFactoryConfig;
import com.thanhvh.rest.factory.data.IDataFactory;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.filter.ISearchFilter;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BasePagingResponse;
import com.thanhvh.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * BaseDataFactory
 * <br>
 * Implement caching, layer step logic
 *
 * @param <I> id type
 * @param <T> info type
 * @param <D> detail type
 */
@Slf4j
@Transactional
@SuppressWarnings("unchecked")
public abstract class BaseDataFactory<I, T extends IBaseData<I>, D extends T> implements
        IDataFactory<I, T, D> {
    /**
     * Cache manager
     */
    @Autowired(required = false)
    protected ITimeRedisCacheManager cacheManager;

    @Override
    public D createModel(D detail) throws InvalidException {
        detail = aroundCreate(preCreate(detail));
        cachePutCreate(detail);
        return detail;
    }

    /**
     * Cache put when create
     *
     * @param detail detail
     * @throws InvalidException cannot clean cache collection
     */
    protected void cachePutCreate(D detail) throws InvalidException {
        clearCollection();
    }

    /**
     * pre create logic
     *
     * @param detail detail model
     * @return detail
     * @throws InvalidException invalid
     */
    protected D preCreate(D detail) throws InvalidException {
        return detail;
    }

    /**
     * Create logic
     *
     * @param detail detail model
     * @return model after create
     * @throws InvalidException invalid
     */
    protected abstract D aroundCreate(D detail) throws InvalidException;

    @Override
    public D updateModel(D detail) throws InvalidException {
        if (detail.getId() == null) {
            throw new InvalidException(badRequest());
        }
        detail = aroundUpdate(detail.getId(), preUpdate(detail.getId(), detail));
        cachePutUpdate(detail);
        return detail;
    }

    /**
     * Cache put when update
     *
     * @param detail detail
     * @throws InvalidException cannot clean cache collection
     */
    protected void cachePutUpdate(D detail) throws InvalidException {
        cacheEvict(makeKey(detail.getId(), null));
        clearCollection();
    }

    /**
     * Pre update Logic
     *
     * @param id     id model
     * @param detail detail model
     * @return detail
     * @throws InvalidException invalid
     */
    protected D preUpdate(I id, D detail) throws InvalidException {
        return detail;
    }

    /**
     * Update Logic
     *
     * @param id     id model
     * @param detail detail model
     * @return Model after update
     * @throws InvalidException invalid
     */
    protected abstract D aroundUpdate(I id, D detail) throws InvalidException;


    @Override
    public <F extends IFilter> boolean deleteModel(I id, F filter) throws InvalidException {
        F preFilter = preDelete(id, filter);
        aroundDelete(id, preFilter);
        postDelete(id, preFilter);
        cacheModelEvict(id, preFilter);
        return true;
    }

    /**
     * Cache evict when delete
     *
     * @param id     id
     * @param filter filter
     * @param <F>    filter type
     * @throws InvalidException invalid
     */
    protected <F extends IFilter> void cacheModelEvict(I id, F filter) throws InvalidException {
        cacheEvict(makeKey(id, null));
        clearCollection();
    }

    /**
     * Delete logic
     *
     * @param id     id
     * @param filter filter
     * @param <F>    filter type
     * @throws InvalidException invalid
     */
    protected abstract <F extends IFilter> void aroundDelete(I id, F filter) throws InvalidException;

    /**
     * Post logic delete
     *
     * @param id     id
     * @param filter filter
     * @param <F>    filter type
     * @throws InvalidException invalid
     */
    protected <F extends IFilter> void postDelete(I id, F filter) throws InvalidException {
    }

    /**
     * Pre logic delete
     *
     * @param id     id
     * @param filter filter
     * @param <F>    filter type
     * @return filter
     * @throws InvalidException invalid
     */
    protected <F extends IFilter> F preDelete(I id, F filter) throws InvalidException {
        return filter;
    }

    @Override
    public <F extends IFilter> D getDetailModel(I id, F filter) throws InvalidException {
        D detail = getCacheModel(cacheFactoryDetailConfig(), id, null);
        if (detail != null) {
            return detail;
        }
        F preFilter = preGetDetail(id, filter);
        detail = aroundGetDetail(id, preFilter);
        if (detail == null) {
            throw new InvalidException(notFound());
        }
        cacheModelPut(cacheFactoryDetailConfig(), detail.getId(), null, detail);
        return detail;
    }

    @Override
    public <F extends IFilter> T getInfoModel(I id, F filter) throws InvalidException {
        T info = getCacheModel(cacheFactoryInfoConfig(), id, null);
        if (info != null) {
            return info;
        }
        F preFilter = preGetInfo(id, filter);
        info = aroundGetInfo(id, preFilter);
        if (info == null) {
            throw new InvalidException(notFound());
        }
        cacheModelPut(cacheFactoryInfoConfig(), info.getId(), null, info);
        return info;
    }

    /**
     * Pre get info logic
     *
     * @param id     id
     * @param filter filter
     * @param <F>    filter type
     * @return filter
     * @throws InvalidException invalid
     */
    protected <F extends IFilter> F preGetInfo(I id, F filter) throws InvalidException {
        return filter;
    }

    /**
     * Get info logic
     *
     * @param id     id
     * @param filter filter
     * @param <F>    filter type
     * @return model detail
     * @throws InvalidException invalid
     */
    protected abstract <F extends IFilter> T aroundGetInfo(I id, F filter) throws InvalidException;

    /**
     * @param config config
     * @param id     id
     * @param filter filter
     * @param <F>    type of filter
     * @param <M>    type of model
     * @return model
     */
    protected <F extends IFilter, M extends T> M getCacheModel(CacheFactoryConfig<M> config, I id, F filter) {
        M detail = null;
        if (id != null) {
            detail = isCache(config) && config.getModel() ? cacheGet(config, makeKey(id, filter), config.objectClass()) : null;
        }
        return detail;
    }


    /**
     * Get detail logic
     *
     * @param id     id
     * @param filter filter
     * @param <F>    filter type
     * @return model detail
     * @throws InvalidException invalid
     */
    protected abstract <F extends IFilter> D aroundGetDetail(I id, F filter) throws InvalidException;

    /**
     * Pre get detail logic
     *
     * @param id     id
     * @param filter filter
     * @param <F>    filter type
     * @return filter
     * @throws InvalidException invalid
     */
    protected <F extends IFilter> F preGetDetail(I id, F filter) throws InvalidException {
        return filter;
    }

    /**
     * getInfoList
     *
     * @param filter filter
     * @param <F>    IFilter
     * @return List
     * @throws InvalidException InvalidException
     */
    @Override
    public <F extends IFilter> List<T> getInfoList(F filter) throws InvalidException {
        List<T> infos = getCacheList(cacheFactoryInfoConfig(), filter);
        if (infos != null) {
            return infos;
        }
        F preFilter = preGetList(filter);
        infos = aroundGetList(preFilter);
        cacheListPut(cacheFactoryInfoConfig(), filter, infos);
        return infos;
    }

    /**
     * @param config config
     * @param filter filter
     * @param <M>    type of model
     * @param <E>    type of cache config
     * @param <F>    type of filter
     * @return list of model
     */
    protected <M extends T, E, F extends IFilter> List<M> getCacheList(CacheFactoryConfig<E> config, F filter) {
        Object key = makeKey(null, filter);
        return isCache(config) && config.getListFilter() ? (List<M>) cacheListGet(config, key, List.class) : null;
    }

    /**
     * preGetList
     *
     * @param filter filter
     * @param <F>    filter type
     * @return filter
     * @throws InvalidException invalid
     */
    protected <F extends IFilter> F preGetList(F filter) throws InvalidException {
        return filter;
    }

    /**
     * aroundGetList
     *
     * @param filter filter
     * @param <F>    filter type
     * @return List info
     * @throws InvalidException invalid
     */
    protected abstract <F extends IFilter> List<T> aroundGetList(F filter) throws InvalidException;

    @Override
    public List<T> getInfoList() throws InvalidException {
        List<T> infos = getCacheList(cacheFactoryInfoConfig(), null);
        if (infos != null) {
            return infos;
        }
        preGetList();
        infos = aroundGetList();
        cacheListPut(cacheFactoryInfoConfig(), null, infos);
        return infos;
    }

    /**
     * preGetList
     *
     * @throws InvalidException invalid
     */
    protected void preGetList() throws InvalidException {
    }

    /**
     * aroundGetList
     *
     * @return List info
     * @throws InvalidException invalid
     */
    protected abstract List<T> aroundGetList() throws InvalidException;

    /**
     * @param filter filter filter
     * @param number number of page
     * @param size   number of size
     * @param <F>    IFilter
     * @return BasePagingResponse
     * @throws InvalidException InvalidException
     */
    @Override
    public <F extends IFilter> BasePagingResponse<T> getInfoPage(F filter, Integer number, Integer size) throws InvalidException {
        BasePagingResponse<T> response = getCachePage(cacheFactoryInfoConfig(), filter, number, size);
        if (response != null) {
            return response;
        }
        F preFilter = preGetPage(filter, number, size);
        response = aroundGetPage(preFilter, number, size);
        cachePagePut(cacheFactoryInfoConfig(), filter, number, size, response);
        return response;
    }

    @Override
    public <F extends IFilter> BasePagingResponse<D> getDetailPage(F filter, Integer number, Integer size) throws InvalidException {
        BasePagingResponse<D> response = getCachePage(cacheFactoryDetailConfig(), filter, number, size);
        if (response != null) {
            return response;
        }
        F preFilter = preGetPageDetail(filter, number, size);
        response = aroundGetPageDetail(preFilter, number, size);
        cachePagePut(cacheFactoryDetailConfig(), filter, number, size, response);
        return response;
    }

    /**
     * aroundGetPageDetail
     *
     * @param filter filter
     * @param number number
     * @param size   size
     * @param <F>    filter type
     * @return Page
     * @throws InvalidException invalid
     */
    protected abstract <F extends IFilter> BasePagingResponse<D> aroundGetPageDetail(F filter, Integer number, Integer size) throws InvalidException;

    /**
     * preGetPageDetail
     *
     * @param filter filter
     * @param number number
     * @param size   size
     * @param <F>    filter size
     * @return filter
     * @throws InvalidException invalid
     */

    protected <F extends IFilter> F preGetPageDetail(F filter, Integer number, Integer size) throws InvalidException {
        return filter;
    }


    /**
     * cachePageGet
     *
     * @param <F>                type
     * @param cacheFactoryConfig cache config
     * @param key                key
     * @return list info
     */
    protected <F> F cachePageGet(CacheFactoryConfig<?> cacheFactoryConfig, Object key) {
        try {
            return cacheManager.get(cacheFactoryConfig.cacheNamePaging(), key, (Class<F>) BasePagingResponse.class);
        } catch (Exception e) {
            log.warn("CacheListGet Exception {} ", e.getMessage());
        }
        return null;
    }

    /**
     * push page
     *
     * @param <E>    type of cache config
     * @param key    key
     * @param values list value
     * @param config cache config
     */
    protected <E> void cachePagePut(CacheFactoryConfig<E> config, Object key, Object values) {
        try {
            cacheManager.put(config.cacheNamePaging(), key, values, config.collectionTtl());
        } catch (Exception e) {
            log.warn("CacheListPut Exception {} ", e.getMessage());
        }
    }

    /**
     * @param config config
     * @param filter filter
     * @param number number
     * @param size   size
     * @param <M>    type of model
     * @param <E>    type of cache config
     * @param <F>    type of filter
     * @return BasePagingResponse model
     */
    protected <M extends T, E, F extends IFilter> BasePagingResponse<M> getCachePage(CacheFactoryConfig<E> config, F filter, Integer number, Integer size) {
        Object key = makeKey(null, filter, number, size);
        return isCache(config) && config.getPaging() ? (BasePagingResponse<M>) cachePageGet(config, key) : null;
    }

    /**
     * @param config config
     * @param filter filter
     * @param number number
     * @param size   size
     * @param page   page
     * @param <M>    type of model
     * @param <E>    type of cache config
     * @param <F>    type of filter
     */
    protected <M extends T, E, F extends IFilter> void cachePagePut(CacheFactoryConfig<E> config, F filter, Integer number, Integer size, BasePagingResponse<M> page) {
        if (isCache(config) && config.getPaging()) {
            cachePagePut(config, makeKey(null, filter, number, size), page);
        }
    }

    /**
     * @param config config
     * @param filter filter
     * @param data   data
     * @param <M>    type of model
     * @param <E>    type of cache config
     * @param <F>    type of filter
     */
    protected <M extends T, E, F extends IFilter> void cacheListPut(CacheFactoryConfig<E> config, F filter, List<M> data) {
        if (isCache(config) && config.getList()) {
            cacheListPut(config, makeKey(null, filter), data);
        }
    }

    @Override
    public <F extends IFilter> List<D> getDetailList(F filter) throws InvalidException {
        List<D> details = getCacheList(cacheFactoryDetailConfig(), filter);
        if (details != null) {
            return details;
        }
        F preFilter = preGetListDetail(filter);
        details = aroundGetListDetail(preFilter);
        cacheListPut(cacheFactoryDetailConfig(), filter, details);
        return details;
    }

    /**
     * preGetList
     *
     * @param filter filter
     * @param <F>    filter type
     * @return filter
     * @throws InvalidException invalid
     */
    protected <F extends IFilter> F preGetListDetail(F filter) throws InvalidException {
        return filter;
    }

    /**
     * aroundGetList
     *
     * @param filter filter
     * @param <F>    filter type
     * @return List info
     * @throws InvalidException invalid
     */
    protected abstract <F extends IFilter> List<D> aroundGetListDetail(F filter) throws InvalidException;

    @Override
    public List<D> getDetailList() throws InvalidException {
        List<D> details = getDetailList(null);
        if (details != null) {
            return details;
        }
        preGetListDetail();
        details = aroundGetListDetail();
        cacheListPut(cacheFactoryDetailConfig(), null, details);
        return details;
    }


    /**
     * preGetList
     *
     * @throws InvalidException invalid
     */
    protected void preGetListDetail() throws InvalidException {
    }

    /**
     * aroundGetList
     *
     * @return List info
     * @throws InvalidException invalid
     */
    protected abstract List<D> aroundGetListDetail() throws InvalidException;


    @Override
    public <F extends ISearchFilter> BasePagingResponse<T> searchInfo(F search, Integer number, Integer size) throws InvalidException {
        BasePagingResponse<T> response = getCacheSearch(cacheFactoryInfoConfig(), search);
        if (response != null) {
            return response;
        }
        response = aroundSearchInfo(search, number, size);
        cacheSearchPut(cacheFactoryInfoConfig(), search, number, size, response);
        return response;
    }

    @Override
    public <F extends ISearchFilter> BasePagingResponse<D> searchDetail(F search, Integer number, Integer size) throws InvalidException {
        BasePagingResponse<D> response = getCacheSearch(cacheFactoryDetailConfig(), search);
        if (response != null) {
            return response;
        }
        response = aroundSearchDetail(search, number, size);
        cacheSearchPut(cacheFactoryDetailConfig(), search, number, size, response);
        return response;
    }

    /**
     * @param search search object
     * @param number number
     * @param size   size
     * @param <F>    type of filter
     * @return BasePagingResponse info model
     * @throws InvalidException ex
     */
    protected abstract <F extends ISearchFilter> BasePagingResponse<T> aroundSearchInfo(F search, Integer number, Integer size) throws InvalidException;

    /**
     * @param search search object
     * @param number number
     * @param size   size
     * @param <F>    type of filter
     * @return BasePagingResponse detail model
     * @throws InvalidException ex
     */
    protected abstract <F extends ISearchFilter> BasePagingResponse<D> aroundSearchDetail(F search, Integer number, Integer size) throws InvalidException;

    /**
     * @param config     config
     * @param searchText searchText
     * @param <M>        type of model
     * @param <E>        type of cache config
     * @param <F>        type of filter
     * @return BasePagingResponse model
     */
    protected <M extends T, E, F extends ISearchFilter> BasePagingResponse<M> getCacheSearch(CacheFactoryConfig<E> config, F searchText) {
        Object key = makeKey(null, searchText);
        return isCache(config) && config.getSearch() ? (BasePagingResponse<M>) cacheSearchGet(config, key, BasePagingResponse.class) : null;
    }

    /**
     * @param config     config
     * @param searchText searchText
     * @param number     number
     * @param size       size
     * @param page       page
     * @param <M>        type of model
     * @param <E>        type of cache config
     * @param <F>        type of filter
     */
    protected <M extends T, E, F extends ISearchFilter> void cacheSearchPut(CacheFactoryConfig<E> config, F searchText, Integer number, Integer size, BasePagingResponse<M> page) {
        Object key = makeKey(null, searchText, number, size);
        if (isCache(config) && config.getSearch()) {
            cacheSearchPut(config, key, page);
        }
    }


    /**
     * aroundGetPage
     *
     * @param filter filter
     * @param number number
     * @param size   size
     * @param <F>    filter type
     * @return Page
     * @throws InvalidException invalid
     */
    protected abstract <F extends IFilter> BasePagingResponse<T> aroundGetPage(F filter, Integer number, Integer size) throws InvalidException;

    /**
     * preGetPage
     *
     * @param filter filter
     * @param number number
     * @param size   size
     * @param <F>    filter size
     * @return filter
     * @throws InvalidException invalid
     */

    protected <F extends IFilter> F preGetPage(F filter, Integer number, Integer size) throws InvalidException {
        return filter;
    }

    /**
     * Make data key
     *
     * @param id     id
     * @param filter filter
     * @return key
     */
    protected Object makeKey(Object id, Object filter) {
        return makeKey(id, filter, null, null);
    }

    /**
     * Make data key
     *
     * @param id     id
     * @param filter filter
     * @param number number
     * @param size   size
     * @return key
     */
    protected Object makeKey(Object id, Object filter, Integer number, Integer size) {
        try {
            if (filter == null && id == null && number == null && size == null) {
                return encode("all");
            }
            if (filter == null) {
                return id;
            }
            Map<String, Object> key = new HashMap<>(4);
            key.put("id", id);
            key.put("filter", MapperUtil.writeValueAsString(filter));
            key.put("number", number);
            key.put("size", size);
            return encode(key);
        } catch (JsonProcessingException e) {
            log.warn("MakeKey JsonProcessingException {} ", e.getMessage());
        }
        return null;
    }

    /**
     * cachePut
     *
     * @param <E>    type of cache config
     * @param config config
     * @param key    key
     * @param value  value
     */
    protected <E> void cachePut(CacheFactoryConfig<E> config, Object key, Object value) {
        try {
            cacheManager.put(config.cacheName(), key, value, config.singleTtl());
        } catch (Exception e) {
            log.warn("cacheModelPut Exception {} ", e.getMessage());
        }
    }

    /**
     * @param config config
     * @param id     id
     * @param filter filter
     * @param model  model
     * @param <E>    type of cache config
     * @param <M>    type of model
     * @param <F>    type of filter
     */
    protected <E, M extends T, F extends IFilter> void cacheModelPut(CacheFactoryConfig<E> config, I id, F filter, M model) {
        if (isCache(config) && config.getModel()) {
            cachePut(config, makeKey(id, null), model);
        }
    }


    /**
     * push list
     *
     * @param <E>    type of cache config
     * @param config config
     * @param key    key
     * @param values list value
     */
    protected <E> void cacheListPut(CacheFactoryConfig<E> config, Object key, Object values) {
        try {
            cacheManager.put(config.cacheNameList(), key, values, config.collectionTtl());
        } catch (Exception e) {
            log.warn("CacheListPut Exception {} ", e.getMessage());
        }
    }

    /**
     * push search
     *
     * @param <E>    type of cache config
     * @param config config
     * @param key    key
     * @param values list value
     */
    protected <E> void cacheSearchPut(CacheFactoryConfig<E> config, Object key, Object values) {
        try {
            cacheManager.put(config.cacheNameSearch(), key, values, config.collectionTtl());
        } catch (Exception e) {
            log.warn("CacheSearchPut Exception {} ", e.getMessage());
        }
    }

    /**
     * encode
     *
     * @param key key
     * @return hashcode
     * @throws JsonProcessingException json error
     */
    protected int encode(Object key) throws JsonProcessingException {
        return Objects.hashCode(Objects.requireNonNullElse(MapperUtil.writeValueAsString(key), "all"));
    }

    /**
     * cacheListGet
     *
     * @param <E>           type of cache config
     * @param config        config
     * @param key           key
     * @param responseClass class
     * @param <F>           type
     * @return list info
     */
    protected <E, F> F cacheListGet(CacheFactoryConfig<E> config, Object key, Class<F> responseClass) {
        try {
            return cacheManager.get(config.cacheNameList(), key, responseClass);
        } catch (Exception e) {
            log.warn("CacheListGet Exception {} ", e.getMessage());
        }
        return null;
    }

    /**
     * cacheSearchGet
     *
     * @param <E>           type of cache config
     * @param config        config
     * @param key           key
     * @param responseClass class
     * @param <F>           type
     * @return list info
     */
    protected <E, F> F cacheSearchGet(CacheFactoryConfig<E> config, Object key, Class<F> responseClass) {
        try {
            return cacheManager.get(config.cacheNameSearch(), key, responseClass);
        } catch (Exception e) {
            log.warn("cacheSearchGet Exception {} ", e.getMessage());
        }
        return null;
    }

    /**
     * cacheEvict
     *
     * @param key key
     * @throws InvalidException when get cache fail
     */
    protected void cacheEvict(Object key) throws InvalidException {
        try {
            if (isCache(cacheFactoryInfoConfig())) {
                cacheManager.evict(cacheFactoryInfoConfig().cacheName(), key);
                for (String cacheForward : cacheFactoryInfoConfig().cacheNameForward()) {
                    cacheManager.clear(cacheForward);
                }
            }
            if (isCache(cacheFactoryDetailConfig())) {
                cacheManager.evict(cacheFactoryDetailConfig().cacheName(), key);
                for (String cacheForward : cacheFactoryDetailConfig().cacheNameForward()) {
                    cacheManager.clear(cacheForward);
                }
            }
        } catch (Exception e) {
            log.error("CacheEvict Exception {} ", e.getMessage());
            throw new InvalidException(ErrorCode.GET_CACHE_ERROR);
        }
    }

    /**
     * clearCollection
     *
     * @throws InvalidException when get cache fail
     */
    protected void clearCollection() throws InvalidException {
        if (isCache(cacheFactoryDetailConfig())) {
            try {
                cacheManager.clear(cacheFactoryDetailConfig().cacheNameList());
                cacheManager.clear(cacheFactoryDetailConfig().cacheNameSearch());
                cacheManager.clear(cacheFactoryDetailConfig().cacheNamePaging());
                clearCacheCollectionForward(cacheFactoryDetailConfig());
            } catch (CreateCacheException e) {
                throw new InvalidException(ErrorCode.CLEAR_CACHE_ERROR);
            }
        }
        if (isCache(cacheFactoryInfoConfig())) {
            try {
                cacheManager.clear(cacheFactoryInfoConfig().cacheNameList());
                cacheManager.clear(cacheFactoryInfoConfig().cacheNameSearch());
                cacheManager.clear(cacheFactoryInfoConfig().cacheNamePaging());
                clearCacheCollectionForward(cacheFactoryInfoConfig());
            } catch (CreateCacheException e) {
                throw new InvalidException(ErrorCode.CLEAR_CACHE_ERROR);
            }
        }
    }

    protected void clearCacheCollectionForward(CacheFactoryConfig<?> config) throws CreateCacheException {
        for (String cacheForward : config.cacheSearchForward()) {
            cacheManager.clear(cacheForward);
        }
        for (String cacheForward : config.cachePageForward()) {
            cacheManager.clear(cacheForward);
        }
        for (String cacheForward : config.cacheListForward()) {
            cacheManager.clear(cacheForward);
        }
    }

    /**
     * cacheGet
     *
     * @param <E>           type of cache config
     * @param config        config
     * @param key           key
     * @param responseClass class
     * @param <F>           type
     * @return model
     */
    protected <F, E> F cacheGet(CacheFactoryConfig<E> config, Object key, Class<F> responseClass) {
        try {
            return cacheManager.get(config.cacheName(), key, responseClass);
        } catch (Exception e) {
            log.warn("CacheGet Exception {} ", e.getMessage());
        }
        return null;
    }

    /**
     * isDetailCache
     *
     * @param <E>    type of cache config
     * @param config config
     * @return true if cache
     */
    protected <E> boolean isCache(CacheFactoryConfig<E> config) {
        return config != null && config.cache() && cacheManager != null;
    }

    /**
     * cacheFactoryConfig
     *
     * @param <C> {@link CacheFactoryConfig}
     * @return {@link CacheFactoryConfig}
     */
    protected <C extends CacheFactoryConfig<D>> C cacheFactoryDetailConfig() {
        return (C) new CacheFactoryConfig<D>() {
            @Override
            public boolean cache() {
                return false;
            }

            @Override
            @NonNull
            public Class<? extends D> objectClass() {
                return getDetailClass();
            }
        };
    }

    /**
     * cacheFactoryConfig
     *
     * @param <C> {@link CacheFactoryConfig}
     * @return {@link CacheFactoryConfig}
     */
    protected <C extends CacheFactoryConfig<T>> C cacheFactoryInfoConfig() {
        return (C) new CacheFactoryConfig<T>() {
            @Override
            public boolean cache() {
                return false;
            }

            @Override
            @NonNull
            public Class<? extends T> objectClass() {
                return getInfoClass();
            }
        };
    }

    /**
     * Detail class
     *
     * @return detail class
     */
    @NonNull
    protected abstract Class<? extends D> getDetailClass();

    /**
     * Info class
     *
     * @return info class
     */
    @NonNull
    protected abstract Class<? extends T> getInfoClass();


    /**
     * Not found
     *
     * @return Not found error code
     */
    protected IErrorCode notFound() {
        return ErrorCode.NOT_FOUND;
    }

    /**
     * Conflict
     *
     * @return Conflict error code
     */
    protected IErrorCode conflict() {
        return ErrorCode.CONFLICT;
    }

    /**
     * Bad request
     *
     * @return Bad request code
     */
    protected IErrorCode badRequest() {
        return ErrorCode.BAD_REQUEST;
    }


}

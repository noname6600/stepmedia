/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller.base;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.exception.RestException;
import com.thanhvh.rest.factory.data.ICrudDataFactory;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BaseResponse;
import com.thanhvh.rest.model.response.SuccessResponse;
import org.springframework.http.ResponseEntity;

/**
 * Crud base controller
 *
 * @param <I> id type
 * @param <T> info type
 * @param <U> detail type
 */
public interface ICrudFactoryController<I, T extends IBaseData<I>, U extends T> extends IFactoryController<I, T, U> {

    /**
     * Get CrudDataFactory
     *
     * @return ICrudDataFactory
     */
    ICrudDataFactory<I, T, U> getCrudDataFactory();


    /**
     * Create model
     *
     * @param request model
     * @return {@link ResponseEntity}
     */
    default ResponseEntity<BaseResponse<U>> factoryCreateModel(U request) {
        try {
            U response = getCrudDataFactory().createModel(request);
            return getResponseFactory().success(response);
        } catch (InvalidException e) {
            throw RestException.create(e);
        }
    }

    /**
     * Update model
     *
     * @param request model have id
     * @return {@link ResponseEntity}
     */
    default ResponseEntity<BaseResponse<U>> factoryUpdateModel(U request) {
        try {
            U response = getCrudDataFactory().updateModel(request);
            return getResponseFactory().success(response);
        } catch (InvalidException e) {
            throw RestException.create(e);
        }
    }

    /**
     * Delete model and return true
     *
     * @param id     id of model
     * @param filter extend {@link IFilter}
     * @param <F>    type of filter
     * @return boolean if deleted return true if else return false
     */
    default <F extends IFilter> ResponseEntity<BaseResponse<SuccessResponse>> factoryDeleteWithFilter(I id, F filter) {
        try {
            return getResponseFactory().success(
                    SuccessResponse
                            .builder()
                            .success(getCrudDataFactory().deleteModel(id, filter))
                            .build()
            );
        } catch (InvalidException e) {
            throw RestException.create(e);
        }
    }

    /**
     * Return a detail model with id
     *
     * @param id of model
     * @return detail model
     */
    default ResponseEntity<BaseResponse<U>> factoryGetDetailById(I id) {
        return factoryGetDetailWithFilter(id, null);
    }

    /**
     * Return a detail model with filter
     *
     * @param id     of model
     * @param filter extend {@link IFilter}
     * @param <F>    type of filter
     * @return detail of model
     */
    default <F extends IFilter> ResponseEntity<BaseResponse<U>> factoryGetDetailWithFilter(I id, F filter) {
        try {
            return getResponseFactory().success(
                    getCrudDataFactory().getDetailModel(id, filter)
            );
        } catch (InvalidException e) {
            throw RestException.create(e);
        }
    }

    /**
     * Return an info model with filter
     *
     * @param id     of model
     * @param filter extend {@link IFilter}
     * @param <F>    type of filter
     * @return info of model
     */
    default <F extends IFilter> ResponseEntity<BaseResponse<T>> factoryGetInfo(I id, F filter) {
        try {
            return getResponseFactory().success(
                    getCrudDataFactory().getInfoModel(id, filter)
            );
        } catch (InvalidException e) {
            throw RestException.create(e);
        }
    }

}

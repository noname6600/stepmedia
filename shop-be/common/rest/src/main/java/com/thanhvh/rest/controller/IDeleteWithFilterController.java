/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller;

import com.thanhvh.logging.Logging;
import com.thanhvh.rest.controller.base.ICrudFactoryController;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BaseResponse;
import com.thanhvh.rest.model.response.SuccessResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * IDeleteWithFilterController interface
 *
 * @param <F> filter extends {@link IFilter}
 */
@RequestMapping("/")
@Logging
public interface IDeleteWithFilterController<I, T extends IBaseData<I>, U extends T, F extends IFilter> extends ICrudFactoryController<I, T, U> {
    /**
     * Delete method
     *
     * @param filter extends {@link IFilter}
     * @return {@link SuccessResponse}
     */
    @Transactional
    @DeleteMapping
    default ResponseEntity<BaseResponse<SuccessResponse>> deleteModelWithFilter(@Valid @ParameterObject F filter) {
        return factoryDeleteWithFilter(null, filter);
    }
}

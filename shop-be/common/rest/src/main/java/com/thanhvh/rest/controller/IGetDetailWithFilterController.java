/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller;

import com.thanhvh.logging.Logging;
import com.thanhvh.rest.controller.base.ICrudFactoryController;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BaseResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * IGetDetailWithFilterController
 * <p>
 * Get detail model by filter
 *
 * @param <I> id of model
 * @param <U> detail model
 * @param <F> extends {@link IFilter}
 * @see IGetDetailByIdController
 */
@RequestMapping("/")
@Logging
public interface IGetDetailWithFilterController<I, T extends IBaseData<I>, U extends T, F extends IFilter> extends ICrudFactoryController<I, T, U> {

    /**
     * getDetailByFilter method
     *
     * @param filter extend filter
     * @return detail model
     */
    @Transactional(readOnly = true)
    @GetMapping("/filter")
    default ResponseEntity<BaseResponse<U>> getDetailWithFilter(@ParameterObject F filter) {
        return factoryGetDetailWithFilter(null, filter);
    }
}

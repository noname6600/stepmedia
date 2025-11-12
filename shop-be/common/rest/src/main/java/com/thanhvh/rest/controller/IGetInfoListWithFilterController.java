/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller;

import com.thanhvh.logging.Logging;
import com.thanhvh.rest.controller.base.IListFactoryController;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BaseResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * IGetsFilterController
 *
 * @param <I> id of model
 * @param <T> info model
 * @param <F> extend {@link IFilter}
 * @see IGetInfoListController
 */
@RequestMapping("/")
@Logging
public interface IGetInfoListWithFilterController<I, T extends IBaseData<I>, U extends T, F extends IFilter> extends IListFactoryController<I, T, U> {

    /**
     * getListWithFilter  method
     *
     * @param filter filter
     * @return {@link ResponseEntity}
     */
    @Transactional(readOnly = true)
    @GetMapping("/all/filter")
    default ResponseEntity<BaseResponse<List<T>>> getInfoListWithFilter(@ParameterObject @Valid F filter) {
        return factoryGetInfoListWithFilter(filter);
    }
}

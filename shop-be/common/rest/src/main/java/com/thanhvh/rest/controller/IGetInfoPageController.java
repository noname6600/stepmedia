/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller;


import com.thanhvh.logging.Logging;
import com.thanhvh.rest.controller.base.IPagingFactoryController;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BasePagingResponse;
import com.thanhvh.rest.model.response.BaseResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * IGetInfoPageController interface
 *
 * @param <I> id of model
 * @param <T> info model
 * @param <F> extends {@link IFilter}
 */
@RequestMapping("/")
@Logging
public interface IGetInfoPageController<I, T extends IBaseData<I>, U extends T, F extends IFilter> extends IPagingFactoryController<I, T, U> {

    /**
     * getPaging method
     *
     * @param filter extends {@link IFilter}
     * @param number page number
     * @param size   page size
     * @return {@link BasePagingResponse}
     */
    @Transactional(readOnly = true)
    @GetMapping
    default ResponseEntity<BaseResponse<BasePagingResponse<T>>> getInfoPageWithFilter(
            @ParameterObject @Valid F filter,
            @RequestParam(name = "number", defaultValue = "0") Integer number,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        return factoryGetInfoPageWithFilter(filter, number, size);
    }
}

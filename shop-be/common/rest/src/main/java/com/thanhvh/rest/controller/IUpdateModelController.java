/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller;

import com.thanhvh.logging.Logging;
import com.thanhvh.rest.controller.base.ICrudFactoryController;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * IUpdateModelController interface
 *
 * @param <I> id of model
 * @param <U> detail model
 */
@RequestMapping("/")
@Logging
public interface IUpdateModelController<I, T extends IBaseData<I>, U extends T> extends ICrudFactoryController<I, T, U> {

    /**
     * updateModel
     *
     * @param request detail model
     * @return detail model
     */
    @Transactional
    @PutMapping
    default ResponseEntity<BaseResponse<U>> updateModel(@RequestBody @Valid U request) {
        return factoryUpdateModel(request);
    }
}

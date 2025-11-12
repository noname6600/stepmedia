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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @param <I> id of model
 * @param <U> detail model extends {@link IBaseData}
 */
@RequestMapping("/")
@Logging
public interface ICreateModelController<I, T extends IBaseData<I>, U extends T> extends ICrudFactoryController<I, T, U> {
    /**
     * Post method create model
     *
     * @param request Detail Model
     * @return {@link ResponseEntity} have detail model
     */
    @Transactional
    @PostMapping
    default ResponseEntity<BaseResponse<U>> createModel(@RequestBody @Valid U request) {
        return factoryCreateModel(request);
    }
}

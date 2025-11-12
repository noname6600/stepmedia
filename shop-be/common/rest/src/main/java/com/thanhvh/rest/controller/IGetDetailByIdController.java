/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller;

import com.thanhvh.logging.Logging;
import com.thanhvh.rest.controller.base.ICrudFactoryController;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * IGetDetailByIdController interface
 *
 * @param <I> id of model
 * @param <U> detail model
 */
@RequestMapping("/")
@Logging
public interface IGetDetailByIdController<I, T extends IBaseData<I>, U extends T> extends ICrudFactoryController<I, T, U> {

    /**
     * getDetail method
     *
     * @param id of model
     * @return detail model
     */
    @Transactional(readOnly = true)
    @GetMapping("{id}")
    default ResponseEntity<BaseResponse<U>> getDetailById(@PathVariable("id") I id) {
        return factoryGetDetailById(id);
    }
}

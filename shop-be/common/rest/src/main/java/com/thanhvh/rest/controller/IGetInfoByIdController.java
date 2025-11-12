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
 * Get info by id
 *
 * @param <I> id type
 */
@RequestMapping("/")
@Logging
public interface IGetInfoByIdController<I, T extends IBaseData<I>, U extends T> extends ICrudFactoryController<I, T, U> {
    /**
     * Get info by id
     *
     * @param id id
     * @return info composite
     */
    @Transactional(readOnly = true)
    @GetMapping("{id}/info")
    default ResponseEntity<BaseResponse<T>> getInfoById(@PathVariable("id") I id) {
        return factoryGetInfo(id, null);
    }
}

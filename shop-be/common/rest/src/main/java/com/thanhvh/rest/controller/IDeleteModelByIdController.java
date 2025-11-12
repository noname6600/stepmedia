/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller;

import com.thanhvh.logging.Logging;
import com.thanhvh.rest.controller.base.ICrudFactoryController;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BaseResponse;
import com.thanhvh.rest.model.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @param <I> id of model
 */
@RequestMapping("/")
@Logging
public interface IDeleteModelByIdController<I, T extends IBaseData<I>, U extends T> extends ICrudFactoryController<I, T, U> {
    /**
     * Delete method for delete model by id
     *
     * @param id id of Model
     * @return {@link SuccessResponse}
     */
    @Transactional
    @DeleteMapping("{id}")
    default ResponseEntity<BaseResponse<SuccessResponse>> deleteModelById(@PathVariable("id") I id) {
        return factoryDeleteWithFilter(id, null);
    }
}

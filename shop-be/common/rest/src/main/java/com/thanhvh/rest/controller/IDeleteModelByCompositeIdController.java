/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller;

import com.thanhvh.logging.Logging;
import com.thanhvh.rest.controller.base.ICrudFactoryController;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BaseResponse;
import com.thanhvh.rest.model.response.SuccessResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @param <I> id of model
 * @param <T> info of model
 * @param <U> detail of model
 */
@RequestMapping("/")
@Logging
public interface IDeleteModelByCompositeIdController<I, T extends IBaseData<I>, U extends T> extends ICrudFactoryController<I, T, U> {
    /**
     * Delete method for delete model by composite id
     *
     * @param id id of Model
     * @return {@link SuccessResponse}
     */
    @Transactional(readOnly = true)
    @DeleteMapping("/composite-id")
    default ResponseEntity<BaseResponse<SuccessResponse>> deleteModelCompositeById(@ParameterObject I id) {
        return factoryDeleteWithFilter(id, null);
    }
}

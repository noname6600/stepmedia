/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.controller;

import com.thanhvh.logging.Logging;
import com.thanhvh.rest.controller.base.IListFactoryController;
import com.thanhvh.rest.model.IBaseData;
import com.thanhvh.rest.model.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * IGetListInfoController interface
 *
 * @param <I> id of model
 * @param <T> info model
 * @see IGetInfoListWithFilterController
 */
@RequestMapping("/")
@Logging
public interface IGetInfoListController<I, T extends IBaseData<I>, U extends T> extends IListFactoryController<I, T, U> {

    /**
     * getList method
     *
     * @return list info model
     */
    @Transactional(readOnly = true)
    @GetMapping("all")
    default ResponseEntity<BaseResponse<List<T>>> getInfoList() {
        return factoryGetInfoListWithFilter(null);
    }
}

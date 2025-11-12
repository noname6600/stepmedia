package com.thanhvh.rest.controller;

import com.thanhvh.rest.controller.base.IPagingFactoryController;
import com.thanhvh.rest.filter.ISearchFilter;
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
 * @param <I> ID
 * @param <T> INFO
 * @param <U> DETAIL
 * @param <F> FILTER
 */
@RequestMapping("/")
public interface ISearchDetailController<I, T extends IBaseData<I>, U extends T, F extends ISearchFilter> extends IPagingFactoryController<I, T, U> {
    /**
     * search
     *
     * @param filter filter
     * @param number number
     * @param size   size
     * @return BasePagingResponse
     */
    @Transactional(readOnly = true)
    @GetMapping("/search/detail")
    default ResponseEntity<BaseResponse<BasePagingResponse<U>>> searchDetail(
            @ParameterObject @Valid F filter,
            @RequestParam(name = "number", defaultValue = "0") Integer number,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        return factoryGetSearchDetailWithFilter(filter, number, size);
    }
}

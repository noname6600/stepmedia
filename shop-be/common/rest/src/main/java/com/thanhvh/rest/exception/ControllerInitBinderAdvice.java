package com.thanhvh.rest.exception;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Init binder
 */
@ControllerAdvice
public class ControllerInitBinderAdvice {

    /**
     * Init binder
     *
     * @param binder {@link WebDataBinder}
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }

}

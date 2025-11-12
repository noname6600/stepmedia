package com.thanhvh.logging;

import java.lang.annotation.*;

/**
 * Ignore field when write log
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface LogIgnore {
}

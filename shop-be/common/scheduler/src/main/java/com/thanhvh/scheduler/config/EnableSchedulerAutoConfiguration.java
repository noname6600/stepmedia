package com.thanhvh.scheduler.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableJpaSchedulerAutoConfiguration
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({QuartzSchedulerConfig.class})
public @interface EnableSchedulerAutoConfiguration {
}

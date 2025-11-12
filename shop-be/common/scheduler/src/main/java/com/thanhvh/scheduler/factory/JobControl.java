package com.thanhvh.scheduler.factory;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.scheduler.service.IJobService;

/**
 * JobControl
 */
@FunctionalInterface
public interface JobControl {
    /**
     * @param jobName     jobName
     * @param groupKey    groupKey
     * @param iJobService iJobService
     * @return boolean
     * @throws InvalidException InvalidException
     */
    boolean handle(String jobName, String groupKey, IJobService iJobService) throws InvalidException;
}

package com.thanhvh.processor;

import com.thanhvh.exception.InvalidException;

/**
 * Processor chain
 *
 * @param <Q> request
 * @param <P> response
 */
public interface IProcessorChain<Q, P> {
    /**
     * Start chain
     *
     * @param request request
     * @return response
     * @throws InvalidException invalid
     */
    P startProcess(Q request) throws InvalidException;
}

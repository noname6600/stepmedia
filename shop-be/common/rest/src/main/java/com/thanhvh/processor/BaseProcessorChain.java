package com.thanhvh.processor;


import com.thanhvh.exception.ErrorCode;
import com.thanhvh.exception.InvalidException;
import com.thanhvh.exception.RestException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base processor chain
 *
 * @param <Q> request
 * @param <P> response
 */
public abstract class BaseProcessorChain<Q, P> implements IProcessorChain<Q, P> {

    private final List<BaseProcessor<Q, P>> processors = new ArrayList<>();

    /**
     * Processor chain
     *
     * @param processors list {@link BaseProcessor}
     */
    protected BaseProcessorChain(List<BaseProcessor<Q, P>> processors) {
        this.processors.addAll(processors);
    }

    @Transactional
    @Override
    public P startProcess(Q request) throws InvalidException {
        return doProcess(request, processors.iterator());
    }

    private P doProcess(Q request, Iterator<BaseProcessor<Q, P>> iterator) throws InvalidException {
        if (!iterator.hasNext()) {
            throw RestException.create(ErrorCode.BAD_REQUEST);
        }
        return iterator.next().doChainProcess(request, this, iterator);
    }

    /**
     * Base Processor
     *
     * @param <Q> request
     * @param <P> response
     */
    public abstract static class BaseProcessor<Q, P> {

        private P doChainProcess(
                Q request,
                BaseProcessorChain<Q, P> processorChain,
                Iterator<BaseProcessor<Q, P>> iterator
        ) throws InvalidException {

            if (this.canProcess(request)) {
                return this.doProcess(request);
            }

            return processorChain.doProcess(request, iterator);
        }

        /**
         * Can process request
         *
         * @param request request
         * @return true if can process
         * @throws InvalidException invalid
         */
        protected abstract boolean canProcess(Q request) throws InvalidException;

        /**
         * Process
         *
         * @param request request
         * @return response
         * @throws InvalidException invalid
         */
        protected abstract P doProcess(Q request) throws InvalidException;

    }

}

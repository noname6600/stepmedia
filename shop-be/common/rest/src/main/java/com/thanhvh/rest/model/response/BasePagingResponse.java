/*
 * Copyright (c) 2022. Rizers
 */

package com.thanhvh.rest.model.response;

import com.thanhvh.logging.LogIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Paging response
 *
 * @param <T> type content
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasePagingResponse<T extends Serializable> implements Serializable {
    /**
     * content
     */
    @LogIgnore
    private ArrayList<T> content;
    /**
     * pageNumber
     */
    private int pageNumber;
    /**
     * pageSize
     */
    private int pageSize;
    /**
     * totalElements
     */
    private long totalElements;

    /**
     * BasePagingResponse
     *
     * @param content       {@link List}
     * @param pageNumber    {@link Integer}
     * @param pageSize      {@link Integer}
     * @param totalElements {@link Long}
     */
    BasePagingResponse(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = new ArrayList<>(content);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
    }

    /**
     * Builder
     *
     * @param <T> type content
     * @return {@link BasePagingResponseBuilder}
     */
    public static <T extends Serializable> BasePagingResponseBuilder<T> builder() {
        return new BasePagingResponseBuilder<>();
    }

    /**
     * Get content
     *
     * @return content
     */
    public List<T> getContent() {
        return this.content;
    }

    /**
     * Get page
     *
     * @return pageNumber
     */
    public int getPageNumber() {
        return this.pageNumber;
    }

    /**
     * Get page size
     *
     * @return pageSize
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * Get total elements
     *
     * @return totalElements
     */
    public long getTotalElements() {
        return this.totalElements;
    }

    /**
     * Builder class
     *
     * @param <T> type content
     */
    public static class BasePagingResponseBuilder<T extends Serializable> {
        private List<T> content;
        private int pageNumber;
        private int pageSize;
        private long totalElements;

        BasePagingResponseBuilder() {
        }

        /**
         * Content
         *
         * @param content list
         * @return Builder
         */
        public BasePagingResponseBuilder<T> content(List<T> content) {
            this.content = content;
            return this;
        }

        /**
         * Content
         *
         * @param pageNumber {@link Integer}
         * @return Builder
         */
        public BasePagingResponseBuilder<T> pageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        /**
         * Content
         *
         * @param pageSize {@link Integer}
         * @return Builder
         */
        public BasePagingResponseBuilder<T> pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        /**
         * Content
         *
         * @param totalElements {@link Long}
         * @return Builder
         */
        public BasePagingResponseBuilder<T> totalElements(long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        /**
         * Build
         *
         * @return {@link BasePagingResponse}
         */
        public BasePagingResponse<T> build() {
            return new BasePagingResponse<>(this.content, this.pageNumber, this.pageSize, this.totalElements);
        }

        @Override
        public String toString() {
            return "BasePagingResponseBuilder{" +
                    "content=" + content +
                    ", pageNumber=" + pageNumber +
                    ", pageSize=" + pageSize +
                    ", totalElements=" + totalElements +
                    '}';
        }
    }
}

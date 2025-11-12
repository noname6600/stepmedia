package com.thanhvh.postgresql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractAuditable_.*;

/**
 * Jpa Audit
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Audit implements Serializable {

    /**
     * CreatedBy
     */
    @CreatedBy
    @Column(name = CREATED_BY, updatable = false)
    private UUID createdBy;

    /**
     * CreatedDate
     */
    @Builder.Default
    @CreatedDate
    @Column(name = CREATED_DATE, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    /**
     * LastModifiedBy
     */
    @LastModifiedBy
    @Column(name = LAST_MODIFIED_BY)
    private UUID lastModifiedBy;

    /**
     * LastModifiedDate
     */
    @Builder.Default
    @LastModifiedDate
    @Column(name = LAST_MODIFIED_DATE)
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

}

package com.thanhvh.jpa.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;


/**
 * Jpa Persistence
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@MappedSuperclass
public abstract class Persistence extends Audit {

    /**
     * deleted default = false
     */
    @Builder.Default
    private boolean deleted = false;

}

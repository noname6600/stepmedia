package com.thanhvh.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

/**
 * BaseAuditData
 *
 * @param <T> T extends Serializable
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseAuditData<T extends Serializable> extends BaseData<T> {

    /**
     * createdDate
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long createdDate;

    /**
     * createdBy
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID createdBy;

    /**
     * lastModifiedBy
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID lastModifiedBy;

    /**
     * lastModifiedDate
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastModifiedDate;

}

package com.thanhvh.shopmanagement.modules.product.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thanhvh.rest.model.BaseAuditData;
import com.thanhvh.shopmanagement.modules.shop.model.ShopInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class ProductInfo extends BaseAuditData<UUID> {

    @NotNull
    private String name;

    private String description;

    @NotNull
    @Min(value = 0)
    private Long price;

    @NotNull
    private UUID shopId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private ShopInfo shop;
}

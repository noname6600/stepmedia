package com.thanhvh.shopmanagement.modules.shop.model;

import com.thanhvh.rest.model.BaseAuditData;
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
public class ShopInfo extends BaseAuditData<UUID> {

    @NotNull
    private String name;

    @NotNull
    private String location;
}

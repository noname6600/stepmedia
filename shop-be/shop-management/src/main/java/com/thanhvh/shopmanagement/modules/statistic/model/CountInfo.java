package com.thanhvh.shopmanagement.modules.statistic.model;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CountInfo {

    @NotNull
    @Builder.Default
    private Long customerCount = 0L;

    @NotNull
    @Builder.Default
    private Long shopCount = 0L;

    @NotNull
    @Builder.Default
    private Long productCount = 0L;
}

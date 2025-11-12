package com.thanhvh.shopmanagement.modules.product.model;

import com.thanhvh.rest.filter.IFilter;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class ProductFilter implements IFilter {

    private String name;
    private UUID shopId;
}

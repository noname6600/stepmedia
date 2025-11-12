package com.thanhvh.shopmanagement.modules.shop.model;

import com.thanhvh.rest.filter.IFilter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class ShopFilter implements IFilter {

    private String name;
    private String location;
}

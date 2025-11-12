package com.thanhvh.shopmanagement.modules.order.model;

import com.thanhvh.rest.filter.IFilter;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class OrderFilter implements IFilter {

    private UUID productId;
    private UUID shopId;
    private UUID customerId;
    private Long from;
    private Long to;
}

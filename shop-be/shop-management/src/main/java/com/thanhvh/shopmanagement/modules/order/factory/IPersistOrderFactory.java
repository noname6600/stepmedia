package com.thanhvh.shopmanagement.modules.order.factory;

import com.thanhvh.rest.factory.data.IPersistDataFactory;
import com.thanhvh.shopmanagement.modules.order.entity.OrderEntity;
import com.thanhvh.shopmanagement.modules.order.model.OrderDetail;
import com.thanhvh.shopmanagement.modules.order.model.OrderInfo;

public interface IPersistOrderFactory extends IPersistDataFactory<OrderInfo, OrderDetail, OrderEntity> {

    void activeOrders();
}

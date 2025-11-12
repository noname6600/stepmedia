package com.thanhvh.shopmanagement.modules.order.controller;

import com.thanhvh.rest.controller.*;
import com.thanhvh.shopmanagement.modules.order.model.OrderDetail;
import com.thanhvh.shopmanagement.modules.order.model.OrderFilter;
import com.thanhvh.shopmanagement.modules.order.model.OrderInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Orders Controller")
public interface IOrderController extends
        ICreateModelController<UUID, OrderInfo, OrderDetail>,
        IGetInfoPageController<UUID, OrderInfo, OrderDetail, OrderFilter>,
        IGetDetailByIdController<UUID, OrderInfo, OrderDetail>,
        IUpdateModelController<UUID, OrderInfo, OrderDetail>,
        IDeleteModelByIdController<UUID, OrderInfo, OrderDetail> {
}

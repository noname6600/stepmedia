package com.thanhvh.shopmanagement.modules.order.controller.impl;

import com.thanhvh.rest.controller.base.BaseController;
import com.thanhvh.rest.factory.data.IDataFactory;
import com.thanhvh.rest.factory.response.IResponseFactory;
import com.thanhvh.shopmanagement.modules.order.controller.IOrderController;
import com.thanhvh.shopmanagement.modules.order.model.OrderDetail;
import com.thanhvh.shopmanagement.modules.order.model.OrderInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Transactional
public class OrderController extends BaseController<UUID, OrderInfo, OrderDetail> implements IOrderController {
    /**
     * @param iResponseFactory response factory
     * @param iDataFactory     data factory
     */
    protected OrderController(IResponseFactory iResponseFactory, IDataFactory<UUID, OrderInfo, OrderDetail> iDataFactory) {
        super(iResponseFactory, iDataFactory);
    }
}

package com.thanhvh.shopmanagement.modules.customer.controller.impl;

import com.thanhvh.rest.controller.base.BaseController;
import com.thanhvh.rest.factory.data.IDataFactory;
import com.thanhvh.rest.factory.response.IResponseFactory;
import com.thanhvh.shopmanagement.modules.customer.controller.ICustomerController;
import com.thanhvh.shopmanagement.modules.customer.model.CustomerDetail;
import com.thanhvh.shopmanagement.modules.customer.model.CustomerInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Transactional
public class CustomerController extends BaseController<UUID, CustomerInfo, CustomerDetail> implements ICustomerController {
    /**
     * @param iResponseFactory response factory
     * @param iDataFactory     data factory
     */
    protected CustomerController(IResponseFactory iResponseFactory, IDataFactory<UUID, CustomerInfo, CustomerDetail> iDataFactory) {
        super(iResponseFactory, iDataFactory);
    }
}

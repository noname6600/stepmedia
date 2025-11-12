package com.thanhvh.shopmanagement.modules.customer.controller;

import com.thanhvh.rest.controller.*;
import com.thanhvh.rest.filter.SearchFilter;
import com.thanhvh.shopmanagement.modules.customer.model.CustomerDetail;
import com.thanhvh.shopmanagement.modules.customer.model.CustomerFilter;
import com.thanhvh.shopmanagement.modules.customer.model.CustomerInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/api/v1/customers")
@Tag(name = "Customers", description = "Customers Controller")
public interface ICustomerController extends
        ICreateModelController<UUID, CustomerInfo, CustomerDetail>,
        IGetInfoPageController<UUID, CustomerInfo, CustomerDetail, CustomerFilter>,
        ISearchInfoController<UUID, CustomerInfo, CustomerDetail, SearchFilter>,
        IGetDetailByIdController<UUID, CustomerInfo, CustomerDetail>,
        IUpdateModelController<UUID, CustomerInfo, CustomerDetail>,
        IDeleteModelByIdController<UUID, CustomerInfo, CustomerDetail> {
}

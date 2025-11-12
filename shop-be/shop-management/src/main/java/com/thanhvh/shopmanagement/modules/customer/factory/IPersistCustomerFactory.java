package com.thanhvh.shopmanagement.modules.customer.factory;

import com.thanhvh.rest.factory.data.IDataFactory;
import com.thanhvh.rest.factory.data.IPersistDataFactory;
import com.thanhvh.shopmanagement.modules.customer.entity.CustomerEntity;
import com.thanhvh.shopmanagement.modules.customer.model.CustomerDetail;
import com.thanhvh.shopmanagement.modules.customer.model.CustomerInfo;

import java.util.UUID;

public interface IPersistCustomerFactory extends
        IPersistDataFactory<CustomerInfo, CustomerDetail, CustomerEntity>,
        IDataFactory<UUID, CustomerInfo, CustomerDetail> {

    Long count();
}

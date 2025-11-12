package com.thanhvh.shopmanagement.modules.statistic.controller.impl;

import com.thanhvh.rest.model.response.BaseResponse;
import com.thanhvh.shopmanagement.modules.customer.factory.IPersistCustomerFactory;
import com.thanhvh.shopmanagement.modules.product.factory.IPersistProductFactory;
import com.thanhvh.shopmanagement.modules.shop.factory.IPersistShopFactory;
import com.thanhvh.shopmanagement.modules.statistic.controller.IStatisticController;
import com.thanhvh.shopmanagement.modules.statistic.model.CountInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class StatisticController implements IStatisticController {

    private final IPersistCustomerFactory customerFactory;
    private final IPersistShopFactory shopFactory;
    private final IPersistProductFactory productFactory;

    public StatisticController(IPersistCustomerFactory customerFactory, IPersistShopFactory shopFactory, IPersistProductFactory productFactory) {
        this.customerFactory = customerFactory;
        this.shopFactory = shopFactory;
        this.productFactory = productFactory;
    }

    @Override
    public ResponseEntity<BaseResponse<CountInfo>> counts() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        BaseResponse
                                .<CountInfo>builder()
                                .success(true)
                                .data(CountInfo
                                        .builder()
                                        .customerCount(customerFactory.count())
                                        .shopCount(shopFactory.count())
                                        .productCount(productFactory.count())
                                        .build())
                                .build()
                );

    }
}

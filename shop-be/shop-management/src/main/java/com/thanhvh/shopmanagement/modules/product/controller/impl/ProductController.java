package com.thanhvh.shopmanagement.modules.product.controller.impl;

import com.thanhvh.rest.controller.base.BaseController;
import com.thanhvh.rest.factory.data.IDataFactory;
import com.thanhvh.rest.factory.response.IResponseFactory;
import com.thanhvh.shopmanagement.modules.product.controller.IProductController;
import com.thanhvh.shopmanagement.modules.product.model.ProductDetail;
import com.thanhvh.shopmanagement.modules.product.model.ProductInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Transactional
public class ProductController extends BaseController<UUID, ProductInfo, ProductDetail> implements IProductController {
    /**
     * @param iResponseFactory response factory
     * @param iDataFactory     data factory
     */
    protected ProductController(IResponseFactory iResponseFactory, IDataFactory<UUID, ProductInfo, ProductDetail> iDataFactory) {
        super(iResponseFactory, iDataFactory);
    }
}

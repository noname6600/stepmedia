package com.thanhvh.shopmanagement.modules.shop.controller.impl;

import com.thanhvh.rest.controller.base.BaseController;
import com.thanhvh.rest.factory.data.IDataFactory;
import com.thanhvh.rest.factory.response.IResponseFactory;
import com.thanhvh.shopmanagement.modules.shop.controller.IShopController;
import com.thanhvh.shopmanagement.modules.shop.model.ShopDetail;
import com.thanhvh.shopmanagement.modules.shop.model.ShopInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Transactional
public class ShopController extends BaseController<UUID, ShopInfo, ShopDetail> implements IShopController {
    /**
     * @param iResponseFactory response factory
     * @param iDataFactory     data factory
     */
    protected ShopController(IResponseFactory iResponseFactory, IDataFactory<UUID, ShopInfo, ShopDetail> iDataFactory) {
        super(iResponseFactory, iDataFactory);
    }
}

package com.thanhvh.shopmanagement.modules.shop.factory;

import com.thanhvh.rest.factory.data.IDataFactory;
import com.thanhvh.rest.factory.data.IPersistDataFactory;
import com.thanhvh.shopmanagement.modules.shop.entity.ShopEntity;
import com.thanhvh.shopmanagement.modules.shop.model.ShopDetail;
import com.thanhvh.shopmanagement.modules.shop.model.ShopInfo;

import java.util.UUID;

public interface IPersistShopFactory extends
        IPersistDataFactory<ShopInfo, ShopDetail, ShopEntity>,
        IDataFactory<UUID, ShopInfo, ShopDetail> {
    Long count();
}

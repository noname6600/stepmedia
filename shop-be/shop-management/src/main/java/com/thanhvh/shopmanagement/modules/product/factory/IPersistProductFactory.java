package com.thanhvh.shopmanagement.modules.product.factory;

import com.thanhvh.rest.factory.data.IDataFactory;
import com.thanhvh.rest.factory.data.IPersistDataFactory;
import com.thanhvh.shopmanagement.modules.product.entity.ProductEntity;
import com.thanhvh.shopmanagement.modules.product.model.ProductDetail;
import com.thanhvh.shopmanagement.modules.product.model.ProductInfo;

import java.util.UUID;

public interface IPersistProductFactory extends
        IPersistDataFactory<ProductInfo, ProductDetail, ProductEntity>,
        IDataFactory<UUID, ProductInfo, ProductDetail> {

    Long count();
}

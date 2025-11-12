package com.thanhvh.shopmanagement.modules.product.controller;

import com.thanhvh.rest.controller.*;
import com.thanhvh.shopmanagement.modules.product.model.ProductDetail;
import com.thanhvh.shopmanagement.modules.product.model.ProductFilter;
import com.thanhvh.shopmanagement.modules.product.model.ProductInfo;
import com.thanhvh.shopmanagement.modules.product.model.ProductSearchFilter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

/**
 * The interface Product controller.
 */
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Products Controller")
public interface IProductController extends
        ICreateModelController<UUID, ProductInfo, ProductDetail>,
        IGetInfoPageController<UUID, ProductInfo, ProductDetail, ProductFilter>,
        ISearchInfoController<UUID, ProductInfo, ProductDetail, ProductSearchFilter>,
        IGetDetailByIdController<UUID, ProductInfo, ProductDetail>,
        IUpdateModelController<UUID, ProductInfo, ProductDetail>,
        IDeleteModelByIdController<UUID, ProductInfo, ProductDetail> {
}

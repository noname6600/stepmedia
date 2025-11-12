package com.thanhvh.shopmanagement.modules.shop.controller;

import com.thanhvh.rest.controller.*;
import com.thanhvh.rest.filter.SearchFilter;
import com.thanhvh.shopmanagement.modules.shop.model.ShopDetail;
import com.thanhvh.shopmanagement.modules.shop.model.ShopFilter;
import com.thanhvh.shopmanagement.modules.shop.model.ShopInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

/**
 * The interface Shop controller.
 */
@RequestMapping("/api/v1/shops")
@Tag(name = "Shops", description = "Shops Controller")
public interface IShopController extends
        ICreateModelController<UUID, ShopInfo, ShopDetail>,
        IGetInfoPageController<UUID, ShopInfo, ShopDetail, ShopFilter>,
        ISearchInfoController<UUID, ShopInfo, ShopDetail, SearchFilter>,
        IGetDetailByIdController<UUID, ShopInfo, ShopDetail>,
        IUpdateModelController<UUID, ShopInfo, ShopDetail>,
        IDeleteModelByIdController<UUID, ShopInfo, ShopDetail> {
}

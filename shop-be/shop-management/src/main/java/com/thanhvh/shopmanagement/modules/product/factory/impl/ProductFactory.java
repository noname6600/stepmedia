package com.thanhvh.shopmanagement.modules.product.factory.impl;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.rest.factory.data.base.BasePersistDataFactory;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.filter.ISearchFilter;
import com.thanhvh.shopmanagement.modules.product.entity.ProductEntity;
import com.thanhvh.shopmanagement.modules.product.factory.IPersistProductFactory;
import com.thanhvh.shopmanagement.modules.product.model.ProductDetail;
import com.thanhvh.shopmanagement.modules.product.model.ProductFilter;
import com.thanhvh.shopmanagement.modules.product.model.ProductInfo;
import com.thanhvh.shopmanagement.modules.product.model.ProductSearchFilter;
import com.thanhvh.shopmanagement.modules.product.repository.ProductRepository;
import com.thanhvh.shopmanagement.modules.shop.factory.IPersistShopFactory;
import com.thanhvh.util.TimeUtil;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductFactory extends
        BasePersistDataFactory<UUID, ProductInfo, ProductDetail, UUID, ProductEntity, ProductRepository> implements
        IPersistProductFactory {

    private final IPersistShopFactory shopFactory;

    /**
     * @param repository repository
     */
    protected ProductFactory(ProductRepository repository, IPersistShopFactory shopFactory) {
        super(repository);
        this.shopFactory = shopFactory;
    }

    @Override
    public ProductEntity createConvertToEntity(ProductDetail detail) throws InvalidException {
        return ProductEntity
                .builder()
                .name(detail.getName())
                .description(detail.getDescription())
                .price(detail.getPrice())
                .shopId(detail.getShopId())
                .build();
    }

    @Override
    public void updateConvertToEntity(ProductEntity entity, ProductDetail detail) throws InvalidException {
        entity.setName(detail.getName());
        entity.setDescription(detail.getDescription());
        entity.setPrice(detail.getPrice());
        entity.setShopId(detail.getShopId());
    }

    @Override
    public ProductDetail convertToDetail(ProductEntity entity) throws InvalidException {
        return ProductDetail
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .shopId(entity.getShopId())
                .shop(shopFactory.getInfoModel(entity.getShopId(), null))
                .createdBy(entity.getCreatedBy())
                .lastModifiedBy(entity.getLastModifiedBy())
                .createdDate(TimeUtil.toTimeStamp(entity.getCreatedDate()))
                .lastModifiedDate(TimeUtil.toTimeStamp(entity.getLastModifiedDate()))
                .build();
    }

    @Override
    protected <F extends IFilter> Page<ProductEntity> pageQuery(F filter, Integer number, Integer size) throws InvalidException {
        if (filter instanceof ProductFilter productFilter) {
            return repository.findByFilter(
                    productFilter.getShopId(),
                    productFilter.getName(),
                    PageRequest.of(number, size)
            );
        }
        return super.pageQuery(filter, number, size);
    }

    @Override
    public ProductInfo convertToInfo(ProductEntity entity) throws InvalidException {
        return convertToDetail(entity);
    }

    @Override
    protected <F extends ISearchFilter> Page<ProductEntity> searchQuery(F search, Integer number, Integer size) throws InvalidException {
        if (search instanceof ProductSearchFilter productSearchFilter) {
            return repository.search(
                    productSearchFilter.getSearchText(),
                    productSearchFilter.getShopId(),
                    PageRequest.of(number, size)
            );
        }
        return super.searchQuery(search, number, size);
    }

    @Override
    @NonNull
    protected Class<? extends ProductDetail> getDetailClass() {
        return ProductDetail.class;
    }

    @Override
    @NonNull
    protected Class<? extends ProductInfo> getInfoClass() {
        return ProductInfo.class;
    }

    @Override
    public Long count() {
        return repository.count();
    }
}

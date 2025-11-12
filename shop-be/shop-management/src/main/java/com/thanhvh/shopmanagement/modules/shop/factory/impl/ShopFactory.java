package com.thanhvh.shopmanagement.modules.shop.factory.impl;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.rest.factory.data.base.BasePersistDataFactory;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.filter.ISearchFilter;
import com.thanhvh.shopmanagement.modules.shop.entity.ShopEntity;
import com.thanhvh.shopmanagement.modules.shop.factory.IPersistShopFactory;
import com.thanhvh.shopmanagement.modules.shop.model.ShopDetail;
import com.thanhvh.shopmanagement.modules.shop.model.ShopFilter;
import com.thanhvh.shopmanagement.modules.shop.model.ShopInfo;
import com.thanhvh.shopmanagement.modules.shop.repository.ShopRepository;
import com.thanhvh.util.TimeUtil;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShopFactory extends
        BasePersistDataFactory<UUID, ShopInfo, ShopDetail, UUID, ShopEntity, ShopRepository> implements
        IPersistShopFactory {

    /**
     * @param repository repository
     */
    protected ShopFactory(ShopRepository repository) {
        super(repository);
    }

    @Override
    public ShopEntity createConvertToEntity(ShopDetail detail) throws InvalidException {
        return ShopEntity
                .builder()
                .name(detail.getName())
                .location(detail.getLocation())
                .build();
    }

    @Override
    public void updateConvertToEntity(ShopEntity entity, ShopDetail detail) throws InvalidException {
        entity.setName(detail.getName());
        entity.setLocation(detail.getLocation());
    }

    @Override
    public ShopDetail convertToDetail(ShopEntity entity) throws InvalidException {
        return ShopDetail
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .location(entity.getLocation())
                .createdBy(entity.getCreatedBy())
                .lastModifiedBy(entity.getLastModifiedBy())
                .createdDate(TimeUtil.toTimeStamp(entity.getCreatedDate()))
                .lastModifiedDate(TimeUtil.toTimeStamp(entity.getLastModifiedDate()))
                .build();
    }

    @Override
    public ShopInfo convertToInfo(ShopEntity entity) throws InvalidException {
        return convertToDetail(entity);
    }

    @Override
    protected <F extends IFilter> Page<ShopEntity> pageQuery(F filter, Integer number, Integer size) throws InvalidException {
        if (filter instanceof ShopFilter shopFilter) {
            return repository.findByFilter(
                    shopFilter.getName(),
                    shopFilter.getLocation(),
                    PageRequest.of(number, size)
            );
        }
        return super.pageQuery(filter, number, size);
    }

    @Override
    protected <F extends ISearchFilter> Page<ShopEntity> searchQuery(F search, Integer number, Integer size) throws InvalidException {
        return repository.search(search.getSearchText(), PageRequest.of(number, size));
    }

    @Override
    @NonNull
    protected Class<? extends ShopDetail> getDetailClass() {
        return ShopDetail.class;
    }

    @Override
    @NonNull
    protected Class<? extends ShopInfo> getInfoClass() {
        return ShopInfo.class;
    }

    @Override
    public Long count() {
        return repository.count();
    }
}

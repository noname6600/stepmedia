package com.thanhvh.shopmanagement.modules.order.factory.impl;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.rest.factory.data.base.BasePersistDataFactory;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.shopmanagement.modules.customer.factory.IPersistCustomerFactory;
import com.thanhvh.shopmanagement.modules.order.entity.OrderEntity;
import com.thanhvh.shopmanagement.modules.order.enumerate.OrderStatus;
import com.thanhvh.shopmanagement.modules.order.factory.IPersistOrderFactory;
import com.thanhvh.shopmanagement.modules.order.model.OrderDetail;
import com.thanhvh.shopmanagement.modules.order.model.OrderFilter;
import com.thanhvh.shopmanagement.modules.order.model.OrderInfo;
import com.thanhvh.shopmanagement.modules.order.repository.OrderRepository;
import com.thanhvh.shopmanagement.modules.product.factory.IPersistProductFactory;
import com.thanhvh.shopmanagement.modules.product.model.ProductInfo;
import com.thanhvh.util.TimeUtil;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class OrderFactory extends
        BasePersistDataFactory<UUID, OrderInfo, OrderDetail, UUID, OrderEntity, OrderRepository> implements
        IPersistOrderFactory {

    private final IPersistCustomerFactory customerFactory;
    private final IPersistProductFactory productFactory;

    /**
     * @param repository repository
     */
    protected OrderFactory(OrderRepository repository,
                           IPersistCustomerFactory customerFactory,
                           IPersistProductFactory productFactory) {
        super(repository);
        this.customerFactory = customerFactory;
        this.productFactory = productFactory;
    }

    @Override
    protected OrderDetail preCreate(OrderDetail detail) throws InvalidException {
        return super.preCreate(prePersist(detail));
    }

    @Override
    protected OrderDetail preUpdate(UUID id, OrderDetail detail) throws InvalidException {
        return super.preUpdate(id, prePersist(detail));
    }

    @Override
    public OrderEntity createConvertToEntity(OrderDetail detail) throws InvalidException {
        return OrderEntity
                .builder()
                .customerId(detail.getCustomerId())
                .shopId(detail.getShopId())
                .productId(detail.getProductId())
                .deliveryDate(detail.getDeliveryDate())
                .status(OrderStatus.N)
                .build();
    }

    @Override
    public void updateConvertToEntity(OrderEntity entity, OrderDetail detail) throws InvalidException {
        entity.setCustomerId(detail.getCustomerId());
        entity.setShopId(detail.getShopId());
        entity.setProductId(detail.getProductId());
        entity.setDeliveryDate(detail.getDeliveryDate());
        entity.setStatus(detail.getStatus());
    }

    @Override
    public OrderDetail convertToDetail(OrderEntity entity) throws InvalidException {
        return OrderDetail
                .builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .customer(customerFactory.getInfoModel(entity.getCustomerId(), null))
                .shopId(entity.getShopId())
                .productId(entity.getProductId())
                .product(productFactory.getInfoModel(entity.getProductId(), null))
                .deliveryDate(entity.getDeliveryDate())
                .status(entity.getStatus())
                .createdBy(entity.getCreatedBy())
                .lastModifiedBy(entity.getLastModifiedBy())
                .createdDate(TimeUtil.toTimeStamp(entity.getCreatedDate()))
                .lastModifiedDate(TimeUtil.toTimeStamp(entity.getLastModifiedDate()))
                .build();
    }

    @Override
    public OrderInfo convertToInfo(OrderEntity entity) throws InvalidException {
        return convertToDetail(entity);
    }

    @Override
    protected <F extends IFilter> Page<OrderEntity> pageQuery(F filter, Integer number, Integer size) throws InvalidException {
        if (filter instanceof OrderFilter orderFilter) {
            return repository.findByFilter(
                    orderFilter.getCustomerId(),
                    orderFilter.getShopId(),
                    orderFilter.getProductId(),
                    TimeUtil.toLocalDateTime(orderFilter.getFrom()),
                    TimeUtil.toLocalDateTime(orderFilter.getTo()),
                    PageRequest.of(number, size)
            );
        }
        return super.pageQuery(filter, number, size);
    }

    @Override
    @NonNull
    protected Class<? extends OrderDetail> getDetailClass() {
        return OrderDetail.class;
    }

    @Override
    @NonNull
    protected Class<? extends OrderInfo> getInfoClass() {
        return OrderInfo.class;
    }

    private OrderDetail prePersist(OrderDetail detail) throws InvalidException {
        ProductInfo productInfo = productFactory.getInfoModel(detail.getProductId(), null);
        return detail
                .toBuilder()
                .customer(customerFactory.getInfoModel(detail.getCustomerId(), null))
                .product(productInfo)
                .shopId(productInfo.getShopId())
                .deliveryDate(detail.getDeliveryDate() != null ? detail.getDeliveryDate() : LocalDate.now().plusDays(3))
                .build();
    }

    @Override
    @Transactional
    public void activeOrders() {
        repository.activeOrders();
    }
}

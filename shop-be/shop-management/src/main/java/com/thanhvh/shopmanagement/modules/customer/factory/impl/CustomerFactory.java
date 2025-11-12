package com.thanhvh.shopmanagement.modules.customer.factory.impl;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.rest.factory.data.base.BasePersistDataFactory;
import com.thanhvh.rest.filter.IFilter;
import com.thanhvh.rest.filter.ISearchFilter;
import com.thanhvh.shopmanagement.modules.customer.entity.CustomerEntity;
import com.thanhvh.shopmanagement.modules.customer.factory.IPersistCustomerFactory;
import com.thanhvh.shopmanagement.modules.customer.model.CustomerDetail;
import com.thanhvh.shopmanagement.modules.customer.model.CustomerFilter;
import com.thanhvh.shopmanagement.modules.customer.model.CustomerInfo;
import com.thanhvh.shopmanagement.modules.customer.repository.CustomerRepository;
import com.thanhvh.util.TimeUtil;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerFactory extends
        BasePersistDataFactory<UUID, CustomerInfo, CustomerDetail, UUID, CustomerEntity, CustomerRepository> implements
        IPersistCustomerFactory {

    /**
     * @param repository repository
     */
    protected CustomerFactory(CustomerRepository repository) {
        super(repository);
    }

    @Override
    public CustomerEntity createConvertToEntity(CustomerDetail detail) throws InvalidException {
        return CustomerEntity
                .builder()
                .fullName(detail.getFullName())
                .dob(detail.getDob())
                .email(detail.getEmail())
                .build();
    }

    @Override
    public void updateConvertToEntity(CustomerEntity entity, CustomerDetail detail) throws InvalidException {
        entity.setFullName(detail.getFullName());
        entity.setDob(detail.getDob());
        entity.setEmail(detail.getEmail());
    }

    @Override
    public CustomerDetail convertToDetail(CustomerEntity entity) throws InvalidException {
        return CustomerDetail
                .builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .dob(entity.getDob())
                .email(entity.getEmail())
                .createdBy(entity.getCreatedBy())
                .lastModifiedBy(entity.getLastModifiedBy())
                .createdDate(TimeUtil.toTimeStamp(entity.getCreatedDate()))
                .lastModifiedDate(TimeUtil.toTimeStamp(entity.getLastModifiedDate()))
                .build();
    }

    @Override
    public CustomerInfo convertToInfo(CustomerEntity entity) throws InvalidException {
        return convertToDetail(entity);
    }

    @Override
    protected <F extends IFilter> Page<CustomerEntity> pageQuery(F filter, Integer number, Integer size) throws InvalidException {
        if (filter instanceof CustomerFilter customerFilter) {
            return repository.findPageByFilter(
                    customerFilter.getFullName(),
                    customerFilter.getEmail(),
                    PageRequest.of(number, size)
            );
        }
        return super.pageQuery(filter, number, size);
    }

    @Override
    protected <F extends ISearchFilter> Page<CustomerEntity> searchQuery(F search, Integer number, Integer size) throws InvalidException {
        return repository.search(search.getSearchText(), PageRequest.of(number, size));
    }

    @Override
    @NonNull
    protected Class<? extends CustomerDetail> getDetailClass() {
        return CustomerDetail.class;
    }

    @Override
    @NonNull
    protected Class<? extends CustomerInfo> getInfoClass() {
        return CustomerInfo.class;
    }

    @Override
    public Long count() {
        return repository.count();
    }
}

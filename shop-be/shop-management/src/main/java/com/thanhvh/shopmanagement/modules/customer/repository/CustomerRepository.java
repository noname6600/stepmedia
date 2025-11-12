package com.thanhvh.shopmanagement.modules.customer.repository;

import com.thanhvh.shopmanagement.modules.customer.entity.CustomerEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    @Query(value = """
            SELECT a FROM CustomerEntity a
                WHERE (:email IS NULL OR LOWER(UNACCENT(a.email)) LIKE LOWER(UNACCENT(CONCAT('%',:email,'%'))))
                AND (:fullName IS NULL OR LOWER(UNACCENT(a.fullName)) LIKE LOWER(UNACCENT(CONCAT('%',:fullName,'%'))))
                ORDER BY a.createdDate DESC
                """)
    Page<CustomerEntity> findPageByFilter(String fullName, String email, @NonNull Pageable pageable);

    @Query(value = """
            SELECT a FROM CustomerEntity a
                WHERE (LOWER(UNACCENT(a.fullName)) LIKE LOWER(UNACCENT(CONCAT('%',:searchText,'%')))
                    OR LOWER(UNACCENT(a.email)) LIKE LOWER(UNACCENT(CONCAT('%',:searchText, '%')))
                    OR :searchText IS NULL)
                ORDER BY a.createdDate DESC
                """)
    Page<CustomerEntity> search(String searchText, Pageable pageable);
}

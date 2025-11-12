package com.thanhvh.shopmanagement.modules.product.repository;

import com.thanhvh.shopmanagement.modules.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    @Query(value = """
                    SELECT a FROM ProductEntity a
                        WHERE (:shopId IS NULL OR a.shopId = :shopId)
                        AND (:name IS NULL OR LOWER(UNACCENT(a.name)) LIKE LOWER(UNACCENT(CONCAT('%',:name,'%'))))
                    ORDER BY a.createdDate DESC
            """)
    Page<ProductEntity> findByFilter(UUID shopId, String name, Pageable pageable);


    @Query(value = """
            SELECT a FROM ProductEntity a
                WHERE (LOWER(a.name) LIKE LOWER(CONCAT('%',:searchText,'%'))
                    OR :searchText IS NULL)
                    AND (CAST(:shopId AS org.hibernate.type.UUIDCharType) IS NULL OR a.shopId = :shopId )
                ORDER BY a.createdDate DESC
                """)
    Page<ProductEntity> search(String searchText, UUID shopId, Pageable pageable);
}

package com.thanhvh.shopmanagement.modules.order.repository;

import com.thanhvh.shopmanagement.modules.order.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE customer_order SET status = 'A' WHERE status = 'N'
            """)
    void activeOrders();

    @Query("""
                    SELECT a FROM OrderEntity a
                    WHERE (:customerId IS NULL  OR a.customerId = :customerId)
                    AND (:shopId IS NULL  OR a.shopId = :shopId)
                    AND (:productId IS NULL  OR a.productId = :productId)
                     AND (CAST(:from AS timestamp ) IS NULL OR a.createdDate >= :from )
                     AND (CAST(:to AS timestamp ) IS NULL OR a.createdDate <= :to )
                    ORDER BY a.customer.email ASC, a.shop.location DESC, a.product.price DESC, a.createdDate DESC
            """)
    Page<OrderEntity> findByFilter(UUID customerId,
                                   UUID shopId,
                                   UUID productId,
                                   LocalDateTime from,
                                   LocalDateTime to,
                                   Pageable pageable);
}

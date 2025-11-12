package com.thanhvh.shopmanagement.modules.order.entity;

import com.thanhvh.postgresql.entity.Audit;
import com.thanhvh.shopmanagement.modules.customer.entity.CustomerEntity;
import com.thanhvh.shopmanagement.modules.order.enumerate.OrderStatus;
import com.thanhvh.shopmanagement.modules.product.entity.ProductEntity;
import com.thanhvh.shopmanagement.modules.shop.entity.ShopEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "customer_order")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Getter
@Setter
public class OrderEntity extends Audit {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false, insertable = false)
    @GeneratedValue
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "shop_id", nullable = false)
    private UUID shopId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "status", nullable = false, length = 2)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(
            name = "shop_id",
            referencedColumnName = "id",
            updatable = false,
            insertable = false,
            foreignKey = @ForeignKey(name = "order_shop_fk")
    )
    private ShopEntity shop;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "order_product_fk")
    )
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(
            name = "customer_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "order_customer_fk")
    )
    private CustomerEntity customer;
}

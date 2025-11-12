package com.thanhvh.shopmanagement.modules.order.enumerate;

/**
 * The enum Order status.
 */
public enum OrderStatus {
    /**
     * N order status.
     */
    N("NEW"),
    /**
     * C order status.
     */
    C("CANCEL"),
    /**
     * A order status.
     */
    A("ACTIVE"),
    ;
    private final String desc;

    OrderStatus(String desc) {
        this.desc = desc;
    }
}

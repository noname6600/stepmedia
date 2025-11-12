package com.thanhvh.shopmanagement.modules.customer.model;

import com.thanhvh.rest.filter.IFilter;
import lombok.*;

/**
 * The type Customer filter.
 */
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerFilter implements IFilter {

    private String email;
    private String fullName;
}

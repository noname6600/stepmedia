package com.thanhvh.shopmanagement.modules.customer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thanhvh.rest.model.BaseAuditData;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class CustomerInfo extends BaseAuditData<UUID> {

    @NotNull
    private String fullName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull
    private LocalDate dob;

    @NotNull
    @Email
    private String email;
}

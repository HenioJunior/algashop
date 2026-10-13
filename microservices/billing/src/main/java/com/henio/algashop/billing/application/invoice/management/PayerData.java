package com.henio.algashop.billing.application.invoice.management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayerData {
    private String fullName;
    private String document;
    private String phone;
    private String email;
    private AddressData address;
}

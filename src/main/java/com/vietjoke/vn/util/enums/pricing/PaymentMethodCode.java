package com.vietjoke.vn.util.enums.pricing;

import lombok.Getter;

@Getter
public enum PaymentMethodCode {
    ZALOPAY("ZaloPay"),
    VNPAY("VNPAY"),
    POSTPAY("Thanh toán sau");

    private final String displayName;

    PaymentMethodCode(String displayName) {
        this.displayName = displayName;
    }

}

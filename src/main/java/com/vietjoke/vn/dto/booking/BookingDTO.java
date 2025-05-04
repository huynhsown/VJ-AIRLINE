package com.vietjoke.vn.dto.booking;

import com.vietjoke.vn.dto.core.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookingDTO extends BaseDTO {
    private String bookingReference;
    private LocalDateTime bookingDate;
    private BigDecimal totalAmount;
    private String currency;
    private String paymentMethod;
    private String paymentReference;
    private String promoCode;
    private BigDecimal discountAmount;
    private int adultCount;
    private int childCount;
    private int infantCount;
    private String tripType;
    private BookingStatusDTO status;
    private List<BookingDetailDTO> bookingDetails;
    private List<BookingPaymentDTO> bookingPayments;
}
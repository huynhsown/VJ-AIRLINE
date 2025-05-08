package com.vietjoke.vn.service.booking;

import java.io.IOException;
import java.math.BigDecimal;

public interface BookingService {
    String createPayPalOrder(BigDecimal amount) throws IOException;
}

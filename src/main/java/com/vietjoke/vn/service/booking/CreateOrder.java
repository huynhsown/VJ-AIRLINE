package com.vietjoke.vn.service.booking;

import org.cloudinary.json.JSONObject;

public interface CreateOrder {
    JSONObject createOrder(String amount) throws Exception;
}

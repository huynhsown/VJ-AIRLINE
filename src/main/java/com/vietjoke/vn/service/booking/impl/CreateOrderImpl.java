package com.vietjoke.vn.service.booking.impl;


import com.vietjoke.vn.config.ZaloPayConfig;
import com.vietjoke.vn.service.booking.CreateOrder;
import com.vietjoke.vn.service.booking.HttpProvider;
import com.vietjoke.vn.service.helper.ZaloPayHelpers;
import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.cloudinary.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CreateOrderImpl implements CreateOrder {

    private final ZaloPayConfig zaloPayConfig;
    private final HttpProvider httpProvider;

    private class CreateOrderData {
        String AppId;
        String AppUser;
        String AppTime;
        String Amount;
        String AppTransId;
        String EmbedData;
        String Items;
        String BankCode;
        String Description;
        String Mac;

        private CreateOrderData(String amount) throws Exception {
            long appTime = new Date().getTime();
            AppId = String.valueOf(zaloPayConfig.getAppId());
            AppUser = "Merchant";
            AppTime = String.valueOf(appTime);
            Amount = amount;
            AppTransId = ZaloPayHelpers.getAppTransId();
            EmbedData = "{}";
            Items = "[]";
            BankCode = "zalopayapp";
            Description = "Merchant pay for order #" + ZaloPayHelpers.getAppTransId();
            String inputHMac = String.format("%s|%s|%s|%s|%s|%s|%s",
                    this.AppId,
                    this.AppTransId,
                    this.AppUser,
                    this.Amount,
                    this.AppTime,
                    this.EmbedData,
                    this.Items);

            Mac = ZaloPayHelpers.getMac(zaloPayConfig.getMacKey(), inputHMac);
        }
    }

    @Override
    public JSONObject createOrder(String amount) throws Exception {
        CreateOrderData input = new CreateOrderData(amount);

        RequestBody formBody = new FormBody.Builder()
                .add("app_id", input.AppId)
                .add("app_user", input.AppUser)
                .add("app_time", input.AppTime)
                .add("amount", input.Amount)
                .add("app_trans_id", input.AppTransId)
                .add("embed_data", input.EmbedData)
                .add("item", input.Items)
                .add("bank_code", input.BankCode)
                .add("description", input.Description)
                .add("mac", input.Mac)
                .build();

        return httpProvider.sendPost(zaloPayConfig.getUrlCreateOrder(), formBody);
    }
}

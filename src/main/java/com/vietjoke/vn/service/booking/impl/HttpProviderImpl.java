package com.vietjoke.vn.service.booking.impl;

import com.vietjoke.vn.service.booking.HttpProvider;
import okhttp3.*;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.cloudinary.json.JSONException;
import org.cloudinary.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class HttpProviderImpl implements HttpProvider {

    @Override
    public JSONObject sendPost(String URL, RequestBody formBody) throws IOException {
        JSONObject data = new JSONObject();
        try {
            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .cipherSuites(
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectionSpecs(Collections.singletonList(spec))
                    .callTimeout(5000, TimeUnit.MILLISECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(URL)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .post(formBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    data = null;
                } else if (response.body() != null) {
                    data = new JSONObject(response.body().string());
                } else {
                    data = null;
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
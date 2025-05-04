package com.vietjoke.vn.service.booking;

import okhttp3.RequestBody;
import org.cloudinary.json.JSONObject;

import java.io.IOException;

public interface HttpProvider {
    JSONObject sendPost(String URL, RequestBody formBody) throws IOException;
}

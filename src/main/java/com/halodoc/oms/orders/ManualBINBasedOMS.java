package com.halodoc.oms.orders;

import com.halodoc.utils.http.RestClient;
import io.restassured.response.Response;

import java.util.HashMap;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;

public class ManualBINBasedOMS {

    public static String url;

    public static Response manualApplyBIN(HashMap headers, String jsonString,String order_id) {
        url = BASE_URL+TIMOR_ORDER_PORT+APPLY_MANUAL_COUPON.replace("{order_id}", order_id);
        RestClient client = new RestClient(url, headers);
        return client.executePost(jsonString);
    }






}

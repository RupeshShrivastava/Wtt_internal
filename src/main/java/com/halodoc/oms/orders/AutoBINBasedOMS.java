package com.halodoc.oms.orders;

import com.halodoc.utils.http.RestClient;
import io.restassured.response.Response;

import java.util.HashMap;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;

public class AutoBINBasedOMS {

    public static String url;

    public static Response createOrder(HashMap headers, String jsonString) {
        url =  BASE_URL+TIMOR_ORDER_PORT+TIMOR_ORDER_V3;
        RestClient client = new RestClient(url, headers);
        return client.executePost(jsonString);
    }

    public static Response autoApplyBIN(HashMap headers, String jsonString, String order_id) {
        url =  BASE_URL+TIMOR_ORDER_PORT+AUTO_APPLY_BIN.replace("{order_id}", order_id);
        RestClient client = new RestClient(url, headers);
        return client.executePut(jsonString);
    }

    public static Response generateToken(HashMap headers, String jsonString, String order_id) {
        url =  BASE_URL+TIMOR_ORDER_PORT+GENERATE_TOKEN.replace("{order_id}", order_id);
        RestClient client = new RestClient(url, headers);
        return client.executePost(jsonString);
    }


    public static Response getOrder(HashMap headers, String order_id) {
        url =  BASE_URL+TIMOR_ORDER_PORT+GET_ORDER.replace("{order_id}", order_id);
        RestClient client = new RestClient(url, headers);
        return client.executeGet();
    }


    public static Response paymentInitialize(HashMap headers, String jsonString, String order_id) {
        url =  BASE_URL+TIMOR_ORDER_PORT+INITIALIZE_PAYMENT.replace("{order_id}", order_id);
        RestClient client = new RestClient(url, headers);
        return client.executePut(jsonString);
    }


    public static Response confirmOrder(HashMap headers, String jsonString, String order_id) {
        url =  BASE_URL+TIMOR_ORDER_PORT+CONFIRM_ORDER_BIN.replace("{order_id}", order_id);
        RestClient client = new RestClient(url, headers);
        return client.executePut(jsonString);
    }


    public static Response cancelOrder(HashMap headers, String jsonString, String order_id) {
        url =  BASE_URL+TIMOR_ORDER_PORT+CANCEL_ORDER_BIN.replace("{order_id}", order_id);
        RestClient client = new RestClient(url, headers);
        return client.executePut(jsonString);
    }


    public static Response refreshPayments(HashMap headers, String jsonString, String order_id) {
        url =  BASE_URL+TIMOR_ORDER_PORT+REFRESH_PAYMENTS.replace("{order_id}", order_id);
        RestClient client = new RestClient(url, headers);
        return client.executePut(jsonString);
    }

}

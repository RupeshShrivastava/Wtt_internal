package com.halodoc.oms.orders.utilities.komodo;

import com.halodoc.oms.orders.AutoBINBasedOMS;
import com.halodoc.oms.orders.library.BaseUtil;
import com.halodoc.oms.orders.utilities.timor.TimorUtil;
import com.halodoc.utils.http.RestClient;
import io.restassured.response.Response;
import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import java.util.ArrayList;
import java.util.HashMap;

public class KomodoUtil extends BaseUtil {
    private HashMap<String,String> headers = getLoginHeaders(CONTENT_TYPE, PHONE_NUMBER_CUSTOMER ,CUSTOMER_PORT_AUTH,CUSTOMER_USER_AGENT,NEW_BASE_URL);

    public Response createOrder(String entityId, String cartId, boolean isInvalidToken, boolean isWithoutToken) {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;

        long currentDate = getEpochTime(CURRENT_DATE_KEY, 0, null);
        cartId = cartId == null ? getUUID() : cartId;
        String url = KOMODO_BASE_URL + TIMOR_ORDER_V3;
        String jsonBody = cartId.equals(EMPTY_STRING) && entityId.equals(EMPTY_STRING) ? getJson(JSON_LOCATION_SEARCH, EMPTY_JSON) :
                            getJson(JSON_LOCATION_KOMODO, KOMODO_CREATE_ORDER_JSON)
                                    .replace("$entityId", entityId)
                                    .replace("$cartId", cartId)
                                    .replace("$orderDate", Long.toString(currentDate));

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executePost(jsonBody);
        String orderId = response.path("customer_order_id");
        checkLocking(orderId);

        return response;
    }

    public Response getOrder(String entityId, String orderId, boolean isInvalidToken, boolean isWithoutToken) {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;

        String customerOrderId = orderId == null ? createAndGetOrderId(entityId) : orderId;

        String url = KOMODO_BASE_URL + KOMODO_GET_ORDER_URI.replace("{customerOrderId}", customerOrderId);

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executeGet();

        return response;
    }

    public Response generateToken(String entityId, String orderId, boolean isInvalidToken, boolean isWithoutToken) throws InterruptedException {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;

        String UUID = null;
        String customerOrderId = createAndGetOrderId(entityId);
        checkStatusUntil(STATUS_APPROVED, customerOrderId);
        String jsonBody = getJson(JSON_LOCATION_ORDER, AUTO_APPLY_BIN_JSON).replace("$entity_id", entityId).replace("$cart_id", getUUID());

        Response response = AutoBINBasedOMS.autoApplyBIN(headers, jsonBody, customerOrderId);
        String total = response.body().path("total").toString();
        int length = response.body().path("applicable_adjustments.size()");

        for (int i=0;i<length;i++) {
            if (response.body().path("applicable_adjustments[" + i + "].reason").equals("bin_based")) {
                UUID = response.body().path("applicable_adjustments[" + i + "].attributes.applicable_adjustment_uuid");
            }
        }

        jsonBody = orderId != null && orderId.equals(EMPTY_STRING) ? getJson(JSON_LOCATION_SEARCH, EMPTY_JSON) :
                getJson(JSON_LOCATION_ORDER, PAYMENT_TOKEN_JSON)
                        .replace("$order_id", customerOrderId)
                        .replace("$amount", total)
                        .replace("$uuid", UUID);
        orderId = orderId == null || orderId.equals(EMPTY_STRING) ? customerOrderId : orderId;
        String url = KOMODO_BASE_URL + GENERATE_TOKEN.replace("{order_id}", orderId);

        RestClient restClient = new RestClient(url, userHeaders);
        response = restClient.executePost(jsonBody);

        return response;
    }

    public Response abandonOrder(String entityId, String customerOrderId, boolean isApproved, boolean isCancel, boolean isInvalidToken, boolean isWithoutToken) throws InterruptedException {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;

        customerOrderId = customerOrderId == null ? createAndGetOrderId(entityId) : customerOrderId;

        if(isApproved) {
            checkStatusUntil(STATUS_APPROVED, customerOrderId);

            if (isCancel) {
                apiCancelOrder(customerOrderId, false, null);
            }
        }

        String url = KOMODO_BASE_URL + KOMODO_ABANDON_ORDER_URI.replace("{customerOrderId}", customerOrderId);
        String jsonBody = customerOrderId.equals(EMPTY_STRING) ? getJson(JSON_LOCATION_SEARCH, EMPTY_JSON) : getJson(JSON_LOCATION_TIMOR, ABANDON_ORDER_JSON);

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executePut(jsonBody);

        return response;
    }

    public Response itemUpdate(String entityId, String customerOrderId, String productId, boolean isApproved, boolean isCancel, boolean isInvalidToken, boolean isWithoutToken) throws InterruptedException {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;

        customerOrderId = customerOrderId == null ? createAndGetOrderId(entityId) : customerOrderId;

        if(isApproved) {
            checkStatusUntil(STATUS_APPROVED, customerOrderId);

            if (isCancel) {
                apiCancelOrder(customerOrderId, false, null);
            }
        }

        String url = KOMODO_BASE_URL + KOMODO_UPDATE_ITEM_URI.replace("{customerOrderId}", customerOrderId);
        String jsonBody = productId.equals(EMPTY_STRING) ? getJson(JSON_LOCATION_SEARCH, EMPTY_JSON) :
                            getJson(JSON_LOCATION_KOMODO, KOMODO_UPDATE_CART_JSON)
                                    .replace("$productId", productId)
                                    .replace("$requestedqty", "1");

        RestClient restClient = new RestClient(url, userHeaders);
        checkLocking(customerOrderId);
        Response response = restClient.executePut(jsonBody);
        checkLocking(customerOrderId);

        return response;
    }

    public Response getOrderDocument(String orderId, String fetchCategory, boolean isInvalidToken, boolean isWithoutToken) {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;

        String url = KOMODO_BASE_URL + KOMODO_GET_ORDER_DOCUMENT_URI.replace("{customerOrderId}", orderId);
        HashMap<String, String> queryParam = new HashMap<String, String>();

        if(!fetchCategory.equals(EMPTY_STRING)) {
            queryParam.put(CATEGORY_KEY, fetchCategory);
        }

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executeGet(queryParam);

        return response;
    }

    public Response applyPromo(String entityId, String orderId, String couponCode, boolean isDuplicateCoupon, boolean isApprove, boolean isInvalidToken, boolean isWithoutToken) throws InterruptedException {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;

        String customerOrderId = createAndGetOrderId(entityId);

        if(isApprove) {
            checkStatusUntil(STATUS_APPROVED, customerOrderId);
        }

        String jsonBody = couponCode.equals(EMPTY_STRING) ? getJson(JSON_LOCATION_SEARCH, EMPTY_JSON) :
                getJson(COMMON_JSON, PROMOTION_JSON).replace("$promotionCode", couponCode);
        orderId = orderId == null ? customerOrderId : orderId;
        Response response = apiApplyPromo(userHeaders, orderId, jsonBody);

        if(isDuplicateCoupon) {
            response = apiApplyPromo(userHeaders, orderId, jsonBody);
        }

        return response;
    }

    public Response deletePromo(String entityId, String orderId, String couponCode, boolean isInvalidToken, boolean isWithoutToken) throws InterruptedException {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;
        String jsonBody = getJson(COMMON_JSON, PROMOTION_JSON).replace("$promotionCode", COUPON_CODE);

        String customerOrderId = createAndGetOrderId(entityId);
        checkStatusUntil(STATUS_APPROVED, customerOrderId);
        apiApplyPromo(null, customerOrderId, jsonBody);

        orderId = orderId == null ? customerOrderId : orderId;
        jsonBody = couponCode.equals(EMPTY_STRING) ? getJson(JSON_LOCATION_SEARCH, EMPTY_JSON) :
                getJson(COMMON_JSON, PROMOTION_JSON).replace("$promotionCode", couponCode);

        Response response = apiDeletePromo(userHeaders, orderId, jsonBody);

        return response;
    }

    public Response cancelOrder(String entityId, String orderId, boolean isInvalidToken, boolean isWithoutToken) throws InterruptedException {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;
        TimorUtil timorUtil = new TimorUtil();

        String customerOrderId = createAndGetOrderId(entityId);
        timorUtil.checkOrderStatus(customerOrderId,"approved");
        confirmOrder(entityId, customerOrderId, true, false, false, false);

        boolean isEmptyParam = orderId != null && orderId.equals(EMPTY_STRING);
        customerOrderId = orderId == null || isEmptyParam ? customerOrderId : orderId;

        Response response = apiCancelOrder(customerOrderId, isEmptyParam, userHeaders);

        return response;
    }

    public Response updatePatient(String entityId, String customerOrderId, boolean isApproved, boolean isCancel, boolean isInvalidToken, boolean isWithoutToken) throws InterruptedException {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;

        customerOrderId = customerOrderId == null ? createAndGetOrderId(USER_ENTITY_ID) : customerOrderId;

        if(isApproved) {
            checkStatusUntil(STATUS_APPROVED, customerOrderId);

            if (isCancel) {
                apiCancelOrder(customerOrderId, false, null);
            }
        }

        String url = KOMODO_BASE_URL + KOMODO_UPDATE_PATIENT_URI.replace("{customerOrderId}", customerOrderId);
        String jsonBody = entityId.equals(EMPTY_STRING) ? getJson(JSON_LOCATION_SEARCH, EMPTY_JSON) :
                getJson(JSON_LOCATION_TIMOR, UPDATE_PATIENT_JSON).replace("$patient_id", entityId);

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executePut(jsonBody);

        return response;
    }

    public Response getCategory(String latitude, String longitude, boolean isInvalidToken, boolean isWithoutToken) {
        String url = KOMODO_BASE_URL + KOMODO_GET_CATEGORY_URI;
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                    isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;
        HashMap<String, String> queryParam = new HashMap<String, String>();

        if(!(latitude == null && longitude == null)) {
            queryParam.put(LATITUDE_KEY, latitude);
            queryParam.put(LONGITUDE_KEY, longitude);
        }

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executeGet(queryParam);

        return response;
    }

    public Response getCategoryProducts(String categoryId, String latitude, String longitude, boolean isInvalidToken, boolean isWithoutToken) {
        String url = KOMODO_BASE_URL + KOMODO_GET_CATEGORY_PRODUCTS_URI.replace("{categoryId}", categoryId);
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;
        HashMap<String, String> queryParam = new HashMap<String, String>();

        if(!(latitude == null && longitude == null)) {
            queryParam.put(LATITUDE_KEY, latitude);
            queryParam.put(LONGITUDE_KEY, longitude);
        }

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executeGet(queryParam);

        return response;
    }

    public Response getStores(String latitude, String longitude, boolean isInvalidToken, boolean isWithoutToken) {
        String url = KOMODO_BASE_URL + KOMODO_GET_STORES_URI;
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;
        HashMap<String, String> queryParam = new HashMap<String, String>();

        if(!(latitude == null && longitude == null)) {
            queryParam.put(LATITUDE_KEY, latitude);
            queryParam.put(LONGITUDE_KEY, longitude);
        }

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executeGet(queryParam);

        return response;
    }

    public Response getStoreProducts(String storeId, String latitude, String longitude, int pageLimit, boolean isInvalidToken, boolean isWithoutToken) {
        String url = KOMODO_BASE_URL + KOMODO_GET_STORE_PRODUCTS_URI.replace("{storeId}", storeId);
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;
        HashMap<String, String> queryParam = new HashMap<String, String>();

        if(!(latitude == null && longitude == null)) {
            queryParam.put(LATITUDE_KEY, latitude);
            queryParam.put(LONGITUDE_KEY, longitude);

            if(pageLimit != 0) {
                queryParam.put(PER_PAGE_KEY, Integer.toString(pageLimit));
            }
        }

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executeGet(queryParam);

        return response;
    }

    public Response searchProducts(String searchText, int pageLimit, boolean isInvalidToken, boolean isWithoutToken) {
        String url = KOMODO_BASE_URL + KOMODO_SEARCH_PRODUCT_URI;
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;
        HashMap<String, String> queryParam = new HashMap<String, String>();
        queryParam.put(SEARCH_TEXT_KEY, searchText);

        if(pageLimit != 0) {
            queryParam.put(PER_PAGE_KEY, Integer.toString(pageLimit));
        }

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executeGet(queryParam);

        return response;
    }

    public Response getProduct(String productId, boolean isInvalidToken, boolean isWithoutToken) {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;
        String url = KOMODO_BASE_URL + KOMODO_GET_PRODUCT_URI.replace("{productId}", productId);

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executeGet();

        return response;
    }

    public Response confirmOrder(String entityId, String customerOrderId, boolean isApproved, boolean isInvalidToken, boolean isWithoutToken, boolean isCreateOrder)
            throws InterruptedException {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;
        String orderId = isCreateOrder ? createAndGetOrderId(USER_ENTITY_ID) : customerOrderId;
        float price = 0;

        if(isApproved) {
            checkStatusUntil(STATUS_APPROVED, orderId);
            Response response = getOrderDetails(orderId);
            price = response.path("total");
        }

        customerOrderId = customerOrderId == null ? orderId : customerOrderId;

        String url = KOMODO_BASE_URL + KOMODO_CONFIRM_ORDER_URI.replace("{customerOrderId}", customerOrderId);
        String jsonBody = entityId.equals(EMPTY_STRING) ? getJson(JSON_LOCATION_SEARCH, EMPTY_JSON) :
                getJson(JSON_LOCATION_TIMOR, CONFIRM_ORDER_JSON).replace("$patient_id", entityId).replace("$price", Float.toString(price));

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executePut(jsonBody);

        return response;
    }

    public Response createNationwideOrder(String storeId, String latitude, String longitude, String productId) {
        long currentDate = getEpochTime(CURRENT_DATE_KEY, 0, null);
        String cartId = getUUID();
        String url = KOMODO_BASE_URL + TIMOR_ORDER_V3;
        String jsonBody = null;

        if(storeId == null) {
            jsonBody = getJson(JSON_LOCATION_KOMODO, KOMODO_CREATE_NON_STORE_ORDER_JSON)
                    .replace("$entityId", USER_ENTITY_ID)
                    .replace("$cartId", cartId)
                    .replace("$orderDate", Long.toString(currentDate))
                    .replace("$productId", productId)
                    .replace("$latitude", latitude)
                    .replace("$longitude", longitude);
        } else {
            jsonBody = getJson(JSON_LOCATION_KOMODO, KOMODO_CREATE_NATIONWIDE_ORDER_JSON)
                                .replace("$storeId", storeId)
                                .replace("$entityId", USER_ENTITY_ID)
                                .replace("$cartId", cartId)
                                .replace("$orderDate", Long.toString(currentDate))
                                .replace("$productId", productId)
                                .replace("$latitude", latitude).replace("$longitude", longitude);
        }

        RestClient restClient = new RestClient(url, headers);
        Response response = restClient.executePost(jsonBody);

        return response;
    }

    public Response createNationwideOrderV4(String storeId, String latitude, String longitude, String productId) {
        long currentDate = getEpochTime(CURRENT_DATE_KEY, 0, null);
        String cartId = getUUID();
        String url = KOMODO_BASE_URL + TIMOR_ORDER_V4;
        String jsonBody = null;

        if(storeId == null) {
            jsonBody = getJson(JSON_LOCATION_KOMODO, KOMODO_CREATE_NON_STORE_ORDER_JSON)
                    .replace("$entityId", USER_ENTITY_ID)
                    .replace("$cartId", cartId)
                    .replace("$orderDate", Long.toString(currentDate))
                    .replace("$productId", productId)
                    .replace("$latitude", latitude)
                    .replace("$longitude", longitude);
        } else {
            jsonBody = getJson(JSON_LOCATION_KOMODO, KOMODO_CREATE_NATIONWIDE_ORDER_JSON)
                    .replace("$storeId", storeId)
                    .replace("$entityId", USER_ENTITY_ID)
                    .replace("$cartId", cartId)
                    .replace("$orderDate", Long.toString(currentDate))
                    .replace("$productId", productId)
                    .replace("$latitude", latitude).replace("$longitude", longitude);
        }

        RestClient restClient = new RestClient(url, headers);
        Response response = restClient.executePost(jsonBody);

        return response;
    }

    public Response reallocateOrder(String entityId, String orderId, boolean isConfirmOrder, boolean isReallocated, boolean isInvalidToken,
            boolean isWithoutToken) throws InterruptedException {
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;

        orderId = orderId == null ? createAndGetOrderId(entityId) : orderId;

        if(isConfirmOrder) {
            confirmOrder(entityId, orderId, true, false, false, false);
        }

        if(isReallocated) {
            cancelOrder(entityId, orderId, false, false);
            checkStatusUntil(STATUS_CONFIRMED, orderId);
        }

        String url = KOMODO_BASE_URL + KOMODO_REALLOCATE_ORDER_URI.replace("{customerOrderId}", orderId);
        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executePut();

        return response;
    }

    public Response apiApplyPromo(HashMap<String, String> userHeaders, String customerOrderId, String jsonBody) {
        userHeaders = userHeaders == null ? headers : userHeaders;
        String url = KOMODO_BASE_URL + KOMODO_PROMO_URI.replace("{customerOrderId}", customerOrderId);

        RestClient restClient = new RestClient(url, userHeaders);
        checkLocking(customerOrderId);
        Response response = restClient.executePost(jsonBody);
        checkLocking(customerOrderId);

        return response;
    }

    public Response apiDeletePromo(HashMap<String, String> userHeaders, String customerOrderId, String jsonBody) {
        String url = KOMODO_BASE_URL + KOMODO_PROMO_URI.replace("{customerOrderId}", customerOrderId);

        RestClient restClient = new RestClient(url, userHeaders);
        Response response = restClient.executeDelete(jsonBody);

        return response;
    }

    public Response apiCancelOrder(String customerOrderId, boolean isEmptyParam, HashMap<String, String> userHeaders) {
        String url = KOMODO_BASE_URL + KOMODO_CANCEL_ORDER_URI.replace("{customerOrderId}", customerOrderId);
        userHeaders = userHeaders == null ? headers : userHeaders;
        String jsonBody = isEmptyParam ? getJson(JSON_LOCATION_SEARCH, EMPTY_JSON) :
                getJson(JSON_LOCATION_TIMOR, CANCEL_ORDER_JSON).replace("$promotionCode", COUPON_CODE);

        RestClient restClient = new RestClient(url, userHeaders);
        checkLocking(customerOrderId);
        Response response = restClient.executePut(jsonBody);
        checkLocking(customerOrderId);

        return response;
    }

    public String createAndGetOrderId(String entityId) {
        Response response = createOrder(entityId, null, false, false);
        String customerOrderId = response.path("customer_order_id");

        return customerOrderId;
    }

    public String getOrderIdByResponseBody(Response response) {
        String customerOrderId = response.path("customer_order_id");

        return customerOrderId;
    }

    public String getDeliveryOptionsId(Response response, String selectedDelivery) {
        ArrayList deliveryOptions = response.path("delivery_options");
        String result = null;

        for(int i = 0; i < deliveryOptions.size(); i++) {
            String deliveryType = response.path("delivery_options[" + i + "].delivery_type");

            if(deliveryType.equals(selectedDelivery)) {
                result = response.path("delivery_options[" + i + "].external_id");
                break;
            }
        }

        return result;
    }

    public Response submitMedicineForm(String JSONFixture, String entityId, String customerId, boolean isInvalidToken, boolean isWithoutToken){
        HashMap<String, String> userHeaders = isInvalidToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, false) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;
        String uri = KOMODO_BASE_URL + KOMODO_SUBMIT_MEDICINE_FORM_URI.replace("{customerOrderId}",customerId);

        String jsonBody = getJson(JSON_LOCATION_KOMODO, JSONFixture)
                .replace("$entity_id", entityId);

        RestClient restClient = new RestClient(uri, userHeaders);
        return restClient.executePost(jsonBody);
    }

}
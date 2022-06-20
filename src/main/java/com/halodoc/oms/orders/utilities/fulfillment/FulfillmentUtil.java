package com.halodoc.oms.orders.utilities.fulfillment;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.halodoc.oms.orders.library.BaseUtil;
import com.halodoc.oms.orders.utilities.komodo.KomodoUtil;
import com.halodoc.utils.http.RestClient;
import io.restassured.response.Response;

public class FulfillmentUtil extends BaseUtil {
    private HashMap<String,String> headers = getXappHeaders(CONTENT_TYPE, X_APP_TOKEN_FULFILLMENT);
    private KomodoUtil komodoUtil = new KomodoUtil();

    public Response checkReallocation(String entityId, String type, boolean isReallocated, boolean isInvalidToken, boolean isWithoutToken)
            throws InterruptedException, ParseException {
        HashMap<String, String> userHeaders = isInvalidToken ? getXappHeaders(CONTENT_TYPE, X_APP_TOKEN_CMS) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : headers;

        Response response = komodoUtil.createOrder(USER_ENTITY_ID, null, false, false);
        String orderId = response.path("customer_order_id");
        komodoUtil.confirmOrder(entityId, orderId, true, false, false, false);
        checkStatusUntil(STATUS_CONFIRMED, orderId);
        response = komodoUtil.getOrder(USER_ENTITY_ID, orderId, false, false);
        String groupId = response.path("items[0].group_id");

        if(isReallocated) {
            komodoUtil.cancelOrder(entityId, orderId, false, false);
            checkStatusUntil(STATUS_CONFIRMED, orderId);
        }

        String url = FULFILLMENT_BASE_URL + FULFILLMENT_CHECK_REALLOCATION_URI;
        String jsonBody = null;

        if(type.equals(EMPTY_STRING)) {
            jsonBody = getJson(JSON_LOCATION_SEARCH, EMPTY_JSON);
        } else {
            String jsonTmp = getJson(JSON_LOCATION_FULFILLMENT, FULFILLMENT_CHECK_REALLOCATION_JSON)
                                .replace("$type", type)
                                .replace("$groupId", groupId);
            jsonBody = constructJsonBody(response, jsonTmp);
        }

        RestClient restClient = new RestClient(url, userHeaders);
        response = restClient.executePut(jsonBody);

        return response;
    }

    public String constructJsonBody(Response response, String jsonBody) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonBody);
        JSONObject bodyResponse = response.getBody().as(JSONObject.class);
        jsonObject.replace("order", bodyResponse);

        String result = jsonObject.toString();

        return result;
    }
}
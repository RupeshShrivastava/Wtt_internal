package com.halodoc.oms.orders.utilities.pdSubscription;

import java.util.HashMap;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.*;
import org.json.JSONObject;
import io.restassured.response.Response;
import static org.testng.Assert.*;

public class SubscriptionManagerUtils extends CommonUtils{

    CommonUtils commonUtils = new CommonUtils();

    public void getSubscription(String userId,String pageNo,String perPage){
        HashMap<String,String> queryParams = new HashMap<>();
        HashMap<String,String> headers = new HashMap<>();
        headers = commonUtils.withDefault(userId,true);

        queryParams.put("per_page",perPage);
        queryParams.put("page_no",pageNo);
        queryParams.put("sort_order","desc");
        queryParams.put("sort_field","updated_at");

        Response response = getApiStatus(CUSTOMER_STAGE+GET_PRODUCT_SUBSCRIPTION,queryParams,headers);
        JSONObject jsonObject = new JSONObject(response.asString());

        assertTrue(jsonObject.getJSONArray("result").length()>0,"product subscription is available for this user "+
                userId);
        assertTrue(!(jsonObject.get("next_page").toString().isEmpty()),"next_page attribute is blank");


    }

    public Response findSubscriptionDetails(String userId,String subscriptionStatus,String subProductID){
        HashMap<String,String> queryParams = new HashMap<>();
        HashMap<String,String> headers = new HashMap<>();
        headers = commonUtils.withDefault(userId,true);

        queryParams.put("per_page","10");
        queryParams.put("page_no","1");
        queryParams.put("sort_order","desc");
        queryParams.put("sort_field","updated_at");

        Response response = getApiStatus(CUSTOMER_STAGE+GET_PRODUCT_SUBSCRIPTION,queryParams,headers);
        JSONObject jsonObject = new JSONObject(response.asString());

        String status = jsonObject.getJSONArray("result").getJSONObject(0).get("status").toString();
        assertTrue(status.equals(subscriptionStatus),"requested status isn't mataching actual "+status+" ,expected "+subscriptionStatus);

        String productId = jsonObject.getJSONArray("result").getJSONObject(0).
                getJSONArray("product_subscription_items").getJSONObject(0).get("product_id").toString();

        assertTrue(productId.equals(subProductID),"requested product id isn't mataching actual "+productId+" ,expected "+subProductID);
        return response;
    }

    public void cancelSubscription(String userId,String subProductID){
        HashMap<String,String> headers = new HashMap<>();
        headers = commonUtils.withDefault(userId,true);
        Response response = putApiWithoutBodyStatus(CUSTOMER_STAGE+CANCEL_PRODUCT_SUBSCRIPTION,headers);

    }

    public void skipSubscriptionDelivery(String userId,String productId){
        HashMap<String,String> headers = new HashMap<>();
        headers = commonUtils.withDefault(userId,true);
        Response response = findSubscriptionDetails(userId,"scheduled",productId);
        JSONObject jsonObject = new JSONObject(response.asString());

        String deliveryId = jsonObject.getJSONArray("result").getJSONObject(0).
                getJSONArray("product_subscription_items").getJSONObject(0).getJSONObject("current_delivery").get("external_id").toString();

        String subscriptionId = jsonObject.getJSONArray("result").getJSONObject(0).get("external_id").toString();

        Response response1 = putApiWithoutBodyStatus(CUSTOMER_STAGE+GET_PRODUCT_SUBSCRIPTION+"/"+subscriptionId+
                "/delivery/"+deliveryId+"/skip",headers);

    }

    public void renewSubscription(String userId,String subProductID){
        HashMap<String,String> headers = new HashMap<>();
        headers = commonUtils.withDefault(userId,true);
        Response response = putApiWithoutBodyStatus(CUSTOMER_STAGE+RENEW_PRODUCT_SUBSCRIPTION,headers);

        JSONObject jsonObject = new JSONObject(response.asString());

        String status = jsonObject.getJSONArray("result").getJSONObject(0).get("status").toString();
        assertTrue(status.equals("scheduled"),"requested status isn't mataching actual "+status+" ,expected scheduled");

    }
}

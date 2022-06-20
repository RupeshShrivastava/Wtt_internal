package com.halodoc.oms.orders.utilities.pdSubscription;

import java.util.HashMap;
import java.util.UUID;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.*;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import com.halodoc.oms.orders.utilities.JsonBuilderUtil;
import io.restassured.response.Response;

public class TimorSubscriptionUtils {

    HashMap<String,String> headers = new HashMap<>();
    CommonUtils commonUtils = new CommonUtils();


    public String createOrder(String userId,String ListingId,String packageId,String fulfillId,String templateName){

        HashMap<String, Object> data=new HashMap<>();
        data.put("user_id".toString(),userId);
        //data.put(patient_id.toString(), userId);
        data.put("guid".toString(), UUID.randomUUID());
        data.put("subscription_package".toString(),packageId);
        data.put("merchant_location_id".toString(), fulfillId);
        data.put("product_extId".toString(),ListingId);
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription",templateName,data);
        headers = commonUtils.withDefault(userId,true);
        Response response = commonUtils.postApiResponse(request,CUSTOMER_STAGE+ timor_order,headers);

        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"found create order api response "+response.statusCode()+" Response \n"
                + response.asString()+"\n");
        JSONObject jsonObject = new JSONObject(response.asString());
        Assert.assertTrue(jsonObject.get("status").equals("created"),"Order went into different status,found this "+jsonObject.get("status"));
        return jsonObject.get("customer_order_id").toString();

    }

    public HashMap<String, String> getOrderDetails(String orderId,String status,Boolean flag,String deliveryType) throws InterruptedException {
        Response response;
        JSONObject jsonObject;
        HashMap<String,String> map = new HashMap<>();
        response = CommonUtils.getApiResponseNoQueryParams(CUSTOMER_STAGE +timorV2order+orderId,headers);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"found get order details api response "+response.statusCode()+" Response \n"
                + response.asString());
        jsonObject = new JSONObject(response.asString());
        while(jsonObject.get("status").equals("created") || jsonObject.get("status").equals("on-hold")){
            Thread.sleep(10000);
            response = CommonUtils.getApiResponseNoQueryParams(CUSTOMER_STAGE +timorV2order+orderId,headers);
            Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"found get order details api response "+response.statusCode()+" Response \n"
                    + response.asString());
            jsonObject = new JSONObject(response.asString());

        }
        Assert.assertTrue(jsonObject.get("status").equals(status),"Order went into different status\n,found this "+jsonObject.get("status"));

        if(flag) {
            map.put("customer_order_id", jsonObject.get("customer_order_id").toString());

            for (int i = 0; i < jsonObject.getJSONArray("items").length(); i++) {

                map.put("listing_id_" + i, jsonObject.getJSONArray("items").getJSONObject(i).get("listing_id").toString());
                map.put("group_id_" + i, jsonObject.getJSONArray("items").getJSONObject(i).get("group_id").toString());
            }

            map.put("itemCnt", String.valueOf(jsonObject.getJSONArray("items").length()));

            int j = 0;
            for (int i = 0; i < jsonObject.getJSONArray("delivery_options").length(); i++) {
                if (jsonObject.getJSONArray("delivery_options").getJSONObject(i).get("delivery_type").equals(deliveryType)
                    /*|| jsonObject.getJSONArray("delivery_options").getJSONObject(i).get("delivery_type").equals("delayed_instant")*/) {
                    map.put("delivery_ext_id_" + j, jsonObject.getJSONArray("delivery_options").getJSONObject(i).get("external_id").toString());
                    j++;
                    // flag = false;
                }/*else
                if(flag)
                map.put("delivery_ext_id",jsonObject.getJSONArray("delivery_options").getJSONObject(i).get("external_id").toString());*/
                //Assert.assertTrue(!map.get("delivery_ext_id_"+j).isEmpty(), "instant delivery option is not available");
                if (j == 2)
                    break;

            }

            for (int i = 0; i < jsonObject.getJSONArray("items").length(); i++) {

                JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONArray("items").getJSONObject(i).toString());

                for (int k = 0; k < jsonObject1.getJSONArray("attribute_list").length(); k++) {

                    System.out.println(jsonObject1.getJSONArray("attribute_list").getJSONObject(k).get("attribute_key").toString());

                    if (jsonObject1.getJSONArray("attribute_list").getJSONObject(k).get("attribute_key").equals("product_subscription_id")) {
                        map.put("product_subscription_id_" + i,
                                jsonObject1.getJSONArray("attribute_list").getJSONObject(k).get("attribute_value").toString());
                    }else if(jsonObject1.getJSONArray("attribute_list").getJSONObject(k).get("attribute_key").equals("product_subscription_order_number")){
                        map.put("product_subscription_order_number_" + i,
                                jsonObject1.getJSONArray("attribute_list").getJSONObject(k).get("attribute_value").toString());
                    }else if(jsonObject1.getJSONArray("attribute_list").getJSONObject(k).get("attribute_key").equals("subscription_id")){
                        map.put("subscription_id_" + i,
                                jsonObject1.getJSONArray("attribute_list").getJSONObject(k).get("attribute_value").toString());
                    }else if(jsonObject1.getJSONArray("attribute_list").getJSONObject(k).get("attribute_key").equals("subscription_id")){
                        map.put("subscription_id_" + i,
                                jsonObject1.getJSONArray("attribute_list").getJSONObject(k).get("attribute_value").toString());

                    }else if(jsonObject1.getJSONArray("attribute_list").getJSONObject(k).get("attribute_key").equals("package_frequency_unit")){
                        map.put("package_frequency_unit_" + i,
                                jsonObject1.getJSONArray("attribute_list").getJSONObject(k).get("attribute_value").toString());

                    }
                }
            }
        }

        if(status.equals("confirmed")){

            for(int i=0;i<jsonObject.getJSONArray("shipments").length();i++){
                map.put("shipemntId_"+i,jsonObject.getJSONArray("shipments").getJSONObject(i).get("id").toString());
                map.put("shipemntGroupId_"+i,jsonObject.getJSONArray("shipments").getJSONObject(i).get("group_id").toString());
                map.put("tracking_id_"+i,jsonObject.getJSONArray("shipments").getJSONObject(i).get("tracking_id").toString());
                map.put("provider"+i,jsonObject.getJSONArray("shipments").getJSONObject(i).get("provider").toString());
                map.put("shipmentCount",String.valueOf(i+1));
            }
        }

        return map;
    }

    public void updateCart(HashMap<String,String> data,String templateName){
        HashMap<String, Object> data1=new HashMap<>();
        for(int i=0;i<Integer.parseInt(data.get("itemCnt"));i++) {
            data1.put(("group_id_"+i).toString(), data.get("group_id_"+i));
            data1.put(("delivery_ext_id_"+i).toString(), data.get("delivery_ext_id_"+i));
            data1.put(("listing_id_"+i).toString(), data.get("listing_id_"+i));
        }

        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription",templateName,data1);
        System.out.println(request);
        Response response = commonUtils.postApiResponse(request,CUSTOMER_STAGE+ UPDATE_MULTIPLE_PHARMACY.replace("${pdOrderId}",data.get("customer_order_id")),headers);

        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"found update cart order api response "+response.statusCode()+
                "\n "+response.asString()+"\n");
        JSONObject jsonObject = new JSONObject(response.asString());
        Assert.assertTrue(jsonObject.get("status").equals("approved"),"Order went into different status,found this "+jsonObject.get("status"));
    }


    private String payAmountByGopayWalletMultiplePharmacy(String pd_order_id) {
        HashMap<String, Object> data=new HashMap<>();
        data.put("order_id", pd_order_id);
        data.put("instrument_id", getUserInstrumentId().path("[0].external_id"));
        headers.put("X-APP-TOKEN",TIMOR_APP_TOKEN);

        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","gopay-charge",data);

        //System.out.println(TIMOR_BASE_URL +timorV1order+"/internal/"+pd_order_id+PD_PAY_AMOUNT_BY_GOPAY_WALLET);
        //Response response = CommonUtils.postApiStatus(request, TIMOR_BASE_URL +timorV1order+"internal/"+pd_order_id+PD_PAY_AMOUNT_BY_GOPAY_WALLET, headers);
        Response response = CommonUtils.postApiStatus(request, CUSTOMER_STAGE +timorV1order+pd_order_id+PD_PAY_AMOUNT_BY_GOPAY_WALLET, headers);
        try {
            Thread.sleep(5*1000);
            Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"payment change api response "+response.statusCode());
            JSONObject jsonObject = new JSONObject(response.asString());

            /*String temp = jsonObject.getJSONArray("attribute_list").getJSONObject(2).get("attribute_key").toString();
            if(temp.equals("verification-link-app")){
                RemoteWebDriver driver = WebDriverSetup.getDriver();
                driver.get(temp);
            }*/
            return jsonObject.get("customer_payment_id").toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void confirmGopayWalletPayment(String order_id) {
        HashMap<String, Object> data=new HashMap<>();
        data.put("payment_id", payAmountByGopayWalletMultiplePharmacy(order_id));

        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","gopay-confirm",data);
        Response response = CommonUtils.putApiResponse(request, CUSTOMER_STAGE +timorV5order+order_id+PD_CONFIRM_PAYMENT_ORDER, headers);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_NO_CONTENT,"found payment confirm order api response "+response.statusCode());
    }


    public Response getUserInstrumentId(){
        HashMap<String,String> queryParam = new HashMap<>();
        queryParam.put("payment_provider","xendit");
        Response response =  CommonUtils.getApiStatus(SCROOGE_BASE_URL+userInstrument, queryParam,headers);
        Reporter.log(response.prettyPrint(),true);
        return response;
    }

    public void orderShipAndDeliver(String orderId,String shipmentId) throws InterruptedException {

                CommonUtils.putApiWithoutBodyStatus(TIMOR_BASE_URL + timorV1orderInternal + orderId + "/shipments/" +
                    shipmentId+"/ship", headers);
            Thread.sleep(10000);
        CommonUtils.putApiWithoutBodyStatus(TIMOR_BASE_URL + timorV1orderInternal + orderId + "/shipments/" +
                shipmentId+"/deliver", headers);

    }
    public void cancelShipment(String orderId,String groupId){
        HashMap<String, Object> data=new HashMap<>();
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","cancel-shipment",data);

        CommonUtils.putApiResponse(request, TIMOR_BASE_URL + timorV1order + orderId + "/groups/" +
                groupId+"/cancel", headers);
        /*Response response = CommonUtils.getApiResponseNoQueryParams(STAGE_BASE_URL+timorV2order+orderId,headers);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"found get order details api response "+response.statusCode()+" Response \n"
                + response.asString());
        JSONObject jsonObject = new JSONObject(response.asString());
        Assert.assertTrue(jsonObject.get("status").equals("cancelled"),"Order went into different status,found this "+jsonObject.get("status"));*/

    }

    public void driverCancel(String trackId,String orderId,String provider){
        HashMap<String, Object> data=new HashMap<>();
        data.put("tracking_id",trackId);
        if(provider.equals("gosend")) {
            String request = JsonBuilderUtil.generateDataFromMap("pd-subscription", "driver-gosend-cancel", data);
            CommonUtils.postApiStatus(request, TIMOR_BASE_URL + SHIPMENT_TRACK_EVENT, headers);
        }else if (provider.equals("shipper")){
            String request = JsonBuilderUtil.generateDataFromMap("pd-subscription", "driver-shipper-cancel", data);
            CommonUtils.postApiStatus(request, TIMOR_BASE_URL + SHIPPER_TRACK_EVENT, headers);

        }

        Response response = CommonUtils.getApiResponseNoQueryParams(TIMOR_BASE_URL +timorV2order+orderId,headers);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"found get order details api response "+response.statusCode()+" Response \n"
                + response.asString());
        JSONObject jsonObject = new JSONObject(response.asString());

        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"found get order details api response "+response.statusCode()+" Response \n"
                + response.asString());
        Assert.assertTrue(jsonObject.get("status").equals("cancelled"),"found get order details api response "+response.statusCode()+" Response \n"
                + response.asString());
        Assert.assertTrue(jsonObject.getJSONArray("shipments").getJSONObject(0).get("status").equals("cancelled"),"found get order details api response "+response.statusCode()+" Response \n"
                + response.asString());
        Assert.assertTrue(jsonObject.getJSONArray("payments").getJSONObject(1).get("type").equals("refund"),"found get order details api response "+response.statusCode()+" Response \n"
                + response.asString());
    }


    public void cancelOrderNotSubscription(String orderId){
        HashMap<String, Object> data=new HashMap<>();
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","cancel-order-notsubcription",data);

        Response response = CommonUtils.putApiResponse(request, TIMOR_BASE_URL + timorV2order +"internal/"+ orderId +"/cancel", headers);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_NO_CONTENT,"found get order details api response "+response.statusCode()+" Response \n"
                + response.asString());
    }

    public void cancelOrderSubscription(String orderId){
        HashMap<String, Object> data=new HashMap<>();
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","cancel-order-subcriptionr",data);

        Response response = CommonUtils.putApiResponse(request, TIMOR_BASE_URL + timorV2order +"internal/"+ orderId +"/cancel", headers);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_NO_CONTENT,"found get order details api response "+response.statusCode()+" Response \n"
                + response.asString());
    }


    public void abandonOrderSubscription(String orderId){
        HashMap<String, Object> data=new HashMap<>();
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","cancel-order-notsubcription",data);

        Response response = CommonUtils.putApiResponse(request, TIMOR_BASE_URL + timorV2order +"internal/"+ orderId +"/abandon", headers);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_NO_CONTENT,"found get order details api response "+response.statusCode()+" Response \n"
                + response.asString());
    }
}

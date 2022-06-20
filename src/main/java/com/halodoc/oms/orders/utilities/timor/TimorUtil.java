package com.halodoc.oms.orders.utilities.timor;

import static com.halodoc.oms.orders.utilities.constants.Constants.ABANDON_ORDER_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.ABANDON_ORDER_URI;
import static com.halodoc.oms.orders.utilities.constants.Constants.AUTH_KEY_XENDIT;
import static com.halodoc.oms.orders.utilities.constants.Constants.BASE_URL;
import static com.halodoc.oms.orders.utilities.constants.Constants.CANCEL_ORDER_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.CANCEL_ORDER_URI;
import static com.halodoc.oms.orders.utilities.constants.Constants.CMS_BASEURL;
import static com.halodoc.oms.orders.utilities.constants.Constants.CMS_GET_PRODUCT_DESCRIPTION;
import static com.halodoc.oms.orders.utilities.constants.Constants.COMMON_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.CONFIRM_BURU_ORDER_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.CONFIRM_ORDER_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.CONFIRM_ORDER_URI;
import static com.halodoc.oms.orders.utilities.constants.Constants.CONFIRM_VA_ORDER;
import static com.halodoc.oms.orders.utilities.constants.Constants.CONTENT_TYPE;
import static com.halodoc.oms.orders.utilities.constants.Constants.COOKIE_KEY;
import static com.halodoc.oms.orders.utilities.constants.Constants.CREATE_BURU_ORDER_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.CREATE_LEAD_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.CREATE_LEAD_ORDER_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.CREATE_MULTIPLE_PHAR_ORDER_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.CREATE_MULTIPLE_PHAR_REALLOCATION_ORDER_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.CREATE_ORDER_FOR_REALLOCATION_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.CREATE_ORDER_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.CREATE_ORDER_JSON_SCHEDULED;
import static com.halodoc.oms.orders.utilities.constants.Constants.CREATE_ORDER_ONE_MEDICINE;
import static com.halodoc.oms.orders.utilities.constants.Constants.CREATE_ORDER_TWO_MEDICINE;
import static com.halodoc.oms.orders.utilities.constants.Constants.CUSTOMER_PORT_AUTH;
import static com.halodoc.oms.orders.utilities.constants.Constants.CUSTOMER_USER_AGENT;
import static com.halodoc.oms.orders.utilities.constants.Constants.DELIVERY_OPTIONS_MP;
import static com.halodoc.oms.orders.utilities.constants.Constants.DEL_OPTION_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.EMPTY_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.GET_PRODUCT_DESCRIPTION;
import static com.halodoc.oms.orders.utilities.constants.Constants.INTERNAL_OMS_REALLOCATION_URI;
import static com.halodoc.oms.orders.utilities.constants.Constants.JSON_LOCATION_BURU;
import static com.halodoc.oms.orders.utilities.constants.Constants.JSON_LOCATION_TIMOR;
import static com.halodoc.oms.orders.utilities.constants.Constants.MARK_ORDER_DELIVERED;
import static com.halodoc.oms.orders.utilities.constants.Constants.MARK_ORDER_SHIPPED;
import static com.halodoc.oms.orders.utilities.constants.Constants.MULTI_GET_DOCS_LEADS;
import static com.halodoc.oms.orders.utilities.constants.Constants.MULTI_GET_LEADS_DOC;
import static com.halodoc.oms.orders.utilities.constants.Constants.NEW_BASE_URL;
import static com.halodoc.oms.orders.utilities.constants.Constants.PAYMENT_CHARGE;
import static com.halodoc.oms.orders.utilities.constants.Constants.PAYMENT_ENDPOINT;
import static com.halodoc.oms.orders.utilities.constants.Constants.PAYMENT_INITIALISE_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.PAYMENT_INITIALISE_TIMOR;
import static com.halodoc.oms.orders.utilities.constants.Constants.PAYMENT_METHOD_VALID;
import static com.halodoc.oms.orders.utilities.constants.Constants.PAYMENT_PORT;
import static com.halodoc.oms.orders.utilities.constants.Constants.PAYMENT_REFRESH;
import static com.halodoc.oms.orders.utilities.constants.Constants.PHONE_NUMBER_CUSTOMER;
import static com.halodoc.oms.orders.utilities.constants.Constants.PRODUCT_LISTING;
import static com.halodoc.oms.orders.utilities.constants.Constants.PROD_BASE_URL;
import static com.halodoc.oms.orders.utilities.constants.Constants.PROD_X_APP_TOKEN_TIMOR;
import static com.halodoc.oms.orders.utilities.constants.Constants.PROMOTION_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.PROMOTION_URI;
import static com.halodoc.oms.orders.utilities.constants.Constants.REAJECT_URI;
import static com.halodoc.oms.orders.utilities.constants.Constants.REJECT_LEAD_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.SIMULATE_VA_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.SIMULATE_VA_XENDIT;
import static com.halodoc.oms.orders.utilities.constants.Constants.STATUS_APPROVED;
import static com.halodoc.oms.orders.utilities.constants.Constants.STATUS_CONFIRMED;
import static com.halodoc.oms.orders.utilities.constants.Constants.SYSTEM_CANCEL_ORDER_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.TIMOR_ADD_DELIVERYOPTION;
import static com.halodoc.oms.orders.utilities.constants.Constants.TIMOR_ADD_FEEDBACK;
import static com.halodoc.oms.orders.utilities.constants.Constants.TIMOR_CHANGE_VERIFICATION_STATUS;
import static com.halodoc.oms.orders.utilities.constants.Constants.TIMOR_LEADS;
import static com.halodoc.oms.orders.utilities.constants.Constants.TIMOR_LEADS_DOCUMENTS;
import static com.halodoc.oms.orders.utilities.constants.Constants.TIMOR_LEADS_DOC_UPLOAD;
import static com.halodoc.oms.orders.utilities.constants.Constants.TIMOR_LEADS_INTERNAL;
import static com.halodoc.oms.orders.utilities.constants.Constants.TIMOR_LEADS_SEARCH;
import static com.halodoc.oms.orders.utilities.constants.Constants.TIMOR_ORDER;
import static com.halodoc.oms.orders.utilities.constants.Constants.TIMOR_ORDER_INTERNAL;
import static com.halodoc.oms.orders.utilities.constants.Constants.TIMOR_ORDER_INTERNAL_V1;
import static com.halodoc.oms.orders.utilities.constants.Constants.TIMOR_ORDER_PORT;
import static com.halodoc.oms.orders.utilities.constants.Constants.TRACK_ORDER_URI;
import static com.halodoc.oms.orders.utilities.constants.Constants.UPDATE_CART_ITEMS_MP;
import static com.halodoc.oms.orders.utilities.constants.Constants.UPDATE_CART_URL;
import static com.halodoc.oms.orders.utilities.constants.Constants.UPDATE_LEAD;
import static com.halodoc.oms.orders.utilities.constants.Constants.UPDATE_LEAD_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.USER_ENTITY_ID;
import static com.halodoc.oms.orders.utilities.constants.Constants.X_APP_TOKEN_CMS;
import static com.halodoc.oms.orders.utilities.constants.Constants.X_APP_TOKEN_PAYMENT;
import static com.halodoc.oms.orders.utilities.constants.Constants.X_APP_TOKEN_TIMOR;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.CUSTOMER_STAGE;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.timorV2order;
import static io.restassured.RestAssured.given;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.testng.Assert;
import com.github.javafaker.Bool;
import com.halodoc.oms.orders.GopayTokenization;
import com.halodoc.oms.orders.library.BaseUtil;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import com.halodoc.utils.http.RestClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

@Slf4j
@ResponseStatus
public class TimorUtil extends BaseUtil {

   public final StringBuilder timorOrderUri =new StringBuilder(BASE_URL).append(TIMOR_ORDER_PORT).append(TIMOR_ORDER);
    private final String timorOrderInternalUri =BASE_URL+TIMOR_ORDER_PORT+TIMOR_ORDER_INTERNAL;
    private final String prodTimorOrderInternalUri =PROD_BASE_URL+TIMOR_ORDER_INTERNAL_V1;
     public final StringBuilder baseUri =new StringBuilder(BASE_URL);
    public static HashMap<String,String> headers;
    public static HashMap<String,String> headersProd;
    public static HashMap<String,String> xAppheaders;
    public static HashMap<String,String> xAppheadersforLeads;
    public static HashMap<String,String> xAppheaders_CMS = null;
    public static HashMap<String,String> headerVirtualAcc;
    public static final int MAX_RETRY=60;
    public TimorUtil(){
        headers=getLoginHeaders(CONTENT_TYPE, PHONE_NUMBER_CUSTOMER ,CUSTOMER_PORT_AUTH,CUSTOMER_USER_AGENT,NEW_BASE_URL);
        xAppheaders=getXappHeaders(CONTENT_TYPE,X_APP_TOKEN_PAYMENT);
        xAppheadersforLeads=getXappHeaders(CONTENT_TYPE,X_APP_TOKEN_TIMOR);
        xAppheaders_CMS = getXappHeaders("application/json",X_APP_TOKEN_CMS);
        headersProd = getXappHeaders(CONTENT_TYPE,PROD_X_APP_TOKEN_TIMOR);
        headerVirtualAcc=getVaPaymentHeaders(CONTENT_TYPE);
    }

    public Response createOrder(String entityId) {
        RestClient client = new RestClient(String.valueOf(timorOrderUri).replace("v1","v3"), headers);
        Response response = client.executePost(getJson(JSON_LOCATION_TIMOR,CREATE_ORDER_JSON).replace("$entity_id", entityId).replace("$cart_id", getUUID()));
        String orderId = response.path("customer_order_id");
        checkLocking(orderId);
        return response;
    }

    public Response confirmDelOptions(String order_id, String externalId, String groupId) {
        RestClient client = new RestClient(BASE_URL + TIMOR_ORDER_PORT + TIMOR_ORDER_INTERNAL + order_id + "/update", headers);
        Response response = client
                .executePut(getJson(JSON_LOCATION_TIMOR, DELIVERY_OPTIONS_MP).replace("$external_id", externalId).replace("$group_id", groupId));
        return response;
    }

    public Response updateItems(String order_id, String listingId, String groupId, int requested_quantity, String external_id) {
        RestClient client = new RestClient(BASE_URL + TIMOR_ORDER_PORT + TIMOR_ORDER_INTERNAL + order_id + "/update", headers);
        Response response = client.executePut(
                getJson(JSON_LOCATION_TIMOR, UPDATE_CART_ITEMS_MP).replace("$listing_id", listingId).replace("$external_id", external_id)
                                                                  .replace("$group_id", groupId)
                                                                  .replace("$requested_quantity", String.valueOf(requested_quantity)));
        return response;
    }

    public Response createInternalOrder(String entityId, String sdsadad) {

        RestClient client = new RestClient(new String(timorOrderInternalUri), headers);
        Response response = client.executePost(getJson(JSON_LOCATION_TIMOR,CREATE_MULTIPLE_PHAR_ORDER_JSON).replace("$entity_id", entityId).replace("$cart_id", getUUID()));
        String orderId = response.path("customer_order_id");
        checkLocking(orderId);
        return response;
    }
    public Response createInternalOrderForReallocation(String entityId) {

        RestClient client = new RestClient(new String(timorOrderInternalUri), headers);
        Response response = client.executePost(getJson(JSON_LOCATION_TIMOR,CREATE_MULTIPLE_PHAR_REALLOCATION_ORDER_JSON).replace("$entity_id", entityId).replace("$cart_id", getUUID()));
        String orderId = response.path("customer_order_id");
        checkLocking(orderId);
        return response;
    }



    public Response createOrder(String entityId,String listingID) {
        RestClient client = new RestClient(String.valueOf(timorOrderUri).replace("v1","v2"), headers);
        Response response = client.executePost(getJson(JSON_LOCATION_TIMOR,CREATE_ORDER_FOR_REALLOCATION_JSON).replace("$entity_id", entityId).replace("$cart_id", getUUID()).replace("$listing_id",listingID));
        String orderId = response.path("customer_order_id");
        checkLocking(orderId);
        return response;
    }

    public Response createOrderV4OneMedicine(String entityId, String listingId) {
        RestClient client = new RestClient(String.valueOf(timorOrderUri).replace("v1","v4"), headers);
        Response response = client.executePost(getJson(JSON_LOCATION_TIMOR,CREATE_ORDER_ONE_MEDICINE).replace("$entity_id", entityId).replace("$cart_id", getUUID()).replace("$listing_id",listingId));
        String orderId = response.path("customer_order_id");
        checkLocking(orderId);
        return response;
    }

    public Response createOrderV4TwoMedicine(String entityId, String listingId1, String listingId2) {
        RestClient client = new RestClient(String.valueOf(timorOrderUri).replace("v1","v4"), headers);
        Response response = client.executePost(getJson(JSON_LOCATION_TIMOR,CREATE_ORDER_TWO_MEDICINE).replace("$entity_id", entityId).replace("$cart_id", getUUID()).replace("$listing_id_1",listingId1).replace("$listing_id_2",listingId2));
        String orderId = response.path("customer_order_id");
        checkLocking(orderId);
        return response;
    }

    public Response createOrderForReallocation(String entityId, String listingId) {

        RestClient client = new RestClient(String.valueOf(timorOrderUri).replace("v1","v2"), headers);
        Response response = client.executePost(getJson(JSON_LOCATION_TIMOR,CREATE_ORDER_FOR_REALLOCATION_JSON).replace("$entity_id", entityId).replace("$cart_id", getUUID()).replace("$listing_id",listingId));
        String orderId = response.path("customer_order_id");
        checkLocking(orderId);
        return response;
    }
    public Response createOrderForLeads(String entityId,String leadId){
        StringBuilder uri = new StringBuilder(timorOrderUri);
        RestClient client = new RestClient(String.valueOf(uri.append(TIMOR_LEADS_INTERNAL)), xAppheadersforLeads);
        Response response = client.executePost(getJson(JSON_LOCATION_TIMOR,CREATE_LEAD_ORDER_JSON).replace("$entity_id", entityId).replace("$cart_id", getUUID()).replace("$leadId",leadId));
        String orderId = response.path("customer_order_id");
        checkLocking(orderId);
        return response;
    }
    public Response getOrderDetails(String customerOrderId){
        StringBuilder uri = new StringBuilder(timorOrderUri);
        RestClient client=new RestClient((uri.append(customerOrderId)).toString(), headers);
        Response response =client.executeGet();
        return response;
    }

    public Response getInternalOrderDetails(String customerOrderId) {
        String uri = timorOrderInternalUri + customerOrderId;
        String uriV2 = String.valueOf(uri).replace("v1", "v2");
        RestClient client = new RestClient(BASE_URL + TIMOR_ORDER_PORT + TIMOR_ORDER_INTERNAL + customerOrderId, headers);
        Response response = client.executeGet();
        return response;
    }

    public Boolean verifyCouponApplied(String customerId,String coupon){
        Response orderResponse =getInternalOrderDetails(customerId);
        List<String> adjustments=orderResponse.path("adjustments.adjustment_reference_id");
        for(String adjustment:adjustments){
            if(adjustment.equalsIgnoreCase(coupon)){
                return true;
            }
        }
        return false;
    }
    public Boolean verifyIfRefundInitiated(String customerOrderID){
        Response response=getInternalOrderDetails(customerOrderID);
        List<String> paymentsStatus=response.path("payments.type");
        for (String status:
                paymentsStatus) {
            if(status.equalsIgnoreCase("refund")){
               return true;
            }

        }
        return false;
    }
    public Response hitRefreshApi(String customerOrderId){
        RestClient client = new RestClient(BASE_URL + TIMOR_ORDER_PORT + PAYMENT_REFRESH.replace("{customer_order_id}",customerOrderId), headers);
        Response response = client.executePut();
        return response;
    }

    public Response getOrderDetailsV2(String customerOrderId) {
        StringBuilder uri = new StringBuilder(timorOrderUri).append(customerOrderId);
        String uriV2 = String.valueOf(uri).replace("v1","v2");
        RestClient client=new RestClient(uriV2, headers);
        Response response =client.executeGet();
        return response;
    }
    public Boolean waitForReallocationToComplete(String customerOrderID){
        int i=0;
        while(i<MAX_RETRY){
            Response orderResponse = getOrderDetailsV2(customerOrderID);
            List<String> groups = orderResponse.jsonPath().getList("groups.attributes.reallocated_group");
            if(groups.contains("true")){
                return true;
            }
            i++;
        }
        return false;
    }

    public Response confirmOrder(String customerOrderId,String patientID,String price) {
        StringBuilder uri = new StringBuilder(timorOrderUri.toString().replace("v1","v4"));
        RestClient client=new RestClient((uri.append(customerOrderId)).append(CONFIRM_ORDER_URI).toString(), headers);
        checkLocking(customerOrderId);
        Response response = client.executePut(getJson(JSON_LOCATION_TIMOR, CONFIRM_ORDER_JSON).replace("$patient_id",patientID).replace("$price",price));
        checkLocking(customerOrderId);

        return response;
    }

    //  /v1/orders/internal/{customerOrderId}/confirm
    @SneakyThrows
    public Response simulateVA(String payment_id, String amount) {

        StringBuilder getOrderDetailsUrl = new StringBuilder();
        getOrderDetailsUrl.append(SIMULATE_VA_XENDIT).append(payment_id).append("/simulate_payment");
        RestAssured.urlEncodingEnabled = false;
        RestClient client = new RestClient(getOrderDetailsUrl.toString(), headerVirtualAcc);
        Response response = client.executePost(getJson(JSON_LOCATION_TIMOR, SIMULATE_VA_JSON).replace("$amount", amount));
        headers.remove("Authorization", "Basic " + AUTH_KEY_XENDIT);
        return response;
    }

    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public String chargeXenditVA(String customerOrderId) {
        RestClient client = new RestClient(BASE_URL + TIMOR_ORDER_PORT + TIMOR_ORDER_INTERNAL.replace("v2", "v1") + customerOrderId + "/charge",
                headers);
        checkLocking(customerOrderId);
        Response response = client.executePost(getJson(JSON_LOCATION_TIMOR, PAYMENT_CHARGE));
        checkLocking(customerOrderId);
        String paymentID = response.path("customer_payment_id");
        return paymentID;
    }

    public Response confirmInternalOrder(String customerOrderId, String patientID, String price) {
        StringBuilder uri = new StringBuilder(timorOrderInternalUri);
        RestClient client = new RestClient(BASE_URL + TIMOR_ORDER_PORT + TIMOR_ORDER_INTERNAL + customerOrderId + "/confirm", headers);
        checkLocking(customerOrderId);
        Response response = client
                .executePut(getJson(JSON_LOCATION_TIMOR, CONFIRM_ORDER_JSON).replace("$patient_id", patientID).replace("$price", price));
        checkLocking(customerOrderId);

        return response;
    }

    public Response confirmVaPayment(String customerOrderId, String patientID, String price,String payment_id){
        StringBuilder uri = new StringBuilder(timorOrderInternalUri);
        RestClient client = new RestClient(BASE_URL + TIMOR_ORDER_PORT + TIMOR_ORDER_INTERNAL.replace("v2","v4") + customerOrderId + "/confirm", headers);
        checkLocking(customerOrderId);
        Response response = client
                .executePut(getJson(JSON_LOCATION_TIMOR, CONFIRM_VA_ORDER).replace("$patient_id", patientID).replace("$amount", price).replace("$payment_id",payment_id));
        checkLocking(customerOrderId);

        return response;
    }
    public Response cancelOrder(String customerOrderId) {
        String uri = timorOrderInternalUri;
        RestClient client = new RestClient(uri + customerOrderId + CANCEL_ORDER_URI, headers);
        checkLocking(customerOrderId);
        Response response = client.executePut(getJson(JSON_LOCATION_TIMOR, CANCEL_ORDER_JSON));
        checkLocking(customerOrderId);
        return response;
    }

    public Response cancelOrderWithGroupID(String customerOrderId, String groupID) {
        String uri = BASE_URL + TIMOR_ORDER_PORT + TIMOR_ORDER_INTERNAL;
        new StringBuilder(timorOrderInternalUri + "/");
        RestClient client = new RestClient(uri + customerOrderId + "/groups/" + groupID + "/cancel" + "", headers);
        checkLocking(customerOrderId);
        Response response = client.executePut(getJson(JSON_LOCATION_TIMOR, CANCEL_ORDER_JSON).replace("merchant_cancelled","merchant_cancelled"));
        checkLocking(customerOrderId);
        return response;
    }

    public Response customerCancelOrder(String customerOrderId) {
        StringBuilder uri = new StringBuilder(timorOrderInternalUri+"/");
        RestClient client=new RestClient((uri.append(customerOrderId).append(CANCEL_ORDER_URI).toString()), headers);
        checkLocking(customerOrderId);
        Response response = client.executePut(getJson(JSON_LOCATION_TIMOR, CANCEL_ORDER_JSON).replace("merchant_cancelled","customer_cancelled"));
        checkLocking(customerOrderId);
        return response;
    }

    public Response systemCancelOrder(String customerOrderId,Boolean prodFlag) {
        StringBuilder uri = null;
        if(prodFlag){
             uri = new StringBuilder(prodTimorOrderInternalUri+"/");
             headers=headersProd;
        }else
         uri = new StringBuilder(timorOrderInternalUri+"/");
        RestClient client=new RestClient((uri.append(customerOrderId).append(CANCEL_ORDER_URI).toString()), headers);
        checkLocking(customerOrderId);
        Response response = client.executePut(getJson(JSON_LOCATION_TIMOR, SYSTEM_CANCEL_ORDER_JSON));
        checkLocking(customerOrderId);
        return response;
    }

    public Response merchantCancelOrder(String customerOrderId) {
        StringBuilder uri = new StringBuilder(timorOrderInternalUri + "/");
        RestClient client = new RestClient(BASE_URL + TIMOR_ORDER_PORT + TIMOR_ORDER_INTERNAL + customerOrderId + "/cancel", headers);
        checkLocking(customerOrderId);
        Response response = client.executePut(getJson(JSON_LOCATION_TIMOR, CANCEL_ORDER_JSON));
        checkLocking(customerOrderId);
        return response;
    }

    public Response mpmerchantCancelOrder(String customerOrderId, String groupId) {
        StringBuilder uri = new StringBuilder(timorOrderInternalUri + "/");
        RestClient client = new RestClient(BASE_URL + TIMOR_ORDER_PORT + TIMOR_ORDER_INTERNAL + customerOrderId + "/groups/" + groupId + "/cancel",
                headers);
        checkLocking(customerOrderId);
        Response response = client.executePut(getJson(JSON_LOCATION_TIMOR, CANCEL_ORDER_JSON));
        checkLocking(customerOrderId);
        return response;
    }

    public Response abandonOrder(String customerOrderId) {
        StringBuilder uri = new StringBuilder(timorOrderUri);
        RestClient client=new RestClient((uri.append(customerOrderId).append(ABANDON_ORDER_URI).toString()), headers);
        return client.executePut(getJson(JSON_LOCATION_TIMOR, ABANDON_ORDER_JSON));
    }

    public Response trackOrder(String customerOrderId) {
        StringBuilder uri = new StringBuilder(timorOrderUri);
        RestClient client=new RestClient((uri.append(customerOrderId).append(TRACK_ORDER_URI).toString()), headers);
        return client.executeGet();
    }
    public Response updateCart(String customerOrderId) {
        StringBuilder uri = new StringBuilder(timorOrderUri);
        RestClient client=new RestClient((uri.append(customerOrderId).append(UPDATE_CART_URL.replace("$orderId", customerOrderId)).toString()), headers);
        checkLocking(customerOrderId);
        Response response = client.executePut(getJson("fixtures", "/update_cart_valid.json"));
        checkLocking(customerOrderId);

        return response;
    }

    public Response paymentInitialise(String customerOrderId,String paymentMethod,String price) {
        StringBuilder uri = new StringBuilder(PAYMENT_ENDPOINT);
        RestClient client=new RestClient((uri.append(PAYMENT_PORT).append(PAYMENT_INITIALISE_TIMOR).toString()), xAppheaders);
        return client.executePost(getJson(JSON_LOCATION_TIMOR, PAYMENT_INITIALISE_JSON)
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$price",price)
                .replace("$order_id",customerOrderId)
                .replace("$payment_method",paymentMethod));
    }
    public Response createLead(String patientId){
       StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(TIMOR_LEADS);
       RestClient client = new RestClient(uri.toString(),headers);
       Response response = client.executePost(getJson(JSON_LOCATION_TIMOR,CREATE_LEAD_JSON).replace("$patientId",patientId));
       return response;
    }
    public Response getLead(String leadId){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(TIMOR_LEADS).append(TIMOR_LEADS_INTERNAL).append(leadId);
        RestClient client = new RestClient(uri.toString(),xAppheadersforLeads);
        return client.executeGet();
    }
    public Response getLeadDocuments(String leadId){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(TIMOR_LEADS_DOCUMENTS.replace("{leadId}",leadId));
        RestClient client = new RestClient(uri.toString(),headers);
        return client.executeGet();
    }

    public Response getLeadMultiDocuments(String leadId,String document_id){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(MULTI_GET_LEADS_DOC.replace("{leadId}",leadId));
        RestClient client = new RestClient(uri.toString(),headers);
        return client.executePut(getJson(JSON_LOCATION_TIMOR,MULTI_GET_DOCS_LEADS).replace("{document_id}",document_id));
    }
    @SneakyThrows
    public Response uploadDocLead(String fileName){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(TIMOR_LEADS_DOC_UPLOAD);
        headers.remove("Content-Type");
        headers.put("x-file-type", "image/png");
        headers.put("x-file-name", "leads-automation");
        Path file_path = Paths.get(System.getProperty("user.dir")+fileName);
        byte[] file_data = Files.readAllBytes(file_path);
        Response response= given(). headers(headers).
                body(file_data).
                log().all().
                post(uri.toString());
        headers.put("Content-Type","application/json");
        return response;
    }
    public Response updateLead(String leadId,String document_id,String doc_url){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(UPDATE_LEAD.replace("{leadId}",leadId));
        RestClient client = new RestClient(uri.toString(),headers);
        return client.executePatch(getJson(JSON_LOCATION_TIMOR,UPDATE_LEAD_JSON).replace("{document_id}",document_id).replace("{doc_url}",doc_url));
    }
    public Response rejectLead(String leadId){
       StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(TIMOR_LEADS).append(TIMOR_LEADS_INTERNAL).append(leadId).append(REAJECT_URI);
       RestClient client = new RestClient(uri.toString(),xAppheadersforLeads);
       return client.executePut(getJson(JSON_LOCATION_TIMOR,REJECT_LEAD_JSON));
    }
    public Response searchLeads(String leadId){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(TIMOR_LEADS_SEARCH);
        RestClient client = new RestClient(uri.toString(),xAppheadersforLeads);
        return client.executeGet();
    }
    public Response applyCoupon(String orderId,String couponCode){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(TIMOR_ORDER).append(orderId).append(PROMOTION_URI);
        RestClient client = new RestClient(uri.toString(),headers);
        checkLocking(orderId);
        Response response = client.executePost(getJson(COMMON_JSON,PROMOTION_JSON).replace("$promotionCode",couponCode));
        checkLocking(orderId);
        return response;
    }
    public Response deleteCoupon(String orderId,String couponCode){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(TIMOR_ORDER).append(orderId).append(PROMOTION_URI);
        RestClient client = new RestClient(uri.toString(),headers);
        return client.executeDelete(getJson(COMMON_JSON,PROMOTION_JSON).replace("$promotionCode",couponCode));
    }
    public Response addFeedBackOrder(String orderId){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(TIMOR_ADD_FEEDBACK.replace("{customerOrderId}",orderId));
        RestClient client = new RestClient(uri.toString(),headers);
        return client.executePost(getJson(COMMON_JSON,"/feedback.json"));
    }
    public Response getFeedbackOrder(String orderId){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(TIMOR_ADD_FEEDBACK.replace("{customerOrderId}",orderId));
        RestClient client = new RestClient(uri.toString(),headers);
        return client.executeGet();
    }
    /*
    Mark non instant order as shipped
     */
    public Response markOrderAsShipped(String orderId, Integer shipmentID){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(MARK_ORDER_SHIPPED.replace("{customerOrderId}",orderId).replace("{shipmentId}",shipmentID.toString()));
        RestClient client = new RestClient(uri.toString(),headers);
        return client.executePut();
    }
    public Response markOrderAsDelivered(String orderId,Integer shipmentID){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(MARK_ORDER_DELIVERED.replace("{customerOrderId}",orderId).replace("{shipmentId}",shipmentID.toString()));
        RestClient client = new RestClient(uri.toString(),headers);
        return client.executePut();
    }


    public Response getRecentProducts(){
        String uri=  "https://customers.stage.halodoc.com/v1/recent_products";
     //   StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(RECENT_PRODUCTS);
        RestClient client = new RestClient(uri.toString(),headers);
        return client.executeGet();

    }
    //Note this method 
    public void multiShipmentMarkDelivered(String orderId,Response orderResponse){
        List<Integer> shipmentIds= (orderResponse.path("shipments.id"));
        log.info("Shipments to mark as delivered "+shipmentIds.size());
       for(Integer shipId:shipmentIds){
           Assert.assertEquals(markOrderAsDelivered(orderId, shipId).statusCode(), HttpStatus.NO_CONTENT_204);
       }
        log.info("Successfully delivered all shipments ");

    }

    public Response createScheduledOrder(String entityId,String listingID, Double latitude, Double longitude) {
        RestClient client = new RestClient(String.valueOf(timorOrderUri).replace("v1","v4"), headers);
        Response response = client.executePost(getJson(JSON_LOCATION_TIMOR,CREATE_ORDER_JSON_SCHEDULED).replace("$entity_id", entityId).replace("$cart_id", getUUID()).replace("$listing_id",listingID).replace("$latitude",Double.toString(latitude)).replace("$longitude",Double.toString(longitude)));
        String orderId = response.path("customer_order_id");
        checkLocking(orderId);
        return response;
    }
    public Response deliveryOption(String orderId,String ExternalId){
        StringBuilder uri = new StringBuilder(baseUri).append(TIMOR_ORDER_PORT).append(TIMOR_ADD_DELIVERYOPTION.replace("{customerOrderId}",orderId));
        RestClient client = new RestClient(uri.toString(),headers);
        return client.executePatch(getJson(COMMON_JSON,DEL_OPTION_JSON).replace("string",ExternalId));
    }

    public Response createScheduledOrderForReallocation(String entityId,String listingId, Double latitude, Double longitude) {

        RestClient client = new RestClient(String.valueOf(timorOrderUri).replace("v1","v4"), headers);
        Response response = client.executePost(getJson(JSON_LOCATION_TIMOR,CREATE_ORDER_JSON_SCHEDULED).replace("$entity_id", entityId).replace("$cart_id", getUUID()).replace("$listing_id",listingId).replace("$latitude",Double.toString(latitude)).replace("$longitude",Double.toString(longitude)));
        String orderId = response.path("customer_order_id");
        checkLocking(orderId);
        return response;
    }

    public Response reallocateOrder(String orderId, boolean isRealocated, boolean isInvalidToken, boolean isWithoutToken) throws InterruptedException {
        HashMap<String, String> userHeaders = isInvalidToken ? getXappHeaders(CONTENT_TYPE, X_APP_TOKEN_CMS) :
                isWithoutToken ? getInvalidToken(CONTENT_TYPE, CUSTOMER_USER_AGENT, true) : xAppheadersforLeads;
        Response response = null;

        if(orderId == null) {
            response = createOrderForReallocation(USER_ENTITY_ID, PRODUCT_LISTING);
            orderId = response.path("customer_order_id");
            checkStatusUntil(STATUS_APPROVED, orderId);
            response = getOrderDetails(orderId);
            float price = response.path("total");
            confirmOrder(orderId, USER_ENTITY_ID, Float.toString(price));
        }

        if(isRealocated) {
            cancelOrder(orderId);
            checkStatusUntil(STATUS_CONFIRMED, orderId);
        }

        String url = timorOrderInternalUri + INTERNAL_OMS_REALLOCATION_URI.replace("{customerOrderId}", orderId);
        RestClient restClient = new RestClient(url, userHeaders);
        response = restClient.executePut();

        return response;
    }

    public String getCurrentDay(String day){
        Calendar calendar = Calendar.getInstance();
        String[] my_day = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
        if (day =="currentDay")
            return my_day[calendar.get(Calendar.DAY_OF_WEEK) -1];
        else
            return my_day[calendar.get(Calendar.DAY_OF_WEEK)];

    }

    public  String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time=  sdf.format(calendar.getTime());
        return time;
    }

    public float getTotalPaymentAmountByType(Response response, String selectedPaymentType) {
        ArrayList payments = response.path("payments");
        float totalAmount = 0;

        for(int i = 0; i < payments.size(); i++) {
            String paymentType = response.path("payments[" + i + "].type");

            if(paymentType.equals(selectedPaymentType)) {
                float amount = response.path("payments[" + i + "].amount");
                totalAmount = totalAmount + amount;
            }
        }

        return totalAmount;
    }

    public Response getProductDescription(String productId){
        String uri = CMS_BASEURL + GET_PRODUCT_DESCRIPTION;
        RestClient client = new RestClient(uri, xAppheaders_CMS);
        Response response = client.executePut(getJson(JSON_LOCATION_TIMOR, CMS_GET_PRODUCT_DESCRIPTION).replace("$product_id", productId));
        return response;
    }

    public Response changeVerificationStatus(String customerId){
        StringBuilder uri = new StringBuilder(timorOrderUri).append(TIMOR_CHANGE_VERIFICATION_STATUS.replace("{customerOrderId}",customerId));
        RestClient client = new RestClient(uri.toString(), headers);
        Response response = client.executePost(EMPTY_JSON);
        return response;
    }

    //created order for buru
    public Response createBuruOrder(String entityId,String listingID) {
        RestClient client = new RestClient(String.valueOf(timorOrderUri).replace("v1","v2"), headers);
        Response response = client.executePost(getJson(JSON_LOCATION_BURU,CREATE_BURU_ORDER_JSON).replace("$entity_id", entityId).replace("$cart_id", getUUID()).replace("$listing_id",listingID));
        String orderId = response.path("customer_order_id");
        checkLocking(orderId);
        return response;
    }

    //confirm order for buru
    public Response confirmBuruOrder(String customerOrderId,String patientID,String price) {
        StringBuilder uri = new StringBuilder(timorOrderUri.toString().replace("v1","v4"));
        RestClient client=new RestClient((uri.append(customerOrderId)).append(CONFIRM_ORDER_URI).toString(), headers);
        checkLocking(customerOrderId);
        Response response = client.executePut(getJson(JSON_LOCATION_BURU, CONFIRM_BURU_ORDER_JSON).replace("$patient_id",patientID).replace("$price",price));
        checkLocking(customerOrderId);

        return response;
    }

    @SneakyThrows
    public Response checkOrderStatus(String orderId,String status) {
        int cnt=0;
        Response orderResponse = getOrderDetails(orderId);
        JSONObject jsonObject = new JSONObject(orderResponse.asString());
        while(!jsonObject.get("status").equals(status)){
            Thread.sleep(10000);
            orderResponse = CommonUtils.getApiResponseNoQueryParams(CUSTOMER_STAGE +timorV2order+orderId,TimorUtil.headers);
            Assert.assertTrue(orderResponse.statusCode() == org.apache.http.HttpStatus.SC_OK,"found get order details api response "+orderResponse.statusCode()+" Response \n"
                    + orderResponse.asString());
            jsonObject = new JSONObject(orderResponse.asString());
            cnt++;
            if(cnt ==5)
                Assert.fail(orderId + " - order stuck in this state "+jsonObject.get("status"));

        }
        Assert.assertTrue(jsonObject.get("status").equals(status), " found order status "+jsonObject.get("status")+
                " expected "+status);
        return orderResponse;
    }


}



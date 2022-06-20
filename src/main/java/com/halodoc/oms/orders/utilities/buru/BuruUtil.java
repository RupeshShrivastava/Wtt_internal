package com.halodoc.oms.orders.utilities.buru;

import com.halodoc.oms.orders.library.BaseUtil;
import com.halodoc.oms.orders.utilities.timor.TimorUtil;
import com.halodoc.utils.http.RestClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import static com.halodoc.oms.orders.utilities.constants.Constants.PRODUCT_LISTING_PHARMACY;
import static com.halodoc.oms.orders.utilities.constants.SearchConstants.CONTENT_TYPE;
import static io.restassured.RestAssured.given;

@Slf4j
//TODO : Code clean up  : a) Reemove hard sleeps b)  improve logs 3) Implement Json path and assert from that function
// TODO :Use retry logic here to retry before throwing exception
public class BuruUtil extends BaseUtil {
    public static HashMap<String ,String> headers_pharmacy = null;
    public static HashMap<String,String> medisendHeaders = null;
    public static HashMap<String,String> medisendHeadersSamePharmacy = null;
    public static HashMap<String,String> inactiveMedisendHeaders = null;
    public static HashMap<String, String> medisendHeadersDiscountPharmacy = null;
    public static HashMap<String,String> xAppheaders = null;
    public static HashMap<String,String> xAppheaders_CMS = null;
    public static HashMap<String, String> removedMedisendHeaders = null;
    public static HashMap<String, String> deletedMedisendHeaders = null;


    public BuruUtil() {
        headers_pharmacy = getLoginHeaders(CONTENT_TYPE,PHONE_NUMBER_NEW_PHARMACY,PHARMACY_PORT_AUTH, PHARMACY_USER_AGENT,BURU_BASEURL);
        medisendHeaders = getLoginHeaders(CONTENT_TYPE, PHONE_NUMBER_MEDISEND, PHARMACY_PORT_AUTH, PHARMACY_USER_AGENT, BURU_BASEURL);
        medisendHeadersSamePharmacy = getLoginHeaders(CONTENT_TYPE, PHONE_NUMBER_MEDISEND_SAME_PHARMACY, PHARMACY_PORT_AUTH, PHARMACY_USER_AGENT, BURU_BASEURL);
        medisendHeadersDiscountPharmacy = getLoginHeaders(CONTENT_TYPE, PHONE_NUMBER_MEDISEND_DISCOUNT_SETUP, PHARMACY_PORT_AUTH, PHARMACY_USER_AGENT, BURU_BASEURL);
        inactiveMedisendHeaders = getLoginHeaders(CONTENT_TYPE, PHONE_NUMBER_MEDISEND_INACTIVE_PHARMACY, PHARMACY_PORT_AUTH, PHARMACY_USER_AGENT, BURU_BASEURL);
        removedMedisendHeaders = getLoginHeaders(CONTENT_TYPE, USER_PENDING_UNMAPPED_PHONE_NUMBER_PHARMACY, PHARMACY_PORT_AUTH, PHARMACY_USER_AGENT, BURU_BASEURL);
        deletedMedisendHeaders = getLoginHeaders(CONTENT_TYPE, USER_DELETED_PENDING_GPID, PHARMACY_PORT_AUTH, PHARMACY_USER_AGENT, BURU_BASEURL);
        xAppheaders = getXappHeaders("application/json",BURU_XAPP_TOKEN);
        xAppheaders_CMS = getXappHeaders("application/json",X_APP_TOKEN_CMS);
    }

    @SneakyThrows
    public Response createAndConfirmOrder(TimorUtil timorHelper) {
        Response orderResponse;
        orderResponse =  timorHelper.createBuruOrder(USER_ENTITY_ID,PRODUCT_LISTING_PHARMACY);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, orderResponse),"Create Order Failed");
        String orderId = orderResponse.path("customer_order_id").toString();
        orderResponse= timorHelper.checkOrderStatus(orderId,"approved");
        String price = orderResponse.path("item_total").toString();
        timorHelper.confirmBuruOrder(orderId,USER_ENTITY_ID,price);
        return timorHelper.checkOrderStatus(orderId,"confirmed");

    }

    public Response getOrderDetails(String orderId,String groupId) {
        StringBuilder getOrderDetailsUrl = new StringBuilder();
        getOrderDetailsUrl.append(BURU_BASEURL).append(ORDERS).append(orderId).append(BURU_GROUP_URI).append(groupId);
        RestClient client = new RestClient(getOrderDetailsUrl.toString(),headers_pharmacy);
        Response response =client.executeGet();
        return response;
    }

    public Response markReady(String orderId,String groupId) {
        StringBuilder getOrderDetailsUrl = new StringBuilder();
        getOrderDetailsUrl.append(BURU_BASEURL).append(ORDERS).append(orderId).append(BURU_GROUP_URI).append(groupId).append(BURU_READY_URI);
       /* RestClient client = new RestClient(getOrderDetailsUrl.toString(),headers_pharmacy);
        Response response =client.executePut();*/
        Response response = given().log().all().headers(headers_pharmacy).when().put(getOrderDetailsUrl.toString());
        return response;
    }

    public Response markDispatch(String orderId,String groupId) {
        StringBuilder getOrderDetailsUrl = new StringBuilder();
        getOrderDetailsUrl.append(BURU_BASEURL).append(ORDERS).append(orderId).append(BURU_GROUP_URI).append(groupId).append(BURU_DISPATCH_URI);
        RestClient client = new RestClient(getOrderDetailsUrl.toString(),headers_pharmacy);
        Response response =client.executePut();
        return response;
    }

    public Response markReject(String orderId,String groupId) {
        StringBuilder getOrderDetailsUrl = new StringBuilder();
        getOrderDetailsUrl.append(BURU_BASEURL).append(ORDERS).append(orderId).append(BURU_GROUP_URI).append(groupId).append(REAJECT_URI);
        RestClient client = new RestClient(getOrderDetailsUrl.toString(),headers_pharmacy);
        Response response =client.executePut(getJson(JSON_LOCATION_BURU,REJECT_SHIPMENT_JSON));
        return response;
    }

    public Response getConfig() {
        StringBuilder getOrderDetailsUrl = new StringBuilder();
        getOrderDetailsUrl.append(BURU_BASEURL).append(BURU_CONFIG);
        RestClient client = new RestClient(getOrderDetailsUrl.toString(),headers_pharmacy);
        Response response =client.executeGet();
        return response;
    }

    public Response fetchGeo(String latitude,String longitude) {
        StringBuilder getOrderDetailsUrl = new StringBuilder();
        getOrderDetailsUrl.append(BURU_BASEURL).append(BURU_GEO_VALIDATE.replace("$long",longitude).replace("$lat",latitude));
        RestClient client = new RestClient(getOrderDetailsUrl.toString(),headers_pharmacy);
        Response response =client.executeGet();
        return response;
    }

    public Response searchInternalOrders(String orderId) {
        StringBuilder getOrderDetailsUrl = new StringBuilder();
        getOrderDetailsUrl.append(BURU_BASEURL).append(BURU_SEARCH_ORDERS.replace("{customer_order_id}",orderId));
        RestClient client = new RestClient(getOrderDetailsUrl.toString(),xAppheaders);
        Response response =client.executeGet();
        return response;
    }

    public Response getAllPharmacyUsers(String merchant_id,String merchant_location_id) {
        StringBuilder getOrderDetailsUrl = new StringBuilder();
        getOrderDetailsUrl.append(BURU_BASEURL).append(BURU_GET_PHARMACY_USERS.replace("{merchant_id}",merchant_id).replace("{merchant_location_id}",merchant_location_id));
        RestClient client = new RestClient(getOrderDetailsUrl.toString(),xAppheaders);
        Response response =client.executeGet();
        return response;
    }

    public Response getAllPharmacyProducts(String per_page,String page_no,String search_text){
        StringBuilder getOrderDetailsUrl = new StringBuilder();
        getOrderDetailsUrl.append(BURU_BASEURL).append(BURU_PRODUCTS.replace("{per_page}",per_page).replace("{page_no}",page_no).replace("{search_text}",search_text));
        RestClient client = new RestClient(getOrderDetailsUrl.toString(),headers_pharmacy);
        Response response =client.executeGet();
        return response;
    }

    public Response getProductSearchForDiscount(String per_page,String page_no,String search_text){
        StringBuilder getOrderDetailsUrl = new StringBuilder();
        getOrderDetailsUrl.append(BURU_BASEURL).append(BURU_PRODUCTS.replace("{per_page}",per_page).replace("{page_no}",page_no).replace("{search_text}",search_text));
        RestClient client = new RestClient(getOrderDetailsUrl.toString(),medisendHeadersDiscountPharmacy);
        Response response =client.executeGet();
        return response;
    }

    public Response getCustomerDetails(String customer_number) {
        StringBuilder getOrderDetailsUrl = new StringBuilder();
        getOrderDetailsUrl.append(BURU_BASEURL).append(BURU_PATIENTS.replace("{phone_number}",customer_number));
        RestClient client = new RestClient(getOrderDetailsUrl.toString(),headers_pharmacy);
        Response response =client.executeGet();
        return response;
    }

    public void updatePharmacy(String merchantId, String merchant_locationId,String day, boolean status){
        Integer slot_id = null;
        StringBuilder getOrderDetailsUrl = new StringBuilder();
        StringBuilder updateMerchantUrl = new StringBuilder();
        getOrderDetailsUrl.append(CMS_BASEURL).append(MERCHANTS_LOCATION_BUSINESSHOURS.replace("{merchantId}",merchantId).replace("{merchantLocationId}",merchant_locationId));
        RestClient client = new RestClient(getOrderDetailsUrl.toString(),xAppheaders_CMS);
        Response response = client.executeGet();
        JSONArray JSONResponseBody = new  JSONArray(response.body().asString());
        for(int i = 0; i < JSONResponseBody.length(); i++){
            JSONObject jsonObject = (JSONObject) JSONResponseBody.get(i);
            slot_id = (Integer) jsonObject.get("id");

            if (jsonObject.get("day_of_week").equals(day)) {
                slot_id = (Integer) jsonObject.get("id");
               break;
            }
         }

        updateMerchantUrl.append(CMS_BASEURL).append(MERCHANTS_LOCATION_BUSINESSHOURS_WITH_ID.replace("{merchantId}",merchantId).replace("{merchantLocationId}",merchant_locationId).replace("{slotId}",slot_id.toString()));
        client = new RestClient(updateMerchantUrl.toString(),xAppheaders_CMS);

        if(status == false)
            client.executePut(getJson(JSON_LOCATION_BURU,MERCHANT_LOCATION_JSON_FALSE).replace("$day_of_week",day));
        else
            client.executePut(getJson(JSON_LOCATION_BURU,MERCHANT_LOCATION_JSON).replace("$day_of_week",day));
    }

    public Response createMedisendOrder(String cartId, String currency, List<MedisendOrderItem> medisendOrderItems) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cart_id", cartId);
        jsonObject.put("currency", currency);

        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < medisendOrderItems.size(); i++) {
            JSONObject jsonObjectItem = new JSONObject();
            jsonObjectItem.put("listing_id", medisendOrderItems.get(i).getListingId());
            jsonObjectItem.put("distributor_id", medisendOrderItems.get(i).getDistributorId());
            jsonObjectItem.put("distributor_branch_id", medisendOrderItems.get(i).getBranchId());
            jsonObjectItem.put("requested_quantity", medisendOrderItems.get(i).getRequestedQuantity());

            jsonArray.put(jsonObjectItem);
        }

        jsonObject.put("items", jsonArray);

        String jsonBody = jsonObject.toString();
        String url = BURU_BASEURL + BURU_CREATE_MEDISEND_ORDER_URI;

        RestClient client = new RestClient(url, medisendHeaders);
        Response response = client.executePost(jsonBody);

        return response;
    }

    //Make single method
    public Response createMedisendOrderRemovedPharmacyUser(String cartId, String currency, List<MedisendOrderItem> medisendOrderItems) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cart_id", cartId);
        jsonObject.put("currency", currency);

        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < medisendOrderItems.size(); i++) {
            JSONObject jsonObjectItem = new JSONObject();
            jsonObjectItem.put("listing_id", medisendOrderItems.get(i).getListingId());
            jsonObjectItem.put("distributor_id", medisendOrderItems.get(i).getDistributorId());
            jsonObjectItem.put("distributor_branch_id", medisendOrderItems.get(i).getBranchId());
            jsonObjectItem.put("requested_quantity", medisendOrderItems.get(i).getRequestedQuantity());

            jsonArray.put(jsonObjectItem);
        }

        jsonObject.put("items", jsonArray);

        String jsonBody = jsonObject.toString();
        String url = BURU_BASEURL + BURU_CREATE_MEDISEND_ORDER_URI;

        RestClient client = new RestClient(url, removedMedisendHeaders);
        Response response = client.executePost(jsonBody);

        return response;
    }

    public Response createMedisendOrderDeletedPharmacyUser(String cartId, String currency, List<MedisendOrderItem> medisendOrderItems) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cart_id", cartId);
        jsonObject.put("currency", currency);

        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < medisendOrderItems.size(); i++) {
            JSONObject jsonObjectItem = new JSONObject();
            jsonObjectItem.put("listing_id", medisendOrderItems.get(i).getListingId());
            jsonObjectItem.put("distributor_id", medisendOrderItems.get(i).getDistributorId());
            jsonObjectItem.put("distributor_branch_id", medisendOrderItems.get(i).getBranchId());
            jsonObjectItem.put("requested_quantity", medisendOrderItems.get(i).getRequestedQuantity());

            jsonArray.put(jsonObjectItem);
        }

        jsonObject.put("items", jsonArray);

        String jsonBody = jsonObject.toString();
        String url = BURU_BASEURL + BURU_CREATE_MEDISEND_ORDER_URI;

        RestClient client = new RestClient(url, deletedMedisendHeaders);
        Response response = client.executePost(jsonBody);

        return response;
    }

    public Response createMedisendOrderDiscount(String cartId, String currency, List<MedisendOrderItem> medisendOrderItems) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cart_id", cartId);
        jsonObject.put("currency", currency);

        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < medisendOrderItems.size(); i++) {
            JSONObject jsonObjectItem = new JSONObject();
            jsonObjectItem.put("listing_id", medisendOrderItems.get(i).getListingId());
            jsonObjectItem.put("distributor_id", medisendOrderItems.get(i).getDistributorId());
            jsonObjectItem.put("distributor_branch_id", medisendOrderItems.get(i).getBranchId());
            jsonObjectItem.put("requested_quantity", medisendOrderItems.get(i).getRequestedQuantity());

            jsonArray.put(jsonObjectItem);
        }

        jsonObject.put("items", jsonArray);

        String jsonBody = jsonObject.toString();
        String url = BURU_BASEURL + BURU_CREATE_MEDISEND_ORDER_URI;

        RestClient client = new RestClient(url, medisendHeadersDiscountPharmacy);
        Response response = client.executePost(jsonBody);

        return response;
    }

    public Response createMedisendOrderWithInactivePharmacy(String cartId, String currency, List<MedisendOrderItem> medisendOrderItems) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cart_id", cartId);
        jsonObject.put("currency", currency);

        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < medisendOrderItems.size(); i++) {
            JSONObject jsonObjectItem = new JSONObject();
            jsonObjectItem.put("listing_id", medisendOrderItems.get(i).getListingId());
            jsonObjectItem.put("distributor_id", medisendOrderItems.get(i).getDistributorId());
            jsonObjectItem.put("distributor_branch_id", medisendOrderItems.get(i).getBranchId());
            jsonObjectItem.put("requested_quantity", medisendOrderItems.get(i).getRequestedQuantity());

            jsonArray.put(jsonObjectItem);
        }

        jsonObject.put("items", jsonArray);

        String jsonBody = jsonObject.toString();
        String url = BURU_BASEURL + BURU_CREATE_MEDISEND_ORDER_URI;

        RestClient client = new RestClient(url, inactiveMedisendHeaders);
        Response response = client.executePost(jsonBody);

        return response;
    }

    public Response getMedisendOrder(String orderId) {
        String url = BURU_BASEURL + BURU_GET_MEDISEND_ORDER_URI.replace("{order_id}", orderId);

        RestClient client = new RestClient(url, medisendHeaders);
        Response response = client.executeGet();

        return response;
    }

    public Response getMedisendOrderByDifferentNumber(String orderId) {
        String url = BURU_BASEURL + BURU_GET_MEDISEND_ORDER_URI.replace("{order_id}", orderId);

        RestClient client = new RestClient(url, medisendHeadersSamePharmacy);
        Response response = client.executeGet();

        return response;
    }

    public Response getMedisendOrderByDiscountPharmacy(String orderId) {
        String url = BURU_BASEURL + BURU_GET_MEDISEND_ORDER_URI.replace("{order_id}", orderId);

        RestClient client = new RestClient(url, medisendHeadersDiscountPharmacy);
        Response response = client.executeGet();

        return response;
    }

    public Response updateMedisendItem(String orderId, List<MedisendOrderItem> medisendOrderItems) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < medisendOrderItems.size(); i++) {
            JSONObject jsonObjectItem = new JSONObject();
            jsonObjectItem.put("listing_id", medisendOrderItems.get(i).getListingId());
            jsonObjectItem.put("distributor_id", medisendOrderItems.get(i).getDistributorId());
            jsonObjectItem.put("distributor_branch_id", medisendOrderItems.get(i).getBranchId());
            jsonObjectItem.put("requested_quantity", medisendOrderItems.get(i).getRequestedQuantity());

            jsonArray.put(jsonObjectItem);
        }

        jsonObject.put("items", jsonArray);

        String jsonBody = jsonObject.toString();
        String url = BURU_BASEURL + BURU_UPDATE_MEDISEND_ORDER_URI.replace("{order_id}", orderId);

        RestClient client = new RestClient(url, medisendHeaders);
        Response response = client.executePut(jsonBody);

        return response;
    }

    public Response updateMedisendItemDiscount(String orderId, List<MedisendOrderItem> medisendOrderItems) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < medisendOrderItems.size(); i++) {
            JSONObject jsonObjectItem = new JSONObject();
            jsonObjectItem.put("listing_id", medisendOrderItems.get(i).getListingId());
            jsonObjectItem.put("distributor_id", medisendOrderItems.get(i).getDistributorId());
            jsonObjectItem.put("distributor_branch_id", medisendOrderItems.get(i).getBranchId());
            jsonObjectItem.put("requested_quantity", medisendOrderItems.get(i).getRequestedQuantity());

            jsonArray.put(jsonObjectItem);
        }

        jsonObject.put("items", jsonArray);

        String jsonBody = jsonObject.toString();
        String url = BURU_BASEURL + BURU_UPDATE_MEDISEND_ORDER_URI.replace("{order_id}", orderId);

        RestClient client = new RestClient(url, medisendHeadersDiscountPharmacy);
        Response response = client.executePut(jsonBody);

        return response;
    }

    public Response abandonMedisendOrder(String orderId, String abandonType, String abandonReason) {
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, CANCEL_JSON).replace("$type", abandonType).replace("$reason", abandonReason);

        String url = BURU_BASEURL + BURU_ABANDON_MEDISEND_ORDER_URI.replace("{order_id}", orderId);

        RestClient client = new RestClient(url, medisendHeaders);
        Response response = client.executePut(jsonBody);

        return response;
    }

    public Response confirmMedisendOrder(String orderId) {
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, CONFIRM_JSON);

        String url = BURU_BASEURL + BURU_CONFIRM_MEDISEND_ORDER_URI.replace("{order_id}", orderId);

        RestClient client = new RestClient(url, medisendHeaders);
        Response response = client.executePut(jsonBody);

        return response;
    }

    public Response confirmMedisendDiscountOrder(String orderId) {
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, CONFIRM_JSON);

        String url = BURU_BASEURL + BURU_CONFIRM_MEDISEND_ORDER_URI.replace("{order_id}", orderId);

        RestClient client = new RestClient(url, medisendHeadersDiscountPharmacy);
        Response response = client.executePut(jsonBody);

        return response;
    }

    public Response uploadDocsMedisendOrder(String orderId, String shipmentId, String docType, String fileName) throws IOException {
        String path = BURU_UPLOAD_DOCS_MEDISEND_ORDER_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);
        HashMap<String, String> uploadMedisendHeaders = (HashMap<String, String>) medisendHeaders.clone();
        uploadMedisendHeaders.put("Content-Type", "application/octet-stream");
        uploadMedisendHeaders.put("X-Document-Type", docType);
        byte[] files = FileUtils.readFileToByteArray(new File("src/main/resources/fixtures/medisend/" + fileName));

        RestAssured.baseURI = BURU_BASEURL;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.headers(uploadMedisendHeaders);
        requestSpecification.body(files);
        Response response = requestSpecification.put(path);

        return response;
    }

    public Response getDocsMedisendOrder(String orderId, String shipmentId) {
        String url = BURU_BASEURL + BURU_GET_DOCS_MEDISEND_ORDER_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);

        RestClient client = new RestClient(url, medisendHeaders);
        Response response = client.executeGet();

        return response;
    }

    public Response requestCancel(String orderId, String shipmentId, String cancelType, String cancelReason) {
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, CANCEL_JSON).replace("$type", cancelType).replace("$reason", cancelReason);

        String url = BURU_BASEURL + BURU_REQUEST_CANCEL_MEDISEND_ORDER_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);

        RestClient client = new RestClient(url, medisendHeaders);
        Response response = client.executePut(jsonBody);

        return response;
    }

    public Response updateMedisendProduct(String distributorId, String branchId, String productMappingId, String status, String availableQuantity,
            String skuId) {
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, PRODUCT_JSON)
                .replace("$status", status)
                .replace("$availableQuantity", availableQuantity)
                .replace("$skuId", skuId);

        String url = CMS_BASEURL + CMS_MEDISEND_UPDATE_PRODUCT_URI
                .replace("{distributor_id}", distributorId)
                .replace("{branch_id}", branchId)
                .replace("{product_id}", productMappingId);

        RestClient client = new RestClient(url, xAppheaders_CMS);
        Response response = client.executePut(jsonBody);

        return response;
    }

    public Response updateMedisendProcurementAttribute(String merchantId, String merchantLocationId, String value) {
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, PROCUREMENT_JSON).replace("$value", value);

        String url = CMS_BASEURL + CMS_MEDISEND_ATTRIBUTE_URL
                .replace("{merchant_id}", merchantId)
                .replace("{merchant_location_id}", merchantLocationId);

        RestClient client = new RestClient(url, xAppheaders_CMS);
        Response response = client.executePut(jsonBody);

        return response;
    }

    public Response getMedisendProductDetails(String productId){
        String url = BURU_BASEURL + BURU_GET_PRODUCT_DETAILS_URI + productId;
        RestClient client = new RestClient(url, medisendHeadersDiscountPharmacy);
        Response response = client.executeGet();
        return response;
    }

    public Response getInventoryOutOfStockFilterType(){
        String url = BURU_BASEURL + BURU_INVENTORY_STOCK_SEARCH;
        RestClient client = new RestClient(url, medisendHeadersDiscountPharmacy);
        Response response = client.executePatch();
        return response;
    }

    public Response addShipmentItemNotes(String customerId, String shipmentId, String itemId, String notes){
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, SHIPMENT_ITEM_NOTES_JSON).replace("$notes", notes);

        String url = BURU_BASEURL + BURU_ADD_SHIPMENT_ITEM_NOTES_URI.replace("{customer_order_id}",customerId).replace("{shipment_id}", shipmentId).replace("{items_id}", itemId);
        RestClient client = new RestClient(url, medisendHeadersDiscountPharmacy);
        Response response = client.executePut(jsonBody);

        return response;
    }

    public Response deleteShipmentNotes(String customerId, String shipmentId, String itemId){
        String url = BURU_BASEURL + BURU_DELETE_SHIPMENT_ITEM_NOTES_URI.replace("{customer_order_id}",customerId).replace("{shipment_id}", shipmentId).replace("{items_id}", itemId);
        RestClient client = new RestClient(url, medisendHeadersDiscountPharmacy);
        Response response = client.executeDelete();
        return response;
    }

    public Response deleteShipmentDocs(String orderId, String shipmentId, String documentId) {
        String url = BURU_BASEURL + BURU_DELETE_DOCS_SHIPMENT_URI
                                        .replace("{order_id}", orderId)
                                        .replace("{shipment_id}", shipmentId)
                                        .replace("{docs_id}", documentId);

        RestClient client = new RestClient(url, medisendHeaders);
        Response response = client.executeDelete();

        return response;
    }

    public Response getUserDetails(String gpid){
        String url = BURU_BASEURL + BURU_GET_USER_DETAILS + gpid;
        RestClient client = new RestClient(url, xAppheaders);
        return client.executeGet();
    }

    public Response getAccessRoles (){
        String url = BURU_BASEURL + BURU_GET_USER_ROLE_URL;
        RestClient client = new RestClient(url, xAppheaders);
        return client.executeGet();
    }

    public Response updateUserDetailsValidData(String gpid, String ownerName, String merchantId, String merchantLocationId){
        String json = getJson(JSON_LOCATION_MEDISEND, USER_UPDATE_VALID_JSON).replace("$ownerName", ownerName).replace("$merchantId",merchantId).replace("$merchantLocationId", merchantLocationId);
        String url = BURU_BASEURL + BURU_UPDATE_USER_DETAILS + gpid;
        RestClient client = new RestClient(url, xAppheaders);
        return client.executePut(json);
    }

    public Response updateUserRole(String gpid, String status, String access_role){
        String json = getJson(JSON_LOCATION_MEDISEND, USER_UPDATE_ROLE_JSON).replace("$gpid", gpid).replace("$status", status).replace("$access_roles", access_role);
        String url = BURU_BASEURL + BURU_UPDATE_USER_DETAILS_CC + gpid;
        RestClient client = new RestClient(url, xAppheaders);
        return client.executePut(json);
    }

    public Response updateUserEmptyRole(String gpid, String status){
        String json = getJson(JSON_LOCATION_MEDISEND, USER_UPDATE_EMPTY_ROLE_JSON).replace("$gpid", gpid).replace("$status", status);
        String url = BURU_BASEURL + BURU_UPDATE_USER_DETAILS_CC + gpid;
        RestClient client = new RestClient(url, xAppheaders);
        return client.executePut(json);
    }


    public Response updateUserDetailsStatusCC(String gpid, String status, String merchantId, String merchantLocationId, String name){
        String json = getJson(JSON_LOCATION_MEDISEND, USER_UPDATE_THROUGH_CC_JSON).replace("$gpid", gpid).replace("$status", status).replace("merchantId", merchantId).replace("merchantLocationId", merchantLocationId).replace("$name", name);
        String url = BURU_BASEURL + BURU_UPDATE_USER_DETAILS_CC + gpid;

        RestClient client = new RestClient(url, xAppheaders);
        return client.executePut(json);
    }

    private Response apiUpdateMerchantMapping(String distributorId, String branchId, String mappingId, String jsonBody) {
        String url = CMS_BASEURL + UPDATE_MERCHANT_MAPPING_URL.replace("{distributor_id}", distributorId).replace("{branch_id}", branchId).replace("{mapping_id}", mappingId);

        RestClient client = new RestClient(url, xAppheaders_CMS);
        return client.executePut(jsonBody);

    }

    public Response updateMerchantMapping(String distributorId, String branchId, String mappingId, String merchantLocationId, String status) {
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, MERCHANT_MAPPING_JSON).replace("$merchantLocationId", merchantLocationId).replace("$status", status);

        return apiUpdateMerchantMapping(distributorId, branchId, mappingId, jsonBody);
    }

    public Response activateUser(String gpid) {
        String url = BURU_BASEURL + BURU_ACTIVATE_USER[0] + gpid + BURU_ACTIVATE_USER[1];

        RestClient client = new RestClient(url, xAppheaders);
        return client.executePut();
    }

    public Response deleteUser(String gpid) {
        String url = BURU_BASEURL + BURU_DELETE_USER[0] + gpid + BURU_DELETE_USER[1];

        RestClient client = new RestClient(url, xAppheaders);
        return client.executePut();
    }

    public Response recoverUser(String gpid) {
        String url = BURU_BASEURL + BURU_RECOVER_USER[0] + gpid + BURU_RECOVER_USER[1];

        RestClient client = new RestClient(url, xAppheaders);
        return client.executePut();
    }

    public Response userSearch(String status, String delete){
        String url = BURU_BASEURL + BURU_INTERNAL_USER_SEARCH.replace("{status}",status).replace("{delete}", delete);

        RestClient client = new RestClient(url, xAppheaders);
        return client.executeGet();
    }

    public Response userSearchUsingPhoneNumber(String status, String phoneNumber, String deleted){
        String url = BURU_BASEURL + BURU_INTERNAL_USER_SEARCH_USING_PHONE_NUMBER.replace("{status}", status).replace("{deleted}", deleted).replace("{phoneNumber}", phoneNumber);

        RestClient client = new RestClient(url, xAppheaders);
        return client.executeGet();
    }

    public String getPhoneNumberFromUserDetails(String gpid){
        Response response = getUserDetails(gpid);
        String number = response.path(PHONE_NUMBER_PATH).toString().replace("+","");

        return number;
    }

    public Response getDistributorSlug(String slugName){
        String url = BURU_BASEURL + GET_DISTRIBUTOR_SLUG + slugName;

        RestClient client = new RestClient(url, xAppheaders);
        return client.executeGet();
    }

    public Response getTimorOrderSearch(){
        String url = BURU_BASEURL + TIMOR_V1_ORDER_SEARCH;

        RestClient client = new RestClient(url, medisendHeaders);
        return client.executeGet();
    }

    public Response getTimorOrderSearchDeletedPharmacy(){
        String url = BURU_BASEURL + TIMOR_V1_ORDER_SEARCH;

        RestClient client = new RestClient(url, deletedMedisendHeaders);
        return client.executeGet();
    }

    public Response getTimorOrderSearchRemovedPharmacy(){
        String url = BURU_BASEURL + TIMOR_V1_ORDER_SEARCH;

        RestClient client = new RestClient(url, removedMedisendHeaders);
        return client.executeGet();
    }
}
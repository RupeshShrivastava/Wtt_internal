package com.halodoc.oms.orders.utilities.derawan;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import static io.restassured.RestAssured.given;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.RestAssuredConfig.config;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import io.restassured.http.ContentType;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.halodoc.oms.orders.library.BaseUtil;
import com.halodoc.utils.http.RestClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

public class DerawanUtil extends BaseUtil {
    public static HashMap<String,String> headers = null;

    public DerawanUtil() {
        headers = getXappHeaders(CONTENT_TYPE, X_APP_TOKEN_DERAWAN);
    }

    public Response deliverMedisendOrder(String orderId, String shipmentId) {
        String url = DERAWAN_BASEURL + DERAWAN_DELIVER_MEDISEND_ORDER_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);

        RestClient client = new RestClient(url, headers);
        Response response = client.executePut();

        return response;
    }

    public Response cancelMedisendOrder(String orderId, String shipmentId, String cancelType, String cancelReason) {
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, CANCEL_JSON).replace("$type", cancelType).replace("$reason", cancelReason);
        String path = DERAWAN_CANCEL_MEDISEND_ORDER_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);

        RestAssured.baseURI = DERAWAN_BASEURL;
        RequestSpecification requestSpecification = given()
                .config(config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
        requestSpecification.headers(headers);
        requestSpecification.body(jsonBody);
        Response response = requestSpecification.put(path);

        return response;
    }

    public Response searchMedisendOrder(String startAt, String endAt, String entityId, String entityType, String orderId, String status, String pageLimit,
            String actionTypes, String sortBy, String sortOrder) {
        HashMap<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("start_created_at", startAt);
        queryParam.put("end_created_at", endAt);
        queryParam.put("entity_id", entityId);
        queryParam.put("entity_type", entityType);
        queryParam.put("customer_order_id", orderId);
        queryParam.put("statuses", status);
        queryParam.put("per_page", pageLimit);
        queryParam.put("action_types", actionTypes);
        queryParam.put("sort_by", sortBy);
        queryParam.put("sort_order", sortOrder);

        String url = DERAWAN_BASEURL + DERAWAN_GET_MEDISEND_ORDER_URI;

        RestClient client = new RestClient(url, headers);
        Response response = client.executeGet(queryParam);

        return response;
    }

    public Response createMedisendOrderNotes(String orderId, String noteType, String noteComment) {
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, NOTES_JSON).replace("$type", noteType).replace("$comment", noteComment);
        String path = DERAWAN_CREATE_MEDISEND_ORDER_NOTES_URI.replace("{order_id}", orderId);

        RestAssured.baseURI = DERAWAN_BASEURL;
        RequestSpecification requestSpecification = given()
                .config(config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
        requestSpecification.headers(headers);
        requestSpecification.body(jsonBody);
        Response response = requestSpecification.post(path);

        return response;
    }

    public Response confirmMedisendShipment(String orderId, String shipmentId) {
        String url = DERAWAN_BASEURL + DERAWAN_CONFIRM_MEDISEND_SHIPMENT_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);

        RestClient client = new RestClient(url, headers);
        Response response = client.executePut();

        return response;
    }

    public Response transitMedisendShipment(String orderId, String shipmentId) {
        String url = DERAWAN_BASEURL + DERAWAN_TRANSIT_MEDISEND_SHIPMENT_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);

        RestClient client = new RestClient(url, headers);
        Response response = client.executePut();

        return response;
    }

    public Response completeMedisendShipment(String orderId, String shipmentId) {
        String url = DERAWAN_BASEURL + DERAWAN_COMPLETE_MEDISEND_SHIPMENT_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);

        RestClient client = new RestClient(url, headers);
        Response response = client.executePut();

        return response;
    }

    public Response updateStockMedisendShipment(String orderId, String shipmentId, List<MedisendOrderItem> medisendOrderItems, String reason) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < medisendOrderItems.size(); i++) {
            JSONObject jsonObjectItem = new JSONObject();
            jsonObjectItem.put("listing_id", medisendOrderItems.get(i).getListingId());
            jsonObjectItem.put("quantity", medisendOrderItems.get(i).getRequestedQuantity());

            jsonArray.put(jsonObjectItem);
        }

        jsonObject.put("items", jsonArray);
        jsonObject.put("reason", reason);

        String jsonBody = jsonObject.toString();
        String path = DERAWAN_UPDATE_STOCK_MEDISEND_SHIPMENT_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);

        RestAssured.baseURI = DERAWAN_BASEURL;
        RequestSpecification requestSpecification = given()
                .config(config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
        requestSpecification.headers(headers);
        requestSpecification.body(jsonBody);
        Response response = requestSpecification.put(path);

        return response;
    }

    public Response updateStockMedisendShipmentWithPriceAndDiscount(String orderId, String shipmentId, List<MedisendOrderItem> medisendOrderItems,
            String reason) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < medisendOrderItems.size(); i++) {
            JSONObject jsonObjectItem = new JSONObject();
            jsonObjectItem.put("listing_id", medisendOrderItems.get(i).getListingId());
            jsonObjectItem.put("quantity", medisendOrderItems.get(i).getRequestedQuantity());
            jsonObjectItem.put("price", medisendOrderItems.get(i).getPrice());
            jsonObjectItem.put("discount", medisendOrderItems.get(i).getDiscount());

            jsonArray.put(jsonObjectItem);
        }

        jsonObject.put("items", jsonArray);
        jsonObject.put("reason", reason);

        String jsonBody = jsonObject.toString();
        String path = DERAWAN_UPDATE_STOCK_MEDISEND_SHIPMENT_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);

        RestAssured.baseURI = DERAWAN_BASEURL;
        RequestSpecification requestSpecification = given()
                                                               .config(config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
        requestSpecification.headers(headers);
        requestSpecification.body(jsonBody);
        Response response = requestSpecification.put(path);


        return response;
    }

    public Response createChildMedisendShipment(String orderId, String distributorId, String entityId, List<MedisendOrderItem> medisendOrderItems) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("distributor_entity_id", entityId);
        jsonObject.put("distributor_id", distributorId);

        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < medisendOrderItems.size(); i++) {
            JSONObject jsonObjectItem = new JSONObject();
            jsonObjectItem.put("listing_id", medisendOrderItems.get(i).getListingId());
            jsonObjectItem.put("quantity", medisendOrderItems.get(i).getRequestedQuantity());

            jsonArray.put(jsonObjectItem);
        }

        jsonObject.put("items", jsonArray);

        String jsonBody = jsonObject.toString();
        String path = DERAWAN_CREATE_CHILD_MEDISEND_SHIPMENT_URI.replace("{order_id}", orderId);

        RestAssured.baseURI = DERAWAN_BASEURL;
        RequestSpecification requestSpecification = given()
                .config(config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
        requestSpecification.headers(headers);
        requestSpecification.body(jsonBody);
        Response response = requestSpecification.post(path);

        return response;
    }

    public Response getOrderDetails(String orderId){
            String url = DERAWAN_BASEURL + DERAWAN_GET_MEDISEND_ORDER_DETAIL_URI.replace("{order_id}", orderId);

            RestClient client = new RestClient(url, headers);
            Response response = client.executeGet();

            return response;
        }

    public Response addShipmentItemNotes(String customerId, String shipmentId, String listingId, String notes) {
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, SHIPMENT_ITEM_NOTES_DERAWAN_JSON).replace("$notes", notes);

        RestAssured.baseURI = DERAWAN_BASEURL;
        String url = DERAWAN_SHIPMENT_ITEM_NOTES_URI.replace("{customer_order_id}", customerId).replace("{shipments_id}", shipmentId).replace("{items_id}",
                listingId);
        RequestSpecification requestSpecification = given().config(config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
        requestSpecification.headers(headers);
        requestSpecification.body(jsonBody);
        Response response = requestSpecification.put(url);

        return response;
    }

    public Response addUpdateBatchNo(String orderId, String shipmentId, String productId, String batchNo, long expiryDate) {
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, BATCH_NO_JSON)
                            .replace("$batchNo", batchNo)
                            .replace("$expiryDate", Long.toString(expiryDate));

        String url = DERAWAN_BASEURL + DERAWAN_ADD_BATCH_NO_URI
                                        .replace("{order_id}", orderId)
                                        .replace("{shipment_id}", shipmentId)
                                        .replace("{entity_product_id}", productId);

        RestClient client = new RestClient(url, headers);
        Response response = client.executePut(jsonBody);

        return response;
    }

    public Response uploadShipmentDocs(String orderId, String shipmentId, String fileType, String docType, String fileName) throws IOException {
        String path = DERAWAN_UPLOAD_DOCS_SHIPMENT_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);
        HashMap<String, String> uploadMedisendHeaders = (HashMap<String, String>) headers.clone();
        uploadMedisendHeaders.put("Content-Type", "application/octet-stream");
        uploadMedisendHeaders.put("X-File-Type", fileType);
        uploadMedisendHeaders.put("X-Document-Type", docType);
        byte[] files = FileUtils.readFileToByteArray(new File("src/main/resources/fixtures/medisend/" + fileName));

        RestAssured.baseURI = DERAWAN_BASEURL;
        RequestSpecification requestSpecification = given();
        requestSpecification.headers(uploadMedisendHeaders);
        requestSpecification.body(files);
        Response response = requestSpecification.put(path);

        return response;
    }

    public Response deleteShipmentDocs(String orderId, String shipmentId, String documentId) {
        String url = DERAWAN_BASEURL + DERAWAN_DELETE_DOCS_SHIPMENT_URI
                                        .replace("{order_id}", orderId)
                                        .replace("{shipment_id}", shipmentId)
                                        .replace("{docs_id}", documentId);

        RestClient client = new RestClient(url, headers);
        Response response = client.executeDelete();

        return response;
    }

    public Response approvePayment(String orderId, String shipmentId) {
        String url = DERAWAN_BASEURL + DERAWAN_APPROVE_PAYMENT_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);

        RestClient client = new RestClient(url, headers);
        Response response = client.executePut();

        return response;
    }

    public Response rejectPayment(String orderId, String shipmentId) {
        String jsonBody = getJson(JSON_LOCATION_MEDISEND, REJECT_PAYMENT_JSON);

        String url = DERAWAN_BASEURL + DERAWAN_REJECT_PAYMENT_URI.replace("{order_id}", orderId).replace("{shipment_id}", shipmentId);

        RestClient client = new RestClient(url, headers);
        Response response = client.executePut(jsonBody);

        return response;
    }
    public static  Response getShipmentIdInfo(String startDate,String sort_order,String per_page){
             return  given().
                log().all().
                contentType(ContentType.JSON).
                header("X-APP-TOKEN",X_APP_TOKEN_DERAWAN).
                queryParam("startDate",startDate).
                queryParam("sort_order",sort_order).
                queryParam("per_page",per_page).
                when().
                get(DERAWAN_BASEURL+DERAWAN_MEDISEND_ORDER);



    }
    public  static  Response getOrderStatus(String startDate,String sort_order,String per_page,String shipment_statuses){
        return given().
                contentType(ContentType.JSON).
                header("X-APP-TOKEN",X_APP_TOKEN_DERAWAN).
                queryParam("startDate",startDate).
                queryParam("sort_order",sort_order).
                queryParam("per_page",per_page).
                queryParam("shipment_statuses",shipment_statuses).
                when().
                get(DERAWAN_BASEURL+DERAWAN_MEDISEND_ORDER);
    }
    public static Response getOrderByShipmentId(String  shipment_id){
        return given().
                contentType(ContentType.JSON).
                header("X-APP-TOKEN",X_APP_TOKEN_DERAWAN).
                queryParam("shipment_id",shipment_id).
                when().
                get(DERAWAN_BASEURL+DERAWAN_MEDISEND_ORDER);
    }

    public  static  Response getOrderByDistributorBranchId(String startDate,String sort_order,String per_page,String distributor_branch_id){
        return given().
                contentType(ContentType.JSON).
                header("X-APP-TOKEN",X_APP_TOKEN_DERAWAN).
                queryParam("startDate",startDate).
                queryParam("sort_order",sort_order).
                queryParam("per_page",per_page).
                queryParam("distributor_branch_id",distributor_branch_id).
                when().
                get(DERAWAN_BASEURL+DERAWAN_MEDISEND_ORDER);
    }
    public static Response getOrderByDistributorId(String startDate,String sort_order,String per_page,String distributor_id){
        return given().
                contentType(ContentType.JSON).
                header("X-APP-TOKEN",X_APP_TOKEN_DERAWAN).
                queryParam("startDate",startDate).
                queryParam("sort_order",sort_order).
                queryParam("per_page",per_page).
                queryParam("distributor_id",distributor_id).
                when().
                get(DERAWAN_BASEURL+DERAWAN_MEDISEND_ORDER);
    }
    public  static  Response getOrderByMultipleDistributorBranchId(String startDate,String sort_order,String per_page,String  distributor_branch_id){

        return given().
                contentType(ContentType.JSON).
                header("X-APP-TOKEN",X_APP_TOKEN_DERAWAN).
                queryParam("startDate",startDate).
                queryParam("sort_order",sort_order).
                queryParam("per_page",per_page).
                queryParam("distributor_branch_id",distributor_branch_id).
                when().
                get(DERAWAN_BASEURL+DERAWAN_MEDISEND_ORDER);

    }
    public  static  Response getOrderByMultipleDistributorId(String startDate,String sort_order,String per_page,String  distributor_id){

        return given().
                contentType(ContentType.JSON).
                header("X-APP-TOKEN",X_APP_TOKEN_DERAWAN).
                queryParam("startDate",startDate).
                queryParam("sort_order",sort_order).
                queryParam("per_page",per_page).
                queryParam("distributor_branch_id",distributor_id).
                when().
                get(DERAWAN_BASEURL+DERAWAN_MEDISEND_ORDER);

    }
}
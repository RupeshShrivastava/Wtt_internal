package com.halodoc.omstests;


import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import static com.halodoc.omstests.Constants.BASE_FLORES_URL;
import static com.halodoc.omstests.Constants.BASE_GARUDA_URL;
import static com.halodoc.omstests.Constants.BASE_PHARMACY_URL;
import static com.halodoc.omstests.Constants.CMS_X_APP_TOKEN;
import static com.halodoc.omstests.Constants.COMMUNICATION_REQUEST;
import static com.halodoc.omstests.Constants.CONTENT_TYPE;
import static com.halodoc.omstests.Constants.FILE_LOCATION;
import static com.halodoc.omstests.Constants.GARUDA_X_APP_TOKEN;
import static com.halodoc.omstests.Constants.GET_TOKEN_DETAILS;
import static com.halodoc.omstests.Constants.LEADS_INTERNAL;
import static com.halodoc.omstests.Constants.LOGOUT_USER;
import static com.halodoc.omstests.Constants.OMS_X_APP_TOKEN;
import static com.halodoc.omstests.Constants.ORDERS;
import static com.halodoc.omstests.Constants.ORDER_ABANDON;
import static com.halodoc.omstests.Constants.ORDER_BASE_URL;
import static com.halodoc.omstests.Constants.ORDER_CANCEL;
import static com.halodoc.omstests.Constants.ORDER_CONFIRM;
import static com.halodoc.omstests.Constants.REQUEST_OTP;
import static com.halodoc.omstests.Constants.USER_AGENT_PATIENT;
import static com.halodoc.omstests.Constants.USER_AGENT_PHARMACY;
import static com.halodoc.omstests.Constants.VALIDATE_OTP;
import static com.halodoc.omstests.Constants.merchant_attributes;
import static com.halodoc.omstests.Constants.promotions;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static io.restassured.RestAssured.given;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.halodoc.oms.orders.utilities.timor.TimorUtil;
import com.halodoc.omstests.orderPojo.CreateOrder;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimorHelper  extends OrdersBaseTest {
    private String order_id;
    public String accessToken;
    public TimorHelper timorHelperUtil;
    TimorUtil timorUtil = new TimorUtil();
    public String userAgent;
    public final Properties properties = new Properties();
    public InputStream inputStream = null;


    public String getRequestFixture(String folderName, String fileName) {

        return fixture(FILE_LOCATION + "/" + folderName + "/" + fileName);
    }

    public String getLeadOrderFixture(String folderName, String fileName) {
        return fixture(FILE_LOCATION + "/" + folderName + "/" + fileName).replace("$cart_id", randomString());
    }

    public String getPatientAccessToken() {
        return timorUtil.getAccessTokenFromGaruda(PHONE_NUMBER_CUSTOMER, CUSTOMER_PORT_AUTH, CUSTOMER_USER_AGENT, NEW_BASE_URL);
    }

        public String getOtherPatientAccessToken() {
            return timorUtil.getAccessTokenFromGaruda(PHONE_NUMBER_OTHER_CUSTOMER, CUSTOMER_PORT_AUTH, CUSTOMER_USER_AGENT, OLD_BASE_URL);
        }

//        Response request_response = given().
//                                                   contentType(CONTENT_TYPE).
//                                                   header("User-Agent", USER_AGENT_PATIENT).
//                                                   body(getRequestFixture("accessToken", "request_otp.json")).
//                                                   expect().
//                                                   statusCode(200).
//                                                   when().
//                                                  post(BASE_FLORES_URL + REQUEST_OTP);
//        Response validate_response = given().
//                                                    contentType(CONTENT_TYPE).
//                                                    header("User-Agent", USER_AGENT_PATIENT).
//                                                    body(getRequestFixture("accessToken", "validate_otp.json")
//                                                            .replace("$otp", request_response.path("otp_id"))).
//                                                    expect().
//                                                    statusCode(200).
//                                                    when().
//                                                    post(BASE_FLORES_URL + VALIDATE_OTP);
//        log.info("validate_response patient : \n" + validate_response.prettyPrint());
//        return validate_response.path("token.access_token");


        public String getCustomerAccessToken () throws InterruptedException, IOException {
            timorHelperUtil = new TimorHelper();
            inputStream = new FileInputStream("src/main/resources/oms.properties");
            properties.load(inputStream);
            accessToken = properties.getProperty("access_token");
            System.out.println("From file : " + accessToken);

            Response response = given().
                    headers("User-Agent", USER_AGENT_PATIENT).
                    headers("Authorization", "Bearer " + accessToken).
                    expect().
                    log().all().
                    when().
                    get(GET_TOKEN_DETAILS);
            log.info("Current Time is :" + String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));


            if (response.statusCode() == 200 && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) < Long.parseLong(response.jsonPath().get("exp").toString())) {
                log.info("Response expire time :" + String.valueOf(Long.parseLong(response.jsonPath().get("exp").toString())));
                return accessToken;
            }
            return access_token();
        }

      /*  else {
            String otpCode;
            Response request_response = given().
                    contentType(ContentType.JSON).
                    header("User-Agent", USER_AGENT_PATIENT).
                    body(getRequestFixture("accessToken", "request_otp.json")).
                    expect().
                    log().all().
                    statusCode(200).
                    when().
                    post(BASE_FLORES_URL + REQUEST_OTP);

            JsonObject jsonObject = (JsonObject) new JsonParser().parse(getRequestFixture("accessToken", "request_otp.json"));
            String destination = URLEncoder.encode(jsonObject.get("phone_number").toString().replace("\"", ""), "UTF-8");
            Response garuda_response = given().
                    header("x-app-token", GARUDA_X_APP_TOKEN).
                    queryParam("destination", destination).
                    queryParam("channel", 1).
                    queryParam("limit", 1).
                    urlEncodingEnabled(false).
                    expect().
                    log().all().
                    statusCode(200).
                    when().
                    get(BASE_GARUDA_URL + COMMUNICATION_REQUEST);

            otpCode = garuda_response.path("[0].data.code1").toString() + garuda_response.path("[0].data.code2").toString();
            Response validate_response = given().
                    contentType(ContentType.JSON).
                    header("User-Agent", USER_AGENT_PATIENT).
                    body(getRequestFixture("accessToken", "validate_otp_garuda.json")
                            .replace("$otp", request_response.path("otp_id")).replace("$code", otpCode)).
                    expect().
                    log().all().
                    statusCode(200).
                    when().
                    post(BASE_FLORES_URL + VALIDATE_OTP);
            log.info("OTP response : {}", validate_response.prettyPrint());
            properties.setProperty("access_token", validate_response.path("token.access_token"));

            File file = new File("src/main/resources/oms.properties");
            FileOutputStream fileOut = new FileOutputStream(file);
            properties.store(fileOut, "Favorite Things");
            fileOut.close();

            System.out.println("From output file : " + accessToken);

            return validate_response.path("token.access_token");
        }*/

        public String access_token ()throws InterruptedException, IOException
        {
            String otpCode;
            Response request_response = given().
                    contentType(ContentType.JSON).
                    header("User-Agent", USER_AGENT_PATIENT).
                    body(getRequestFixture("accessToken", "request_otp.json")).
                    expect().
                    log().all().
                    statusCode(200).
                    when().
                    post(BASE_FLORES_URL + REQUEST_OTP);

            JsonObject jsonObject = (JsonObject) new JsonParser().parse(getRequestFixture("accessToken", "request_otp.json"));
            String destination = URLEncoder.encode(jsonObject.get("phone_number").toString().replace("\"", ""), "UTF-8");
            Response garuda_response = given().
                    header("x-app-token", GARUDA_X_APP_TOKEN).
                    queryParam("destination", destination).
                    queryParam("channel", 1).
                    queryParam("limit", 1).
                    urlEncodingEnabled(false).
                    expect().
                    log().all().
                    statusCode(200).
                    when().
                    get(BASE_GARUDA_URL + COMMUNICATION_REQUEST);

            otpCode = garuda_response.path("[0].data.code1").toString() + garuda_response.path("[0].data.code2").toString();
            Response validate_response = given().
                    contentType(ContentType.JSON).
                    header("User-Agent", USER_AGENT_PATIENT).
                    body(getRequestFixture("accessToken", "validate_otp_garuda.json")
                            .replace("$otp", request_response.path("otp_id")).replace("$code", otpCode)).
                    expect().
                    log().all().
                    statusCode(200).
                    when().
                    post(BASE_FLORES_URL + VALIDATE_OTP);
            log.info("OTP response : {}", validate_response.prettyPrint());
            properties.setProperty("access_token", validate_response.path("token.access_token"));

            File file = new File("src/main/resources/oms.properties");
            FileOutputStream fileOut = new FileOutputStream(file);
            properties.store(fileOut, "Favorite Things");
            fileOut.close();

            System.out.println("From output file : " + accessToken);

            return validate_response.path("token.access_token");

        }
        public void logoutPatient (String access_token){
            given().
                    contentType(CONTENT_TYPE).
                    header("User-Agent", USER_AGENT_PATIENT).
                    header("Authorization", "bearer " + access_token).
                    expect().
                    statusCode(204).
                    when().
                    post(BASE_FLORES_URL + LOGOUT_USER);

            log.info("Patient Logout successful");
        }

        public String getPharmacyAccessToken () {
            Response request_response = given().
                    contentType(CONTENT_TYPE).
                    header("User-Agent", USER_AGENT_PHARMACY).
                    body(getRequestFixture("accessToken", "request_otp_pharmacy.json")).
                    expect().
                    statusCode(200).
                    when().
                    post(BASE_PHARMACY_URL + REQUEST_OTP);
            Response validate_response = given().
                    contentType(CONTENT_TYPE).
                    header("User-Agent", USER_AGENT_PHARMACY).
                    body(getRequestFixture("accessToken", "validate_otp.json")
                            .replace("$otp", request_response.path("otp_id"))).
                    expect().
                    statusCode(200).
                    when().
                    post(BASE_PHARMACY_URL + VALIDATE_OTP);
            log.info("validate_response doctor : \n" + validate_response.prettyPrint());
            return validate_response.path("token.access_token");
        }

   /* public void logoutPharmacy(String access_token) {
        given().
                       contentType(CONTENT_TYPE).
                       header("User-Agent", USER_AGENT_PATIENT).
                       header("Authorization", "bearer " + access_token).
                       expect().
                       statusCode(204).
                       when().
                       post(BASE_PHARMACY_URL + LOGOUT_USER);

        log.info("Pharmacy Logout successful");
    }*/

        public String createOrder (String access_token){
            Response order_response = given().
                    contentType(CONTENT_TYPE).
                    headers("Authorization", "Bearer " + access_token).
                    headers("User-Agent", USER_AGENT_PATIENT).
                    body(getRequestFixture("Orders", "create_order.json")
                            .replace("$cart_id", randomString())).
                    expect().
                    log().all().
                    statusCode(200).
                    when().
                    post(ORDER_BASE_URL + ORDERS);
            order_id = order_response.path("customer_order_id");
            return order_id;

        }
        public Response createPickupDeliveryOrder (String access_token, CreateOrder order) throws
        JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            Response order_response = given().
                    contentType(CONTENT_TYPE).
                    headers("Authorization", "Bearer " + access_token).
                    headers("User-Agent", USER_AGENT_PATIENT).
                    body(objectMapper.writeValueAsString(order)).
                    expect().
                    log().all().
                    when().
                    post(ORDER_BASE_URL + ORDERS);
            return order_response;

        }

        public void confirmOrderByCash (String access_token, String order_id){

            given().

                    contentType(CONTENT_TYPE).
                    headers("Authorization", "Bearer " + access_token).
                    headers("User-Agent", USER_AGENT_PATIENT).
                    body(getRequestFixture("Orders", "order_confirm_cash.json")).
                    expect().
                    log().all().
                    statusCode(204).
                    when().
                    put(ORDER_BASE_URL + ORDER_CONFIRM.replace("$order_id", order_id));

            log.info("path is________________" + ORDER_BASE_URL + ORDER_CONFIRM.replace("$order_id", order_id));
            log.info("Confirm order completed \n");

        }
        public void confirmOrderByWallet (String access_token, String order_id){

            given().

                    contentType(CONTENT_TYPE).
                    headers("Authorization", "Bearer " + access_token).
                    headers("User-Agent", USER_AGENT_PATIENT).
                    body(getRequestFixture("Orders", "order_confirm_wallet.json")).
                    expect().
                    statusCode(204).
                    when().
                    put(ORDER_BASE_URL + ORDER_CONFIRM.replace("$order_id", order_id));
            log.info("Confirm order completed \n");

        }
        public void cancelOrder (String access_token, String order_id){
            given().
                    contentType(CONTENT_TYPE).
                    headers("Authorization", "Bearer " + access_token).
                    headers("User-Agent", USER_AGENT_PATIENT).
                    body(getRequestFixture("Orders", "cancel_order.json")).
                    expect().
                    statusCode(204).
                    when().
                    put(ORDER_BASE_URL + ORDER_CANCEL.replace("$order_id", order_id));
            log.info("Order cancelled\n");
        }
        public void abandonOrder (String access_token, String order_id){
            given().
                    contentType(CONTENT_TYPE).
                    headers("Authorization", "Bearer " + access_token).
                    headers("User-Agent", USER_AGENT_PATIENT).
                    body(getRequestFixture("Orders", "abandon_order.json")).
                    expect().
                    statusCode(204).
                    when().
                    put(ORDER_BASE_URL + ORDER_ABANDON, order_id);
            log.info("Order abandon\n");
        }

        public void applyCoupon (String access_token, String order_id){
            given().
                    contentType(CONTENT_TYPE).
                    headers("Authorization", "Bearer " + access_token).
                    headers("User-Agent", USER_AGENT_PATIENT).
                    body(getRequestFixture("Orders", "promotion.json")).
                    expect().
                    statusCode(204).
                    when().
                    put(ORDER_BASE_URL + promotions.replace("$order_id", order_id));
            log.info("Order cancelled\n");
        }

        public void confirmOrder (String access_token, String order_id){
            given().
                    contentType(CONTENT_TYPE).
                    headers("Authorization", "Bearer " + access_token).
                    headers("User-Agent", USER_AGENT_PATIENT).
                    body(getRequestFixture("Orders", "order_confirm_cash.json")).
                    expect().
                    statusCode(204).
                    when().
                    put(ORDER_BASE_URL + ORDER_CONFIRM.replace("$order_id", order_id));
        }

        public String createLead () {
            log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return given().
                    header("X-APP-TOKEN", OMS_X_APP_TOKEN).
                    header("content-type", ContentType.JSON).
                    body(getRequestFixture("leads", "create_lead_internal.json")).
                    expect().
                    log().all().
                    statusCode(201).
                    when().
                    post(LEADS_INTERNAL).then().extract().path("external_id");
        }

        public void updateMerchantAttributes (String flag) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            given().
                    contentType(CONTENT_TYPE).
                    headers("X-APP-TOKEN", CMS_X_APP_TOKEN).
                    headers("User-Agent", USER_AGENT_PATIENT).
                    body(getRequestFixture("orders", "merchant_attributes.json").replace("flag", flag)).
                    expect().
                    log().all().
                    statusCode(204).
                    when().
                    put(merchant_attributes);
        }


        public String randomString () {
            return UUID.randomUUID().toString();
        }
    }

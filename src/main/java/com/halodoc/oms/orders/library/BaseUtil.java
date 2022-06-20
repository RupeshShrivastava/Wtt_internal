package com.halodoc.oms.orders.library;

import com.halodoc.core.flores.FloresCore;
import com.halodoc.core.scrooge.Wallet;
import com.halodoc.utils.flores.OTPUtil;
import com.halodoc.utils.http.RestClient;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.testng.asserts.SoftAssert;
import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.*;

@Slf4j
public class BaseUtil {
    public static StringBuilder walletUrl= new StringBuilder(WALLET_BASE_URL);
    public static Wallet wallet = new Wallet(walletUrl.append(WALLET_PORT).toString(),X_APP_WALLET, PHARMACY_USER_AGENT);
    private String orderId;
    private String expectedStatus;

    public String getJson(String path, String fileName) {
        return fixture(path + "/" + fileName);
    }

    public String getUUID() {
        return UUID.randomUUID().toString();
    }

    public Response sendAttachment(String uri, File file, Map headers) {
        RestAssured.baseURI = String.valueOf(new StringBuilder(BASE_URL).append(TIMOR_ORDER_PORT));
        headers.remove("Content-Type");
        headers.put("X-File-Type","image/png");
        Response res = given().log().all().headers(headers).given().urlEncodingEnabled(false).multiPart(getMultiPart()).when().post(uri);
        return res;
    }

    private MultiPartSpecification getMultiPart() {
        return new MultiPartSpecBuilder("Test-Content-In-File".getBytes()).
                fileName("/Users/gauravmehta/IdeaProjects/oms-backend-test/src/main/resources/upload_presc.png").
                controlName("file").
                mimeType("image/png").
                build();
    }

    public String getOTPCodeFromGaruda(String phoneNumber,String baseURL) {
        String otp_code = null;
        baseURL.replace(":","");
        StringBuilder uri = new StringBuilder(OLD_BASE_URL).append(OTP_CODE_URI);

        try {
            OTPUtil OtpUtility = new OTPUtil((uri.toString()),COMMUNICATION_REQUEST,X_APP_TOKEN_GARUDA);
            otp_code =OtpUtility.getOTPCode(phoneNumber, 1, 1);
        } catch (Exception e) {
            log.info("Error!!! some thing went while getting response from garuda!!!!");
            log.info("Exception  : "+e);
        }

        return otp_code;
    }

    public String getOTPFromGaruda(String phoneNumber,String port,  String user_agent,String baseURL) {
        String otp = null;
        String uri;

        if(port!=PHARMACY_PORT) {
            uri= baseURL+port;
        } else {
            uri = baseURL;
        }

        try {
            FloresCore floresCore = new FloresCore((uri.toString()), user_agent);
            otp =floresCore.getOtpId(phoneNumber);
        } catch (Exception e) {
            log.info("Error!!! some thing went while getting response from garuda!!!!");
            log.info("Exception  : "+e);
        }

        return otp;
    }

    public String getAccessTokenFromGaruda(String phoneNumber,String port, String user_agent,String baseURL) {
        String accessToken = null;
        String otpID =this.getOTPFromGaruda(phoneNumber,port, user_agent,baseURL);
        String otpCode =  this.getOTPCodeFromGaruda(phoneNumber,baseURL);
        String uri;

        if(port!=PHARMACY_PORT) {
            uri=baseURL+port;
        } else {
            uri=baseURL;
        }

        try {
            FloresCore floresCore = new FloresCore((uri.toString()), user_agent);
            accessToken =floresCore.getAccessToken(otpID,otpCode,(phoneNumber));
        } catch (Exception e) {
            log.info("Error!!! some thing went while getting response from garuda!!!!");
            log.info("Exception  : "+e);
        }

        return accessToken;
    }

    public HashMap<String, String> getLoginHeaders(String content_type,String phoneNumber,String port, String user_agent,String baseURL) {
        HashMap<String, String> headers = new HashMap();
        headers.put("Content-Type",content_type);
        headers.put("User-Agent",user_agent);
        headers.put("Authorization",("bearer " + getAccessTokenFromGaruda(phoneNumber, port, user_agent,baseURL)));
        headers.put("x-app-token", X_APP_TOKEN_TIMOR);
        return headers;
    }

    public HashMap<String, String> getXappHeaders(String content_type,String xAppToken) {
        HashMap<String, String> headers = new HashMap();
        headers.put("Content-Type",content_type);
        headers.put("X-APP-TOKEN",xAppToken);
        return headers;
    }
    public HashMap<String, String> getVaPaymentHeaders(String content_type) {
        HashMap<String, String> headers = new HashMap();
        headers.put("Authorization","Basic "+AUTH_KEY_XENDIT);
        headers.put("Content-Type",content_type);
        headers.put("Cookie",COOKIE_KEY);
        return headers;
    }

    public boolean validateStatusCode(HttpStatus statusCode, Response response) {
        String[] actual = response.statusLine().toUpperCase().split(" ");
        String[] expected = statusCode.toString().toUpperCase().split(" ");
        log.info("Response Validation Expected Code :"+expected [0]+ "//Actual Code is :"+actual[1]);
        return actual[1].equals(expected[0]);
    }

    public boolean validateErrorCode(String expected, Response response) {
        String actual = response.path("code").toString().toUpperCase();
        log.info("Response Code Validation -- Expected Code :"+expected+ "//Actual Code is :"+actual);
        return actual.equals(expected.toUpperCase());
    }

    public boolean validateSimple(String expected, String actual) {
        actual = actual.toUpperCase();
        expected = expected.toUpperCase();
        log.info("Response Item_total Validation -- Expected total price :"+expected+ "//Actual total price is :"+actual);
        return actual.equals(expected);
    }

    public boolean verifyOrderStatus(String status,Response response) {
        boolean res = false;
        String responseStatus = response.path("status");

        try {
            if (responseStatus.equals(status)) {
                log.info("Order moved to " + status + " successfully!!!");
                res = true;
            } else log.info("Order is not in status: " + status + ", the current status is: " + response.path("status"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public boolean verifyOrderStatuses(String[] status,Response response) {
        boolean res = false;
        String responseStatus = response.path("status");

        try {
            if (responseStatus.equals(status[0]) || responseStatus.equals(status[1])) {
                log.info("Order moved successfully!!!");
                res = true;
            } else log.info("Order is not in status " + status[0] + " or status " + status[0] + ", the current status is: " + response.path("status"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public void walletCredit(String entityID,String amount) {
        wallet.credit(entityID,(getJson(COMMON_JSON,CREDIT_WALLET_JSON).replace("$payRefId",getUUID()).replace("$amount",amount)));
    }

    public long getEpochTime(String dateKey, int unit, ChronoUnit chronoUnit) {
        Instant instant = Instant.now();

        long result = instant.toEpochMilli();

        return result;
    }

    public boolean validateArraySize(Response response, String pathToValidate, String condition, int expectedValue) {
        int size = response.jsonPath().getList(pathToValidate).size();
        boolean result = false;

        if(condition.equals(NOT_EQUAL_KEY)) {
            result = size != expectedValue;
        } else if(condition.equals(EQUAL_KEY)) {
            result = size == expectedValue;
        }

        return result;
    }

    public boolean validateArrayResponseContainExpectedArrayValue(Response response, String rootPath, String[] pathToValidate, List expectedValue, int arrayLevel, int index) {
        int size = response.jsonPath().getList(rootPath).size();
        boolean result = true;

        if(arrayLevel == 1) {
            for (int i = 0; i < size; i++) {
                Object value = response.path(rootPath + "[" + i + "]." + pathToValidate[index]);
                expectedValue.remove(value);

                if(expectedValue.isEmpty())
                    break;
            }
        } else {
            for(int i = 0; i < size; i++) {
                int arrayLevelTmp = arrayLevel - 1;
                int indexTmp = index + 1;
                result = validateArrayResponseContainExpectedArrayValue(response, rootPath + "[" + i + "]." + pathToValidate[index], pathToValidate, expectedValue, arrayLevelTmp, indexTmp);

                if(!result)
                    break;
            }
        }

        result = expectedValue.size() != 0 ? false : result;

        return result;
    }

    public boolean validateResponseValue(Response response, String pathToValidate, Object expectedValue) {
        Object actualValue = response.path(pathToValidate);
        boolean result = actualValue.toString().equals(expectedValue.toString());

        return result;
    }

    public boolean validateResponseValueIsNull(Response response, String pathToValidate) {
        boolean result = false;
        Object actualValue = response.path(pathToValidate);
        if(actualValue == null){
            result = true;
        }

        return result;
    }


    public boolean validateResponseValueArray(Response response, String pathArray, String pathToValidate, Object expectedValue, String mode) {
        int size = response.jsonPath().getList(pathArray).size();
        boolean result = false;

        for(int i = 0; i < size; i++) {
            Object actualValue = response.path(pathArray + "[" + i +"]." + pathToValidate);

            switch(mode) {
                case MODE_EQUALS:
                    result = actualValue.toString().equals(expectedValue.toString());
                    break;
                case MODE_NOT_EQUALS:
                    result = !actualValue.toString().equals(expectedValue.toString());
                    break;
                case MODE_CONTAINS:
                    result = actualValue.toString().replace(" ", "").toLowerCase()
                                        .contains(expectedValue.toString().replace(" ", "").toLowerCase());
                    break;
            }

            if(!result)
                break;
        }

        return result;
    }

    public boolean validateReallocation(Response response) {
        String orderId = response.path("customer_order_id");
        checkStatusUntil(STATUS_CONFIRMED, orderId);
        response = getOrderDetails(orderId);
        boolean result = response.path("status").equals(STATUS_CONFIRMED);

        return result;
    }

    public List constructList(Object[] objects) {
        List list = new ArrayList();

        for (Object object : objects) {
            list.add(object);
        }

        return list;
    }

    public HashMap<String, String> getInvalidToken(String content_type, String user_agent, boolean isWithoutToken) {
        HashMap<String, String> headers = new HashMap();
        headers.put("Content-Type", content_type);
        headers.put("User-Agent", user_agent);

        if(!isWithoutToken) {
            headers.put("Authorization", INVALID_ID);
        }

        return headers;
    }

    public void validatePaymentAttributesDbData(Map<String, Object> paymentAttributesDbData, String payment_id, String attribute_key, String attribute_value){
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotNull(paymentAttributesDbData.get("id"), "Failed at validating 'id' in payment attributes db table.");
        softAssert.assertNotNull(paymentAttributesDbData.get("created_at"), "'created_at' in payment attributes db table is null.");
        softAssert.assertNotNull(paymentAttributesDbData.get("created_by"), "Failed at validating 'created_by' in payment attributes db table.");
        softAssert.assertNotNull(paymentAttributesDbData.get("updated_at"), "'updated_at' in payment attributes db table is null.");
        softAssert.assertNotNull(paymentAttributesDbData.get("updated_by"), "Failed at validating 'updated_by' in payment attributes db table.");
        softAssert.assertEquals(paymentAttributesDbData.get("payment_id").toString(), payment_id, "Failed at validating 'payment_id' in payment attributes db table.");
        softAssert.assertEquals(paymentAttributesDbData.get("attribute_key").toString(), attribute_key, "Failed at validating 'attribute_key' in payment attributes db table.");
        softAssert.assertEquals(paymentAttributesDbData.get("attribute_value").toString(), attribute_value, "Failed at validating 'attribute_value' in payment attributes db table.");
        softAssert.assertAll();
    }

    public boolean validateStatusCodes(HttpStatus[] statusCodes, Response response) {
        String[] actual = response.statusLine().toUpperCase().split(" ");
        String[] expected = new String[2];

        for(int i = 0; i < statusCodes.length; i++) {
            expected[i] = statusCodes[i].toString().toUpperCase().split(" ")[0];
        }

        log.info("Response Validation Expected Code :"+expected [0]+ "//Actual Code is :"+actual[1]);
        boolean result = actual[1].equals(expected[0]) || actual[1].equals(expected[1]);

        return result;
    }

    public boolean validateShipmentDelivered(Response response) {
        ArrayList shipments = response.path("shipments");
        boolean result = false;

        for(int i = 0; i < shipments.size(); i++) {
            String status = response.path("shipments[" + i + "].status");

            if(status.equals("delivered")) {
                String actionType = response.path("shipments[" + i + "].shipment_audits[0].action_type");

                result = actionType.equals("delivered");
                break;
            }
        }

        return result;
    }

    public Response getOrderDetails(String customerOrderId){
        HashMap<String, String> headers = getXappHeaders(CONTENT_TYPE, X_APP_TOKEN_TIMOR);
        String url = BASE_URL + TIMOR_ORDER_PORT + TIMOR_ORDER_INTERNAL + customerOrderId;
        RestClient client=new RestClient(url, headers);
        Response response =client.executeGet();
        return response;
    }

    public void checkLocking(String orderId) {
        this.orderId = orderId;
      //  await().until(checkStatus(MODE_CHECK_LOCK));
    }

    public boolean checkStatusUntil(String expectedStatus, String orderId) {
        try {
            this.expectedStatus = expectedStatus;
            checkLocking(orderId);
            await().until(checkStatus(MODE_CHECK_STATUS));
            checkLocking(orderId);
        } catch(Exception e) {
            return false;
        }

        return true;
    }

    private Callable<Boolean> checkStatus(String mode) {
        return () -> {
            switch(mode) {
                case MODE_CHECK_STATUS:
                    Response response = getOrderDetails(orderId);
                    String actualStatus = response.path("status").toString();
                    return actualStatus.equals(expectedStatus);
                case MODE_CHECK_LOCK:
                    boolean isLockAcquired = false;
                    try {
                        isLockAcquired = lockHelper.lock(orderId);
                    } catch(Exception e) {
                        log.info("Error in acquiring lock : " + e.getMessage());
                    } finally {
                        if(isLockAcquired) {
                            lockHelper.unlock(orderId);
                        }
                        log.info("Inside Finally Locking Acquire: " + isLockAcquired);
                    }
                    log.info("Locking Acquire: " + isLockAcquired);
                    return isLockAcquired;
                default:
                    return false;
            }
        };
    }

    public boolean validateMedisendOrderByUniqueId(Response response, String pathArray, String pathUniqueId, String pathToValidate,
            Object expectedUniqueId, Object expectedValue, String mode) {
        int size = response.jsonPath().getList(pathArray).size();
        boolean result = false;

        for(int i = 0; i < size; i++) {
            Object actualUniqueId = response.path(pathArray + "[" + i +"]." + pathUniqueId);

            if(actualUniqueId.toString().equals(expectedUniqueId.toString())) {
                Object actualValue = response.path(pathArray + "[" + i +"]." + pathToValidate);

                if(actualValue != null) {
                    switch(mode) {
                        case MODE_EQUALS:
                            result = actualValue.toString().equals(expectedValue.toString());
                            break;
                        case MODE_NOT_EQUALS:
                            result = !actualValue.toString().equals(expectedValue.toString());
                            break;
                        case MODE_CONTAINS:
                            result = actualValue.toString().replace(" ", "").toLowerCase()
                                                .contains(expectedValue.toString().replace(" ", "").toLowerCase());
                            break;
                    }

                    if(!result)
                        break;
                } else
                    break;
            }
        }

        return result;
    }

    public boolean validateMedisendOrderByUniqueIdExpectedArray(Response response, String pathArray, String pathUniqueId, String pathToValidate,
            Object expectedUniqueId, List expectedValue) {
        int size = response.jsonPath().getList(pathArray).size();
        boolean result = false;

        for(int i = 0; i < size; i++) {
            Object actualUniqueId = response.path(pathArray + "[" + i +"]." + pathUniqueId);

            if(actualUniqueId.toString().equals(expectedUniqueId.toString())) {
                int sizeExpectedValue = expectedValue.size();

                for(int j = 0; j < sizeExpectedValue; j++) {
                    Object actualValue = response.path(pathArray + "[" + i + "]." + pathToValidate.replace("index", Integer.toString(j)));
                    expectedValue.remove(actualValue);
                }
            }
        }

        result = expectedValue.size() == 0;

        return result;
    }

    public String getShipmentIdByBranchId(Response response, String branchId) {
        int size = response.jsonPath().getList("shipments").size();
        String result = null;

        for(int i = 0; i < size; i++) {
            String actualBranchId = response.path("shipments[" + i + "].distributor_entity_id");

            if(actualBranchId.equals(branchId)) {
                result = response.path("shipments[" + i + "].external_id");
                break;
            }
        }

        return result;
    }

    public String getShipmentIdByParentId(Response response, String parentId) {
        int size = response.jsonPath().getList("shipments").size();
        String result = null;

        for(int i = 0; i < size; i++) {
            String actualParentId = response.path("shipments[" + i + "].attributes.parent_shipment_id");

            if(actualParentId != null && actualParentId.equals(parentId)) {
                result = response.path("shipments[" + i + "].external_id");
                break;
            }
        }

        return result;
    }

    public boolean validatePathContainsValueInList(Response response, String pathToValidate, boolean contains) {
        boolean result = false;
        String path = response.jsonPath().getList(pathToValidate).toString();

        if((path.equals("[]") && !contains)||((!path.equals("[]")) && contains)){
            result = true;
        }
        return result;
    }

    public boolean validatePathContainsValueInPath(Response response, String pathToValidate, boolean contains) {
        boolean result = false;
        String path = response.jsonPath().get(pathToValidate).toString();
        if((path.isEmpty() && !contains)||((!path.isEmpty()) && contains)){
            result = true;
        }
        return result;
    }

    public String getShipmentByDocumentType(Response response, String documentType) {
        int size = response.jsonPath().getList("shipments[0].documents").size();
        String result = null;

        for(int i = 0; i < size; i++) {
            String actualDocumentType = response.path("shipments[0].documents[" + i + "].document_type");

            if(actualDocumentType.equals(documentType)) {
                result = response.path("shipments[0].documents[" + i + "].external_id");
                break;
            }
        }

        return result;
    }

    public boolean validateStatusAndDeletedInUserSearch(Response response, String expectedStatus, String expectedDeletedFlag) {
        String actualStatus = response.path(RESULT_PATH + FIRST_ARRAY_ELEMENT + STATUS_PATH);
        String actualDeletedFlag = response.path( RESULT_PATH + FIRST_ARRAY_ELEMENT + STATUS_DELETED).toString();

        return actualStatus.equals(expectedStatus) && actualDeletedFlag.equals(expectedDeletedFlag);
    }

    public boolean validateGPIDStatusAndDeletedInUserSearch(Response response, String expectedGPID, String expectedStatus, String expectedDeletedFlag) {
        String actualStatus = response.path(RESULT_PATH + FIRST_ARRAY_ELEMENT + STATUS_PATH);
        String actualDeletedFlag = response.path( RESULT_PATH + FIRST_ARRAY_ELEMENT + STATUS_DELETED).toString();
        String actualGPID = response.path(RESULT_PATH + FIRST_ARRAY_ELEMENT + GPID_PATH);

        return actualStatus.equals(expectedStatus) && actualDeletedFlag.equals(expectedDeletedFlag) && actualGPID.equals(expectedGPID);
    }
}
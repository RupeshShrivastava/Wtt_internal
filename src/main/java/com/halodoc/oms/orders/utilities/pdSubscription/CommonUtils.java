package com.halodoc.oms.orders.utilities.pdSubscription;

import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.PHONIX_APP_TOKEN;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.PHONONIX_URL;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.USER_AGENT_CUSTOMER_APP;
import static io.restassured.RestAssured.given;
import static io.restassured.config.RedirectConfig.redirectConfig;
import java.util.HashMap;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import ch.qos.logback.classic.Level;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class CommonUtils {

    HashMap<String,String> headers = new HashMap<>();
    public static String access_token;

    @BeforeSuite
    public void beforeSuite() {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
        root.setLevel(Level.OFF);

    }


    public HashMap<String,String> withDefault(String entityId,Boolean flag){

        headers.put("Content-Type", "application/json");
        headers.put("User-Agent",USER_AGENT_CUSTOMER_APP);
        headers.put("Authorization","bearer "+getAccessToken(entityId,flag));
        return headers;
    }

    public String getAccessToken(String entityId,Boolean flag) {
        if(flag) {
            HashMap<String, String> header = new HashMap<>();
            String postBody = "{\"entity_id\": \"" + entityId + "\", \"device_id\" : \"string\" }";
            header.put("x-app-token", PHONIX_APP_TOKEN);
            header.put("Content-Type", "application/json");
            Response response = CommonUtils.postApiStatusWithoutLog(postBody,PHONONIX_URL, header);
            JSONObject jsonObject = new JSONObject(response.asString());
            access_token = jsonObject.get("access_token").toString();
            return access_token;
        }else
            return access_token;
    }

    @Step ("{0}")
    public Response postApiResponse(String jsonBody, String pathParams,HashMap<String, String> headers) {
        Response response;
        response = given().
                           config(RestAssured.config().redirect(redirectConfig().followRedirects(false))).
                           log().uri()
                          .headers(headers)
                          .when()
                          .body(jsonBody)
                          .post(pathParams)
                          .then()
                          .extract()
                          .response();
        return response;
    }

    @Step ("{0}")
    public static Response putApiResponse(String jsonBody, String pathParams,HashMap<String, String> headers) {
        Response response;
        response = given().log().all()
                          .headers(headers)
                          .when()
                          .body(jsonBody)
                          .put(pathParams)
                          .then()
                          .extract()
                          .response();
        return response;
    }
    @Step ("{0}")
    public static Response getApiResponseNoQueryParams(String pathParams,HashMap<String, String> headers) {
        Response response;
        response = given().log().uri()
                          .headers(headers)
                          .when()
                          .get(pathParams)
                          .then()
                          .extract()
                          .response();
        return response;
    }

    @Step ("{0}")
    public static Response getApiStatus(String pathParams,HashMap<String, String> queryParm,HashMap<String, String> headers) {
        Response response;
        response = given()
                //.urlEncodingEnabled(false)
                .log().all()
                .headers(headers)
                .queryParams(queryParm)
                .when()
                .get(pathParams)
                .then()
                .extract()
                .response();
        if(response.getStatusCode()!=200)
            Assert.fail("Response obtained is " + response.getStatusCode());
        else if(response.getBody().toString().isEmpty())
            Assert.fail("Response body is empty");
        return response;
    }

    @Step ("{0}")
    public static Response postApiStatusWithoutLog(String jsonBody, String pathParams,HashMap<String, String> headers) {
        Response response;
        response = given().log().all()
                          .headers(headers)
                          .when()
                          .body(jsonBody)
                          .post(pathParams)
                          .then()
                          .extract()
                          .response();

        if(response.getStatusCode()!= HttpStatus.SC_OK  && response.getStatusCode()!= HttpStatus.SC_CREATED)
            Assert.fail("Response obtained is " + response.getStatusCode()
                    +"\nResponse body "+ response.getBody().asString());
        else if(response.getBody().toString().isEmpty())
            Assert.fail("Response body is empty");
        return response;
    }

    @Step ("{0}")
    public static Response postApiStatus(String jsonReqBody,String endPoint,HashMap<String, String> headers) {
        Response response;
        response = given().contentType("application/x-www-form-urlencoded; charset=utf-8")
                          .log().all()
                          .headers(headers)
                          .when()
                          .redirects().follow(false)
                          .body(jsonReqBody)
                          .post(endPoint)
                          .then()
                          .extract()
                          .response();
        //ReporterUtil.log(response.asString());
        if(response.getStatusCode()!= HttpStatus.SC_OK  && response.getStatusCode()!= HttpStatus.SC_CREATED
                && response.getStatusCode()!= HttpStatus.SC_ACCEPTED && response.getStatusCode()!= HttpStatus.SC_NO_CONTENT) {
            Assert.fail("Response obtained is " + response.getStatusCode() + "\nResponse body " + response.getBody().asString());
        }
        else if(response.getBody().toString().isEmpty())
            Assert.fail("Response body is empty");
        return response;
    }

    @Step ("{0}")
    public static Response putApiWithoutBodyStatus(String endPoint,HashMap<String, String> headers) {
        Response response;
        response = given().log().all()
                          .headers(headers)
                          .when()
                          .put(endPoint)
                          .then()
                          .log().all()
                          .extract()
                          .response();
        //ReporterUtil.log(response.asString());
        if(response.getStatusCode()!= HttpStatus.SC_OK  && response.getStatusCode()!= HttpStatus.SC_CREATED && response.getStatusCode()!= HttpStatus.SC_NO_CONTENT)
            Assert.fail("Response obtained is " + response.getStatusCode()
                    +"\nResponse body "+ response.getBody().asString());
        else if(response.getBody().toString().isEmpty())
            Assert.fail("Response body is empty");
        return response;
    }
}

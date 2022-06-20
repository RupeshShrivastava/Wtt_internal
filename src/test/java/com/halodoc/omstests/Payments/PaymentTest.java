package com.halodoc.omstests.Payments;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halodoc.omstests.TimorHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.hamcrest.Matchers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.halodoc.omstests.Constants.*;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;


@Slf4j
public class PaymentTest {
    private String customer_access_token;

    private TimorHelper timorHelper;

    @BeforeClass
    public void setUp() {
        timorHelper = new TimorHelper();
        customer_access_token = timorHelper.getPatientAccessToken();

    }

    @Test(priority = 0)
    public void paymentFlow() throws InterruptedException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
                given().
                contentType(CONTENT_TYPE).
                headers("Authorization", "Bearer " + customer_access_token).
                headers("User-Agent", USER_AGENT_PATIENT).
                queryParam("amount","5000").
                queryParam("version","2.600").
                expect().
                log().all().
                statusCode(200).
                when().
                get(PAYMENT_URL +TOP_UP_PAYMENT);
    }
    @Test(priority = 1)
    public void paymentwithoutAuthorization() throws InterruptedException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
                given().
                contentType(CONTENT_TYPE).
                headers("User-Agent", USER_AGENT_PATIENT).
                queryParam("amount","5000").
                queryParam("version","2.600").
                expect().
                log().all().
                statusCode(403).
                when().
                get(PAYMENT_URL +TOP_UP_PAYMENT);
    }
    @Test(priority = 2)
    public void paymentWithLowerVersion() throws InterruptedException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
                given().
                contentType(CONTENT_TYPE).
                headers("User-Agent", USER_AGENT_PATIENT).
                queryParam("amount","5000").
                queryParam("version","2.3.0").
                expect().
                log().all().
                statusCode(200).
                when().
                get(PAYMENT_URL +TOP_UP_PAYMENT);
    }
    @Test(priority = 3)
    public void notifyPayment() throws InterruptedException,IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        ObjectMapper mapper = new ObjectMapper();

        String request = fixture(FILE_LOCATION+"/" + "payment_notify.json");
        Map<String, Object> map = new HashMap<String, Object>();
        map = mapper.readValue(request, new TypeReference<Map<String, String>>(){});

        System.out.println("Req : "+request);
        given().
                //contentType(ContentType.JSON).
                config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("*/*", ContentType.JSON))).
                formParams(map).
                expect().
                log().all().
                statusCode(200).
                when().
                post(PAYMENT_URL +NOTIFY);
    }

    @AfterClass
    public void cleanUp() {
       timorHelper.logoutPatient(customer_access_token);
    }

}

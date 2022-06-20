package com.halodoc.omstests.orders.timor;

import com.halodoc.oms.orders.utilities.DbUtils;
import com.halodoc.oms.orders.utilities.payments.Instrument;
import com.halodoc.oms.orders.AutoBINBasedOMS;
import com.halodoc.oms.orders.GopayTokenization;
import com.halodoc.omstests.Constants;
import com.halodoc.omstests.TimorHelper;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import com.halodoc.oms.orders.library.BaseUtil;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import static com.halodoc.omstests.Constants.*;


@Slf4j
public class GopayTokenizationTests extends BaseUtil {

    public HashMap<String, String> headers = new HashMap<>();

    private String customer_access_token, customer_access_token_other, gopay_instrument_other, order_id_price, order_id_total;

    private TimorHelper timorHelper;

    public String gopay_instrument, order_id_above_threshold, order_id_below_threshold, order_total_below_threshold, order_total_above_threshold;
    public String payment_id_below_threshold,payment_id_above_threshold;
    private DbUtils dbUtils;

    public GopayTokenizationTests() {
        dbUtils = DbUtils.getDbUtilsInstance();
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() throws InterruptedException {
        timorHelper = new TimorHelper();
        customer_access_token = timorHelper.getPatientAccessToken();
        customer_access_token_other = timorHelper.getOtherPatientAccessToken();
        getUserInstrument();
        getUserInstrumentOtherUser();
        createOrderBelowThreshold();
        getOrderBlowThreshold();
        createOrderAboveThreshold();
        getOrderAboveThreshold();
        createOrderWithPriceMoreThanWalletBalance();
        getOrderWithPriceMoreThanWalletBalance();

    }


    @AfterClass(alwaysRun = true)
    public void afterMethod() throws IOException {
        cancelOrderBelowThreshold();
        cancelOrderAboveThreshold();
        dbUtils.closeDbConnection();
    }


    public void getUserInstrument(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        Response response = Instrument.getInstrument(headers);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK,response));
        gopay_instrument = response.getBody().path("[0].external_id");
        log.info("gopay_instrument is" +gopay_instrument);
    }

    public void getUserInstrumentOtherUser(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token_other);
        headers.put("User-Agent", user_agent);
        Response response = Instrument.getInstrument(headers);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK,response));
        gopay_instrument_other = response.getBody().path("[0].external_id");
        log.info("gopay_instrument is" +gopay_instrument_other);
    }


    public void createOrderBelowThreshold() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
       String jsonString = timorHelper.getRequestFixture("orders", "create_order.json")
                .replace("$cart_id", timorHelper.randomString())
                .replace("$quantity", "1");
        Response response = GopayTokenization.createOrder(headers,jsonString);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        order_id_below_threshold =  response.body().path("customer_order_id");
        log.info("order_id is" +order_id_below_threshold);

    }


    public void getOrderBlowThreshold() throws InterruptedException {
        //WAIT FOR ORDER TO APPROVE
        Thread.sleep(10000);
        Response response = GopayTokenization.getOrder(headers, order_id_below_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK,response));
        response.getBody().print().contains("approved");
        order_total_below_threshold = response.body().path("requested_price").toString();

    }


    public void createOrderAboveThreshold() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "create_order.json")
                .replace("$cart_id", timorHelper.randomString())
                .replace("$quantity", "10");
        Response response = GopayTokenization.createOrder(headers,jsonString);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        order_id_above_threshold =  response.body().path("customer_order_id");
        log.info("order_id is" +order_id_above_threshold);


    }


    public void getOrderAboveThreshold() throws InterruptedException {
        //WAIT FOR ORDER TO APPROVE
        Thread.sleep(10000);
        Response response = GopayTokenization.getOrder(headers, order_id_above_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK,response));
        response.getBody().print().contains("approved");
        order_total_above_threshold = response.body().path("requested_price").toString();

    }



    public void createOrderWithPriceMoreThanWalletBalance() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "create_order.json")
                .replace("$cart_id", timorHelper.randomString())
                .replace("$quantity", "100");
        Response response = GopayTokenization.createOrder(headers,jsonString);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        order_id_price =  response.body().path("customer_order_id");
        log.info("order_id is" +order_id_price);


    }


    public void getOrderWithPriceMoreThanWalletBalance() throws InterruptedException {
        //WAIT FOR ORDER TO APPROVE
        Thread.sleep(10000);
        Response response = GopayTokenization.getOrder(headers, order_id_price);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK,response));
        response.getBody().print().contains("approved");
        order_id_total = response.body().path("requested_price").toString();

    }


    @Test(description = "Pay for Order ", groups = {"regression"}, priority = 0)
    public void payWithGoPayOrderWithPaymentMethodAsCard() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_below_threshold)
                .replace("$amount", order_total_below_threshold)
                .replace("$instrument_id", gopay_instrument)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD1);
        Response response = GopayTokenization.payForOrder(headers,jsonString, order_id_below_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST,response));

    }


    @Test(description = "Pay for Order ", groups = {"regression"}, priority = 1)
    public void payWithGoPayOrderWithPaymentMethodAsWallet() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_below_threshold)
                .replace("$amount", order_total_below_threshold)
                .replace("$instrument_id", gopay_instrument)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD2);
        Response response = GopayTokenization.payForOrder(headers,jsonString, order_id_below_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST,response));

    }



    @Test(description = "Pay for Order ", groups = {"regression"}, priority = 2)
    public void payWithGoPayOrderWithPaymentMethodAsCash() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_below_threshold)
                .replace("$amount", order_total_below_threshold)
                .replace("$instrument_id", gopay_instrument)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD3);
        Response response = GopayTokenization.payForOrder(headers,jsonString, order_id_below_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST,response));

    }



    @Test(description = "Pay for Order ", groups = {"sanity","regression"}, priority = 3)
    public void payWithGoPayOrderForBelowThreshold() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_below_threshold)
                .replace("$amount", order_total_below_threshold)
                .replace("$instrument_id", gopay_instrument)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD_VALID);
        Response response = GopayTokenization.payForOrder(headers,jsonString, order_id_below_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK,response));
        payment_id_below_threshold = response.getBody().path("customer_payment_id");
        log.info("payment_id is" +payment_id_below_threshold);
        ResponseBody body = response.getBody();
        Assert.assertTrue(body.print().contains("successful"));

        //db get validations for recording gopay_wallet info
        String paymentId =  dbUtils.getDbDataByQuery(SQL_PAYMENTS_BY_SERVICE_REF_ID.replace("${service_reference_id}", order_id_below_threshold)).get(0).get("id").toString();
        Map<String, Object> paymentAttributesDbData = dbUtils.getDbDataByQuery(SQL_PAYMENT_ATTRIBUTES_BY_ID_AND_KEY
                .replace("${payment_id}", paymentId)
                .replace("${attribute_key}", payment_option_attribute_key)).get(0);
        validatePaymentAttributesDbData(paymentAttributesDbData,paymentId, payment_option_attribute_key, payment_option);

        Map<String, Object> paymentAttributesDbData1 = dbUtils.getDbDataByQuery(SQL_PAYMENT_ATTRIBUTES_BY_ID_AND_KEY
                .replace("${payment_id}", paymentId)
                .replace("${attribute_key}", user_instrument_id_attribute_key)).get(0);
        validatePaymentAttributesDbData(paymentAttributesDbData1,paymentId, user_instrument_id_attribute_key, gopay_instrument);
    }

    @Test(description = "Pay for Order ", groups = {"sanity","regression"}, priority = 4)
    public void payWithGoPayOrderForAboveThreshold() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_above_threshold)
                .replace("$amount", order_total_above_threshold)
                .replace("$instrument_id", gopay_instrument)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD_VALID);
        Response response = GopayTokenization.payForOrder(headers,jsonString, order_id_above_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK,response));
        payment_id_above_threshold = response.getBody().path("customer_payment_id");
        log.info("payment_id is" +payment_id_above_threshold);
        ResponseBody body = response.getBody();
        Assert.assertTrue(body.print().contains("processing"));
        Assert.assertTrue(body.print().contains("user-verification"));
        Assert.assertTrue(body.print().contains("verification-link-url"));
        Assert.assertTrue(body.print().contains("verification-link-app"));

    }



    @Test(description = "Pay for Order ", groups = {"regression"}, priority = 5)
    public void payWithGoPayOrderWithInvaidInstrumentId() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_above_threshold)
                .replace("$amount", order_total_above_threshold)
                .replace("$instrument_id", INVALID_INSTRUMENT_ID)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD_VALID);
        Response response = GopayTokenization.payForOrder(headers,jsonString, order_id_above_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST,response));

    }


    @Test(description = "Pay for Order ", groups = {"sanity","regression"}, priority = 6)
    public void payWithGoPayOrderWithInstrumentIdOfOtherUser() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_above_threshold)
                .replace("$amount", order_total_above_threshold)
                .replace("$instrument_id", gopay_instrument_other)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD_VALID);
        Response response = GopayTokenization.payForOrder(headers,jsonString, order_id_above_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST,response));

    }



    @Test(description = "Pay for Order ", groups = {"regression"}, priority = 7)
    public void payWithGoPayOrderWithOutInstrumentId() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_above_threshold)
                .replace("$amount", order_total_above_threshold)
                .replace("$instrument_id", "")
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD_VALID);
        Response response = GopayTokenization.payForOrder(headers,jsonString, order_id_above_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST,response));

    }



    @Test(description = "Pay for Order ", groups = {"regression"}, priority = 8)
    public void payWithGoPayOrderWithInstrumentIdWithSpecialCharacters() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_above_threshold)
                .replace("$amount", order_total_above_threshold)
                .replace("$instrument_id", "@#$@#")
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD_VALID);
        Response response = GopayTokenization.payForOrder(headers,jsonString, order_id_above_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST,response));

    }



    @Test(description = "Pay for Order ", groups = {"regression"}, priority = 9)
    public void chargeWithInvalidEntityType() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_above_threshold)
                .replace("$amount", order_total_above_threshold)
                .replace("$instrument_id", gopay_instrument_other)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "doctor")
                .replace("$payment_method", PAYMENT_METHOD_VALID);
        Response response = GopayTokenization.payForOrder(headers,jsonString, order_id_above_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST,response));

    }


    @Test(description = "Pay for Order ", groups = {"regression"}, priority = 10)
    public void payWithGoPayForCancelledOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_below_threshold)
                .replace("$amount", order_total_below_threshold)
                .replace("$instrument_id", gopay_instrument)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD_VALID);
        Response response = GopayTokenization.payForOrder(headers,jsonString, order_id_below_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY,response));

    }


    @Test(description = "Pay for Order ", groups = {"regression"}, priority = 11)
    public void payWithGoPayForDeliveredOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_below_threshold)
                .replace("$amount", order_total_below_threshold)
                .replace("$instrument_id", gopay_instrument)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD_VALID);
        Response response = GopayTokenization.payForOrder(headers,jsonString, DELIVERED_ORDER);
        Assert.assertTrue(validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY,response));

    }


    @Test(description = "Pay for Order ", groups = {"regression"}, priority = 12)
    public void payWithGoPayForAbandonedOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_below_threshold)
                .replace("$amount", order_total_below_threshold)
                .replace("$instrument_id", gopay_instrument)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD_VALID);
        Response response = GopayTokenization.payForOrder(headers,jsonString, ABANDONED_ORDER);
        Assert.assertTrue(validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY,response));

    }


    @Test(description = "Pay for Order ", groups = {"regression"}, priority = 13)
    public void payWithGoPayWithInvalidOrderId() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", INVALID_ORDER_ID)
                .replace("$amount", order_total_below_threshold)
                .replace("$instrument_id", gopay_instrument)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD_VALID);
        Response response = GopayTokenization.payForOrder(headers,jsonString, ABANDONED_ORDER);
        Assert.assertTrue(validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY,response));

    }



    @Test(description = "Pay for Order ", groups = {"sanity","regression"}, priority = 14)
    public void payWithGoPayOrderWithPriceMoreThanWalletBalance() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "pay_gopay.json")
                .replace("$order_id", order_id_price)
                .replace("$amount", order_id_total)
                .replace("$instrument_id", gopay_instrument)
                .replace("$service", "pharmacy_delivery")
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$entity_type", "user")
                .replace("$payment_method", PAYMENT_METHOD_VALID);
        Response response = GopayTokenization.payForOrder(headers,jsonString, order_id_price);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST,response));
        ResponseBody body = response.getBody();
        Assert.assertTrue(body.print().contains("Cart Value is not in allowable range"));


    }



    public void cancelOrderBelowThreshold() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("timor", "cancel_order.json");
        Response response = AutoBINBasedOMS.cancelOrder(headers, jsonString, order_id_below_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));

    }



    public void cancelOrderAboveThreshold() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("timor", "cancel_order.json");
        Response response = AutoBINBasedOMS.cancelOrder(headers, jsonString, order_id_above_threshold);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));

    }

}

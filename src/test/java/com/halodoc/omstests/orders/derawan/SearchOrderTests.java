package com.halodoc.omstests.orders.derawan;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Feature("SEARCH ORDER TESTS")
public class SearchOrderTests extends OrdersBaseTest {

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY SEARCH ORDER")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifySearchOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = derawanHelper.searchMedisendOrder(SEARCH_START_AT, SEARCH_END_AT, SEARCH_ENTITY_ID, SEARCH_ENTITY_TYPE, EMPTY_STRING,
                EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateArraySize(response, RESULT_PATH, EQUAL_KEY, 10));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY SEARCH ORDER BY ID")
    @Test (groups = { "regression" }, priority = 2)
    public void verifySearchOrderByOrderId() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = derawanHelper.searchMedisendOrder(SEARCH_START_AT, SEARCH_END_AT, SEARCH_ENTITY_ID, SEARCH_ENTITY_TYPE, SEARCH_ORDER_ID,
                EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateArraySize(response, RESULT_PATH, EQUAL_KEY, 1));
        Assert.assertTrue(buruHelper.validateResponseValue(response, RESULT_PATH + "[0]." + CUSTOMER_ORDER_ID_PATH, SEARCH_ORDER_ID));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY SEARCH ORDER BY STATUS CREATED")
    @Test (groups = { "regression" }, priority = 3)
    public void verifySearchOrderByStatusCreated() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = derawanHelper.searchMedisendOrder(SEARCH_START_AT, SEARCH_END_AT, SEARCH_ENTITY_ID, SEARCH_ENTITY_TYPE, EMPTY_STRING,
                STATUS_CREATED, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateArraySize(response, RESULT_PATH, EQUAL_KEY, 7));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY SEARCH ORDER WITH PAGE LIMIT")
    @Test (groups = { "regression" }, priority = 4)
    public void verifySearchOrderWithPageLimit() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = derawanHelper.searchMedisendOrder(SEARCH_START_AT, SEARCH_END_AT, SEARCH_ENTITY_ID, SEARCH_ENTITY_TYPE, EMPTY_STRING,
                EMPTY_STRING, Integer.toString(PAGE_LIMIT), EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateArraySize(response, RESULT_PATH, EQUAL_KEY, PAGE_LIMIT));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY SEARCH ORDER WITH CANCEL REQUEST")
    @Test (groups = { "regression" }, priority = 5)
    public void verifySearchOrderWithCancelRequest() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = derawanHelper.searchMedisendOrder(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, SEARCH_ENTITY_TYPE, EMPTY_STRING,
                EMPTY_STRING, Integer.toString(PAGE_LIMIT), ACTION_TYPE_CANCEL_REQUEST, CREATED_AT_PATH, SORT_ORDER_ASC);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, RESULT_PATH, SHIPMENTS_PATH + "[0]." + ATTRIBUTES_PATH + "." +
                CANCELLATION_REQUEST_TYPE_PATH, TYPE_CANCELLED_PHARMACY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY SEARCH ORDER WITH PARTIAL FULFILMENT")
    @Test (groups = { "regression" }, priority = 6)
    public void verifySearchOrderWithPartialFulfil() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = derawanHelper.searchMedisendOrder(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, SEARCH_ENTITY_TYPE, EMPTY_STRING,
                EMPTY_STRING, Integer.toString(PAGE_LIMIT), ACTION_TYPE_PARTIAL_FULFIL, CREATED_AT_PATH, SORT_ORDER_ASC);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, RESULT_PATH, SHIPMENTS_PATH + "[0]." + ATTRIBUTES_PATH + "." +
                FULFILLMENT_TYPE_PATH, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY SEARCH ORDER WITH FAILED CREATE")
    @Test (groups = { "regression" }, priority = 7)
    public void verifySearchOrderWithFailedCreate() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = derawanHelper.searchMedisendOrder(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, SEARCH_ENTITY_TYPE, EMPTY_STRING,
                EMPTY_STRING, Integer.toString(PAGE_LIMIT), ACTION_TYPE_FAILED_ORDER, CREATED_AT_PATH, SORT_ORDER_ASC);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, RESULT_PATH, SHIPMENTS_PATH + "[0]." + ATTRIBUTES_PATH + "." +
                ORDER_CREATION_FAILURE_PATH, TRUE, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
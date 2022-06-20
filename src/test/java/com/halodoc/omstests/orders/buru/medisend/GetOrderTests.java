package com.halodoc.omstests.orders.buru.medisend;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import java.util.ArrayList;
import java.util.List;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Feature("GET ORDER TESTS")
public class GetOrderTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();
    private String MEDISEND_ORDER_ID;

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY GET ORDER")
    @Test(groups = {"sanity", "regression"}, priority = 1)
    public void verifyGetOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        int count = 0;

        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);
        MEDISEND_ORDER_ID = response.path(CUSTOMER_ORDER_ID_PATH);

        Response response1 = buruHelper.getMedisendOrder(MEDISEND_ORDER_ID);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response1), ORDER_GET_FAILURE);
        softAssert.assertTrue(buruHelper.validateResponseValue(response1, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response1, TOTAL_PATH, 2200), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response1), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response1, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response1, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response1, SHIPMENTS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response1, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY GET ORDER WITH DIFFERENT NUMBER SAME PHARMACY")
    @Test(groups = {"regression"}, priority = 2)
    public void verifyGetOrderWithDifferentNumberSamePharmacy() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        int count = 0;

        Response response1 = buruHelper.getMedisendOrderByDifferentNumber(MEDISEND_ORDER_ID);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response1), ORDER_GET_FAILURE);
        Assert.assertTrue(buruHelper.validateResponseValue(response1, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        Assert.assertTrue(buruHelper.validateResponseValue(response1, TOTAL_PATH, 2200), RESULT_MATCH_FAILURE+ ++count);
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response1), RESULT_MATCH_FAILURE+ ++count);
        Assert.assertTrue(buruHelper.validateResponseValueArray(response1, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        Assert.assertTrue(buruHelper.validateResponseValueArray(response1, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        Assert.assertTrue(buruHelper.validateResponseValueArray(response1, SHIPMENTS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        Assert.assertTrue(buruHelper.validateResponseValueArray(response1, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
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
@Feature("CREATE ORDER TESTS")
public class CreateOrderTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER")
    @Test(groups = {"sanity", "regression"}, priority = 1)
    public void verifyCreateOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH TWO ITEMS")
    @Test(groups = {"regression"}, priority = 2)
    public void verifyCreateOrderWithTwoItems() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 4400), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH TWO SHIPMENTS")
    @Test(groups = {"regression"}, priority = 3)
    public void verifyCreateOrderWithTwoShipments() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 4400), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper
                .validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH OUT OF STOCK ITEM")
    @Test(groups = {"regression"}, priority = 4)
    public void verifyCreateOrderWithOutOfStockItem() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 0), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_ON_HOLD, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_ON_HOLD, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, OUT_OF_STOCK_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_DELETED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_DELETED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH TWO ITEMS: ONE IS OUT OF STOCK")
    @Test(groups = {"regression"}, priority = 5)
    public void verifyCreateOrderWithTwoItemsOneIsOutOfStock() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_UNAVAILABLE_ID_1, STATUS_ON_HOLD, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_UNAVAILABLE_ID_1, OUT_OF_STOCK_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH,
                new String[]{STATUS_PATH}, buruHelper.constructList(new String[]{STATUS_CREATED, STATUS_DELETED}), 1, 0), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH TWO SHIPMENTS: ONE SHIPMENT IS OUT OF STOCK")
    @Test(groups = {"regression"}, priority = 6)
    public void verifyCreateOrderWithTwoShipmentsOneShipmentIsOutOfStock() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_UNAVAILABLE_ID_1, STATUS_ON_HOLD, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_UNAVAILABLE_ID_1, OUT_OF_STOCK_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_DELETED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_DELETED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH REQUESTED MORE THAN AVAILABLE")
    @Test(groups = {"regression"}, priority = 7)
    public void verifyCreateOrderWithRequestedMoreThanAvailable() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductMoreThanAvailable1BranchNoMinimum1.getInstance(MAX_REQUESTED_QUANTITY)); //ProductID : 8c3f8ffd-f8a6-49a3-927f-0a992b354a79

        Response productResponse = buruHelper.updateMedisendProduct(DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_MAPPING_ID_1, STATUS_ACTIVE, Integer.toString(MAX_QUANTITY), SKU_ID_PRODUCT_1);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, productResponse), UPDATE_FAILURE);

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 11000), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, MAX_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH TWO ITEMS: ONE IS REQUESTED MORE THAN AVAILABLE")
    @Test(groups = {"regression"}, priority = 8)
    public void verifyCreateOrderWithTwoItemsOneIsRequestedMoreThanAvailable() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductMoreThanAvailable2BranchNoMinimum1.getInstance(MAX_REQUESTED_QUANTITY)); //ProductID: c6a2fa18-8d2a-4920-9d05-f492399e3df9
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY)); //ProductID: df669afe-dfca-458c-acd4-f1181278f6ed

        Response productResponse = buruHelper.updateMedisendProduct(DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_MAPPING_ID_2, STATUS_ACTIVE, Integer.toString(MAX_QUANTITY), SKU_ID_PRODUCT_2);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, productResponse), UPDATE_FAILURE);

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 13200), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_ID_2, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_ID_2, MAX_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH TWO SHIPMENTS: ONE SHIPMENT IS REQUESTED MORE THAN AVAILABLE")
    @Test(groups = {"regression"}, priority = 9)
    public void verifyCreateOrderWithTwoshipmentsOneshipmentisrequestedmorethanavailable() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductMoreThanAvailable3BranchNoMinimum1.getInstance(MAX_REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response productUpdate = buruHelper.updateMedisendProduct(DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_MAPPING_ID_3, STATUS_ACTIVE, Integer.toString(MAX_QUANTITY), SKU_ID_PRODUCT_3);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, productUpdate), UPDATE_FAILURE);

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), ORDER_DETAILS_FAILED_MESSAGE);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 13200), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_ID_3, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_ID_3, MAX_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH REQUESTED LESS THAN BASKET SIZE")
    @Test(groups = {"regression"}, priority = 10)
    public void verifyCreateOrderWithRequestedLessThanBasketSize() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_ON_HOLD, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_DRAFT, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH TWO SHIPMENTS: ONE SHIPMENT IS REQUESTED LESS THAN BASKET SIZE")
    @Test(groups = {"regression"}, priority = 11)
    public void verifyCreateOrderWithTwoShipmentsOneShipmentIsRequestedLessThanBasketSize() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 4400), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_ON_HOLD, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_WITH_MINIMUM_ID_1, STATUS_DRAFT, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_WITH_MINIMUM_ID_1, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH TWO ITEMS: ONE IS REQUESTED LESS THAN BASKET SIZE AND ONE IS OUT OF STOCK")
    @Test(groups = {"regression"}, priority = 12)
    public void verifyCreateOrderWithTwoItemsOneIsRequestedLessThanBasketSizeOneIsOutOfStock() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_ON_HOLD, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_UNAVAILABLE_ID_1, STATUS_ON_HOLD, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_UNAVAILABLE_ID_1, OUT_OF_STOCK_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_DRAFT, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH,
                new String[]{STATUS_PATH}, buruHelper.constructList(new String[]{STATUS_CREATED, STATUS_DELETED}), 1, 0), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH TWO SHIPMENTS: ONE SHIPMENT IS REQUESTED LESS THAN BASKET SIZE AND ONE SHIPMENT IS OUT OF STOCK")
    @Test(groups = {"regression"}, priority = 13)
    public void verifyCreateOrderWithTwoShipmentsOneShipmentIsRequestedLessThanBasketSizeOneshipmentIsOutOfStock() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_ON_HOLD, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_UNAVAILABLE_ID_1, STATUS_ON_HOLD, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_UNAVAILABLE_ID_1, OUT_OF_STOCK_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_WITH_MINIMUM_ID_1, STATUS_DRAFT, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_DELETED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_WITH_MINIMUM_ID_1, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_DELETED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH INACTIVE BRANCH")
    @Test(groups = {"regression"}, priority = 14)
    public void verifyCreateOrderWithInactiveBranch() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchInactive.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response), INVALID_RESPONSE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH INACTIVE PRODUCT")
    @Test(groups = {"regression"}, priority = 15)
    public void verifyCreateOrderWithInactiveProduct() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductInactiveBranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 0), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_ON_HOLD, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_ON_HOLD, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, ZERO_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_DELETED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_DELETED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH INACTIVE PHARMACY")
    @Test(groups = {"regression"}, priority = 16)
    public void verifyCreateOrderWithInactivePharmacy() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrderWithInactivePharmacy(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response), INVALID_RESPONSE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH PROCUREMENT FALSE")
    @Test(groups = {"regression"}, priority = 17)
    public void verifyCreateOrderWithProcurementFalse() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = buruHelper.updateMedisendProcurementAttribute(MEDISEND_MERCHANT_INACTIVE_ID, MEDISEND_MERCHANT_LOCATION_INACTIVE_ID, FALSE);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response), UPDATE_FAILURE);

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        response = buruHelper.createMedisendOrderWithInactivePharmacy(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response), INVALID_RESPONSE);

        response = buruHelper.updateMedisendProcurementAttribute(MEDISEND_MERCHANT_INACTIVE_ID, MEDISEND_MERCHANT_LOCATION_INACTIVE_ID, TRUE);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response), UPDATE_FAILURE);;

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH STATUS ACTIVE DISPLAY FALSE")
    @Test(groups = {"regression"}, priority = 18)
    public void verifyCreateOrderWithStatusActiveDisplayFalse() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductStatusActiveDisplayFalseBranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response), INVALID_RESPONSE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH STATUS IN ACTIVE DISPLAY TRUE")
    @Test(groups = {"regression"}, priority = 19)
    public void verifyCreateOrderWithStatusInactiveDisplayTrue() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductStatusInactiveDisplayTrueBranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response), INVALID_RESPONSE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH ACTIVE AND IN ACTIVE PRODUCT")
    @Test(groups = {"regression"}, priority = 20)
    public void verifyCreateOrderWithActiveAndInactiveProduct() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductInactiveBranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_INACTIVE_ID, STATUS_ON_HOLD, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_INACTIVE_ID, OUT_OF_STOCK_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH,
                new String[]{STATUS_PATH}, buruHelper.constructList(new String[]{STATUS_CREATED, STATUS_DELETED}), 1, 0), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH PPO PRODUCT")
    @Test(groups = {"regression"}, priority = 21)
    public void verifyCreateOrderWithPPOProduct() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductPPOBranchMAI.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 0), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_ON_HOLD, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_ON_HOLD, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, ZERO_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_DELETED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_DELETED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_CREATION_FAILURE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER WITH PPO AND NON PPO PRODUCT")
    @Test(groups = {"regression"}, priority = 22)
    public void verifyCreateOrderWithPPOAndNonPPOProduct() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        SoftAssert softAssert = new SoftAssert();
        int count = 0;
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductPPOBranchMAI.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductNonPPOBranchMAI.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response), ORDER_CREATION_FAILURE);

        softAssert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_PPO_ID, STATUS_ON_HOLD, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_PPO_ID, OUT_OF_STOCK_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SEGMENTATION_PATH, MEDISEND_PRODUCT_PPO_ID, SEGMENTATION_PPO, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SELLING_UNIT_PATH, MEDISEND_PRODUCT_PPO_ID, SELLING_UNIT_BOX, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SKU_ID_PATH, MEDISEND_PRODUCT_PPO_ID, SKU_ID_PRODUCT_PPO, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_NON_PPO_ID, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_NON_PPO_ID, REQUESTED_QUANTITY, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SEGMENTATION_PATH, MEDISEND_PRODUCT_NON_PPO_ID, SEGMENTATION_RED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SELLING_UNIT_PATH, MEDISEND_PRODUCT_NON_PPO_ID, SELLING_UNIT_BOX, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SKU_ID_PATH, MEDISEND_PRODUCT_NON_PPO_ID, SKU_ID_PRODUCT_NON_PPO, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_MAI_ID, STATUS_CREATED, MODE_EQUALS), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertTrue(buruHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH,
                new String[]{STATUS_PATH}, buruHelper.constructList(new String[]{STATUS_CREATED, STATUS_DELETED}), 1, 0), RESULT_MATCH_FAILURE+ ++count);
        softAssert.assertAll(ORDER_DETAILS_FAILED_MESSAGE);

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
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
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Feature("UPDATE ITEM TESTS")
public class UpdateItemTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM CREATED TO REQUESTED QUANTITY LESS THAN CURRENT")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyUpdateItemCreatedToRequestedQuantityLessThanCurrent() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 1100));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM CREATED TO BELOW MIN BASKET SIZE")
    @Test (groups = { "regression" }, priority = 2)
    public void verifyUpdateItemCreatedToBelowMinBasketSize() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 1100));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_ON_HOLD, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_DRAFT, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM CREATED TO ONE NORMAL AND ONE BELOW MIN BASKET SIZE")
    @Test (groups = { "regression" }, priority = 3)
    public void verifyUpdateItemCreatedToOnenormalOnebelowminbasketsize() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);
        MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_ON_HOLD, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_WITH_MINIMUM_ID_1, STATUS_DRAFT, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM CREATED TO REQUESTED QUANTITY ABOVE AVAILABLE")
    @Test (groups = { "regression" }, priority = 4)
    public void verifyUpdateItemCreatedToRequestedQuantityAboveAvailable() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductMoreThanAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductMoreThanAvailable1BranchNoMinimum1.getInstance(MAX_REQUESTED_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 11000));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, MAX_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS));

        response = buruHelper.updateMedisendProduct(DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_MAPPING_ID_1, STATUS_ACTIVE, Integer.toString(MAX_QUANTITY), SKU_ID_PRODUCT_1);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM CREATED TO ZERO QUANTITY")
    @Test (groups = { "regression" }, priority = 5)
    public void verifyUpdateItemCreatedToQuantity0() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(OUT_OF_STOCK_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 0));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_ON_HOLD, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_ON_HOLD, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, OUT_OF_STOCK_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_DELETED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_DELETED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM CREATED WITH TWO SHIPMENTS TO ZERO TOTAL QUANTITY")
    @Test (groups = { "regression" }, priority = 6)
    public void verifyUpdateItemCreatedWithTwoShipmentsToTotalQuantity0() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(OUT_OF_STOCK_QUANTITY);
        MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(OUT_OF_STOCK_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 0));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_ON_HOLD, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_ON_HOLD, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, OUT_OF_STOCK_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_DELETED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_DELETED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM CREATED WITH TWO ITEMS TO ONE QUANTITY AS 0 AND ANOTHER QUANTITY MORE THAN ZERO")
    @Test (groups = { "regression" }, priority = 7)
    public void verifyUpdateItemCreatedWithTwoItemsToOnequantity0Onequantitymorethan0() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(OUT_OF_STOCK_QUANTITY);
        MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 1100));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, STATUS_ON_HOLD, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, OUT_OF_STOCK_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH,
                new String[] {STATUS_PATH}, buruHelper.constructList(new String[] {STATUS_CREATED, STATUS_DELETED}), 1, 0));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM CREATED WITH TWO SHIPMENTS TO ONE QUANTITY AS 0 AND ANOTHER QUANTITY MORE THAN ZERO")
    @Test (groups = { "regression" }, priority = 8)
    public void verifyUpdateItemCreatedWithTwoShipmentsToOnequantity0Onequantitymorethan0() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(OUT_OF_STOCK_QUANTITY);
        MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 1100));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_ON_HOLD, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, OUT_OF_STOCK_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_DELETED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_DELETED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CREATED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM ON HOLD WITH BELOW MINIMUM BASKET SIZE TO NORMAL")
    @Test (groups = { "regression" }, priority = 9)
    public void verifyUpdateItemOnholdWithBelowminbasketsizetonormal() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 6600));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, ABOVE_BASKET_SIZE_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM ON HOLD WITH TWO SHIPMENTS ONE NORMAL, ONE BELOW MIN BASKET SIZE TO NORMAL")
    @Test (groups = { "regression" }, priority = 10)
    public void verifyUpdateItemOnholdWithTwoShipmentsOnenormalOnebelowminbasketsizetonormal() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 8800));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CREATED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH,
                BRANCH_ACTIVE_WITH_MINIMUM_ID_1, ABOVE_BASKET_SIZE_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CREATED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CREATED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM ON HOLD WITH BELOW MINIMUM BASKET SIZE TO NORMAL")
    @Test (groups = { "regression" }, priority = 11)
    public void verifyUpdateItemConfirmed() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY UPDATE ITEM CANCELLED")
    @Test (groups = { "regression" }, priority = 12)
    public void verifyUpdateItemCancelled() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.cancelMedisendOrder(customerOrderId, shipmentId, TYPE_CANCEL_CS, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY UPDATE ITEM ABANDONED")
    @Test (groups = { "regression" }, priority = 13)
    public void verifyUpdateItemAbandoned() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.abandonMedisendOrder(customerOrderId, TYPE_CANCEL_USER, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM DELIVERED")
    @Test (groups = { "regression" }, priority = 14)
    public void verifyUpdateItemDelivered() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.deliverMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY UPDATE ITEM DELETED")
    @Test (groups = { "regression" }, priority = 15)
    public void verifyUpdateItemDeleted() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductUnavailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE ITEM COMPLETED")
    @Test (groups = { "regression" }, priority = 16)
    public void verifyUpdateItemCompleted() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.confirmMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.completeMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = buruHelper.updateMedisendItem(customerOrderId, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
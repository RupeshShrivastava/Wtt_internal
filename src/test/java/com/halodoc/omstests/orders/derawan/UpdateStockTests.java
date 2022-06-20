package com.halodoc.omstests.orders.derawan;

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
@Feature("UPDATE STOCK TESTS")
public class UpdateStockTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT PROCESSED")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyUpdateStockShipmentProcessed() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 1100));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH,
                FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK ONE ITEM SHIPMENT PROCESSED WITH TWO ITEMS")
    @Test (groups = { "regression" }, priority = 2)
    public void verifyUpdateStockOneItemShipmentProcessedWithTwoItems() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.remove(MedisendOrderItem.ProductAvailable2BranchNoMinimum1);
        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 3300));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH,
                FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                QUANTITY_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                QUANTITY_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK BOTH ITEMS SHIPMENT PROCESSED WITH TWO ITEMS")
    @Test (groups = { "regression" }, priority = 3)
    public void verifyUpdateStockBothItemsShipmentProcessedWithTwoItems() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);
        MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH,
                FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                QUANTITY_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                QUANTITY_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK ONE SHIPMENT PROCESSED WITH TWO SHIPMENTS")
    @Test (groups = { "regression" }, priority = 4)
    public void verifyUpdateStockOneShipmentProcessedWithTwoShipments() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId1 = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);
        String shipmentId2 = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_2);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.remove(MedisendOrderItem.ProductAvailable1BranchNoMinimum2);
        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId1, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 3300));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentId1,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, shipmentId1, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentId1, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentId1, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentId2,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentId2, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentId2, REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK BOTH SHIPMENTS PROCESSED WITH TWO SHIPMENTS")
    @Test (groups = { "regression" }, priority = 5)
    public void verifyUpdateStockBothShipmentsProcessedWithTwoShipments() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId1 = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);
        String shipmentId2 = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_2);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY));

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId1, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(UPDATED_REQUESTED_QUANTITY));

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId2, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentId1,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, shipmentId1, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentId1, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentId1, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentId2,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, shipmentId2, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentId2, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentId2, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT WITH QUANTITY MORE THAN CURRENT")
    @Test (groups = { "regression" }, priority = 6)
    public void verifyUpdateStockShipmentWithQuantitymorethancurrent() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK ONE ITEM SHIPMENT WITH TWO ITEMS AND EDITED QUANTITY MORE THAN CURRENT")
    @Test (groups = { "regression" }, priority = 7)
    public void verifyUpdateStockOneItemShipmentWithTwoItemsandEditedQuantitymorethancurrent() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.remove(MedisendOrderItem.ProductAvailable2BranchNoMinimum1);
        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK BOTH ITMES SHIPMENT WITH TWO ITEMS AND EDITED QUANTITY MORE THAN CURRENT")
    @Test (groups = { "regression" }, priority = 8)
    public void verifyUpdateStockBothItemsShipmentWithTwoItemsandEditedQuantitymorethancurrent() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY);
        MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(ABOVE_BASKET_SIZE_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK BOTH ITEMS SHIPMENT WITH TWO ITEMS AND EDITED QUANTITY MORE THAN CURRENT AND LESS THAN CURRENT")
    @Test (groups = { "regression" }, priority = 9)
    public void verifyUpdateStockBothItemsShipmentWithTwoItemsandEditedQuantitymorethancurrentandlessthancurrent() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY);
        MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT WITH QUANTITY EQUALS TO CURRENT")
    @Test (groups = { "regression" }, priority = 10)
    public void verifyUpdateStockShipmentWithQuantityequalstocurrent() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueIsNull(response, SHIPMENTS_PATH + "[0]." + ATTRIBUTES_PATH + "." +
                        FULFILLMENT_TYPE_PATH));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT WITH TWO ITEMS QUANTITY EQUALS TO CURRENT")
    @Test (groups = { "regression" }, priority = 11)
    public void verifyUpdateStockShipmentWithTwoItemsQuantityequalstocurrent() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 4400));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueIsNull(response, SHIPMENTS_PATH + "[0]." + ATTRIBUTES_PATH + "." +
                FULFILLMENT_TYPE_PATH));
        Assert.assertTrue(buruHelper.validateResponseValueIsNull(response, SHIPMENTS_PATH + "[1]." + ATTRIBUTES_PATH + "." +
                FULFILLMENT_TYPE_PATH));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT WITH QUANTITY ZERO")
    @Test (groups = { "regression" }, priority = 12)
    public void verifyUpdateStockShipmentWithQuantityZero() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ZERO_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 0));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CANCEL, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, ZERO_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH,
                FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                ZERO_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK ONE ITEM SHIPMENT WITH TWO ITEMS AND EDITED QUANTITY ZERO")
    @Test (groups = { "regression" }, priority = 13)
    public void verifyUpdateStockOneItemShipmentWithTwoItemsandEditedQuantityZero() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.remove(MedisendOrderItem.ProductAvailable2BranchNoMinimum1);
        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ZERO_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, ZERO_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH,
                FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                STATUS_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                QUANTITY_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, ZERO_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                STATUS_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                QUANTITY_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK TWO ITEMS SHIPMENT WITH EDITED QUANTITY ZERO AND GREATER THAN ZERO")
    @Test (groups = { "regression" }, priority = 14)
    public void verifyUpdateStockTwoItemShipmentWithEditedQuantityZeroandGreaterthanZero() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ZERO_QUANTITY);
        MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 1100));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, ZERO_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH,
                FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                STATUS_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                QUANTITY_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, ZERO_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                STATUS_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                QUANTITY_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK TWO ITEM SHIPMENT WITH TOTAL QUANTITY ZERO")
    @Test (groups = { "regression" }, priority = 15)
    public void verifyUpdateStockTwoItemShipmentWithTotalQuantityZero() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ZERO_QUANTITY);
        MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(ZERO_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 0));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CANCEL, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, ZERO_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, ZERO_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH,
                FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                STATUS_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                QUANTITY_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, ZERO_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                STATUS_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                QUANTITY_PATH, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, ZERO_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT WITH TWO SHIPMENTS ONE IS ZERO AND ANOTHER ONE GREATER THAN ZERO")
    @Test (groups = { "regression" }, priority = 16)
    public void verifyUpdateStockShipmentWithTwoShipmentsOneZeroOneGreaterthanZero() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentIdZero = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);
        String shipmentIdNonZero = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_2);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ZERO_QUANTITY));

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentIdZero, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(UPDATED_REQUESTED_QUANTITY));

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentIdNonZero, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 1100));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                ZERO_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2,
                STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2,
                UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentIdZero,
                STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, shipmentIdZero, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentIdZero, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentIdZero, ZERO_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentIdNonZero,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, shipmentIdNonZero, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentIdNonZero, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentIdNonZero, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT WITH TWO SHIPMENTS TOTAL ZERO")
    @Test (groups = { "regression" }, priority = 17)
    public void verifyUpdateStockShipmentWithTwoShipmentsTotalZero() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentIdZero = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);
        String shipmentIdNonZero = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_2);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ZERO_QUANTITY));

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentIdZero, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(ZERO_QUANTITY));

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentIdNonZero, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 0));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CANCEL, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, ZERO_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH,
                FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                ZERO_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT PARTIAL")
    @Test (groups = { "regression" }, priority = 18)
    public void verifyUpdateStockShipmentPartial() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(MAX_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT PENDING")
    @Test (groups = { "regression" }, priority = 19)
    public void verifyUpdateStockShipmentPending() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(MAX_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY * 2);

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String childShipmentId = buruHelper.getShipmentIdByParentId(response, shipmentId);

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, childShipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT CREATED")
    @Test (groups = { "regression" }, priority = 20)
    public void verifyUpdateStockShipmentCreated() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT ABANDONED")
    @Test (groups = { "regression" }, priority = 21)
    public void verifyUpdateStockShipmentAbandoned() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.abandonMedisendOrder(customerOrderId, TYPE_CANCEL_USER, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT CONFIRMED")
    @Test (groups = { "regression" }, priority = 22)
    public void verifyUpdateStockShipmentConfirmed() {
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

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 1100));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH,
                FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT TRANSIT")
    @Test (groups = { "regression" }, priority = 23)
    public void verifyUpdateStockShipmentTransit() {
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

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT CANCEL")
    @Test (groups = { "regression" }, priority = 24)
    public void verifyUpdateStockShipmentCancel() {
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

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT DELIVERED")
    @Test (groups = { "regression" }, priority = 25)
    public void verifyUpdateStockShipmentDelivered() {
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

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE STOCK SHIPMENT COMPLETED")
    @Test (groups = { "regression" }, priority = 26)
    public void verifyUpdateStockShipmentCompleted() {
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

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
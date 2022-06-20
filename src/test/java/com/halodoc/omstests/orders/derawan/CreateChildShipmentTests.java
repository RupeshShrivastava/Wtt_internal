package com.halodoc.omstests.orders.derawan;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import java.io.IOException;
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
@Feature("CREATE CHILD SHIPMENT TESTS")
public class CreateChildShipmentTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT WITHOUT PURCHASE LETTER")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyCreateChildShipmentWithoutPurchaseLetter() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String childShipmentId = buruHelper.getShipmentIdByParentId(response, shipmentId);

        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, UPDATED_REQUESTED_QUANTITY * 2, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentId, STATUS_PROCESSED,
                MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, shipmentId, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentId, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentId, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, childShipmentId,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, childShipmentId, FULFILLMENT_TYPE_PENDING, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, childShipmentId, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, childShipmentId, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));

        response = buruHelper.getDocsMedisendOrder(customerOrderId, childShipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateArraySize(response, "", EQUAL_KEY, 0));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT TWICE")
    @Test (groups = { "regression" }, priority = 2)
    public void verifyCreateChildShipmentTwice() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT WITH PURCHASE LETTER")
    @Test (groups = { "regression" }, priority = 3)
    public void verifyCreateChildShipmentWithPurchaseLetter() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PURCHASELETTER, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String childShipmentId = buruHelper.getShipmentIdByParentId(response, shipmentId);

        response = buruHelper.getDocsMedisendOrder(customerOrderId, childShipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateArraySize(response, "", EQUAL_KEY, 1));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT WITHOUT PURCHASE LETTER FROM TWO SHIPMENTS, ONE PURCHASE WITH LETTER AND ONE PURCHASE WITHOUT LETTER")
    @Test (groups = { "regression" }, priority = 4)
    public void verifyCreateChildShipmentWithoutPurchaseLetterFromTwoShipmentsOnewithpurchaseletteronewithoutpurchaseletter() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(ABOVE_BASKET_SIZE_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentIdWithPurchaseLetter = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_2);
        String shipmentIdWithoutPurchaseLetter = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentIdWithPurchaseLetter, DOCUMENT_TYPE_PURCHASELETTER, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.remove(MedisendOrderItem.ProductAvailable1BranchNoMinimum2);
        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentIdWithoutPurchaseLetter, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String childShipmentId = buruHelper.getShipmentIdByParentId(response, shipmentIdWithoutPurchaseLetter);

        response = buruHelper.getDocsMedisendOrder(customerOrderId, childShipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateArraySize(response, "", EQUAL_KEY, 0));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT FROM TWO SHIPMENTS WITH DIFFERENT QUANTITY")
    @Test (groups = { "regression" }, priority = 5)
    public void verifyCreateChildShipmentFromTwoShipmentsWithDifferentQuantity() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ABOVE_BASKET_SIZE_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(ABOVE_BASKET_SIZE_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId1 = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);
        String shipmentId2 = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_2);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId1, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(UPDATED_REQUESTED_QUANTITY));

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId2, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_2, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String childShipmentId1 = buruHelper.getShipmentIdByParentId(response, shipmentId1);
        String childShipmentId2 = buruHelper.getShipmentIdByParentId(response, shipmentId2);

        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 6600));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                REQUESTED_QUANTITY * 2, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2,
                UPDATED_REQUESTED_QUANTITY * 2, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentId1, STATUS_PROCESSED,
                MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, shipmentId1, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentId1, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentId1, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentId2,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, shipmentId2, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentId2, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentId2, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, childShipmentId1,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, childShipmentId1, FULFILLMENT_TYPE_PENDING, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, childShipmentId1, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, childShipmentId1, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, childShipmentId2,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, childShipmentId2, FULFILLMENT_TYPE_PENDING, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, childShipmentId2, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, childShipmentId2, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT WITH TWO ITEMS QUANTITY ZERO AND FULL")
    @Test (groups = { "regression" }, priority = 6)
    public void verifyCreateChildShipmentWithTwoItemsQuantityZeroandFull() {
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

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(ZERO_QUANTITY));

        response = derawanHelper.updateStockMedisendShipment(customerOrderId, shipmentId, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String childShipmentId = buruHelper.getShipmentIdByParentId(response, shipmentId);

        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 4400));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentId, STATUS_PROCESSED,
                MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, shipmentId, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueIdExpectedArray(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[index]." + STATUS_PATH, shipmentId,
                buruHelper.constructList(new String[] {STATUS_PROCESSED, STATUS_CANCEL})));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueIdExpectedArray(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[index]." + QUANTITY_PATH, shipmentId,
                buruHelper.constructList(new Integer[] {REQUESTED_QUANTITY, ZERO_QUANTITY})));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, childShipmentId,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, childShipmentId, FULFILLMENT_TYPE_PENDING, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, childShipmentId, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, childShipmentId, REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT WITH TWO ITEMS QUANTITY ZERO AND HALF")
    @Test (groups = { "regression" }, priority = 7)
    public void verifyCreateChildShipmentWithTwoItemsQuantityZeroandHalf() {
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

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY);
        MedisendOrderItem.ProductAvailable2BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String childShipmentId = buruHelper.getShipmentIdByParentId(response, shipmentId);

        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 4400));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentId, STATUS_PROCESSED,
                MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, shipmentId, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueIdExpectedArray(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[index]." + STATUS_PATH, shipmentId,
                buruHelper.constructList(new String[] {STATUS_PROCESSED, STATUS_CANCEL})));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueIdExpectedArray(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[index]." + QUANTITY_PATH, shipmentId,
                buruHelper.constructList(new Integer[] {UPDATED_REQUESTED_QUANTITY, ZERO_QUANTITY})));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, childShipmentId,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, childShipmentId, FULFILLMENT_TYPE_PENDING, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueIdExpectedArray(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[index]." + STATUS_PATH, childShipmentId,
                buruHelper.constructList(new String[] {STATUS_PROCESSED, STATUS_PROCESSED})));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueIdExpectedArray(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[index]." + QUANTITY_PATH, childShipmentId,
                buruHelper.constructList(new Integer[] {REQUESTED_QUANTITY, UPDATED_REQUESTED_QUANTITY})));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT CREATED")
    @Test (groups = { "regression" }, priority = 8)
    public void verifyCreateChildShipmentCreated() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT DRAFT")
    @Test (groups = { "regression" }, priority = 9)
    public void verifyCreateChildShipmentDraft() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT ABANDONED")
    @Test (groups = { "regression" }, priority = 10)
    public void verifyCreateChildShipmentAbandoned() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.abandonMedisendOrder(customerOrderId, TYPE_CANCEL_USER, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT CANCELLED")
    @Test (groups = { "regression" }, priority = 11)
    public void verifyCreateChildShipmentCancelled() {
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

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT CONFIRMED")
    @Test (groups = { "regression" }, priority = 12)
    public void verifyCreateChildShipmentConfirmed() {
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

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT CONFIRMED PARTIAL")
    @Test (groups = { "regression" }, priority = 13)
    public void verifyCreateChildShipmentConfirmedPartial() {
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

        response = derawanHelper.confirmMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String childShipmentId = buruHelper.getShipmentIdByParentId(response, shipmentId);

        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, UPDATED_REQUESTED_QUANTITY * 2, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentId, STATUS_CONFIRMED,
                MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, shipmentId, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentId, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentId, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, childShipmentId,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, childShipmentId, FULFILLMENT_TYPE_PENDING, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, childShipmentId, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, childShipmentId, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT TRANSIT")
    @Test (groups = { "regression" }, priority = 14)
    public void verifyCreateChildShipmentTransit() {
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

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT TRANSIT PARTIAL")
    @Test (groups = { "regression" }, priority = 15)
    public void verifyCreateChildShipmentTransitPartial() {
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

        response = derawanHelper.confirmMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String childShipmentId = buruHelper.getShipmentIdByParentId(response, shipmentId);

        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, UPDATED_REQUESTED_QUANTITY * 2, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentId, STATUS_TRANSIT,
                MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, shipmentId, FULFILLMENT_TYPE_PARTIAL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentId, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentId, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, childShipmentId,
                STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                ATTRIBUTES_PATH + "." + FULFILLMENT_TYPE_PATH, childShipmentId, FULFILLMENT_TYPE_PENDING, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, childShipmentId, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, childShipmentId, UPDATED_REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT COMPLETED")
    @Test (groups = { "regression" }, priority = 16)
    public void verifyCreateChildShipmentCompleted() {
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

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE CHILD SHIPMENT COMPLETED PARTIAL")
    @Test (groups = { "regression" }, priority = 17)
    public void verifyCreateChildShipmentCompletedPartial() {
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

        response = derawanHelper.confirmMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.completeMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(UPDATED_REQUESTED_QUANTITY);

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
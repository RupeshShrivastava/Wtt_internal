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
@Feature("CONFIRMED ORDER TESTS")
public class ConfirmOrderTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CONFIRM ORDER CREATED")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyConfirmOrderCreated() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, REQUESTED_QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_PROCESSED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CONFIRM ORDER ON HOLD SHIPMENT STATUS DRAFT")
    @Test (groups = { "regression" }, priority = 2)
    public void verifyConfirmOrderOnholdShipmentStatusDraft() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CONFIRM ORDER ON HOLD SHIPMENT STATUS DELETED")
    @Test (groups = { "regression" }, priority = 3)
    public void verifyConfirmOrderOnholdShipmentStatusDeleted() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CONFIRM ORDER ON HOLD AND SHIPMENT STATUS DRAFT AND CREATED")
    @Test (groups = { "regression" }, priority = 4)
    public void verifyConfirmOrderOnholdShipmentStatusDraftandcreated() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CONFIRM ORDER ON HOLD AND SHIPMENT STATUS DELETED AND CREATED")
    @Test (groups = { "regression" }, priority = 5)
    public void verifyConfirmOrderOnholdShipmentStatusDeletedandcreated() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_ON_HOLD, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, OUT_OF_STOCK_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, REQUESTED_QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, REQUESTED_QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_DELETED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_DELETED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_PROCESSED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY CONFIRM ORDER CANCELLED")
    @Test (groups = { "regression" }, priority = 6)
    public void verifyConfirmOrderCancelled() {
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

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CONFIRM ORDER ABANDONED")
    @Test (groups = { "regression" }, priority = 7)
    public void verifyConfirmOrderAbandoned() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.abandonMedisendOrder(customerOrderId, TYPE_CANCEL_USER, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CONFIRM ORDER DELIVERED")
    @Test (groups = { "regression" }, priority = 8)
    public void verifyConfirmOrderDelivered() {
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

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY CONFIRM ORDER ON HOLD")
    @Test (groups = { "regression" }, priority = 9)
    public void verifyConfirmOrderOnhold() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY CONFIRM ORDER CONFIRMED")
    @Test (groups = { "regression" }, priority = 10)
    public void verifyConfirmOrderConfirmed() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY CONFIRM ORDER COMPLETED")
    @Test (groups = { "regression" }, priority = 11)
    public void verifyConfirmOrderCompleted() {
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

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY CONFIRM ORDER WITH REQUESTED MORE THAN AVAILABLE")
    @Test (groups = { "regression" }, priority = 12)
    public void verifyConfirmOrderWithRequestedmorethanavailable() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductMoreThanAvailable1BranchNoMinimum1.getInstance(MAX_REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 11000));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, MAX_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, REQUESTED_QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, MAX_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_PROCESSED, MODE_EQUALS));

        response = buruHelper.updateMedisendProduct(DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_MAPPING_ID_1, STATUS_ACTIVE, Integer.toString(MAX_QUANTITY), SKU_ID_PRODUCT_1);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CONFIRM ORDER MAI")
    @Test (groups = { "regression" }, priority = 13)
    public void verifyConfirmOrderMAI() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductNonPPOBranchMAI.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, REQUESTED_QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_PROCESSED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CONFIRM ORDER MBS")
    @Test (groups = { "regression" }, priority = 14)
    public void verifyConfirmOrderMBS() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailableBranchMBS.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_MBS_ID);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        Assert.assertTrue(checkUntilAttributeExist(customerOrderId, shipmentId, ATTRIBUTES_PATH + "." + ORDER_CREATION_FAILURE_PATH));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 10197));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, REQUESTED_QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, ATTRIBUTES_PATH + "." + SEGMENTATION_PATH,
                SEGMENTATION_CONSUMER_GOODS, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, ATTRIBUTES_PATH + "." + SKU_ID_PATH,
                SKU_ID_MBS, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, ATTRIBUTES_PATH + "." + DISTRIBUTOR_CODE_PATH,
                DISTRIBUTOR_CODE_MBS, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, ATTRIBUTES_PATH + "." + ORDER_CREATION_FAILURE_PATH,
                FALSE, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, ATTRIBUTES_PATH + "." + PHARMACY_REFERENCE_ID_PATH,
                PHARMACY_REFERENCE_MBS_ID, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_PROCESSED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CONFIRM ORDER MAI AND MBS")
    @Test (groups = { "regression" }, priority = 15)
    public void verifyConfirmOrderMAIandMBS() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductNonPPOBranchMAI.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailableBranchMBS.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 12397));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, REQUESTED_QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SEGMENTATION_PATH, MEDISEND_PRODUCT_NON_PPO_ID, SEGMENTATION_RED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SEGMENTATION_PATH, MEDISEND_PRODUCT_MBS_ID, SEGMENTATION_CONSUMER_GOODS, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SELLING_UNIT_PATH, MEDISEND_PRODUCT_NON_PPO_ID, SELLING_UNIT_BOX, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SKU_ID_PATH, MEDISEND_PRODUCT_NON_PPO_ID, SKU_ID_PRODUCT_NON_PPO, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SKU_ID_PATH, MEDISEND_PRODUCT_MBS_ID, SKU_ID_MBS, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                ATTRIBUTES_PATH + "." + DISTRIBUTOR_CODE_PATH, BRANCH_MBS_ID, DISTRIBUTOR_CODE_MBS, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                ATTRIBUTES_PATH + "." + PHARMACY_REFERENCE_ID_PATH, BRANCH_MBS_ID, PHARMACY_REFERENCE_MBS_ID, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_PROCESSED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CONFIRM ORDER MAI SUCCESS AND MBS FAIL")
    @Test (groups = { "regression" }, priority = 16)
    public void verifyConfirmOrderMAISuccessMBSFail() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductNonPPOBranchMAI.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductInvalidBranchMBS.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_MBS_ID);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        Assert.assertTrue(checkUntilAttributeExist(customerOrderId, shipmentId, ATTRIBUTES_PATH + "." + ORDER_CREATION_FAILURE_PATH));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 13200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, REQUESTED_QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SEGMENTATION_PATH, MEDISEND_PRODUCT_NON_PPO_ID, SEGMENTATION_RED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SELLING_UNIT_PATH, MEDISEND_PRODUCT_NON_PPO_ID, SELLING_UNIT_BOX, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SELLING_UNIT_PATH, MEDISEND_INVALID_PRODUCT_MBS_ID, SELLING_UNIT_BOX, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SKU_ID_PATH, MEDISEND_PRODUCT_NON_PPO_ID, SKU_ID_PRODUCT_NON_PPO, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SKU_ID_PATH, MEDISEND_INVALID_PRODUCT_MBS_ID, SKU_ID_INVALID_MBS, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                ATTRIBUTES_PATH + "." + DISTRIBUTOR_CODE_PATH, BRANCH_MBS_ID, DISTRIBUTOR_CODE_MBS, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                ATTRIBUTES_PATH + "." + ORDER_CREATION_FAILURE_PATH, BRANCH_MBS_ID, TRUE, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                ATTRIBUTES_PATH + "." + PHARMACY_REFERENCE_ID_PATH, BRANCH_MBS_ID, PHARMACY_REFERENCE_MBS_ID, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_PROCESSED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY CONFIRM ORDER MBS ONE NORMAL ONE DELETED")
    @Test (groups = { "regression" }, priority = 17)
    public void verifyConfirmOrderMBSOneNormalOneDeleted() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailableBranchMBS.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailableBranchMBS.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 10197));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH, MEDISEND_PRODUCT_MBS_ID,
                STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, STATUS_PATH, MEDISEND_PRODUCT_UNAVAILABLE_MBS_ID,
                STATUS_ON_HOLD, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH, MEDISEND_PRODUCT_MBS_ID,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH, QUANTITY_PATH,
                MEDISEND_PRODUCT_UNAVAILABLE_MBS_ID, OUT_OF_STOCK_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, REQUESTED_QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, ATTRIBUTES_PATH + "." + SEGMENTATION_PATH,
                SEGMENTATION_CONSUMER_GOODS, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SKU_ID_PATH, MEDISEND_PRODUCT_MBS_ID, SKU_ID_MBS, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, LISTING_ID_PATH,
                ATTRIBUTES_PATH + "." + SKU_ID_PATH, MEDISEND_PRODUCT_UNAVAILABLE_MBS_ID, SKU_ID_UNAVAILABLE_MBS, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                ATTRIBUTES_PATH + "." + DISTRIBUTOR_CODE_PATH, BRANCH_MBS_ID, DISTRIBUTOR_CODE_MBS, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                ATTRIBUTES_PATH + "." + PHARMACY_REFERENCE_ID_PATH, BRANCH_MBS_ID, PHARMACY_REFERENCE_MBS_ID, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                STATUS_PATH, MEDISEND_PRODUCT_MBS_ID, STATUS_PROCESSED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, LISTING_ID_PATH,
                STATUS_PATH, MEDISEND_PRODUCT_UNAVAILABLE_MBS_ID, STATUS_DELETED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
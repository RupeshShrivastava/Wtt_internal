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
@Feature("TRANSIT SHIPMENT TESTS")
public class TransitShipmentTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY TRANSIT SHIPMENT CONFIRM")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyTransitShipmentConfirm() {
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

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY TRANSIT SHIPMENT WITH RESULT SHIPMENT TRANSIT AND CONFIRM")
    @Test (groups = { "regression" }, priority = 2)
    public void verifyTransitShipmentWithResultShipmentTransitandConfirm() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentIdTransit = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);
        String shipmentIdConfirm = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_2);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.confirmMedisendShipment(customerOrderId, shipmentIdTransit);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.confirmMedisendShipment(customerOrderId, shipmentIdConfirm);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentIdTransit);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 4400));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2,
                STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentIdConfirm,
                STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentIdConfirm, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentIdConfirm, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentIdTransit,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentIdTransit, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentIdTransit, REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY TRANSIT SHIPMENT WITH RESULT SHIPMENT TRANSIT AND CANCEL")
    @Test (groups = { "regression" }, priority = 3)
    public void verifyTransitShipmentWithResultShipmentTransitandCancel() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentIdTransit = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);
        String shipmentIdCancel = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_2);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.confirmMedisendShipment(customerOrderId, shipmentIdTransit);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.cancelMedisendOrder(customerOrderId, shipmentIdCancel, TYPE_CANCEL_CS, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentIdTransit);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2,
                STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2,
                ZERO_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentIdCancel,
                STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentIdCancel, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentIdCancel, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH, STATUS_PATH, shipmentIdTransit,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, shipmentIdTransit, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, EXTERNAL_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + QUANTITY_PATH, shipmentIdTransit, REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY TRANSIT SHIPMENT CREATED")
    @Test (groups = { "regression" }, priority = 4)
    public void verifyTransitShipmentCreated() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY TRANSIT SHIPMENT CANCELLED")
    @Test (groups = { "regression" }, priority = 5)
    public void verifyTransitShipmentCancelled() {
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

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY TRANSIT SHIPMENT PROCESSED")
    @Test (groups = { "regression" }, priority = 6)
    public void verifyTransitShipmentProcessed() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY TRANSIT SHIPMENT TRANSIT")
    @Test (groups = { "regression" }, priority = 7)
    public void verifyTransitShipmentTransit() {
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

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY TRANSIT SHIPMENT DELIVERED")
    @Test (groups = { "regression" }, priority = 8)
    public void verifyTransitShipmentDelivered() {
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

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY TRANSIT SHIPMENT COMPLETED")
    @Test (groups = { "regression" }, priority = 9)
    public void verifyTransitShipmentCompleted() {
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

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
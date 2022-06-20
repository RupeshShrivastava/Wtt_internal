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
import com.halodoc.oms.orders.utilities.constants.Constants;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Feature("DELIVERY SHIPMENT TESTS")
public class DeliverShipmentTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY DELIVERY SHIPMENT CONFIRMED")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyDeliverShipmentConfirmed() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(Constants.MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.deliverMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_DELIVERED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_DELIVERED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_DELIVERED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_DELIVERED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY DELIVERY SHIPMENT CONFIRMED FROM TWO SHIPMENTS RESULT ONE DELIVERED ONE CREATED")
    @Test (groups = { "regression" }, priority = 2, enabled = false)
    public void verifyDeliverShipmentConfirmedFromTwoShipmentsResultOnedeliveredonecreated() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(Constants.MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(Constants.MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.deliverMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 4400));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_DELIVERED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_DELIVERED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_DELIVERED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CONFIRMED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY DELIVERY SHIPMENT FROM TWO SHIPMENTS RESULT ONE DELIVERED ONE CANCELLED")
    @Test (groups = { "regression" }, priority = 3, enabled = false)
    public void verifyDeliverShipmentFromTwoShipmentsResultOnedeliveredonecancelled() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(Constants.MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        medisendOrderItemsArray.add(Constants.MedisendOrderItem.ProductAvailable1BranchNoMinimum2.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId1 = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);
        String shipmentId2 = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_2);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.deliverMedisendOrder(customerOrderId, shipmentId1);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.cancelMedisendOrder(customerOrderId, shipmentId2, TYPE_CANCEL_CS, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 4400));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_DELIVERED, response));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_DELIVERED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, ITEMS_PATH, BRANCH_ID_PATH, QUANTITY_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_DELIVERED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH, STATUS_PATH,
                BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CANCEL, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_1, STATUS_DELIVERED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateMedisendOrderByUniqueId(response, SHIPMENTS_PATH, BRANCH_ID_PATH,
                SHIPMENT_ITEMS_PATH + "[0]." + STATUS_PATH, BRANCH_ACTIVE_NO_MINIMUM_ID_2, STATUS_CANCEL, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY DELIVERY SHIPMENT REQUEST CANCEL")
    @Test (groups = { "regression" }, priority = 4)
    public void verifyDeliverShipmentRequestCancel() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(Constants.MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.requestCancel(customerOrderId, shipmentId, TYPE_CANCEL_USER, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.deliverMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_DELIVERED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_DELIVERED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_DELIVERED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_DELIVERED, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY DELIVERY SHIPMENT CREATED")
    @Test (groups = { "regression" }, priority = 5)
    public void verifyDeliverShipmentCreated() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(Constants.MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = derawanHelper.deliverMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY DELIVERY SHIPMENT CANCELLED")
    @Test (groups = { "regression" }, priority = 6)
    public void verifyDeliverShipmentCancelled() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(Constants.MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.cancelMedisendOrder(customerOrderId, shipmentId, TYPE_CANCEL_CS, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.deliverMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY DELIVERY SHIPMENT DELIVERED")
    @Test (groups = { "regression" }, priority = 7)
    public void verifyDeliverShipmentDelivered() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(Constants.MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.deliverMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.deliverMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
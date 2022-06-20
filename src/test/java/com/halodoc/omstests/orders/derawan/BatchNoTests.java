package com.halodoc.omstests.orders.derawan;

import com.halodoc.omstests.orders.OrdersBaseTest;
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
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Feature("BATCH NO TESTS")
public class BatchNoTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY ADD BATCH NO")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyAddBatchNo() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        long expiryDate = derawanHelper.getEpochTime(CURRENT_DATE_KEY, 0, null);
        String batchNo = CREATE_BATCH_NO + expiryDate;

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
         String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.confirmMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.addUpdateBatchNo(customerOrderId, shipmentId, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, batchNo, expiryDate);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, ATTRIBUTES_PATH + "." + BATCH_NO_PATH, batchNo,
                MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, ATTRIBUTES_PATH + "." + EXPIRY_DATE_PATH, expiryDate,
                MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE BATCH NO")
    @Test (groups = { "regression" }, priority = 2)
    public void verifyUpdateBatchNo() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        long expiryDate = derawanHelper.getEpochTime(CURRENT_DATE_KEY, 0, null);
        String batchNo = CREATE_BATCH_NO + expiryDate;

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.confirmMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.addUpdateBatchNo(customerOrderId, shipmentId, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, batchNo, expiryDate);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        long updatedExpiryDate = derawanHelper.getEpochTime(CURRENT_DATE_KEY, 0, null);
        String updatedBatchNo = CREATE_BATCH_NO + updatedExpiryDate;

        response = derawanHelper.addUpdateBatchNo(customerOrderId, shipmentId, MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, updatedBatchNo, updatedExpiryDate);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(buruHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, ATTRIBUTES_PATH + "." + BATCH_NO_PATH, updatedBatchNo,
                MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, ITEMS_PATH, ATTRIBUTES_PATH + "." + EXPIRY_DATE_PATH, updatedExpiryDate,
                MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
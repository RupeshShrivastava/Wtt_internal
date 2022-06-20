package com.halodoc.omstests.orders.derawan;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentTaxTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPLOAD TAX WITH SHIPMENT CREATED")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyUploadTaxWithShipmentCreated() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPLOAD TAX WITH SHIPMENT ABANDONED")
    @Test (groups = { "regression" }, priority = 2)
    public void verifyUploadTaxWithShipmentAbandoned() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.abandonMedisendOrder(customerOrderId, TYPE_CANCEL_USER, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPLOAD TAX WITH SHIPMENT DELETED")
    @Test (groups = { "regression" }, priority = 3)
    public void verifyUploadTaxWithShipmentDeleted() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPLOAD TAX WITH SHIPMENT PROCESSED")
    @Test (groups = { "regression" }, priority = 4)
    public void verifyUploadTaxWithShipmentProcessed() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPLOAD TAX WITH SHIPMENT CANCELLED")
    @Test (groups = { "regression" }, priority = 5)
    public void verifyUploadTaxWithShipmentCancelled() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPLOAD TAX WITH SHIPMENT CONFIRMED")
    @Test (groups = { "regression" }, priority = 6)
    public void verifyUploadTaxWithShipmentConfirmed() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("UPLOAD TAX WITH SHIPMENT IN TRANSIT AND INVOICE EXISTS")
    @Test (groups = { "regression" }, priority = 7)
    public void verifyUploadTaxWithShipmentTransitAndInvoiceExist() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_TAX}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPLOAD TAX WITH SHIPMENT TRANSIT AND INVOICE DOES NOT EXIST")
    @Test (groups = { "regression" }, priority = 8)
    public void verifyUploadTaxWithShipmentTransitAndInvoiceNotExist() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("UPLOAD TAX WITH PDF FORMAT")
    @Test (groups = { "regression" }, priority = 9)
    public void verifyUploadTaxWithPDFFormat() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PDF, DOCUMENT_TYPE_TAX, FILE_NAME_PDF);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_TAX}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("UPLOAD TAX WITH JPG FORMAT")
    @Test (groups = { "regression" }, priority = 10)
    public void verifyUploadTaxWithJPGFormat() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_JPG, DOCUMENT_TYPE_TAX, FILE_NAME_JPG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_TAX}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("UPLOAD TAX WITH JPEG FORMAT")
    @Test (groups = { "regression" }, priority = 11)
    public void verifyUploadTaxWithJPEGFormat() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_JPEG, DOCUMENT_TYPE_TAX, FILE_NAME_JPEG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_TAX}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("UPLOAD TAX WITH 3 DOCS")
    @Test (groups = { "regression" }, priority = 12)
    public void verifyUploadTaxWith3Docs() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArraySize(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH, EQUAL_KEY, 4));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPLOAD TAX WITH MORE THAN 3 DOCS")
    @Test (groups = { "regression" }, priority = 13)
    public void verifyUploadTaxWithMoreThan3Docs() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("UPLOAD TAX WITH 5MB DOCS")
    @Test (groups = { "regression" }, priority = 14)
    public void verifyUploadTaxWith5MBDocs() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_5MB);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_TAX}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPLOAD TAX WITH MORE THAN 5MB DOCS")
    @Test (groups = { "regression" }, priority = 15)
    public void verifyUploadTaxWithMoreThan5MBDocs() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.transitMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_TAX, FILE_NAME_MORETHAN5MB);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
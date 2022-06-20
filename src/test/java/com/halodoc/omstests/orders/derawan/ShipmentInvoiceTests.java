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
@Feature("SHIPMENT INVOICE TESTS")
public class ShipmentInvoiceTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD INVOICE WITH SHIPMENT CREATED")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyUploadInvoiceWithShipmentCreated() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD INVOICE WITH SHIPMENT ABANDONED")
    @Test (groups = { "regression" }, priority = 2)
    public void verifyUploadInvoiceWithShipmentAbandoned() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.abandonMedisendOrder(customerOrderId, TYPE_CANCEL_USER, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD INVOICE WITH SHIPMENT DRAFT")
    @Test (groups = { "regression" }, priority = 3)
    public void verifyUploadInvoiceWithShipmentDraft() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_WITH_MINIMUM_ID_1);

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD INVOICE WITH SHIPMENT DELETED")
    @Test (groups = { "regression" }, priority = 4)
    public void verifyUploadInvoiceWithShipmentDeleted() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD INVOICE WITH SHIPMENT PROCESSED")
    @Test (groups = { "regression" }, priority = 5)
    public void verifyUploadInvoiceWithShipmentProcessed() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD INVOICE WITH SHIPMENT CANCELLED")
    @Test (groups = { "regression" }, priority = 6)
    public void verifyUploadInvoiceWithShipmentCancelled() throws IOException {
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

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD INVOICE WITH SHIPMENT CONFIRMED")
    @Test (groups = { "regression" }, priority = 7)
    public void verifyUploadInvoiceWithShipmentConfirmed() throws IOException {
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

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD INVOICE WITH SHIPMENT TRANSIT")
    @Test (groups = { "regression" }, priority = 8)
    public void verifyUploadInvoiceWithShipmentTransit() throws IOException {
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

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH, DOCUMENT_TYPE_PATH,
               DOCUMENT_TYPE_INVOICE, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_UNPAID, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD INVOICE WITH SHIPMENT COMPLETED")
    @Test (groups = { "regression" }, priority = 9)
    public void verifyUploadInvoiceWithShipmentCompleted() throws IOException {
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

        response = derawanHelper.completeMedisendShipment(customerOrderId, shipmentId);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_COMPLETED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_COMPLETED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_COMPLETED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH, DOCUMENT_TYPE_PATH,
                DOCUMENT_TYPE_INVOICE, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_COMPLETED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_UNPAID, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPDATE INVOICE")
    @Test (groups = { "regression" }, priority = 10)
    public void verifyUpdateInvoice() throws IOException {
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

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PNG, DOCUMENT_TYPE_INVOICE, FILE_NAME_PNG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH, DOCUMENT_TYPE_PATH,
                DOCUMENT_TYPE_INVOICE, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_UNPAID, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY UPDATE INVOICE WITH PDF FORMAT")
    @Test (groups = { "regression" }, priority = 11)
    public void verifyUpdateInvoiceWithFormatPDF() throws IOException {
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

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_PDF, DOCUMENT_TYPE_INVOICE, FILE_NAME_PDF);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH, DOCUMENT_TYPE_PATH,
                DOCUMENT_TYPE_INVOICE, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_UNPAID, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY UPDATE INVOICE WITH JPG FORMAT")
    @Test (groups = { "regression" }, priority = 12)
    public void verifyUpdateInvoiceWithFormatJPG() throws IOException {
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

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_JPG, DOCUMENT_TYPE_INVOICE, FILE_NAME_JPG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH, DOCUMENT_TYPE_PATH,
                DOCUMENT_TYPE_INVOICE, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_UNPAID, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY UPDATE INVOICE WITH JPEG FORMAT")
    @Test (groups = { "regression" }, priority = 13)
    public void verifyUpdateInvoiceWithFormatJPEG() throws IOException {
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

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_JPEG, DOCUMENT_TYPE_INVOICE, FILE_NAME_JPEG);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH, DOCUMENT_TYPE_PATH,
                DOCUMENT_TYPE_INVOICE, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_UNPAID, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY UPDATE INVOICE WITH CSV FORMAT")
    @Test (groups = { "regression" }, priority = 14)
    public void verifyUpdateInvoiceWithFormatCSV() throws IOException {
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

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_CSV, DOCUMENT_TYPE_INVOICE, FILE_NAME_CSV);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY UPDATE INVOICE WITH 5 MB")
    @Test (groups = { "regression" }, priority = 15)
    public void verifyUpdateInvoiceWith5MB() throws IOException {
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

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_JPG, DOCUMENT_TYPE_INVOICE, FILE_NAME_5MB);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH, DOCUMENT_TYPE_PATH,
                DOCUMENT_TYPE_INVOICE, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_UNPAID, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY UPDATE INVOICE MORE THAN 5 MB")
    @Test (groups = { "regression" }, priority = 16)
    public void verifyUpdateInvoiceWithMoreThan5MB() throws IOException {
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

        response = derawanHelper.uploadShipmentDocs(customerOrderId, shipmentId, FILE_TYPE_JPG, DOCUMENT_TYPE_INVOICE, FILE_NAME_MORETHAN5MB);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY GET INVOICE")
    @Test (groups = { "regression" }, priority = 17)
    public void verifyGetInvoice() throws IOException {
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

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        String documentId = derawanHelper.getShipmentByDocumentType(response, DOCUMENT_TYPE_INVOICE);

        response = buruHelper.getDocsMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, EMPTY_STRING, DOCUMENT_ID_PATH, documentId, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
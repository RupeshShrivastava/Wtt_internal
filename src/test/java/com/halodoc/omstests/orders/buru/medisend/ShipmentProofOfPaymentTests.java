package com.halodoc.omstests.orders.buru.medisend;

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
public class ShipmentProofOfPaymentTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity (SeverityLevel.BLOCKER)
    @Description ("UPLOAD PROOF OF PAYMENT WITH SHIPMENT CREATED")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyUploadProofOfPaymentWithShipmentCreated() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENT WITH SHIPMENT ABANDONED")
    @Test (groups = { "regression" }, priority = 2)
    public void verifyUploadProofOfPaymentWithShipmentAbandoned() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.abandonMedisendOrder(customerOrderId, TYPE_CANCEL_USER, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENT WITH SHIPMENT DRAFT")
    @Test (groups = { "regression" }, priority = 3)
    public void verifyUploadProofOfPaymentWithShipmentDraft() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_WITH_MINIMUM_ID_1);

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENT WITH SHIPMENT DELETE")
    @Test (groups = { "regression" }, priority = 4)
    public void verifyUploadProofOfPaymentWithShipmentDeleted() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENT WITH SHIPMENT PROCESSED")
    @Test (groups = { "regression" }, priority = 5)
    public void verifyUploadProofOfPaymentWithShipmentProcessed() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENT WITH SHIPMENT CANCELLED")
    @Test (groups = { "regression" }, priority = 6)
    public void verifyUploadProofOfPaymentWithShipmentCancelled() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENT WITH SHIPMENT CONFIRMED")
    @Test (groups = { "regression" }, priority = 7)
    public void verifyUploadProofOfPaymentWithShipmentConfirmed() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("UPLOAD PROOF OF PAYMENT WITH SHIPMENET IN-TRANSIT AND INVOICE EXISTS")
    @Test (groups = { "regression" }, priority = 8)
    public void verifyUploadProofOfPaymentWithShipmentTransitandInvoiceExist() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_PROOFOFPAYMENT}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_WAITING_FOR_VERIFICATION, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENT WITH SHIPMENT IN-TRANSIT AND INVOICE DOES NOT EXIST")
    @Test (groups = { "regression" }, priority = 9)
    public void verifyUploadProofOfPaymentWithShipmentTransitandInvoiceNotExist() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("UPLOAD PROOF OF PAYMENR WITH SHIPMENT COMPLETED AND INVOICE EXISTS")
    @Test (groups = { "regression" }, priority = 10)
    public void verifyUploadProofOfPaymentWithShipmentCompleteandInvoiceExist() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_COMPLETED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_COMPLETED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_COMPLETED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_PROOFOFPAYMENT}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_COMPLETED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_WAITING_FOR_VERIFICATION, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENT WITH SHIPMENT COMPLETED AND INVOICE DOES NOT EXIST")
    @Test (groups = { "regression" }, priority = 11)
    public void verifyUploadProofOfPaymentWithShipmentCompleteandInvoiceNotExist() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("UPLOAD PROOF OF PAYMENT WITH FORMAT PDF")
    @Test (groups = { "regression" }, priority = 12)
    public void verifyUploadProofOfPaymentWithFormatPDF() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PDF);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_PROOFOFPAYMENT}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_WAITING_FOR_VERIFICATION, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("UPLOAD PROOF OF PAYMENT WITH FORMAT JPG")
    @Test (groups = { "regression" }, priority = 13)
    public void verifyUploadProofOfPaymentWithFormatJPG() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_JPG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_PROOFOFPAYMENT}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_WAITING_FOR_VERIFICATION, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("UPLOAD PROOF OF PAYMENT WITH FORMAT JPEG")
    @Test (groups = { "regression" }, priority = 14)
    public void verifyUploadProofOfPaymentWithFormatJPEG() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_JPEG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_PROOFOFPAYMENT}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_WAITING_FOR_VERIFICATION, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENT WITH STATUS PAID")
    @Test (groups = { "regression" }, priority = 15)
    public void verifyUploadProofOfPaymentWithStatusPaid() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.approvePayment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("UPLOAD PROOF OF PAYMENT WITH STATUS REJECTED")
    @Test (groups = { "regression" }, priority = 16)
    public void verifyUploadProofOfPaymentWithStatusRejected() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.rejectPayment(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_PROOFOFPAYMENT}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_WAITING_FOR_VERIFICATION, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENTS WITH INVALID FORMAT")
    @Test (groups = { "regression" }, priority = 17)
    public void verifyUploadProofOfPaymentWithInvalidFormat() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_CSV);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENT WITHOUT INVOICE")
    @Test (groups = { "regression" }, priority = 18)
    public void verifyUploadProofOfPaymentWithoutInvoice() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_CSV);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("UPLOAD PROOF OF PAYMENT WITH 3 DOCS")
    @Test (groups = { "regression" }, priority = 19)
    public void verifyUploadProofOfPaymentWith3Docs() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

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
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_WAITING_FOR_VERIFICATION, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENT WITH MORE THAN 3 DOCS")
    @Test (groups = { "regression" }, priority = 20)
    public void verifyUploadProofOfPaymentWithMoreThan3Docs() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("UPLOAD PROOF OF PAYMENT WITH 5MB")
    @Test (groups = { "regression" }, priority = 21)
    public void verifyUploadProofOfPaymentWith5MB() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_5MB);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_PROOFOFPAYMENT}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_WAITING_FOR_VERIFICATION, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPLOAD PROOF OF PAYMENT WITH MORE THAN 5MB")
    @Test (groups = { "regression" }, priority = 22)
    public void verifyUploadProofOfPaymentWithMoreThan5MB() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_MORETHAN5MB);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
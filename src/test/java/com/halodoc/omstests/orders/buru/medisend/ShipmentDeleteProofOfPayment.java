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
public class ShipmentDeleteProofOfPayment extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity (SeverityLevel.BLOCKER)
    @Description ("DELETE PROOF OF PAYMENT WITH SINGLE DOCS")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyDeleteProofOfPaymentWithSingleDocs() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        String documentId = derawanHelper.getShipmentByDocumentType(response, DOCUMENT_TYPE_PROOFOFPAYMENT);

        response = buruHelper.deleteShipmentDocs(customerOrderId, shipmentId, documentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = derawanHelper.getOrderDetails(customerOrderId);
        Assert.assertTrue(derawanHelper.validateResponseValue(response, ENTITY_TYPE_PATH, ENTITY_TYPE_PHARMACY_USER));
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(derawanHelper.validateResponseValue(response, TOTAL_PATH, 2200));
        Assert.assertTrue(derawanHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, STATUS_PATH, STATUS_CONFIRMED, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, ITEMS_PATH, QUANTITY_PATH, REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH, STATUS_PATH, STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertFalse(derawanHelper.validateArrayResponseContainExpectedArrayValue(response, SHIPMENTS_PATH + "[0]." + DOCUMENTS_PATH,
                new String[] {DOCUMENT_TYPE_PATH}, derawanHelper.constructList(new String[] {DOCUMENT_TYPE_INVOICE, DOCUMENT_TYPE_PROOFOFPAYMENT}),
                1, 0));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, STATUS_PATH,
                STATUS_TRANSIT, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + SHIPMENT_ITEMS_PATH, QUANTITY_PATH,
                REQUESTED_QUANTITY, MODE_EQUALS));
        Assert.assertTrue(derawanHelper.validateResponseValueArray(response, SHIPMENTS_PATH + "[0]." + PAYMENTS_PATH, STATUS_PATH,
                STATUS_PAYMENT_UNPAID, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("DELETE PROOF OF PAYMENT WITH MULTI DOCS")
    @Test (groups = { "regression" }, priority = 2)
    public void verifyDeleteProofOfPaymentWithMultiDocs() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        String documentId = derawanHelper.getShipmentByDocumentType(response, DOCUMENT_TYPE_PROOFOFPAYMENT);

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PROOFOFPAYMENT, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.deleteShipmentDocs(customerOrderId, shipmentId, documentId);
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

    @Severity (SeverityLevel.NORMAL)
    @Description ("DELETE PROOF OF PAYMENT WITH STATUS PAID")
    @Test (groups = { "regression" }, priority = 3)
    public void verifyDeleteProofOfPaymentWithStatusPaid() throws IOException {
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
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.OK, response));
        String documentId = derawanHelper.getShipmentByDocumentType(response, DOCUMENT_TYPE_PROOFOFPAYMENT);

        response  = derawanHelper.approvePayment(customerOrderId, shipmentId);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.deleteShipmentDocs(customerOrderId, shipmentId, documentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
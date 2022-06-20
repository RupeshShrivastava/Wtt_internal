package com.halodoc.omstests.orders.buru.medisend;

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
@Feature("SHIPMENT DOCS TESTS")
public class ShipmentDocsTests extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY GET DOCS")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyGetDocs() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = buruHelper.getDocsMedisendOrder(MEDISEND_ORDER_ID_WITH_DOCS, MEDISEND_SHIPMENT_ID_WITH_DOCS);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, EMPTY_STRING, NAME_PATH, MEDISEND_SHIPMENT_DOCS_NAME, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValueArray(response, EMPTY_STRING, DOCUMENT_ID_PATH, MEDISEND_SHIPMENT_DOCS_ID, MODE_EQUALS));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD DOCS SHIPMENT CREATED")
    @Test (groups = { "regression" }, priority = 2)
    public void verifyUploadDocsShipmentCreated() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PURCHASELETTER, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getDocsMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateArraySize(response, "", EQUAL_KEY, 1));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD DOCS SHIPMENT DRAFT")
    @Test (groups = { "regression" }, priority = 3)
    public void verifyUploadDocsShipmentDraft() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchWithMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_WITH_MINIMUM_ID_1);

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PURCHASELETTER, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD DOCS SHIPMENT REQUEST CANCEL")
    @Test (groups = { "regression" }, priority = 4)
    public void verifyUploadDocsShipmentRequestCancel() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.requestCancel(customerOrderId, shipmentId, TYPE_CANCEL_USER, CANCEL_REASON);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PURCHASELETTER, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getDocsMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateArraySize(response, "", EQUAL_KEY, 1));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD DOCS SHIPMENT CANCELLED")
    @Test (groups = { "regression" }, priority = 5)
    public void verifyUploadDocsShipmentCancelled() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PURCHASELETTER, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD DOCS SHIPMENT DELIVERED")
    @Test (groups = { "regression" }, priority = 6)
    public void verifyUploadDocsShipmentDelivered() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PURCHASELETTER, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD DOCS SHIPMENT DELETED")
    @Test (groups = { "regression" }, priority = 7)
    public void verifyUploadDocsShipmentDeleted() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductUnavailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_ACTIVE_NO_MINIMUM_ID_1);

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PURCHASELETTER, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD DOCS SHIPMENT PARTIAL")
    @Test (groups = { "regression" }, priority = 8)
    public void verifyUploadDocsShipmentPartial() throws IOException {
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

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PURCHASELETTER, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getDocsMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateArraySize(response, "", EQUAL_KEY, 1));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD DOCS SHIPMENT PENDING")
    @Test (groups = { "regression" }, priority = 9)
    public void verifyUploadDocsShipmentPending() throws IOException {
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

        response = derawanHelper.createChildMedisendShipment(customerOrderId, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String childShipmentId = buruHelper.getShipmentIdByParentId(response, shipmentId);

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, childShipmentId, DOCUMENT_TYPE_PURCHASELETTER, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.getDocsMedisendOrder(customerOrderId, childShipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateArraySize(response, "", EQUAL_KEY, 1));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY UPLOAD DOCS SHIPMENT MBS BEFORE CONFIRM")
    @Test (groups = { "regression" }, priority = 10)
    public void verifyUploadDocsShipmentMBSBeforeConfirm() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailableBranchMBS.getInstance(REQUESTED_QUANTITY));

        Response response = buruHelper.createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        String customerOrderId = response.path(CUSTOMER_ORDER_ID_PATH);
        String shipmentId = buruHelper.getShipmentIdByBranchId(response, BRANCH_MBS_ID);

        response = buruHelper.uploadDocsMedisendOrder(customerOrderId, shipmentId, DOCUMENT_TYPE_PURCHASELETTER, FILE_NAME_PNG);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = buruHelper.confirmMedisendOrder(customerOrderId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        Assert.assertTrue(checkUntilAttributeExist(customerOrderId, shipmentId, ATTRIBUTES_PATH + "." + ORDER_CREATION_FAILURE_PATH));

        response = buruHelper.getDocsMedisendOrder(customerOrderId, shipmentId);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(buruHelper.validateArraySize(response, "", EQUAL_KEY, 1));

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
}
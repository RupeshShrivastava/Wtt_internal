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
@Feature("DISCOUNT SETUP TESTS DERAWAN")
public class DiscountSetupTestsDerawan extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    private String CUSTOMER_ORDER_ID;

    private String SHIPMENT_ID;

    private String LISTING_ID;

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY RATE CARD ON UPDATE CARD")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyRateCardOnUpdateCard() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1DiscountSingle.getInstance(REQUESTED_QUANTITY));
        Response response = buruHelper.createMedisendOrderDiscount(getUUID(), currency, medisendOrderItemsArray);
        CUSTOMER_ORDER_ID = response.path(CUSTOMER_ORDER_ID_PATH);
        SHIPMENT_ID = response.path(SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + EXTERNAL_ID_PATH);
        LISTING_ID = response.path(ITEMS_PATH + FIRST_ARRAY_ELEMENT + LISTING_ID_PATH);
        Assert.assertTrue(buruHelper.validateResponseValue(response, STATUS_PATH, STATUS_CREATED));
        Assert.assertTrue(buruHelper.validateResponseValue(response, ITEMS_PATH + FIRST_ARRAY_ELEMENT + PRICE_PATH, SELLING_PRICE_FOR_CREATE_ORDER));
        Assert.assertTrue(buruHelper.validatePathContainsValueInPath(response, ITEMS_PATH + FIRST_ARRAY_ELEMENT + ATTRIBUTES_PATH, true));
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1DiscountSingle.getInstance(MAX_REQUESTED_QUANTITY));
        buruHelper.updateMedisendItemDiscount(CUSTOMER_ORDER_ID, medisendOrderItemsArray);
        Response response1 = buruHelper.getMedisendOrderByDiscountPharmacy(CUSTOMER_ORDER_ID);
        Assert.assertTrue(
                buruHelper.validateResponseValueIsNull(response1, ITEMS_PATH + FIRST_ARRAY_ELEMENT + ATTRIBUTES_PATH + PATH + RATE_CARD_ID_PATH));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY RATE CARD UPDATE SHIPMENT ITEMS STOCK")
    @Test (groups = { "regression" }, priority = 2, dependsOnMethods = "verifyRateCardOnUpdateCard")
    public void verifyRateCardUpdateShipmentItemsStock() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = buruHelper.confirmMedisendDiscountOrder((CUSTOMER_ORDER_ID));
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        Response response1 = buruHelper.getMedisendOrderByDiscountPharmacy(CUSTOMER_ORDER_ID);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response1));
        Assert.assertTrue(buruHelper.validateResponseValue(response1, STATUS_PATH, STATUS_CONFIRMED));
        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1DiscountSingle.getInstance(MAX_QUANTITY));
        Response response2 = derawanHelper.updateStockMedisendShipment(CUSTOMER_ORDER_ID, SHIPMENT_ID, medisendOrderItemsArray, UPDATE_STOCK_REASON);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response2));
        Response response3 = buruHelper.getMedisendOrderByDiscountPharmacy(CUSTOMER_ORDER_ID);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.OK, response3));
        Assert.assertTrue(buruHelper.validatePathContainsValueInPath(response3, ITEMS_PATH + FIRST_ARRAY_ELEMENT + ATTRIBUTES_PATH, true));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY RATE CARD ON SHIPMENT CANCELLATION")
    @Test (groups = { "regression" }, priority = 3, dependsOnMethods = "verifyRateCardUpdateShipmentItemsStock")
    public void verifyRateCardOnShipmentCancellation() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = derawanHelper.cancelMedisendOrder(CUSTOMER_ORDER_ID, SHIPMENT_ID, TYPE_CANCEL_CS, CANCEL_REASON);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        Response response2 = buruHelper.getMedisendOrderByDiscountPharmacy(CUSTOMER_ORDER_ID);
        Assert.assertTrue(buruHelper.validateResponseValue(response2, STATUS_PATH, STATUS_CANCEL));
        Assert.assertTrue(buruHelper.validateResponseValue(response2, TOTAL_PATH, RATE_VALUE_ZERO));
        Assert.assertTrue(
                buruHelper.validateResponseValueIsNull(response2, ITEMS_PATH + FIRST_ARRAY_ELEMENT + ATTRIBUTES_PATH + PATH + RATE_CARD_ID_PATH));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY ADDING SHIPMENT ORDER ITEM NOTES")
    @Test (groups = { "regression" }, priority = 4)
    public void verifyAddingShipmentOrderItemNotes() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1DiscountSingle.getInstance(REQUESTED_QUANTITY));
        Response response = buruHelper.createMedisendOrderDiscount(getUUID(), currency, medisendOrderItemsArray);
        CUSTOMER_ORDER_ID = response.path(CUSTOMER_ORDER_ID_PATH);
        SHIPMENT_ID = response.path(SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + EXTERNAL_ID_PATH);
        LISTING_ID = response.path(ITEMS_PATH + FIRST_ARRAY_ELEMENT + LISTING_ID_PATH);
        Assert.assertTrue(buruHelper.validateResponseValue(response, STATUS_PATH, STATUS_CREATED));
        Response response1 = derawanHelper.addShipmentItemNotes(CUSTOMER_ORDER_ID, SHIPMENT_ID, LISTING_ID, SHIPMENT_NOTES_1);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response1));
        Response response2 = derawanHelper.getOrderDetails(CUSTOMER_ORDER_ID);
        Assert.assertTrue(derawanHelper.validateResponseValue(response2,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + TYPE_PATH,
                USER_NOTE));
        Assert.assertTrue(derawanHelper.validateResponseValue(response2,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + COMMENTS_PATH,
                SHIPMENT_NOTES_1));
        Assert.assertTrue(derawanHelper.validateResponseValue(response2,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + STATUS_DELETED,
                FALSE));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY ADDING SHIPMENT ORDER ITEM NOTES WHEN EXISTING")
    @Test (groups = { "regression" }, priority = 5, dependsOnMethods = "verifyAddingShipmentOrderItemNotes")
    public void verifyAddingShipmentOrderItemNotesWhenExisting() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = derawanHelper.addShipmentItemNotes(CUSTOMER_ORDER_ID, SHIPMENT_ID, LISTING_ID, SHIPMENT_NOTES_2);
        Assert.assertTrue(derawanHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        Response response1 = derawanHelper.getOrderDetails(CUSTOMER_ORDER_ID);
        Assert.assertTrue(derawanHelper.validateResponseValue(response1,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + COMMENTS_PATH,
                SHIPMENT_NOTES_2));
        Assert.assertTrue(derawanHelper.validateResponseValue(response1,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + STATUS_DELETED,
                FALSE));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY DELETING NOTES")
    @Test (groups = { "regression" }, priority = 6, dependsOnMethods = "verifyAddingShipmentOrderItemNotesWhenExisting")
    public void verifyDeletingNotes() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = buruHelper.deleteShipmentNotes(CUSTOMER_ORDER_ID, SHIPMENT_ID, LISTING_ID);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        Response response2 = derawanHelper.getOrderDetails(CUSTOMER_ORDER_ID);
        Assert.assertTrue(derawanHelper.validateResponseValue(response2,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + COMMENTS_PATH,
                SHIPMENT_NOTES_2));
        Assert.assertTrue(derawanHelper.validateResponseValue(response2,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + STATUS_DELETED,
                TRUE));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}

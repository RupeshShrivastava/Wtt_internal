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
@Feature("DISCOUNT SETUP TESTS BURU")
public class DiscountSetupTestsBuru extends OrdersBaseTest {
    List<MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    private String CUSTOMER_ORDER_ID = null;

    private String SHIPMENT_ID = null;

    private String LISTING_ID = null;

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY GET PRODUCT DETAILS DISCOUNT")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void verifyGetProductDetailsDiscount() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = buruHelper.getMedisendProductDetails(DISCOUNT_SETUP_PRODUCT_ID);
        Assert.assertTrue(buruHelper.validateResponseValue(response, BASE_PRICE_PATH, BASE_PRICE));
        Assert.assertTrue(buruHelper.validateArraySize(response, BRANCH_MAPPING_PATH, EQUAL_KEY, NUMBER_OF_BRANCHES));
        Assert.assertTrue(
                buruHelper.validateResponseValue(response, BRANCH_MAPPING_PATH + FIRST_ARRAY_ELEMENT + NAME_PATH, DISCOUNT_SETUP_SECOND_BRANCH));
        Assert.assertTrue(
                buruHelper.validateResponseValue(response, BRANCH_MAPPING_PATH + SECOND_ARRAY_ELEMENT + NAME_PATH, DISCOUNT_SETUP_FIRST_BRANCH));
        Assert.assertTrue(
                buruHelper.validateResponseValue(response, BRANCH_MAPPING_PATH + THIRD_ARRAY_ELEMENT + NAME_PATH, DISCOUNT_SETUP_THIRD_BRANCH));
        Assert.assertTrue(
                buruHelper.validateResponseValue(response, BRANCH_MAPPING_PATH + FIRST_ARRAY_ELEMENT + SELLING_PRICE_PATH, SELLING_PRICE_FOR_BRANCH));
        Assert.assertTrue(buruHelper.validateResponseValue(response, BRANCH_MAPPING_PATH + SECOND_ARRAY_ELEMENT + SELLING_PRICE_PATH, BASE_PRICE));
        Assert.assertTrue(
                buruHelper.validateResponseValue(response, BRANCH_MAPPING_PATH + THIRD_ARRAY_ELEMENT + SELLING_PRICE_PATH, SELLING_PRICE_FOR_BRANCH));
        Assert.assertTrue(buruHelper.validatePathContainsValueInList(response, BRANCH_MAPPING_PATH + FIRST_ARRAY_ELEMENT + RATE_CARDS_PATH, true));
        Assert.assertTrue(buruHelper.validatePathContainsValueInList(response, BRANCH_MAPPING_PATH + SECOND_ARRAY_ELEMENT + RATE_CARDS_PATH, false));
        Assert.assertTrue(buruHelper.validatePathContainsValueInList(response, BRANCH_MAPPING_PATH + THIRD_ARRAY_ELEMENT + RATE_CARDS_PATH, true));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY PRODUCT SEARCH DISCOUNT")
    @Test (groups = { "regression" }, priority = 2)
    public void verifyProductSearchDiscount() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = buruHelper.getProductSearchForDiscount("10", "1", SEARCH_PRODUCT_DISCOUNT);
        Assert.assertTrue(buruHelper.validateResponseValue(response, RESULT_PATH + FIRST_ARRAY_ELEMENT + BASE_PRICE_PATH, BASE_PRICE));
        Assert.assertTrue(buruHelper
                .validateResponseValue(response, RESULT_PATH + FIRST_ARRAY_ELEMENT + BRANCH_MAPPING_PATH + FIRST_ARRAY_ELEMENT + SELLING_PRICE_PATH,
                        SELLING_PRICE_FOR_BRANCH));
        Assert.assertTrue(buruHelper
                .validateResponseValue(response, RESULT_PATH + FIRST_ARRAY_ELEMENT + BRANCH_MAPPING_PATH + FIRST_ARRAY_ELEMENT + NAME_PATH,
                        DISCOUNT_SETUP_SECOND_BRANCH));
        Assert.assertTrue(buruHelper.validatePathContainsValueInList(response,
                RESULT_PATH + FIRST_ARRAY_ELEMENT + BRANCH_MAPPING_PATH + FIRST_ARRAY_ELEMENT + RATE_CARDS_PATH, true));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY INVENTORY OUT OF STOCK FILTER FOR DISCOUNT")
    @Test (groups = { "regression" }, priority = 3, enabled = false)
    public void verifyInventoryOutOfStockFilterForDiscount() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = buruHelper.getInventoryOutOfStockFilterType();
        Assert.assertTrue(buruHelper
                .validateArraySize(response, RESULT_PATH + FIRST_ARRAY_ELEMENT + BRANCH_MAPPING_PATH, EQUAL_KEY, UPDATED_REQUESTED_QUANTITY));
        Assert.assertTrue(buruHelper.validateResponseValue(response, RESULT_PATH + FIRST_ARRAY_ELEMENT + PRICE_PATH, BASE_PRICE));
        Assert.assertTrue(buruHelper
                .validateResponseValue(response, RESULT_PATH + FIRST_ARRAY_ELEMENT + BRANCH_MAPPING_PATH + FIRST_ARRAY_ELEMENT + SELLING_PRICE_PATH,
                        SELLING_PRICE_FOR_BRANCH));
        Assert.assertTrue(buruHelper.validatePathContainsValueInList(response,
                RESULT_PATH + FIRST_ARRAY_ELEMENT + BRANCH_MAPPING_PATH + FIRST_ARRAY_ELEMENT + RATE_CARDS_PATH, true));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY CREATE ORDER DISCOUNT")
    @Test (groups = { "regression" }, priority = 4)
    public void verifyCreateOrderDiscount() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1DiscountSingle.getInstance(REQUESTED_QUANTITY));
        Response response = buruHelper.createMedisendOrderDiscount(getUUID(), currency, medisendOrderItemsArray);
        CUSTOMER_ORDER_ID = response.path(CUSTOMER_ORDER_ID_PATH);
        SHIPMENT_ID = response.path(SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + EXTERNAL_ID_PATH);
        LISTING_ID = response.path(ITEMS_PATH + FIRST_ARRAY_ELEMENT + LISTING_ID_PATH);
        Assert.assertTrue(buruHelper.validateResponseValue(response, STATUS_PATH, STATUS_CREATED));
        Assert.assertTrue(buruHelper.validateResponseValue(response, ITEMS_PATH + FIRST_ARRAY_ELEMENT + PRICE_PATH, SELLING_PRICE_FOR_CREATE_ORDER));
        Assert.assertTrue(
                buruHelper.validateResponseValue(response, ITEMS_PATH + FIRST_ARRAY_ELEMENT + ITEMS_REQUESTED_PRICE_PATH, BASE_PRICE_CREATE_ORDER));
        Assert.assertTrue(buruHelper.validatePathContainsValueInPath(response, ITEMS_PATH + FIRST_ARRAY_ELEMENT + ATTRIBUTES_PATH, true));
        Assert.assertTrue(buruHelper.validatePathContainsValueInList(response, ITEMS_PATH + FIRST_ARRAY_ELEMENT + ITEMS_APPLICABLE_RATE_PATH, true));
        Assert.assertTrue(buruHelper
                .validateMedisendOrderByUniqueId(response, ADJUSTMENT_PATH, TYPE_PATH, VALUE_PATH, TYPE_TAX, EXPECTED_TAX_VALUE, MODE_EQUALS));
        Assert.assertTrue(
                buruHelper.validateMedisendOrderByUniqueId(response, ADJUSTMENT_PATH, TYPE_PATH, VALUE_PATH, TYPE_CONVENIENCE_FEE, 0, MODE_EQUALS));
        Assert.assertTrue(buruHelper.validateResponseValue(response, SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + ATTRIBUTES_PATH + PATH + SHIPMENT_DISTRIBUTOR_ENTITY_NAME_PATH, DISCOUNT_SETUP_SECOND_BRANCH));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY ADDING SHIPMENT ITEM NOTES")
    @Test (groups = { "regression" }, priority = 5, dependsOnMethods = { "verifyCreateOrderDiscount" })
    public void verifyAddingShipmentItemNotes() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = buruHelper.addShipmentItemNotes(CUSTOMER_ORDER_ID, SHIPMENT_ID, LISTING_ID, SHIPMENT_NOTES_1);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        Response response1 = buruHelper.getMedisendOrderByDiscountPharmacy(CUSTOMER_ORDER_ID);
        Assert.assertTrue(buruHelper.validateResponseValue(response1, STATUS_PATH, STATUS_CREATED));
        Assert.assertTrue(buruHelper.validateResponseValue(response1, SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + STATUS_PATH, STATUS_CREATED));
        Assert.assertTrue(buruHelper
                .validateResponseValue(response1, SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + STATUS_PATH,
                        STATUS_CREATED));
        Assert.assertTrue(buruHelper.validateResponseValue(response1,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + TYPE_PATH,
                USER_NOTE));
        Assert.assertTrue(buruHelper.validateResponseValue(response1,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + COMMENTS_PATH,
                SHIPMENT_NOTES_1));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY ADDING SHIPMENT ITEM NOTES WHEN NOTES EXISTS")
    @Test (groups = { "regression" }, priority = 6, dependsOnMethods = { "verifyAddingShipmentItemNotes" })
    public void verifyAddingShipmentItemNotesWhenNotesExists() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = buruHelper.addShipmentItemNotes(CUSTOMER_ORDER_ID, SHIPMENT_ID, LISTING_ID, SHIPMENT_NOTES_1);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        Response response1 = buruHelper.getMedisendOrderByDiscountPharmacy(CUSTOMER_ORDER_ID);
        Assert.assertTrue(buruHelper.validateResponseValue(response1,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + TYPE_PATH,
                USER_NOTE));
        Assert.assertTrue(buruHelper.validateResponseValue(response1,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + COMMENTS_PATH,
                SHIPMENT_NOTES_1));
        Assert.assertTrue(buruHelper.validateResponseValue(response1,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + STATUS_DELETED,
                FALSE));
        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VERIFY DELETING SHIPMENT NOTES")
    @Test (groups = { "regression" }, priority = 7, dependsOnMethods = { "verifyAddingShipmentItemNotesWhenNotesExists" })
    public void verifyDeletingShipmentNotes() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = buruHelper.deleteShipmentNotes(CUSTOMER_ORDER_ID, SHIPMENT_ID, LISTING_ID);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        Response response1 = buruHelper.getMedisendOrderByDiscountPharmacy(CUSTOMER_ORDER_ID);
        Assert.assertTrue(buruHelper.validateResponseValue(response1,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + COMMENTS_PATH,
                SHIPMENT_NOTES_1));
        Assert.assertTrue(buruHelper.validateResponseValue(response1,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + STATUS_DELETED,
                TRUE));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY ADDING NOTES WHEN PREVIOUS DELETED")
    @Test (groups = { "regression" }, priority = 8, dependsOnMethods = { "verifyAddingShipmentItemNotesWhenNotesExists" })
    public void verifyAddingNotesWhenPreviousDeleted() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = buruHelper.addShipmentItemNotes(CUSTOMER_ORDER_ID, SHIPMENT_ID, LISTING_ID, SHIPMENT_NOTES_2);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        Response response1 = buruHelper.getMedisendOrderByDiscountPharmacy(CUSTOMER_ORDER_ID);
        Assert.assertTrue(buruHelper.validateResponseValue(response1,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + COMMENTS_PATH,
                SHIPMENT_NOTES_2));
        Assert.assertTrue(buruHelper.validateResponseValue(response1,
                SHIPMENTS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_ITEMS_PATH + FIRST_ARRAY_ELEMENT + SHIPMENT_NOTES_PATH + FIRST_ARRAY_ELEMENT + STATUS_DELETED,
                FALSE));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("VERIFY DELETING NOTES WHEN ORDER CONFIRMED")
    @Test (groups = { "regression" }, priority = 9, dependsOnMethods = { "verifyAddingNotesWhenPreviousDeleted" })
    public void verifyDeletingNotesWhenOrderConfirmed() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = buruHelper.confirmMedisendDiscountOrder(CUSTOMER_ORDER_ID);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        Response response1 = buruHelper.deleteShipmentNotes(CUSTOMER_ORDER_ID, SHIPMENT_ID, LISTING_ID);
        Assert.assertTrue(buruHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response1));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
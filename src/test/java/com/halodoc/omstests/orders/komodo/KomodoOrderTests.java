package com.halodoc.omstests.orders.komodo;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.komodo.KomodoUtil;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KomodoOrderTests {

    KomodoUtil komodoHelper = new KomodoUtil();
    @Test
    public void verifyGetCategory() {
        Response response = komodoHelper.getCategory(LATITUDE_VALUE, LONGITUDE_VALUE, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.validateArraySize(response, RESULT_KEY, NOT_EQUAL_KEY, 0));
    }

    @Test
    public void verifyGetCategoryWithoutParam() {
        Response response = komodoHelper.getCategory(null, null, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyGetCategoryWithInvalidToken() {
        Response response = komodoHelper.getCategory(LATITUDE_VALUE, LONGITUDE_VALUE, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyGetCategoryWithoutoken() {
        Response response = komodoHelper.getCategory(LATITUDE_VALUE, LONGITUDE_VALUE, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyGetCategoryProducts() {
        Response response = komodoHelper.getCategoryProducts(CATEGORY_ID, LATITUDE_VALUE, LONGITUDE_VALUE, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.validateArraySize(response, RESULT_KEY, NOT_EQUAL_KEY, 0));
    }

    @Test
    public void verifyGetCategoryProductsWithInvalidCategoryId() {
        Response response = komodoHelper.getCategoryProducts(INVALID_ID, LATITUDE_VALUE, LONGITUDE_VALUE, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyGetCategoryProductsWithoutParam() {
        Response response = komodoHelper.getCategoryProducts(CATEGORY_ID, null, null, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyGetCategoryProductsWithInvalidToken() {
        Response response = komodoHelper.getCategoryProducts(CATEGORY_ID, LATITUDE_VALUE, LONGITUDE_VALUE, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyGetCategoryProductsWithoutToken() {
        Response response = komodoHelper.getCategoryProducts(CATEGORY_ID, LATITUDE_VALUE, LONGITUDE_VALUE, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyGetStores() {
        Response response = komodoHelper.getStores(LATITUDE_VALUE, LONGITUDE_VALUE, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.validateArraySize(response, RESULT_KEY, NOT_EQUAL_KEY, 0));
    }

    @Test
    public void verifyGetStoresWithoutParam() {
        Response response = komodoHelper.getStores(null, null, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyGetStoresWithInvalidToken() {
        Response response = komodoHelper.getStores(LATITUDE_VALUE, LONGITUDE_VALUE, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyGetStoresWithoutToken() {
        Response response = komodoHelper.getStores(LATITUDE_VALUE, LONGITUDE_VALUE, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyGetStoreProducts() {
        Response response = komodoHelper.getStoreProducts(STORE_ID, LATITUDE_VALUE, LONGITUDE_VALUE, 0, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.validateArraySize(response, RESULT_KEY, NOT_EQUAL_KEY, 0));
    }

    @Test
    public void verifyGetStoreProductsWithPageLimit() {
        Response response = komodoHelper.getStoreProducts(STORE_ID, LATITUDE_VALUE, LONGITUDE_VALUE, PAGE_LIMIT, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.validateArraySize(response, RESULT_KEY, EQUAL_KEY, PAGE_LIMIT));
    }

    @Test
    public void verifyGetStoreProductsWithInvalidStoreId() {
        Response response = komodoHelper.getStoreProducts(INVALID_ID, LATITUDE_VALUE, LONGITUDE_VALUE, 0, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyGetStoreProductsWithoutParam() {
        Response response = komodoHelper.getStoreProducts(STORE_ID, null, null, 0, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyGetStoreProductsWithInvalidToken() {
        Response response = komodoHelper.getStoreProducts(STORE_ID, LATITUDE_VALUE, LONGITUDE_VALUE, 0, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyGetStoreProductsWithoutToken() {
        Response response = komodoHelper.getStoreProducts(STORE_ID, LATITUDE_VALUE, LONGITUDE_VALUE, 0, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifySearchProducts() {
        Response response = komodoHelper.searchProducts(PRODUCT_SEARCHED, 0, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.validateArraySize(response, RESULT_KEY, NOT_EQUAL_KEY, 0));
        Assert.assertTrue(komodoHelper.validateArrayResponseContainExpectedArrayValue(response, RESULT_KEY, new String[] {NAME_KEY},
                komodoHelper.constructList(new String[] {ACTAL_MEDICINE}), 1, 0));
    }

    @Test
    public void verifySearchProductsWithPageLimit() {
        Response response = komodoHelper.searchProducts(PRODUCT_SEARCHED, PAGE_LIMIT, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.validateArraySize(response, RESULT_KEY, EQUAL_KEY, PAGE_LIMIT));
        Assert.assertTrue(komodoHelper.validateArrayResponseContainExpectedArrayValue(response, RESULT_KEY, new String[] {NAME_KEY},
                komodoHelper.constructList(new String[] {ACTAL_MEDICINE}), 1, 0));
    }

    @Test
    public void verifySearchProductsWithInvalidToken() {
        Response response = komodoHelper.searchProducts(PRODUCT_SEARCHED, 0, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifySearchProductsWithoutToken() {
        Response response = komodoHelper.searchProducts(PRODUCT_SEARCHED, 0, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyCreateOrder() {
        Response response = komodoHelper.createOrder(USER_ENTITY_ID, null, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CREATED, response));
    }

    @Test
    public void verifyCreateOrderWithDuplicateCartId() {
        Response response = komodoHelper.createOrder(USER_ENTITY_ID, DUPLICATE_CART_ID, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyCreateOrderWithInvalidCartId() {
        Response response = komodoHelper.createOrder(USER_ENTITY_ID, INVALID_ID_WITH_SPECIAL_CHARS, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    @Test
    public void verifyCreateOrderWithInvalidEntityId() {
        Response response = komodoHelper.createOrder(OTHERS_ENTITY_ID, null, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyCreateOrderWithEmptyRequest() {
        Response response = komodoHelper.createOrder(EMPTY_STRING, EMPTY_STRING, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    @Test
    public void verifyCreateOrderWithInvalidToken() {
        Response response = komodoHelper.createOrder(USER_ENTITY_ID, null, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyCreateOrderWithoutToken() {
        Response response = komodoHelper.createOrder(USER_ENTITY_ID, null, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyGetOrder() {
        Response response = komodoHelper.getOrder(USER_ENTITY_ID, null, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatuses(new String[] {STATUS_CREATED, STATUS_APPROVED}, response));
    }

    @Test
    public void verifyGetOrderWithInvalidOrderId() {
        Response response = komodoHelper.getOrder(USER_ENTITY_ID, OTHERS_ORDER_ID, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyGetOrderWithInvalidToken() {
        Response response = komodoHelper.getOrder(USER_ENTITY_ID, null, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyGetOrderWithoutToken() {
        Response response = komodoHelper.getOrder(USER_ENTITY_ID, null, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyAbandonOrder() throws InterruptedException {
        Response response = komodoHelper.abandonOrder(USER_ENTITY_ID, null, true, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
    }

    @Test
    public void verifyAbandonOrderInCancelState() throws InterruptedException {
        Response response = komodoHelper.abandonOrder(USER_ENTITY_ID, null, true, true, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    @Test
    public void verifyAbandonOrderWithInvalidOrderId() throws InterruptedException {
        Response response = komodoHelper.abandonOrder(USER_ENTITY_ID, OTHERS_ORDER_ID, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyAbandonOrderWithEmptyRequest() throws InterruptedException {
        Response response = komodoHelper.abandonOrder(USER_ENTITY_ID, EMPTY_STRING, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyAbandonOrderWithInvalidToken() throws InterruptedException {
        Response response = komodoHelper.abandonOrder(USER_ENTITY_ID, null, true, false, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyAbandonOrderWithoutToken() throws InterruptedException {
        Response response = komodoHelper.abandonOrder(USER_ENTITY_ID, null, true, false, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyGetProduct() {
        Response response = komodoHelper.getProduct(PRODUCT_LISTING, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, NAME_KEY, ACTAL_MEDICINE));
    }

    @Test
    public void verifyGetProductWithInvalidProductId() {
        Response response = komodoHelper.getProduct(INVALID_ID, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyGetProductWithInvalidToken() {
        Response response = komodoHelper.getProduct(PRODUCT_LISTING, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyGetProductWithoutToken() {
        Response response = komodoHelper.getProduct(PRODUCT_LISTING, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyItemUpdate() throws InterruptedException {
        Response response = komodoHelper.itemUpdate(USER_ENTITY_ID, null, PRODUCT_LISTING, true, false, false,
                false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
    }

    @Test
    public void verifyItemUpdateWithInvalidOrderId() throws InterruptedException {
        Response response = komodoHelper.itemUpdate(USER_ENTITY_ID, OTHERS_ORDER_ID, PRODUCT_LISTING, false, false, false,
                false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyItemUpdateWithInvalidProductId() throws InterruptedException {
        Response response = komodoHelper.itemUpdate(USER_ENTITY_ID, null, INVALID_ID, true, false, false,
                false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyItemUpdateInCancelState() throws InterruptedException {
        Response response = komodoHelper.itemUpdate(USER_ENTITY_ID, null, PRODUCT_LISTING, true, true, false,
                false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    @Test
    public void verifyItemUpdateWithEmptyRequest() throws InterruptedException {
        Response response = komodoHelper.itemUpdate(USER_ENTITY_ID, null, EMPTY_STRING, true, false, false,
                false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    @Test
    public void verifyItemUpdateWithInvalidToken() throws InterruptedException {
        Response response = komodoHelper.itemUpdate(USER_ENTITY_ID, null, PRODUCT_LISTING, true, false, true,
                false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyItemUpdateWithoutToken() throws InterruptedException {
        Response response = komodoHelper.itemUpdate(USER_ENTITY_ID, null, PRODUCT_LISTING, true, false, false,
                true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyUpdatePatient() throws InterruptedException {
        Response response = komodoHelper.updatePatient(USER_ENTITY_ID, null, true, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
    }

    @Test
    public void verifyUpdatePatientWithInvalidOrderId() throws InterruptedException {
        Response response = komodoHelper.updatePatient(USER_ENTITY_ID, OTHERS_ORDER_ID, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyUpdatePatientWithInvalidPatientId() throws InterruptedException {
        Response response = komodoHelper.updatePatient(OTHERS_ENTITY_ID, null, true, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyUpdatePatientInCancelState() throws InterruptedException {
        Response response = komodoHelper.updatePatient(USER_ENTITY_ID, null, true, true, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyUpdatePatientWithEmptyRequest() throws InterruptedException {
        Response response = komodoHelper.updatePatient(EMPTY_STRING, null, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    @Test
    public void verifyUpdatePatientWithInvalidToken() throws InterruptedException {
        Response response = komodoHelper.updatePatient(USER_ENTITY_ID, null, true, false, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyUpdatePatientWithoutToken() throws InterruptedException {
        Response response = komodoHelper.updatePatient(USER_ENTITY_ID, null, true, false, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyConfirmOrder() throws InterruptedException {
        Response response = komodoHelper.confirmOrder(USER_ENTITY_ID,null, true, false, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
    }

    @Test
    public void verifyConfirmOrderWithInvalidOrderId() throws InterruptedException {
        Response response = komodoHelper.confirmOrder(USER_ENTITY_ID, OTHERS_ORDER_ID,false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyConfirmOrderWithEmptyRequest() throws InterruptedException {
        Response response = komodoHelper.confirmOrder(EMPTY_STRING, null,true, false, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    @Test
    public void verifyConfirmOrderWithInvalidToken() throws InterruptedException {
        Response response = komodoHelper.confirmOrder(USER_ENTITY_ID, null,true, true, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyConfirmOrderWithoutToken() throws InterruptedException {
        Response response = komodoHelper.confirmOrder(USER_ENTITY_ID, null,true, false, true, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyGetDocument() {
        Response response = komodoHelper.getOrderDocument(ORDER_ID_WITH_DOCUMENT, CATEGORY_VALUE, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
    }

    @Test
    public void verifyGetDocumentWithInvalidOrderId() {
        Response response = komodoHelper.getOrderDocument(INVALID_ORDER_ID, CATEGORY_VALUE, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyGetDocumentWithInvalidToken() {
        Response response = komodoHelper.getOrderDocument(ORDER_ID_WITH_DOCUMENT, CATEGORY_VALUE, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyGetDocumentWithoutToken() {
        Response response = komodoHelper.getOrderDocument(ORDER_ID_WITH_DOCUMENT, CATEGORY_VALUE, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyCancelOrder() throws InterruptedException {
        Response response = komodoHelper.cancelOrder(USER_ENTITY_ID, null, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
    }

    @Test
    public void verifyCancelOrderWithInvalidOrderId() throws InterruptedException {
        Response response = komodoHelper.cancelOrder(USER_ENTITY_ID, OTHERS_ORDER_ID, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyCancelOrderWithEmptyRequest() throws InterruptedException {
        Response response = komodoHelper.cancelOrder(USER_ENTITY_ID, EMPTY_STRING, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    @Test
    public void verifyCancelOrderWithInvalidToken() throws InterruptedException {
        Response response = komodoHelper.cancelOrder(USER_ENTITY_ID, null, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyCancelOrderWithoutToken() throws InterruptedException {
        Response response = komodoHelper.cancelOrder(USER_ENTITY_ID, null, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyGenerateToken() throws InterruptedException {
        Response response = komodoHelper.generateToken(USER_ENTITY_ID, null, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.CREATED, response));
    }

    @Test
    public void verifyGenerateTokenWithInvalidOrderId() throws InterruptedException {
        Response response = komodoHelper.generateToken(USER_ENTITY_ID, INVALID_ORDER_ID, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyGenerateTokenWithEmptyRequest() throws InterruptedException {
        Response response = komodoHelper.generateToken(USER_ENTITY_ID, EMPTY_STRING, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    @Test
    public void verifyGenerateTokenWithInvalidToken() throws InterruptedException {
        Response response = komodoHelper.generateToken(USER_ENTITY_ID, null, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyGenerateTokenWithoutToken() throws InterruptedException {
        Response response = komodoHelper.generateToken(USER_ENTITY_ID, null, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyApplyPromo() throws InterruptedException {
        Response response = komodoHelper.applyPromo(USER_ENTITY_ID, null, COUPON_CODE, false, true, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCodes(new HttpStatus[] {HttpStatus.OK, HttpStatus.NO_CONTENT}, response));
    }

    @Test
    public void verifyApplyPromoWithDuplicateCoupon() throws InterruptedException {
        Response response = komodoHelper.applyPromo(USER_ENTITY_ID, null, COUPON_CODE, true, true, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyApplyPromoWithInvalidOrderId() throws InterruptedException {
        Response response = komodoHelper.applyPromo(USER_ENTITY_ID, INVALID_ORDER_ID, COUPON_CODE, false, true, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyApplyPromoWithInvalidCoupon() throws InterruptedException {
        Response response = komodoHelper.applyPromo(USER_ENTITY_ID, null, INVALID_KEY, false, true, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test(enabled = false)
    public void verifyApplyPromoNotInApprovedState() throws InterruptedException {
        Response response = komodoHelper.applyPromo(USER_ENTITY_ID, null, COUPON_CODE, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    @Test
    public void verifyApplyPromoWithEmptyRequest() throws InterruptedException {
        Response response = komodoHelper.applyPromo(USER_ENTITY_ID, null, EMPTY_STRING, false, true, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    @Test
    public void verifyApplyPromoWithInvalidToken() throws InterruptedException {
        Response response = komodoHelper.applyPromo(USER_ENTITY_ID, null, COUPON_CODE, false, true, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyApplyPromoWithoutToken() throws InterruptedException {
        Response response = komodoHelper.applyPromo(USER_ENTITY_ID, null, COUPON_CODE, false, true, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyDeletePromo() throws InterruptedException {
        Response response = komodoHelper.deletePromo(USER_ENTITY_ID, null, COUPON_CODE, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCodes(new HttpStatus[] {HttpStatus.OK, HttpStatus.CREATED}, response));
    }

    @Test
    public void verifyDeletePromoWithInvalidOrderId() throws InterruptedException {
        Response response = komodoHelper.deletePromo(USER_ENTITY_ID, INVALID_ORDER_ID, COUPON_CODE, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyDeletePromoWithInvalidCoupon() throws InterruptedException {
        Response response = komodoHelper.deletePromo(USER_ENTITY_ID, null, INVALID_KEY, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyDeletePromoWithEmptyRequest() throws InterruptedException {
        Response response = komodoHelper.deletePromo(USER_ENTITY_ID, null, EMPTY_STRING, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    @Test
    public void verifyDeletePromoWithInvalidToken() throws InterruptedException {
        Response response = komodoHelper.deletePromo(USER_ENTITY_ID, null, COUPON_CODE, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyDeletePromoWithoutToken() throws InterruptedException {
        Response response = komodoHelper.deletePromo(USER_ENTITY_ID, null, COUPON_CODE, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyReallocateOrder() throws InterruptedException {
        Response response = komodoHelper.reallocateOrder(USER_ENTITY_ID, null, true, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_APPROVED, response));
        Assert.assertTrue(komodoHelper.validateReallocation(response));
    }

    @Test
    public void verifyReallocateOrderReallocatedTrue() throws InterruptedException {
        Response response = komodoHelper.reallocateOrder(USER_ENTITY_ID, null, true, true, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test
    public void verifyReallocateOrderOtherUser() throws InterruptedException {
        Response response = komodoHelper.reallocateOrder(USER_ENTITY_ID, OTHERS_ORDER_ID, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Test
    public void verifyReallocateOrderWithInvalidToken() throws InterruptedException {
        Response response = komodoHelper.reallocateOrder(USER_ENTITY_ID, null, true, false, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Test
    public void verifyReallocateOrderWithoutToken() throws InterruptedException {
        Response response = komodoHelper.reallocateOrder(USER_ENTITY_ID, null, true, false, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }
}
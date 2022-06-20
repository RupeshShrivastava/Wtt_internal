package com.halodoc.omstests.orders.komodo;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NationwideShippingv4Tests extends OrdersBaseTest {
    @Test (groups = { "sanity" })
    public void verifyOrderLessThan20KMInstantPharmacyOpen() throws InterruptedException {
        Response response = komodoHelper.createNationwideOrderV4(STORE_NATIONWIDE_PHARMACY_OPEN_ID, LATITUDE_INSIDE_GEOZONE, LONGITUDE_INSIDE_GEOZONE,
                NATIONWIDE_PRODUCT_ID);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CREATED, response));

        String orderId = komodoHelper.getOrderIdByResponseBody(response);
        Assert.assertTrue(checkStatusUntil(STATUS_APPROVED, orderId), MESSAGE_CHECK_STATUS_FAILED);
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        String deliveryOptionsId = komodoHelper.getDeliveryOptionsId(response, INSTANT_KEY);
        response = timorHelper.deliveryOption(orderId, deliveryOptionsId);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));

        response = komodoHelper.confirmOrder(USER_ENTITY_ID, orderId, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_ID_PATH, MERCHANT_NATIONWIDE_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_LOCATION_ID_PATH, PHARMACY_ALWAYS_OPEN_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_STORE_ID_PATH, STORE_NATIONWIDE_PHARMACY_OPEN_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_DELIVERY_TYPE_PATH, INSTANT_KEY));
    }

    @Test
    public void verifyOrderLessThan20KMNonInstantPharmacyOpen() throws InterruptedException {
        Response response = komodoHelper.createNationwideOrderV4(STORE_NATIONWIDE_NON_INSTANT_PHARMACY_OPEN_ID, LATITUDE_INSIDE_GEOZONE,
                LONGITUDE_INSIDE_GEOZONE, NATIONWIDE_PRODUCT_ID);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CREATED, response));

        String orderId = komodoHelper.getOrderIdByResponseBody(response);
        Assert.assertTrue(checkStatusUntil(STATUS_APPROVED, orderId), MESSAGE_CHECK_STATUS_FAILED);
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        String deliveryOptionsId = komodoHelper.getDeliveryOptionsId(response, DELAYED_INSTANT_KEY);
        response = timorHelper.deliveryOption(orderId, deliveryOptionsId);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));

        response = komodoHelper.confirmOrder(USER_ENTITY_ID, orderId, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_ID_PATH, MERCHANT_NATIONWIDE_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_LOCATION_ID_PATH, PHARMACY_ALWAYS_OPEN_NON_INSTANT_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_STORE_ID_PATH, STORE_NATIONWIDE_NON_INSTANT_PHARMACY_OPEN_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_DELIVERY_TYPE_PATH, DELAYED_INSTANT_KEY));
    }

    @Test
    public void verifyOrderLessThan20KMInstantPharmacyClose() throws InterruptedException {
        Response response = komodoHelper.createNationwideOrderV4(STORE_NATIONWIDE_INSTANT_PHARMACY_CLOSE_ID, LATITUDE_INSIDE_GEOZONE,
                LONGITUDE_INSIDE_GEOZONE, NATIONWIDE_PRODUCT_ID);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CREATED, response));

        String orderId = komodoHelper.getOrderIdByResponseBody(response);
        Assert.assertTrue(checkStatusUntil(STATUS_APPROVED, orderId), MESSAGE_CHECK_STATUS_FAILED);
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        String deliveryOptionsId = komodoHelper.getDeliveryOptionsId(response, DELAYED_INSTANT_KEY);
        response = timorHelper.deliveryOption(orderId, deliveryOptionsId);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));

        response = komodoHelper.confirmOrder(USER_ENTITY_ID, orderId, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_ID_PATH, MERCHANT_NATIONWIDE_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_LOCATION_ID_PATH, PHARMACY_ALWAYS_CLOSE_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_STORE_ID_PATH, STORE_NATIONWIDE_INSTANT_PHARMACY_CLOSE_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_DELIVERY_TYPE_PATH, DELAYED_INSTANT_KEY));
    }

    @Test
    public void verifyOrderNationwideOutsideGeozone() throws InterruptedException {
        Response response = komodoHelper.createNationwideOrderV4(STORE_NATIONWIDE_PHARMACY_OPEN_ID, LATITUDE_OUTSIDE_GEOZONE, LONGITUDE_OUTSIDE_GEOZONE,
                NATIONWIDE_PRODUCT_ID);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CREATED, response));

        String orderId = komodoHelper.getOrderIdByResponseBody(response);
        Assert.assertTrue(checkStatusUntil(STATUS_APPROVED, orderId), MESSAGE_CHECK_STATUS_FAILED);
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        String deliveryOptionsId = komodoHelper.getDeliveryOptionsId(response, DELAYED_INSTANT_KEY);
        response = timorHelper.deliveryOption(orderId, deliveryOptionsId);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));

        response = komodoHelper.confirmOrder(USER_ENTITY_ID, orderId, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_ID_PATH, MERCHANT_NATIONWIDE_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_LOCATION_ID_PATH, PHARMACY_ALWAYS_OPEN_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_STORE_ID_PATH, STORE_NATIONWIDE_PHARMACY_OPEN_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_DELIVERY_TYPE_PATH, DELAYED_INSTANT_KEY));
    }

    @Test
    public void verifyOrderNonNationwideOutsideGeozone() throws InterruptedException {
        Response response = komodoHelper.createNationwideOrderV4(STORE_NON_NATIONWIDE_ID, LATITUDE_OUTSIDE_GEOZONE, LONGITUDE_OUTSIDE_GEOZONE,
                PRODUCT_NO_REALLOC_ID);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CREATED, response));

        String orderId = komodoHelper.getOrderIdByResponseBody(response);
        Assert.assertTrue(checkStatusUntil(STATUS_APPROVED, orderId), MESSAGE_CHECK_STATUS_FAILED);
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        String deliveryOptionsId = komodoHelper.getDeliveryOptionsId(response, DELAYED_INSTANT_KEY);
        response = timorHelper.deliveryOption(orderId, deliveryOptionsId);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));

        response = komodoHelper.confirmOrder(USER_ENTITY_ID, orderId, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_ID_PATH, MERCHANT_NATIONWIDE_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_LOCATION_ID_PATH, PHARMACY_ALWAYS_OPEN_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_STORE_ID_PATH, STORE_NON_NATIONWIDE_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_DELIVERY_TYPE_PATH, DELAYED_INSTANT_KEY));
    }

    @Test
    public void verifyOrderNonNationwideScheduled() throws InterruptedException {
        Response response = komodoHelper.createNationwideOrderV4(STORE_NON_NATIONWIDE_SCHEDULED_ID, LATITUDE_INSIDE_GEOZONE, LONGITUDE_INSIDE_GEOZONE,
                PRODUCT_NO_REALLOC_SCHEDULED_ID);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CREATED, response));

        String orderId = komodoHelper.getOrderIdByResponseBody(response);
        Assert.assertTrue(checkStatusUntil(STATUS_APPROVED, orderId), MESSAGE_CHECK_STATUS_FAILED);
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        String deliveryOptionsId = komodoHelper.getDeliveryOptionsId(response, SCHEDULED_INSTANT_KEY);
        response = timorHelper.deliveryOption(orderId, deliveryOptionsId);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));

        response = komodoHelper.confirmOrder(USER_ENTITY_ID, orderId, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_ID_PATH, MERCHANT_NATIONWIDE_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_LOCATION_ID_PATH, PHARMACY_ALWAYS_OPEN_SCHEDULED_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_STORE_ID_PATH, STORE_NON_NATIONWIDE_SCHEDULED_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_DELIVERY_TYPE_PATH, SCHEDULED_INSTANT_KEY));
    }

    @Test(groups = { "sanity" })
    public void verifyOrderNonStoreOutsideGeozone() throws InterruptedException {
        Response response = komodoHelper.createNationwideOrderV4(null, LATITUDE_OUTSIDE_GEOZONE, LONGITUDE_OUTSIDE_GEOZONE, NATIONWIDE_PRODUCT_ID);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CREATED, response));

        String orderId = komodoHelper.getOrderIdByResponseBody(response);
        Assert.assertTrue(checkStatusUntil(STATUS_APPROVED, orderId), MESSAGE_CHECK_STATUS_FAILED);
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        String deliveryOptionsId = komodoHelper.getDeliveryOptionsId(response, DELAYED_INSTANT_KEY);
        response = timorHelper.deliveryOption(orderId, deliveryOptionsId);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));

        response = komodoHelper.confirmOrder(USER_ENTITY_ID, orderId, false, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CONFIRMED, response));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_ID_PATH, MERCHANT_NATIONWIDE_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_ORDER_MERCHANT_LOCATION_ID_PATH, PHARMACY_ALWAYS_OPEN_ID));
        Assert.assertTrue(komodoHelper.validateResponseValue(response, ATTRIBUTE_DELIVERY_TYPE_PATH, DELAYED_INSTANT_KEY));
    }

    @Test
    public void verifyNationwideOrderNoMedicineOutsideGeozone() throws InterruptedException {
        Response response = komodoHelper.createNationwideOrderV4(STORE_NATIONWIDE_NON_INSTANT_PHARMACY_OPEN_ID, LATITUDE_OUTSIDE_GEOZONE,
                LONGITUDE_OUTSIDE_GEOZONE, PRODUCT_NO_REALLOC_ID);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_CREATED, response));

        String orderId = komodoHelper.getOrderIdByResponseBody(response);

        checkStatusUntil(STATUS_ON_HOLD, orderId);
        response = komodoHelper.getOrder(USER_ENTITY_ID, orderId, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(komodoHelper.verifyOrderStatus(STATUS_ON_HOLD, response));
    }

    @Test
    public void verifyOrderInactiveStore() {
        Response response = komodoHelper.createNationwideOrderV4(STORE_INACTIVE_ID, LATITUDE_INSIDE_GEOZONE, LONGITUDE_INSIDE_GEOZONE, NATIONWIDE_PRODUCT_ID);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }
}
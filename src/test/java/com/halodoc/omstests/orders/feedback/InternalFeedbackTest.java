package com.halodoc.omstests.orders.feedback;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;


//duplicate scripts --need to delete
@Slf4j
public class InternalFeedbackTest extends OrdersBaseTest {
    @Test
    public void addInternalFeedbackTest() throws InterruptedException {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        String orderId = orderResponse.path("customer_order_id").toString();
        Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Approved");
        orderResponse = timorHelper.getOrderDetails(orderId);
        String price = orderResponse.path("item_total").toString();
        orderResponse = timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
        Assert.assertEquals(orderResponse.statusCode(), HttpStatus.NO_CONTENT.value());
        Assert.assertTrue(checkStatusUntil("confirmed", orderId), "Order Creation Status in not Confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        Integer shipmentId = orderResponse.path("shipments[0].id");
        log.info("Order details: ");
        log.info(timorHelper.getOrderDetails(orderId).toString());
        orderResponse = timorHelper.markOrderAsDelivered(orderId, shipmentId);
        Assert.assertEquals(orderResponse.statusCode(), HttpStatus.NO_CONTENT.value());
        orderResponse = timorHelper.addFeedBackOrder(orderId);
        Assert.assertEquals(orderResponse.statusCode(), HttpStatus.CREATED.value());
    }

    @Test
    public void getInternalFeedbackTest() throws InterruptedException {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        String orderId = orderResponse.path("customer_order_id").toString();
        Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        String price = orderResponse.path("item_total").toString();
        orderResponse = timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
        Assert.assertEquals(orderResponse.statusCode(),  HttpStatus.NO_CONTENT.value());
        orderResponse = timorHelper.getOrderDetails(orderId);
        Integer shipmentId = orderResponse.path("shipments[0].id");
        log.info("Order details: ");
        log.info(timorHelper.getOrderDetails(orderId).toString());
        orderResponse = timorHelper.markOrderAsDelivered(orderId, shipmentId);
        Assert.assertEquals(orderResponse.statusCode(),  HttpStatus.NO_CONTENT.value());
        orderResponse = timorHelper.addFeedBackOrder(orderId);
        Assert.assertEquals(orderResponse.statusCode(),  HttpStatus.CREATED.value());
        Response getFeedbackResponse = timorHelper.getFeedbackOrder(orderId);
        Assert.assertEquals(getFeedbackResponse.statusCode(), HttpStatus.OK.value());
    }

    @Test(enabled = false)
    public void getInternalFeedbackTestByFeedbackId() throws InterruptedException {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        String orderId = orderResponse.path("customer_order_id").toString();
        Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        String price = orderResponse.path("item_total").toString();
        orderResponse = timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
        Assert.assertEquals(orderResponse.statusCode(),  HttpStatus.NO_CONTENT);
        orderResponse = timorHelper.getOrderDetails(orderId);
        Integer shipmentId = orderResponse.path("shipments[0].id");
        log.info("Order details: ");
        log.info(timorHelper.getOrderDetails(orderId).toString());
        orderResponse = timorHelper.markOrderAsDelivered(orderId, shipmentId);
        Assert.assertEquals(orderResponse.statusCode(),  HttpStatus.NO_CONTENT);
        orderResponse = timorHelper.addFeedBackOrder(orderId);
        Assert.assertEquals(orderResponse.statusCode(),  HttpStatus.OK);
        Response getFeedbackResponse = timorHelper.getFeedbackOrder("feedbackId");
        Assert.assertEquals(getFeedbackResponse.statusCode(),  HttpStatus.OK);
    }
}
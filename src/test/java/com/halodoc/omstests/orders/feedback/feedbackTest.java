package com.halodoc.omstests.orders.feedback;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import java.io.IOException;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.timor.TimorUtil;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class feedbackTest {
    TimorUtil timorHelper = new TimorUtil();
    Response orderResponse;
    JSONObject jsonObject;

    @Test
    public void AddFeedbackTest() throws InterruptedException {
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order Creation Failed");
        String orderId = orderResponse.path("customer_order_id").toString();
        timorHelper.checkOrderStatus(orderId,"approved");
        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Order Confirm Failed,but this is status "+ orderResponse.statusCode());
        timorHelper.checkOrderStatus(orderId,"confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        Integer shipmentId = orderResponse.path("shipments[0].id");
        log.info("Order details: ");
        log.info(timorHelper.getOrderDetails(orderId).toString());
        orderResponse=timorHelper.markOrderAsDelivered(orderId,shipmentId);
        Assert.assertEquals(orderResponse.statusCode(),204);
        orderResponse = timorHelper.addFeedBackOrder(orderId);
        Assert.assertEquals(orderResponse.statusCode(),201);
    }

    @Test
    public void getFeedbackTest() throws InterruptedException, IOException {
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order Creation Failed");
        String orderId = orderResponse.path("customer_order_id").toString();
        timorHelper.checkOrderStatus(orderId,"approved");
        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Order Confirm Failed,but this is status "+ orderResponse.statusCode());
        timorHelper.checkOrderStatus(orderId,"confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        Integer shipmentId = orderResponse.path("shipments[0].id");
        log.info("Order details: ");
        log.info(timorHelper.getOrderDetails(orderId).toString());
        orderResponse=timorHelper.markOrderAsDelivered(orderId,shipmentId);
        Assert.assertEquals(orderResponse.statusCode(),204);
        orderResponse = timorHelper.addFeedBackOrder(orderId);
        Assert.assertEquals(orderResponse.statusCode(),201);
        Response getFeedbackResponse = timorHelper.getFeedbackOrder(orderId);
        Assert.assertEquals(getFeedbackResponse.statusCode(),200);
    }
}
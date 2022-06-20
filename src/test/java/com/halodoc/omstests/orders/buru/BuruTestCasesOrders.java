package com.halodoc.omstests.orders.buru;

import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BuruTestCasesOrders extends OrdersBaseTest {

    @Test(groups = { "sanity" }, priority = 1)
    public void getOrderDetails() {
        Response orderDetails = buruHelper.createAndConfirmOrder(timorHelper);
        Response getOrderDetails =  buruHelper.getOrderDetails(orderDetails.path("customer_order_id").toString(),orderDetails.path("items[0].group_id"));
        Assert.assertTrue(getOrderDetails.statusCode() == HttpStatus.SC_OK,"Get order details failed");

    }

    @Test(groups = { "sanity" }, priority = 2)
    public void markReadyForShipment() {
        Response orderDetails = buruHelper.createAndConfirmOrder(timorHelper);
        Response readyResponse =  buruHelper.markReady(orderDetails.path("customer_order_id").toString(),orderDetails.path("items[0].group_id"));
        Assert.assertTrue(readyResponse.statusCode() == HttpStatus.SC_NO_CONTENT,"Mark for Ready Order - Failed");
    }


    @Test(groups = { "sanity" }, priority = 3)
    public void markReadyForDispatch() {
        Response orderDetails = buruHelper.createAndConfirmOrder(timorHelper);
        Response readyResponse =  buruHelper.markReady(orderDetails.path("customer_order_id").toString(),orderDetails.path("items[0].group_id"));
        Assert.assertTrue(readyResponse.statusCode() == HttpStatus.SC_NO_CONTENT,"Mark for Ready Order - Failed");
        Response dispatchResponse =  buruHelper.markDispatch(orderDetails.path("customer_order_id").toString(),orderDetails.path("items[0].group_id"));
        Assert.assertTrue(dispatchResponse.statusCode() == HttpStatus.SC_NO_CONTENT,"Mark for reject Order - Failed");
    }

    @Test(groups = { "sanity" }, priority = 4)
    public void rejectOrder() {
       Response orderDetails = buruHelper.createAndConfirmOrder(timorHelper);
       Response rejectResponse =  buruHelper.markReject(orderDetails.path("customer_order_id").toString(),orderDetails.path("items[0].group_id"));
       Assert.assertTrue(rejectResponse.statusCode() == HttpStatus.SC_NO_CONTENT,"Mark for reject Order - Failed");
    }
}
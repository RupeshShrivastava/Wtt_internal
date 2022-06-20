package com.halodoc.omstests.orders.timor;

import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.halodoc.oms.orders.utilities.constants.Constants.PRODUCT_LISTING;
import static com.halodoc.oms.orders.utilities.constants.Constants.USER_ENTITY_ID;

@Slf4j
public class MultiplePharmaTests extends OrdersBaseTest {
    /* */
    protected String orderID;
    protected Integer trackingID;
    protected  String groupId;
    @Test(groups = {"sanity"}, priority = 1)
    public void createMPOrder() throws InterruptedException {

        Response orderResponse = timorHelper.createInternalOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Order Creation Failed");
        orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getInternalOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
    }

    @Test(groups = {"sanity"},dependsOnMethods ="createMPOrder")
    private void checkIfgroupIdGenerated() {
        Assert.assertTrue(checkIfGroupGenerated(orderID));
    }

    @Test(groups = {"sanity"},dependsOnMethods ="checkIfgroupIdGenerated")
    private void updateDeliveryOptions() {
        Response orderResponse = orderResponse = timorHelper.getInternalOrderDetails(orderID);
        Assert.assertTrue(updateDelOption(orderResponse));
    }

    @Test(groups = {"sanity"},dependsOnMethods ="updateDeliveryOptions")
    private void confirmMPorder() {
        Response orderResponse = timorHelper.getInternalOrderDetails(orderID);
        orderResponse = timorHelper.confirmInternalOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getInternalOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");

    }
    @Test(groups = {"sanity"},dependsOnMethods ="confirmMPorder")
    private void checkIfShipmentGenerated() {
        Response orderResponse = timorHelper.getInternalOrderDetails(orderID);
        groupId = orderResponse.path("items.group_id[0]");
        trackingID= (orderResponse.path("shipments.id[0]"));
        Assert.assertNotNull(groupId);
        Assert.assertNotNull(trackingID);
    }


    @Test(groups = {"sanity"},dependsOnMethods ="checkIfShipmentGenerated")
    public void merchantCancelMPOrder() {
        Response orderResponse =
                orderResponse = timorHelper.getInternalOrderDetails(orderID);
        Assert.assertEquals(timorHelper.mpmerchantCancelOrder(orderID,groupId).statusCode(),204);
    }

    @Test(groups = {"sanity"},dependsOnMethods ="merchantCancelMPOrder")
    private void markShipmentAsDelivered() {
//        Response orderResponse = timorHelper.getInternalOrderDetails(orderID);
//        timorHelper.multiShipmentMarkDelivered(orderID,orderResponse);
    }
}

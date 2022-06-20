package com.halodoc.omstests.orders.timor;

import static com.halodoc.oms.orders.utilities.constants.Constants.PRODUCT_LISTING;
import static com.halodoc.oms.orders.utilities.constants.Constants.USER_ENTITY_ID;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;

public class VerifyRetryPayment extends OrdersBaseTest {
    /* */
    protected String orderID;

    protected Integer trackingID;

    protected String groupId;

    protected String payment_id;

    private final String[] adjustments = new String[] { "card", "gopay", "mandiri_va", "linkaja", "wallet", "bca_va" };

    @Test (groups = { "sanity" }, priority = 1)
    public void createMPOrder() throws InterruptedException {
        Response orderResponse = timorHelper.createInternalOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Order Creation Failed");
        orderID = "";
        orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getInternalOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkIfGroupGenerated(orderID));
        orderResponse = timorHelper.getInternalOrderDetails(orderID);
        Assert.assertTrue(updateDelOption(orderResponse));
    }

    @Test (groups = { "sanity" }, priority = 3)
    public void verifyRefreshApi() {
        timorHelper.chargeXenditVA(orderID);
        Assert.assertEquals(timorHelper.hitRefreshApi(orderID).statusCode(), 200);
        Response orderResponse = timorHelper.getInternalOrderDetails(orderID);
        Assert.assertEquals(timorHelper.confirmOrder(orderID, USER_ENTITY_ID, orderResponse.path("item_total").toString()).statusCode(), 204);
    }

    @Test (groups = { "sanity" }, priority = 4)
    public void retryRefreshApi() {
        Assert.assertEquals(timorHelper.hitRefreshApi(orderID).statusCode(), 422);
    }

    @Test (groups = { "sanity" }, priority = 5)
    public void verifyAdjustments() {
        Assert.assertTrue(timorHelper.verifyCouponApplied(orderID, "AutoWallet"));
    }
}

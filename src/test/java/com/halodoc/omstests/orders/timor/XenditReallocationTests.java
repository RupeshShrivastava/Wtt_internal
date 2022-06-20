package com.halodoc.omstests.orders.timor;

import static com.halodoc.oms.orders.utilities.constants.Constants.USER_ENTITY_ID;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XenditReallocationTests extends OrdersBaseTest {
    /* */
    protected String orderID;

    protected Integer trackingID;

    protected String groupId;

    protected String payment_id;

    private final String[] adjustments = new String[] { "card", "gopay", "mandiri_va", "linkaja", "wallet", "bca_va" };

    @Test (groups = { "sanity" }, priority = 1)
    public void createMPOrderForReallocation() throws InterruptedException {
        Response orderResponse = timorHelper.createInternalOrderForReallocation(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Order Creation Failed");
        orderID = "";
        orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getInternalOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkIfGroupGenerated(orderID));
        orderResponse = timorHelper.getInternalOrderDetails(orderID);
        Assert.assertTrue(updateDelOption(orderResponse));
    }

    @Test (groups = { "sanity" }, priority = 2)
    public void payForReallocationOrder() {
        payment_id = timorHelper.chargeXenditVA(orderID);
        if (!payment_id.isEmpty()) {
            Response orderResponse = timorHelper.getInternalOrderDetails(orderID);
            String amount = orderResponse.path("total").toString();
            Assert.assertEquals(timorHelper.simulateVA(payment_id, amount).statusCode(), 200);
        }
        timorHelper.chargeXenditVA(orderID);
        Response orderResponse = timorHelper.getInternalOrderDetails(orderID);
        String status = orderResponse.path("status").toString();
        Assert.assertEquals(status, "confirmed");
    }

    @Test (groups = { "sanity" }, priority = 3)
    public void cancelOrderForReallocation() {
        Response orderResponse = timorHelper.getOrderDetailsV2(orderID);
        List<String> items = orderResponse.path("items.name");
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equalsIgnoreCase("Calpol 500mg Tablet")) {
                groupId = orderResponse.path("items.group_id[" + i + "]");
            }
        }
        orderResponse = timorHelper.cancelOrderWithGroupID(orderID, groupId);
        Assert.assertEquals(orderResponse.statusCode(), 204);
    }

    @Test (groups = { "sanity" }, priority = 4)
    public void verifyReallocationSuccess() {
        Assert.assertTrue(timorHelper.waitForReallocationToComplete(orderID));
    }
}

package com.halodoc.omstests.orders.timor;

import static com.halodoc.oms.orders.utilities.constants.Constants.PRODUCT_LISTING;
import static com.halodoc.oms.orders.utilities.constants.Constants.USER_ENTITY_ID;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XenditPaymentTests extends OrdersBaseTest {
    /* */
    protected String orderID;

    protected Integer trackingID;

    protected String groupId;

    protected String payment_id;

    private final String[] adjustmentsvalues = new String[] { "card", "gopay", "mandiri_va", "linkaja", "wallet", "bca_va" };

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

    @Test (groups = { "sanity" }, priority = 2)
    public void verifyRefresAhpi() {
        timorHelper.chargeXenditVA(orderID);
        Assert.assertEquals(timorHelper.hitRefreshApi(orderID).statusCode(), 200);
    }

    @Test (groups = { "sanity" }, priority = 3)
    public void verifyRetryPayment() {
        payment_id = timorHelper.chargeXenditVA(orderID);
        Assert.assertTrue(!payment_id.isEmpty());
    }

    @Test (priority = 4)
    public void verifyApplicableAdjustments() {
        Response orderResponse = timorHelper.getInternalOrderDetails(orderID);
        List<String> adjustments = orderResponse.path("applicable_adjustments.value");
        Assert.assertTrue(adjustments.containsAll(Arrays.asList(adjustmentsvalues)));
    }

    @Test (groups = { "sanity" }, priority = 5)
    public void verifyAdjustments() {
        Assert.assertTrue(timorHelper.verifyCouponApplied(orderID, "BCA"));
    }

    @Test (groups = { "sanity" }, priority = 6)
    public void confirmOrder() {
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

    @Test (groups = { "sanity" }, priority = 7)
    public void cancelOrderVerifyRefund() {
        Response response = timorHelper.cancelOrder(orderID);
        Assert.assertEquals(response.statusCode(), 204);
        Assert.assertTrue(timorHelper.verifyIfRefundInitiated(orderID));
    }

}

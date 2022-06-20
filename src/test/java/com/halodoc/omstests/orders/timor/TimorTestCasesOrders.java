package com.halodoc.omstests.orders.timor;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.CUSTOMER_STAGE;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.timorV2order;
import java.io.IOException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import com.halodoc.oms.orders.utilities.timor.TimorUtil;
import com.halodoc.omstests.orders.OrdersBaseTest;
import ch.qos.logback.classic.Level;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

//TODO: break into multiple test classes
@Slf4j
public class TimorTestCasesOrders {

    TimorUtil timorHelper = new TimorUtil();
        Response orderResponse;
        JSONObject jsonObject;

    @BeforeTest (alwaysRun = true)
    public void setUp() throws IOException {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
        ch.qos.logback.classic.Logger root1 = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("io.netty");
        ch.qos.logback.classic.Logger root2 = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.redisson");
        root.setLevel(Level.OFF);
        root1.setLevel(Level.OFF);
        root2.setLevel(Level.OFF);
    }

    @Test(groups = { "sanity" },priority = 1)
    public void createAndConfirmOrder() throws InterruptedException {
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        timorHelper.checkOrderStatus(orderID,"approved");
        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Order Confirm Failed,but this is status "+ orderResponse.statusCode());
        timorHelper.checkOrderStatus(orderID,"confirmed");
    }


    @Test(groups = { "sanity" },priority = 2)
    public void cancelOrder() throws InterruptedException {
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        timorHelper.checkOrderStatus(orderID,"approved");
        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Order Confirm Failed,but this is status "+ orderResponse.statusCode());
        timorHelper.checkOrderStatus(orderID,"confirmed");

        orderResponse = timorHelper.cancelOrder(orderID);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Order cancelled Failed,but this is status "+ orderResponse.statusCode());
        timorHelper.checkOrderStatus(orderID,"cancelled");
    }


    @Test(groups = { "sanity" },priority = 3)
    public void abandonOrder() throws InterruptedException {
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        timorHelper.checkOrderStatus(orderID,"approved");

        orderResponse = timorHelper.abandonOrder(orderID);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT,
                "Order abandon Failed,but this is status "+ orderResponse.statusCode());

        timorHelper.checkOrderStatus(orderID,"abandoned");
    }


    @Test(groups = { "sanity" },priority = 4)
    public void trackOrder() {
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        timorHelper.checkOrderStatus(orderID,"approved");
        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Order Confirm Failed,but this is status "+ orderResponse.statusCode());
        timorHelper.checkOrderStatus(orderID,"confirmed");

        orderResponse = timorHelper.cancelOrder(orderID);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Order cancelled Failed,but this is status "+ orderResponse.statusCode());
        timorHelper.checkOrderStatus(orderID,"cancelled");

        orderResponse = timorHelper.trackOrder(orderID);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT,
                "Order track Failed,but this is status "+ orderResponse.statusCode()+"\n Response \n"+orderResponse.statusCode());
    }


    @Test(groups = { "sanity" },priority = 5)
    public void createOrderForPendingLead() {
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_CREATED, "Order create lead is Failed");

        String leadId = orderResponse.path("external_id").toString();
        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order get lead is Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("pending", orderResponse));

        orderResponse = timorHelper.createOrderForLeads(USER_ENTITY_ID, leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_CREATED, "Create Order for Lead - Failed");

        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode()==HttpStatus.SC_OK, "Get Lead - Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("processed", orderResponse));
    }


    @Test(groups = { "sanity" },priority = 6)
    public void getLeadById() {
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_CREATED, "Order create lead is Failed");

        String leadId = orderResponse.path("external_id").toString();
        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order get lead is Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("pending", orderResponse));
    }


    @Test(groups = { "sanity" },priority = 7)
    public void rejectLead() {
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_CREATED, "Order create lead is Failed");

        String leadId = orderResponse.path("external_id").toString();
        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order get lead is Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("pending", orderResponse));

        orderResponse = timorHelper.rejectLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Reject Lead - Failed");

        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK , "Get Lead - Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("rejected", orderResponse));
    }


    @Test(groups = { "sanity" },priority = 8)
    public void CreateOrderForRejectedLead() {
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_CREATED, "Order create lead is Failed");

        String leadId = orderResponse.path("external_id").toString();
        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order get lead is Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("pending", orderResponse));

        orderResponse = timorHelper.rejectLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Reject Lead - Failed");

        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK , "Get Lead - Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("rejected", orderResponse));

        orderResponse = timorHelper.createOrderForLeads(USER_ENTITY_ID, leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NOT_FOUND,
                "Create Order for Rejected Lead - Unexpected Error!!");

        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK , "Get Lead - Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("rejected", orderResponse));
    }


    @Test(groups = { "sanity" },priority = 9)
    public void rejectLeadAfterCreateOrder() {
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_CREATED, "Order create lead is Failed");

        String leadId = orderResponse.path("external_id").toString();
        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order get lead is Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("pending", orderResponse));

        orderResponse = timorHelper.createOrderForLeads(USER_ENTITY_ID, leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_CREATED, "Create Order for Rejected Lead - Unexpected Error!!");
        Assert.assertTrue(timorHelper.verifyOrderStatus("created", orderResponse));

        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order get lead is Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("processed", orderResponse));

        orderResponse = timorHelper.rejectLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_BAD_REQUEST, "Reject Lead - Failed");
    }


    @Test(groups = { "sanity" },priority = 10)
    public void confirmOrderCreatedForPendingLeadAndCreateOrderForProcessedLead() {
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_CREATED, "Order create lead is Failed");

        String leadId = orderResponse.path("external_id").toString();
        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order get lead is Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("pending", orderResponse));

        orderResponse = timorHelper.createOrderForLeads(USER_ENTITY_ID, leadId);
        String orderID = orderResponse.path("customer_order_id");
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_CREATED, "Order create lead is Failed");


        orderResponse = timorHelper.getOrderDetails(orderID);
        String price = orderResponse.path("item_total").toString();
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order get lead is Failed");

        orderResponse = timorHelper.confirmOrder(orderID, USER_ENTITY_ID, price);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Order Confirm Failed ,found this status "
                +orderResponse.statusCode()+"\n response \n"+orderResponse.asString());

        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Get Lead - Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("processed", orderResponse));

        orderResponse = timorHelper.createOrderForLeads(USER_ENTITY_ID, leadId);
        Assert.assertTrue(orderResponse.statusCode() ==HttpStatus.SC_NOT_FOUND, "Create Order for Processed Lead - Unexpected Error Code");

        orderResponse = timorHelper.getLead(leadId);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Get Lead - Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("processed", orderResponse));
    }


    @Test(groups = { "sanity" },priority = 11)
    public void applyValidCouponAndConfirmOrder() {
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        timorHelper.checkOrderStatus(orderID,"approved");

        orderResponse = timorHelper.applyCoupon(orderID, "dop_backend");
        Assert.assertTrue(orderResponse.statusCode()==HttpStatus.SC_OK,
                "found this status "+orderResponse.statusCode()+" and error "+orderResponse.asString());
        timorHelper.checkOrderStatus(orderID,"approved");

        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Order Confirm Failed,but this is status "+ orderResponse.statusCode());
        timorHelper.checkOrderStatus(orderID,"confirmed");

    }

    @Test(groups = { "sanity" },priority = 12)
    public void applyInvalidCoupon() {
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        timorHelper.checkOrderStatus(orderID,"approved");
        String price = orderResponse.path("item_total").toString();

        orderResponse = timorHelper.applyCoupon(orderID, "pd_m22ax");
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NOT_FOUND, "Apply Invalid Coupon - Failed");
        Assert.assertTrue(timorHelper.validateErrorCode("3023", orderResponse), "Apply Invalid Coupon - Unexpected Error");

        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order Get - Failed");
        Assert.assertTrue(timorHelper.validateSimple(price, orderResponse.path("item_total").toString()), "Apply Coupon - Failed");
    }

    @Test(groups = { "sanity" },priority = 13)
    public void applyAndDeleteValidCouponAndConfirmOrder() {
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        timorHelper.checkOrderStatus(orderID,"approved");
        String itemTotal = orderResponse.path("item_total").toString();

        orderResponse = timorHelper.applyCoupon(orderID, "dop_backend");
        Assert.assertTrue(orderResponse.statusCode()==HttpStatus.SC_OK,
                "found this status "+orderResponse.statusCode()+" and error "+orderResponse.asString());
        timorHelper.checkOrderStatus(orderID,"approved");

        orderResponse = timorHelper.deleteCoupon(orderID, "dop_backend");
        Assert.assertTrue(orderResponse.statusCode()==HttpStatus.SC_OK,
                "found this status "+orderResponse.statusCode()+" and error "+orderResponse.asString());
        timorHelper.checkOrderStatus(orderID,"approved");
        Assert.assertTrue(timorHelper.validateSimple(itemTotal, orderResponse.path("item_total").toString()), "Apply Coupon - Failed");

        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Order Confirm Failed,but this is status "+ orderResponse.statusCode());
        timorHelper.checkOrderStatus(orderID,"confirmed");
    }

    @Test(groups = { "sanity" },priority = 14)
    public void createAndConfirmOrderForReallocation() {
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_OK, "Order Creation Failed");
        String orderId = orderResponse.path("customer_order_id").toString();
        timorHelper.checkOrderStatus(orderId,"approved");
        String price = orderResponse.path("item_total").toString();

        orderResponse = timorHelper.applyCoupon(orderId, "dop_backend");
        Assert.assertTrue(orderResponse.statusCode()==HttpStatus.SC_OK,
                "found this status "+orderResponse.statusCode()+" and error "+orderResponse.asString());
        timorHelper.checkOrderStatus(orderId,"approved");

        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Order Confirm Failed,but this is status "+ orderResponse.statusCode());
        timorHelper.checkOrderStatus(orderId,"confirmed");
        String MERCHANT_LOCATION_ID = orderResponse.path("attributes.merchant_location_id").toString();

        Assert.assertTrue(orderResponse.statusCode() == HttpStatus.SC_NO_CONTENT, "Order cancelled Failed,but this is status "+ orderResponse.statusCode());
        timorHelper.checkOrderStatus(orderId,"cancelled");

        orderResponse = timorHelper.getOrderDetails(orderId);
        timorHelper.checkOrderStatus(orderId,"confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        Boolean verifyMerchant = timorHelper.validateSimple(MERCHANT_LOCATION_ID, orderResponse.path("attributes.reallocated_merchant_location_id").toString());
        Assert.assertTrue((!verifyMerchant), "Merchant Location ID not changed after Reject -  Failed in Reallocation");
    }
}
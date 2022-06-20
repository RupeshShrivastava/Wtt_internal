package com.halodoc.omstests.orders.timor;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by gauravmehta
 * since  29/06/20.
 */
/*
TODO: Add util for ORDERS flow .
 */
@Slf4j
public class OrdersRegressionTest extends OrdersBaseTest {
    @Test
    public void orderCustomerCancel() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");

        String orderID = orderResponse.path("customer_order_id").toString();
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Approved Failed");
        orderResponse = timorHelper.customerCancelOrder(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("cancelled", orderID), "Order Creation Failed");
    }

    @Test
    public void orderCustomerAbandon() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Approved Failed");
        orderResponse = timorHelper.abandonOrder(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("abandoned", orderID), "Order Abandon Failed");
    }

    @Test
    public void orderMerchant_cancelledAbandon() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Status in not Approved");
        orderResponse = timorHelper.cancelOrder(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("cancelled", orderID), "Order Creation Failed");
    }

    @Test
    public void verifyReAllocationFailCustomerCancel() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Approved Failed");
        orderResponse = timorHelper.customerCancelOrder(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("cancelled", orderID), "Order Creation Failed");
    }

    @Test
    public void cancelOrderByGroupID() {
        Response orderResponse;
        orderResponse = timorHelper.createOrderForReallocation(USER_ENTITY_ID,PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Create Order Failed");
        String orderId = orderResponse.path("customer_order_id").toString();
        timorHelper.getOrderDetails(orderId);
      //  Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        String price = orderResponse.path("item_total").toString();
        orderResponse = timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status is not Confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        String groupID = orderResponse.path("items.group_id").toString().replace("[", "").replace("]", "");
        log.info(groupID);
        orderResponse = timorHelper.cancelOrderWithGroupID(orderId, groupID);
        log.info(orderResponse.toString());
        orderResponse = timorHelper.getOrderDetails(orderId);
        Assert.assertTrue(timorHelper.verifyOrderStatus("cancelled", orderResponse), "Cancel order by group od failed");
    }

    @Test
    public void trackOrderByid() {
        Response orderResponse;
        orderResponse = timorHelper.createOrderForReallocation(USER_ENTITY_ID,PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Create Order Failed");
        String orderId = orderResponse.path("customer_order_id").toString();
        timorHelper.getOrderDetails(orderId);
        Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        String price = orderResponse.path("item_total").toString();
        timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
        orderResponse = timorHelper.trackOrder(orderId);
        log.info(orderResponse.toString());
        timorHelper.getOrderDetails(orderId);
        log.info("Order details: ");
        log.info(timorHelper.getOrderDetails(orderId).toString());
    }

    @Test
    public void confirmOrderWithInsufficientWallet() {
        Response orderResponse;
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Create Order Failed");
        String orderId = orderResponse.path("customer_order_id").toString();
        timorHelper.getOrderDetails(orderId);
        Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        String price = orderResponse.path("item_total").toString();
        timorHelper.updateCart(orderId);
        orderResponse = timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
        log.info(orderResponse.toString());
    }

    @Test
    public void verifyONholdOrder() {
        Response orderResponse;
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_QUANTITY_ZERO);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Create Order Failed");
        String orderId = orderResponse.path("customer_order_id").toString();
        timorHelper.getOrderDetails(orderId);
        Assert.assertTrue(checkStatusUntil("on_hold", orderId), "Order Creation Status in not Confirmed");
    }

    @Test
    public void verifyRefundAmountForCancelOrder() {
        Response orderResponse;
        orderResponse = timorHelper.createOrder(USER_ENTITY_ID, "78e2fc95-39e0-4d7f-8767-1c077742e26c");
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Create Order Failed");
        String orderId = orderResponse.path("customer_order_id").toString();
        Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Approved");
        orderResponse = timorHelper.getOrderDetails(orderId);

        String price = orderResponse.path("item_total").toString();
        timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
        Assert.assertTrue(checkStatusUntil("confirmed", orderId), "Order Creation Status in not Confirmed");
        timorHelper.cancelOrder(orderId);
        Assert.assertTrue(checkStatusUntil("cancelled", orderId), "Order Creation Status in not Confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        float refundAmount = (orderResponse.path("payments[1].amount"));
        String paidAmount = orderResponse.path("payments[0].amount").toString();
        Assert.assertEquals(Double.valueOf(refundAmount), Double.valueOf(paidAmount));
    }

    @Test
    public void verifyRefundAmountForReallocationCancelOrder() {
        Response orderResponse;
        orderResponse = timorHelper.createOrderForReallocation(USER_ENTITY_ID,PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Create Order Failed");
        String orderId = orderResponse.path("customer_order_id").toString();
        Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Approved");
        orderResponse = timorHelper.getOrderDetails(orderId);

        String price = orderResponse.path("item_total").toString();
        timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
        Assert.assertTrue(checkStatusUntil("confirmed", orderId), "Order Creation Status in not Confirmed");
        timorHelper.cancelOrder(orderId);
        log.info("Cancelled order by 1st merchant");
        Assert.assertTrue(checkStatusUntil("confirmed", orderId), "Order Creation Status in not Confirmed");
        timorHelper.cancelOrder(orderId);
        Assert.assertTrue(checkStatusUntil("cancelled", orderId), "Order Creation Status in not Confirmed");
        log.info("2nd merchant cancelled order ");
        orderResponse = timorHelper.getOrderDetails(orderId);
        float refundAmount = timorHelper.getTotalPaymentAmountByType(orderResponse, "refund");
        String paidAmount = orderResponse.path("payments[0].amount").toString();
        Assert.assertEquals(Double.valueOf(refundAmount), Double.valueOf(paidAmount));
    }

    @Test(priority = 6)
    public void createSelfPaymentCard() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");

        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        String price = orderResponse.path("item_total").toString();

        orderResponse = timorHelper.paymentInitialise(orderID, "card", price);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Payment Initialise - Failed");

        orderResponse = timorHelper.confirmOrder(orderID, USER_ENTITY_ID, price);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");

        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
    }

    @Test(priority = 7)
    public void createSelfPaymentGopay() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");

        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        String price = orderResponse.path("item_total").toString();

        orderResponse = timorHelper.paymentInitialise(orderID, "gopay", price);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Payment Initialise - Failed");

        orderResponse = timorHelper.confirmOrder(orderID, USER_ENTITY_ID, price);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");

        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
    }

    @Test(priority = 8)
    public void createDependentPaymentWallet() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");

        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        String price = orderResponse.path("item_total").toString();

        orderResponse = timorHelper.confirmOrder(orderID, DEPENDENT_PATIENT_ID, price);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");

        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
    }

    @Test(priority = 9)
    public void createDependentPaymentCard() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");

        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        String price = orderResponse.path("item_total").toString();

        orderResponse = timorHelper.paymentInitialise(orderID, "card", price);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Payment Initialise - Failed");

        orderResponse = timorHelper.confirmOrder(orderID, DEPENDENT_PATIENT_ID, price);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");

        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
    }

    @Test(priority = 10)
    public void createDependentPaymentGopay() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");

        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        String price = orderResponse.path("item_total").toString();

        orderResponse = timorHelper.paymentInitialise(orderID, "gopay", price);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Payment Initialise - Failed");

        orderResponse = timorHelper.confirmOrder(orderID, DEPENDENT_PATIENT_ID, price);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");

        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
    }


    @Test
    public void shipNonInstantOrder() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");

        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        String price = orderResponse.path("item_total").toString();
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        timorHelper.confirmOrder(orderID, DEPENDENT_PATIENT_ID, price);
        orderResponse = timorHelper.getOrderDetails(orderID);
        int trackingID = (orderResponse.path("shipments.id[0]"));
        orderResponse = timorHelper.markOrderAsShipped(orderID, trackingID);
        Assert.assertEquals(orderResponse.getStatusCode(), 204);
    }

    @Test
    public void deliverNonInstantOrder() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");

        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        String price = orderResponse.path("item_total").toString();
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        timorHelper.confirmOrder(orderID, DEPENDENT_PATIENT_ID, price);
        orderResponse = timorHelper.getOrderDetails(orderID);
        Integer trackingID = (orderResponse.path("shipments.id[0]"));
        orderResponse = timorHelper.markOrderAsDelivered(orderID, trackingID);
        Assert.assertEquals(orderResponse.getStatusCode(), 204);
    }

    @Test
    public void verifyShipment_history() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");

        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        String price = orderResponse.path("item_total").toString();
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        timorHelper.confirmOrder(orderID, DEPENDENT_PATIENT_ID, price);
        Assert.assertTrue(checkStatusUntil("confirmed", orderID), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        int trackingID = (orderResponse.path("shipments.id[0]"));
        orderResponse = timorHelper.markOrderAsDelivered(orderID, trackingID);
        Assert.assertEquals(orderResponse.getStatusCode(), 204);
        orderResponse=timorHelper.getOrderDetails(orderID);
        List<String> shipment_audits=orderResponse.path("shipments.shipment_audits.action_type[0]");
        Assert.assertTrue(!shipment_audits.isEmpty());
    }

    @Test
    public void verifyShipment_historyDeliveredOrder() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        String price = orderResponse.path("item_total").toString();
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        timorHelper.confirmOrder(orderID, DEPENDENT_PATIENT_ID, price);
        Assert.assertTrue(checkStatusUntil("confirmed", orderID), "Order Confirmed Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        int trackingID = (orderResponse.path("shipments.id[0]"));
        orderResponse = timorHelper.markOrderAsDelivered(orderID, trackingID);
        Assert.assertEquals(orderResponse.getStatusCode(), 204);
        orderResponse=timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateShipmentDelivered(orderResponse));
    }

    @Test
    public void verifyShipment_historyShippedOrder() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        String price = orderResponse.path("item_total").toString();
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        timorHelper.confirmOrder(orderID, DEPENDENT_PATIENT_ID, price);
        Assert.assertTrue(checkStatusUntil("confirmed", orderID), "Order Confirm Failed");
        log.info("wait for sometime");
        orderResponse = timorHelper.getOrderDetails(orderID);
        log.info(timorHelper.getOrderDetails(orderID).toString());
        int trackingID = (orderResponse.path("shipments.id[0]"));
        orderResponse = timorHelper.markOrderAsShipped(orderID, trackingID);
        Assert.assertEquals(orderResponse.getStatusCode(), 204);
        orderResponse=timorHelper.getOrderDetails(orderID);
        String shipmentActionType1 = orderResponse.path("shipments[0].shipment_audits[0].action_type");
        String shipmentActionType2 = orderResponse.path("shipments[1].shipment_audits[0].action_type");
        Assert.assertTrue(shipmentActionType1.equals("shipped") || shipmentActionType2.equals("shipped"));
        timorHelper.customerCancelOrder(orderID);
    }
}
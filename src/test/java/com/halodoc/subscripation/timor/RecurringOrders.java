package com.halodoc.subscripation.timor;

import java.util.HashMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import com.halodoc.oms.orders.utilities.pdSubscription.TimorSubscriptionUtils;
import ch.qos.logback.classic.Level;
import io.qameta.allure.Description;

public class RecurringOrders {

    CommonUtils commonUtils = new CommonUtils();
    TimorSubscriptionUtils timorSubscriptionUtils = new TimorSubscriptionUtils();
    String orderId;
    String userId = "ecc720ca-1b57-433d-9ce8-4a8f7732fee8",listingId="503eae39-6c32-4413-aa5e-40bafd2e2116",
            packageId="f8a1300e-934b-4590-8ee9-5a53ee23f409",fulfillId="269a26b5-15b1-4cbf-9ac5-adfa473778b0";
    HashMap<String,String> data = new HashMap<>();

    @BeforeClass
    public void setup(){
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
        root.setLevel(Level.OFF);
    }

    @Description ("order with 1 shipment and 1 subcribe product delivered")
    @Test
    public void shipment1SubscribeProduct1() throws InterruptedException {
        // create order(1 subscribe) --> get order - approved order -> update order with deliery type -> charge gopay api -> confirm gopay -> shipped -> delivered

        orderId = timorSubscriptionUtils.createOrder(userId, listingId, packageId, fulfillId, "create-order");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"delayed_instant");
        timorSubscriptionUtils.updateCart(data, "update-order");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        timorSubscriptionUtils.orderShipAndDeliver(data.get("customer_order_id"), data.get("shipemntId_0"));
        data.clear();
    }
}

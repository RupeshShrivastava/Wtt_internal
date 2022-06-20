package com.halodoc.subscripation.timor;

import java.util.HashMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import com.halodoc.oms.orders.utilities.pdSubscription.TimorSubscriptionUtils;
import ch.qos.logback.classic.Level;
import io.qameta.allure.Description;

public class OrderWith2ShipmentProductSubscription extends CommonUtils{

    TimorSubscriptionUtils timorSubscriptionUtils = new TimorSubscriptionUtils();

    String orderId;

    String userId = "c4ab50ad-8f1c-4b40-a01c-df37d0902117", listingId = "503eae39-6c32-4413-aa5e-40bafd2e2116", packageId = "f8a1300e-934b-4590-8ee9-5a53ee23f409", fulfillId = "269a26b5-15b1-4cbf-9ac5-adfa473778b0";

    HashMap<String, String> data = new HashMap<>();

    @BeforeClass
    public void setup() {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
        root.setLevel(Level.OFF);
    }

    @Description ("order with 2 shipment and 1 subcribe product+1 non subcribe delivered")
    @Test
    public void shipment2Plus1SubscribeProductPlusNonSubcribeProduct() throws InterruptedException {
        // create order(1 subscribe + 1 non subscribe) different shipemnt

        orderId = timorSubscriptionUtils.createOrder(userId,listingId,packageId,fulfillId,"create-order-withNonSubcribeProduct-2shipment");
        data = timorSubscriptionUtils.getOrderDetails(orderId,"approved",true,"instant");
        timorSubscriptionUtils.updateCart(data,"update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId,"confirmed",false,""));
        timorSubscriptionUtils.orderShipAndDeliver(data.get("customer_order_id"),data.get("shipemntId_0"));
        timorSubscriptionUtils.orderShipAndDeliver(data.get("customer_order_id"),data.get("shipemntId_1"));
        data.clear();
    }

    @Description("order with 2 shipment and 2 subcribe product delivered")
    @Test
    public void shipment1SubscribeProduct2Delivered() throws InterruptedException {
        // create order(2 subscribe) different shipemnt

        orderId = timorSubscriptionUtils.createOrder(userId,listingId,packageId,fulfillId,"create-order-withNonSubcribeProduct-2shipment");
        data = timorSubscriptionUtils.getOrderDetails(orderId,"approved",true,"instant");
        timorSubscriptionUtils.updateCart(data,"update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId,"confirmed",false,""));
        timorSubscriptionUtils.orderShipAndDeliver(data.get("customer_order_id"),data.get("shipemntId_0"));
        timorSubscriptionUtils.orderShipAndDeliver(data.get("customer_order_id"),data.get("shipemntId_1"));
        data.clear();
    }

}


//driver cancel gojek and shipper 


// create order(1 subscribe) --> driver cancel
// create order(2 subscribe) same shipemnt -> driver cancel
// create order(2 subscribe) different shipemnt -> one shipement driver cancel
// create order(2 subscribe) different shipemnt -> both shipement driver cancel
// create order(1 subscribe + 1 non subscribe) different shipemnt -> driver cancel
// create order(1 subscribe + 1 non subscribe) same shipemnt   -> -> both shipemnt driver cancel
// create order(1 subscribe + 1 non subscribe) same shipemnt   -> -> subscribe driver cancel + other delivered
// create order(1 subscribe + 1 non subscribe) same shipemnt   -> -> subscribe driver delivered + other cancel


// insurance user should have benefits on subcribe items ..it should treat normal order

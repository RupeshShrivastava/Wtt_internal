package com.halodoc.subscripation.timor;

import java.util.HashMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import com.halodoc.oms.orders.utilities.pdSubscription.TimorSubscriptionUtils;
import ch.qos.logback.classic.Level;
import io.qameta.allure.Description;

public class OrderWith1Shipment1ProductSubscription extends CommonUtils{

    TimorSubscriptionUtils timorSubscriptionUtils = new TimorSubscriptionUtils();

    String orderId;

    //String userId = "c4ab50ad-8f1c-4b40-a01c-df37d0902117", listingId = "503eae39-6c32-4413-aa5e-40bafd2e2116", packageId = "f8a1300e-934b-4590-8ee9-5a53ee23f409", fulfillId = "269a26b5-15b1-4cbf-9ac5-adfa473778b0";
    String userId = "03306f60-9289-4b0e-9558-7f907011b993", listingId = "206886fd-3a27-47ee-861f-c94a9d804808", packageId = "8b1fd474-5ceb-49da-a60d-d15e8ee173b8", fulfillId = "5355c01d-fe44-4ffc-bfac-83bdb29ef1f0";

    HashMap<String, String> data = new HashMap<>();

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

    @Description ("order with 1 shipment and 1 subcribe product plus 1 non subcribe product delivered")
    @Test
    public void shipment1SubscribeProduct1PlusNonSubcribeProduct1() throws InterruptedException {
        // create order(1 subscribe + 1 non subscribe) same shipemnt

        orderId = timorSubscriptionUtils.createOrder(userId, listingId, packageId, fulfillId, "create-order-withNonSubcribeProduct");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"instant");
        timorSubscriptionUtils.updateCart(data, "update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        timorSubscriptionUtils.cancelOrderSubscription(data.get("customer_order_id"));
        data.clear();
    }

    @Description ("order with 1 shipment and 2 subcribe product delivered")
    @Test
    public void shipment1SubscribeProduct2Delivered() throws InterruptedException {
        // create order(2 subscribe) same shipemnt

        orderId = timorSubscriptionUtils.createOrder(userId, listingId, packageId, fulfillId, "create-order-2SubcribeProduct");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"instant");
        timorSubscriptionUtils.updateCart(data, "update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        timorSubscriptionUtils.orderShipAndDeliver(data.get("customer_order_id"), data.get("shipemntId_0"));
        data.clear();
    }


    @Description ("order with 1 shipment and 1 subcribe+ 1 non product delvered")
    @Test
    public void shipment1SubscribeProductDeliveredPlusCancelled() throws InterruptedException {
        // create order(1 subscribe + 1 non subscribe) same shipemnt   -> -> subscribe shipemnt cancel + other delivered

        orderId = timorSubscriptionUtils.createOrder(userId, listingId, packageId, fulfillId, "create-order-withNonSubcribeProduct");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"instant");
        timorSubscriptionUtils.updateCart(data, "update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        timorSubscriptionUtils.orderShipAndDeliver(data.get("customer_order_id"), data.get("shipemntId_1"));
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


// insurance user shouldn't have benefits on subcribe items ..it should treat normal order

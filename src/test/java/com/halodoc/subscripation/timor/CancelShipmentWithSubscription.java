package com.halodoc.subscripation.timor;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.TimorSubscriptionUtils;
import io.qameta.allure.Description;

public class CancelShipmentWithSubscription {

    TimorSubscriptionUtils timorSubscriptionUtils = new TimorSubscriptionUtils();

    String orderId;

    //String userId = "c4ab50ad-8f1c-4b40-a01c-df37d0902117", listingId = "503eae39-6c32-4413-aa5e-40bafd2e2116", packageId = "f8a1300e-934b-4590-8ee9-5a53ee23f409", fulfillId = "269a26b5-15b1-4cbf-9ac5-adfa473778b0";
    String userId = "03306f60-9289-4b0e-9558-7f907011b993", listingId = "206886fd-3a27-47ee-861f-c94a9d804808", packageId = "8b1fd474-5ceb-49da-a60d-d15e8ee173b8", fulfillId = "5355c01d-fe44-4ffc-bfac-83bdb29ef1f0";

    HashMap<String, String> data = new HashMap<>();


    @Description ("order with 1 shipment and customer order cancel")
    @Test
    public void shipment1SubscribeProduct1CustomerCancelled() throws InterruptedException {
        // create order(1 subscribe) --> customer cancel

        orderId = timorSubscriptionUtils.createOrder(userId, listingId, packageId, fulfillId, "create-order");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"instant");
        timorSubscriptionUtils.updateCart(data, "update-order");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        timorSubscriptionUtils.cancelOrderNotSubscription(data.get("customer_order_id"));
    }

    @Description ("order with 1 shipment and customer order plus subcription cancel")
    @Test
    public void shipment1SubscribeProduct1OrderSubscriptionCancelled() throws InterruptedException {
        // create order(1 subscribe) --> customer cancel

        orderId = timorSubscriptionUtils.createOrder(userId, listingId, packageId, fulfillId, "create-order");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"instant");
        timorSubscriptionUtils.updateCart(data, "update-order");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        timorSubscriptionUtils.cancelOrderSubscription(data.get("customer_order_id"));
    }

    @Description ("order with 1 shipment and 1 subcribe product plus 1 non subcribe product cancel order not subcription")
    @Test
    public void shipment1SubscribeProduct1PlusNonSubcribeProduct1Cancelled() throws InterruptedException {
        // create order(1 subscribe + 1 non subscribe) same shipemnt   -> -> both shipemnt customer cancel

        orderId = timorSubscriptionUtils.createOrder(userId, listingId, packageId, fulfillId, "create-order-withNonSubcribeProduct");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"instant");
        timorSubscriptionUtils.updateCart(data, "update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        timorSubscriptionUtils.cancelShipment(data.get("customer_order_id"), data.get("shipemntGroupId_0"));
        data.clear();
    }

    @Description ("order with 1 shipment and 2 subcribe product order cancelled,not subscription ")
    @Test
    public void shipment1SubscribeProduct2Cancelled() throws InterruptedException {
        // create order(2 subscribe) same shipemnt cancel

        orderId = timorSubscriptionUtils.createOrder(userId, listingId, packageId, fulfillId, "create-order-2SubcribeProduct");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"instant");
        timorSubscriptionUtils.updateCart(data, "update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        timorSubscriptionUtils.cancelOrderNotSubscription(data.get("customer_order_id"));
        data.clear();
    }

    @Description ("order with 1 shipment and 1 subcribe driver cancel")
    @Test
    public void shipment1SubscribeProduct1DriverCancel() throws InterruptedException {
        // create order(1 subscribe) --> get order - approved order -> update order with deliery type -> charge gopay api -> confirm gopay -> shipped -> delivered

        orderId = timorSubscriptionUtils.createOrder(userId, listingId, packageId, fulfillId, "create-order");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"instant");
        timorSubscriptionUtils.updateCart(data, "update-order");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        timorSubscriptionUtils.driverCancel(data.get("tracking_id_0"),data.get("customer_order_id"),"gosend");
        data.clear();
    }


    @Description ("1 subcription item and order cancel")
    @Test
    public void SubscribeProductOrderCancel() throws InterruptedException {
        // create order(1 subscribe) --> get order - approved order -> update order with deliery type -> charge gopay api -> confirm gopay -> shipped -> delivered

        orderId = timorSubscriptionUtils.createOrder(userId, listingId, packageId, fulfillId, "create-order");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"instant");
        timorSubscriptionUtils.updateCart(data, "update-order");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        timorSubscriptionUtils.cancelOrderNotSubscription(data.get("customer_order_id"));
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "cancelled", false,"instant"));

        data.clear();
    }

    @Description ("order with 2 shipment and 1 subcribe product+1 non subcribe,both shipment Cancelled")
    @Test
    public void shipment2Plus1SubscribeProductPlusNonSubcribeProductCancelled() throws InterruptedException {
        // create order(2 subscribe) same shipemnt -> customer cancel

        orderId = timorSubscriptionUtils.createOrder(userId,listingId,packageId,fulfillId,"create-order-withNonSubcribeProduct-2shipment");
        data = timorSubscriptionUtils.getOrderDetails(orderId,"approved",true,"instant");
        timorSubscriptionUtils.updateCart(data,"update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId,"confirmed",false,""));
        timorSubscriptionUtils.cancelShipment(data.get("customer_order_id"),data.get("shipemntGroupId_0"));
        timorSubscriptionUtils.cancelShipment(data.get("customer_order_id"),data.get("shipemntGroupId_1"));
        data.clear();
    }

    @Description ("order with 2 shipment and 1 subcribe product+1 non subcribe ,subscribe shipment cancelled")
    @Test
    public void shipment2Plus1SubscribeProductCancelled() throws InterruptedException {
        // create order(2 subscribe) same shipemnt -> customer subcribe product cancel

        orderId = timorSubscriptionUtils.createOrder(userId,listingId,packageId,fulfillId,"create-order-withNonSubcribeProduct-2shipment");
        data = timorSubscriptionUtils.getOrderDetails(orderId,"approved",true,"instant");
        timorSubscriptionUtils.updateCart(data,"update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId,"confirmed",false,""));
        timorSubscriptionUtils.cancelOrderNotSubscription(data.get("customer_order_id"));
        timorSubscriptionUtils.orderShipAndDeliver(data.get("customer_order_id"),data.get("shipemntId_1"));
        data.clear();
    }

    @Description ("order with 2 shipment and 1 subcribe product+1 non subcribe,non subcribe shipment Cancelled")
    @Test
    public void shipment2Plus1SubscribeNonProductCancelled() throws InterruptedException {
        // create order(2 subscribe) same shipemnt -> customer non subcribe product cancel

        orderId = timorSubscriptionUtils.createOrder(userId,listingId,packageId,fulfillId,"create-order-withNonSubcribeProduct-2shipment");
        data = timorSubscriptionUtils.getOrderDetails(orderId,"approved",true,"instant");
        timorSubscriptionUtils.updateCart(data,"update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId,"confirmed",false,""));
        timorSubscriptionUtils.orderShipAndDeliver(data.get("customer_order_id"),data.get("shipemntId_0"));
        timorSubscriptionUtils.cancelShipment(data.get("customer_order_id"),data.get("shipemntGroupId_1"));
        data.clear();
    }

    @Description("order with 1 shipment and 2 subcribe product cancelled")
    @Test
    public void shipment2SubscribeProduct2Cancelled() throws InterruptedException {
        // create order(2 subscribe) same shipemnt cancel

        orderId = timorSubscriptionUtils.createOrder(userId,listingId,packageId,fulfillId,"create-order-2SubcribeProduct");
        data = timorSubscriptionUtils.getOrderDetails(orderId,"approved",true,"instant");
        timorSubscriptionUtils.updateCart(data,"update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId,"confirmed",false,""));
        timorSubscriptionUtils.cancelShipment(data.get("customer_order_id"),data.get("shipemntGroupId_0"));
        timorSubscriptionUtils.cancelShipment(data.get("customer_order_id"),data.get("shipemntGroupId_1"));
        data.clear();
    }

    @Description ("2 subcription item and order cancel")
    @Test
    public void Subscribe2ProductOrderCancel() throws InterruptedException {
        // create order(1 subscribe) --> get order - approved order -> update order with deliery type -> charge gopay api -> confirm gopay -> shipped -> delivered

        orderId = timorSubscriptionUtils.createOrder(userId,listingId,packageId,fulfillId,"create-order-2SubcribeProduct");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"instant");
        timorSubscriptionUtils.updateCart(data, "update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        timorSubscriptionUtils.cancelOrderSubscription(data.get("customer_order_id"));
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "cancelled", false,"instant"));

        data.clear();
    }
}

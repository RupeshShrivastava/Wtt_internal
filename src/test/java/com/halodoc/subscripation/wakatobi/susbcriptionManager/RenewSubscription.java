package com.halodoc.subscripation.wakatobi.susbcriptionManager;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.SubscriptionManagerUtils;
import com.halodoc.oms.orders.utilities.pdSubscription.TimorSubscriptionUtils;
import io.qameta.allure.Description;

public class RenewSubscription {


    TimorSubscriptionUtils timorSubscriptionUtils = new TimorSubscriptionUtils();
    SubscriptionManagerUtils subscriptionManagerUtils = new SubscriptionManagerUtils();

    String orderId;

    //String userId = "c4ab50ad-8f1c-4b40-a01c-df37d0902117", listingId = "503eae39-6c32-4413-aa5e-40bafd2e2116", packageId = "f8a1300e-934b-4590-8ee9-5a53ee23f409", fulfillId = "269a26b5-15b1-4cbf-9ac5-adfa473778b0";
    String userId = "03306f60-9289-4b0e-9558-7f907011b993", listingId = "206886fd-3a27-47ee-861f-c94a9d804808", packageId = "8b1fd474-5ceb-49da-a60d-d15e8ee173b8", fulfillId = "5355c01d-fe44-4ffc-bfac-83bdb29ef1f0";

    HashMap<String, String> data = new HashMap<>();

    @Description ("order with 1 shipment and 1 subcribe product,renew subcription")
    @Test
    public void shipment1SubscribeProduct1() throws InterruptedException {
        // create order(1 subscribe) --> get order - approved order -> update order with deliery type ->
        // charge gopay api -> confirm gopay -> skipped

        orderId = timorSubscriptionUtils.createOrder(userId, listingId, packageId, fulfillId, "create-order");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"delayed_instant");
        timorSubscriptionUtils.updateCart(data, "update-order");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        subscriptionManagerUtils.renewSubscription(userId,listingId);

        data.clear();
    }

}

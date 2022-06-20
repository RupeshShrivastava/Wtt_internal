package com.halodoc.subscripation.wakatobi.susbcriptionManager;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import com.halodoc.oms.orders.utilities.pdSubscription.SubscriptionManagerUtils;
import com.halodoc.oms.orders.utilities.pdSubscription.TimorSubscriptionUtils;
import io.qameta.allure.Description;

public class CancelSubscription extends CommonUtils {

    TimorSubscriptionUtils timorSubscriptionUtils = new TimorSubscriptionUtils();
    SubscriptionManagerUtils subscriptionManagerUtils = new SubscriptionManagerUtils();
    HashMap<String, String> data = new HashMap<>();
    String orderId;

    String userId = "c4ab50ad-8f1c-4b40-a01c-df37d0902117", listingId = "503eae39-6c32-4413-aa5e-40bafd2e2116",
            packageId = "f8a1300e-934b-4590-8ee9-5a53ee23f409", fulfillId = "269a26b5-15b1-4cbf-9ac5-adfa473778b0";

    @Description ("Cancel subscription")
    @Test
    public void SubscribeProductOrderCancel() throws InterruptedException {

        orderId = timorSubscriptionUtils.createOrder(userId,listingId,packageId,fulfillId,"create-order-2SubcribeProduct");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"instant");
        timorSubscriptionUtils.updateCart(data, "update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        subscriptionManagerUtils.cancelSubscription(userId,listingId);

        data.clear();
    }
}

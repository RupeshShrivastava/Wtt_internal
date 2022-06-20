package com.halodoc.subscripation.wakatobi.susbcriptionManager;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import com.halodoc.oms.orders.utilities.pdSubscription.SubscriptionManagerUtils;
import com.halodoc.oms.orders.utilities.pdSubscription.TimorSubscriptionUtils;
import io.qameta.allure.Description;

public class GetProductSubscription extends CommonUtils {



    TimorSubscriptionUtils timorSubscriptionUtils = new TimorSubscriptionUtils();
    SubscriptionManagerUtils subscriptionManagerUtils = new SubscriptionManagerUtils();
    HashMap<String, String> data = new HashMap<>();
    String orderId;

    String userId = "03306f60-9289-4b0e-9558-7f907011b993", listingId = "503eae39-6c32-4413-aa5e-40bafd2e2116",
            packageId = "f8a1300e-934b-4590-8ee9-5a53ee23f409", fulfillId = "269a26b5-15b1-4cbf-9ac5-adfa473778b0";


    @Description ("Get subscription list for the user")
    @Test
    public void subscriptionList() throws InterruptedException {
        subscriptionManagerUtils.getSubscription(userId,"1","10");
    }


    @Description ("Get subscription detaild for new product subscription")
    @Test
    public void getSubscriptionDetailsForProductSubscription() throws InterruptedException {

        orderId = timorSubscriptionUtils.createOrder(userId,listingId,packageId,fulfillId,"create-order");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"instant");
        timorSubscriptionUtils.updateCart(data, "update-order-wiithNonSubcribeProduct");
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        data.putAll(timorSubscriptionUtils.getOrderDetails(orderId, "confirmed", false,"instant"));
        subscriptionManagerUtils.findSubscriptionDetails(userId,"scheduled",listingId);

        data.clear();
    }
}

package com.halodoc.subscripation.timor;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import com.halodoc.oms.orders.utilities.pdSubscription.TimorSubscriptionUtils;
import io.qameta.allure.Description;

public class AbandonedOrderWithSubscription extends CommonUtils {

    TimorSubscriptionUtils timorSubscriptionUtils = new TimorSubscriptionUtils();

    String orderId;


    String userId = "03306f60-9289-4b0e-9558-7f907011b993", listingId = "328105e9-4911-451b-9cc9-4e8829f4d7c0",
            packageId = "683f6e38-299c-419b-ab73-2702d9d13761", fulfillId = "269a26b5-15b1-4cbf-9ac5-adfa473778b0";

    HashMap<String, String> data = new HashMap<>();

    @Description ("order with 1 shipment and 1 subcribe product abandon")
    @Test
    public void shipment1SubscribeProduct1Abandon() throws InterruptedException {
        // create order(1 subscribe) --> get order - approved order -> adandon order

        orderId = timorSubscriptionUtils.createOrder(userId, listingId, packageId, fulfillId, "create-order");
        data = timorSubscriptionUtils.getOrderDetails(orderId, "approved", true,"delayed_instant");
        timorSubscriptionUtils.abandonOrderSubscription(data.get("customer_order_id"));
        data.clear();
    }

}

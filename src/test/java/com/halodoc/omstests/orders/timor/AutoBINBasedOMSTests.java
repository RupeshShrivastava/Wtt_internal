package com.halodoc.omstests.orders.timor;

import static com.halodoc.oms.orders.utilities.constants.Constants.STATUS_APPROVED;
import static com.halodoc.omstests.Constants.CONTENT_TYPE;
import static com.halodoc.omstests.Constants.OMS_X_APP_TOKEN;
import static com.halodoc.omstests.Constants.user_agent;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.AutoBINBasedOMS;
import com.halodoc.oms.orders.library.BaseUtil;
import com.halodoc.omstests.Constants;
import com.halodoc.omstests.TimorHelper;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutoBINBasedOMSTests extends BaseUtil {
    private String customer_access_token;

    private String UUID;

    private TimorHelper timorHelper;

    private String order_id, total, amount, customer_payment_id;

    public HashMap<String, String> headers = new HashMap<>();

    @BeforeClass
    public void setUp() {
        timorHelper = new TimorHelper();
        customer_access_token = timorHelper.getPatientAccessToken();
    }

    @Test (description = "creating order", groups = { "sanity", "regression" }, priority = 0)
    public void createOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "create_order.json").replace("$cart_id", timorHelper.randomString())
                                       .replace("$quantity", "1");
        Response response = AutoBINBasedOMS.createOrder(headers, jsonString);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        order_id = response.body().path("customer_order_id");
        log.info("order_id is" + order_id);
    }

    @Test (description = "creating order", groups = { "sanity", "regression" }, priority = 1)
    public void getOrder() {
        checkStatusUntil(STATUS_APPROVED, order_id);
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        Response response = AutoBINBasedOMS.getOrder(headers, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));

        response.getBody().print().contains("approved");

        total = response.body().path("total").toString();
    }

    @SneakyThrows
    @Test (description = "Apply BIN Promotions", groups = { "sanity", "regression" }, priority = 2)
    public void orderApplyAutoCouponBIN() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "auto_apply_bin.json");
        Response response = AutoBINBasedOMS.autoApplyBIN(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        int length = response.body().path("applicable_adjustments.size()");
        for (int i = 0; i < length; i++) {
            if (response.body().path("applicable_adjustments[" + i + "].reason").equals("bin_based")) {
                UUID = response.body().path("applicable_adjustments[" + i + "].attributes.applicable_adjustment_uuid");
            }

            log.info("UUID is" + UUID);
        }
    }

    @Test (description = "Generate Token", groups = { "sanity", "regression" }, priority = 3)
    public void generatePaymentToken() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "payment_token.json").replace("$order_id", order_id).replace("$amount", total)
                                       .replace("$uuid", UUID);
        Response response = AutoBINBasedOMS.generateToken(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        amount = response.body().path("amount").toString();
        log.info("amount is " + amount);
        Assert.assertNotEquals(total, amount);
    }

    @Test (description = "refresh payments", groups = { "sanity", "regression" }, priority = 4)
    public void refreshPaymentsOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("orders", "refresh_payments.json");
        Response response = AutoBINBasedOMS.refreshPayments(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        int length = response.body().path("applicable_adjustments.size()");

        for (int i = 0; i < length; i++) {
            if (response.body().path("applicable_adjustments[" + i + "].value").equals("card")) {
                UUID = response.body().path("applicable_adjustments[" + i + "].attributes.applicable_adjustment_uuid");
            }

            log.info("UUID is" + UUID);
        }
    }

    @Test (description = "Initialise Payment", groups = { "sanity", "regression" }, priority = 5)
    public void initialisePayment() {
        orderApplyAutoCouponBIN();
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("timor", "paymentInitialise.json").replace("$order_id", order_id).replace("$UUID", UUID)
                                       .replace("$price", total).replace("$entity_id", "bb1b9841-f34f-4d60-b12c-e255c7025ffa")
                                       .replace("$payment_method", "card");
        Response response = AutoBINBasedOMS.paymentInitialize(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        amount = response.body().path("amount").toString();
        log.info("amount is " + amount);
        Assert.assertNotEquals(total, amount);
        customer_payment_id = response.body().path("customer_payment_id").toString();
        log.info("payment_id is " + customer_payment_id);
    }

    @Test (description = "Confirm Order", groups = { "sanity", "regression" }, priority = 6)
    public void confirmOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("timor", "confirm_pd_order.json").replace("$price", amount)
                                       .replace("$patient_id", "bb1b9841-f34f-4d60-b12c-e255c7025ffa");
        Response response = AutoBINBasedOMS.confirmOrder(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));
    }

    @Test (description = "Cancel Order", groups = { "sanity", "regression" }, priority = 7)
    public void cancelOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("timor", "cancel_order.json");
        Response response = AutoBINBasedOMS.cancelOrder(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));
    }

    @Test (description = "Apply BIN", groups = { "regression" }, priority = 8, enabled = false)
    public void autoCouponBINForCancelledOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("orders", "auto_apply_bin.json").replace("$method", "card").replace("$bin", "481111");
        Response response = AutoBINBasedOMS.autoApplyBIN(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test (description = "Apply BIN", groups = { "regression" }, priority = 9, enabled = false)
    public void autoCouponBINForAbandonedOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("orders", "auto_apply_bin.json").replace("$method", "card").replace("$bin", "481111");
        Response response = AutoBINBasedOMS.autoApplyBIN(headers, jsonString, "6YGK0Z-4104");
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test (description = "Apply BIN", groups = { "regression" }, priority = 10, enabled = false)
    public void autoCouponBINForShippedOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("orders", "auto_apply_bin.json").replace("$method", "card").replace("$bin", "481111");
        Response response = AutoBINBasedOMS.autoApplyBIN(headers, jsonString, "6YGK0Z-5227");
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test (description = "Apply BIN", groups = { "regression" }, priority = 11, enabled = false)
    public void autoCouponBINForDeliveredOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("orders", "auto_apply_bin.json").replace("$method", "card").replace("$bin", "481111");
        Response response = AutoBINBasedOMS.autoApplyBIN(headers, jsonString, "6YGK0Z-4478");
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test (description = "refresh payments", groups = { "regression" }, priority = 12)
    public void refreshPaymentsCancelledOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("orders", "refresh_payments.json");
        Response response = AutoBINBasedOMS.refreshPayments(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test (description = "refresh payments", groups = { "regression" }, priority = 13)
    public void refreshPaymentsAbandonedOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("orders", "refresh_payments.json");
        Response response = AutoBINBasedOMS.refreshPayments(headers, jsonString, "6YGK0Z-4104");
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Test (description = "refresh payments", groups = { "regression" }, priority = 14)
    public void refreshPaymentsShippedOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("orders", "refresh_payments.json");
        Response response = AutoBINBasedOMS.refreshPayments(headers, jsonString, "6YGK0Z-5227");
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));

    }

    @Test (description = "refresh payments", groups = { "regression" }, priority = 15)
    public void refreshPaymentsDeliveredOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture("orders", "refresh_payments.json");
        Response response = AutoBINBasedOMS.refreshPayments(headers, jsonString, "6YGK0Z-4478");
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }
}
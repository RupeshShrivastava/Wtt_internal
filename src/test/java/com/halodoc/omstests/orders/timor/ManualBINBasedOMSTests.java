package com.halodoc.omstests.orders.timor;


import com.halodoc.oms.orders.AutoBINBasedOMS;
import com.halodoc.oms.orders.GopayTokenization;
import com.halodoc.oms.orders.ManualBINBasedOMS;
import com.halodoc.oms.orders.library.BaseUtil;
import com.halodoc.oms.orders.utilities.payments.Instrument;



import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import static com.halodoc.omstests.Constants.*;


import com.halodoc.omstests.Constants;
import com.halodoc.omstests.TimorHelper;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.io.IOException;
import java.util.HashMap;


@Slf4j
public class ManualBINBasedOMSTests extends BaseUtil {

    private String customer_access_token, order_id, gopay_instrument, total, payment_id, amount_initialise, customer_payment_id_initialise, token_amount, UUID, token_amount_after_both_discounts, amount_after_both_discounts;
    private TimorHelper timorHelper;

    public HashMap<String, String> headers = new HashMap<>();


    @BeforeClass(alwaysRun = true)
    public void beforeClass() throws InterruptedException {
        timorHelper = new TimorHelper();
        customer_access_token = timorHelper.getPatientAccessToken();
        createOrder();
        Thread.sleep(50000);
        getUserInstrument();
    }



    public void createOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "create_order.json")
                .replace("$cart_id", timorHelper.randomString())
                .replace("$quantity", "5");
        Response response = AutoBINBasedOMS.createOrder(headers,jsonString);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        order_id =  response.body().path("customer_order_id");
        log.info("order_id is" +order_id);

    }



    public void getUserInstrument(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        Response response = Instrument.getInstrument(headers);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK,response));
        gopay_instrument = response.getBody().path("[0].external_id");
        log.info("gopay_instrument is" +gopay_instrument);
    }



    @Test(description = "Manual BIN", groups = {"sanity", "regression"}, priority = 0)
    /**
     * Applying only manual BIN valid coupon to order
     */
    public void applyManualBINCouponToOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, APPLY_MANUAL_PROMO).
                replace("$coupon",  COUPON_VALID);
        Response response = ManualBINBasedOMS.manualApplyBIN(headers,jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK,response));

    }



   @Test(description = "Manual BIN", groups = {"sanity", "regression"}, priority = 1)
    /**
     * Applying only manual BIN valid coupon to order
     */
    public void applyManualBINSameCouponToOrderLowerCase() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, APPLY_MANUAL_PROMO).
                replace("$coupon",  COUPON_LOWER_CASE);
        Response response = ManualBINBasedOMS.manualApplyBIN(headers,jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST,response));

    }



    @Test(description = "Manual BIN", groups = {"regression"}, priority = 2)
    /**
     * Applying promotional coupon to order
     */
    public void applyManualPromotionalCouponToOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, APPLY_MANUAL_PROMO)
                .replace("$coupon", COUPON_NORMAL);
        Response response = ManualBINBasedOMS.manualApplyBIN(headers,jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK,response));

    }



    @Test(description = "Manual BIN Invalid", groups = {"regression"}, priority = 3)
    /**
     * Applying only manual BIN expired coupon to order
     */
    public void applyExpiredCouponToOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, APPLY_MANUAL_PROMO).
                replace("$coupon",  COUPON_EXPIRED);
        Response response = ManualBINBasedOMS.manualApplyBIN(headers,jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST,response));
        Assert.assertEquals(response.path("header").toString(), "expired_coupon_code");

    }



    @Test(description = "Manual BIN Invalid", groups = {"regression"}, priority = 4)
    /**
     * Applying only manual BIN invalid coupon to order
     */
    public void applyInvalidCouponToOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, APPLY_MANUAL_PROMO).
                replace("$coupon",  COUPON_INVALID);
        Response response = ManualBINBasedOMS.manualApplyBIN(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.NOT_FOUND, response));

    }



    @Test(description = "Manual BIN Invalid", groups = {"regression"}, priority = 5)
    /**
     * Applying auto coupon to order
     */
    public void applyAutoCouponToOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, APPLY_MANUAL_PROMO).
                replace("$coupon",  COUPON_AUTO);
        Response response = ManualBINBasedOMS.manualApplyBIN(headers,jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));

    }



    @Test(description = "Manual BIN Invalid", groups = {"sanity", "regression"}, priority = 6)
    /**
     * Applying coupon with product_id with no_intersection condition to order
     * Having same product defined in condition. - Coupon should not get applied.
     */
    public void applyCouponWithProductIdNoIntersectionCondition() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, APPLY_MANUAL_PROMO).
                replace("$coupon",  COUPON_PROD_ID_NO_INT);
        Response response = ManualBINBasedOMS.manualApplyBIN(headers,jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));

    }



    @Test(description = "Manual BIN Invalid", groups = {"sanity","regression"}, priority = 7)
    /**
     * Applying coupon with product_id with intersection condition to order
     * Having same product defined in condition. - Coupon should get apply.
     */
    public void applyCouponWithProductIdIntersectionCondition() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
       String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, APPLY_MANUAL_PROMO).
                replace("$coupon",  COUPON_PROD_ID_INT);
        Response response = ManualBINBasedOMS.manualApplyBIN(headers,jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));

    }


    @Test(description = "creating order", groups = {"sanity", "regression"}, priority = 8)
    public void getOrder() throws IOException, InterruptedException {
        //WAIT FOR ORDER TO APPROVE
        Thread.sleep(10000);
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        Response response = AutoBINBasedOMS.getOrder(headers,order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));


        response.getBody().print().contains("approved");

        total = response.body().path("total").toString();


    }



    @Test(description = "Manual BIN Invalid", groups = {"regression"}, priority = 9)
    /**
     * Applying manual BIN for "cancelled" order
     */
    public void applyManualCouponBINForCancelledOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, APPLY_MANUAL_PROMO);
        Response response = ManualBINBasedOMS.manualApplyBIN(headers,jsonString, CANCELLED_ORDER);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }



    @Test(description = "Manual BIN Invalid", groups = {"regression"}, priority = 10)
    /**
     * Applying manual BIN for "delivered" order
     */
    public void applyManualCouponBINForDeliveredOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, APPLY_MANUAL_PROMO);
        Response response = ManualBINBasedOMS.manualApplyBIN(headers,jsonString, DELIVERED_ORDER);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }



    @Test(description = "Manual BIN Invalid", groups = {"regression"}, priority = 11)
    /**
     * Applying manual BIN for "confirmed" order
     */
    public void applyManualCouponBINForConfirmedOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, APPLY_MANUAL_PROMO);
        Response response = ManualBINBasedOMS.manualApplyBIN(headers,jsonString, CONFIRMED_ORDER);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }



    @Test(description = "Manual BIN Invalid", groups = {"regression"}, priority = 12)
    /**
     * Applying manual BIN for "shipped" order
     */
    public void applyManualCouponBINForShippedOrder() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, APPLY_MANUAL_PROMO);
        Response response = ManualBINBasedOMS.manualApplyBIN(headers,jsonString, SHIPPED_ORDER);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }




    @Test(description = "generate token", groups = {"sanity", "regression"},  priority = 13)
    /**
     * Generate Token[APP] with discount of Manual BIN only
     */
    public void orderGenerateTokenForManualBINValid() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, GENERATE_TOKEN_MANUAL)
                .replace("$method", METHOD_CARD)
                .replace("$bin", BIN_VISA)
                .replace("$serviceType",SERVICE_PD)
                .replace("$orderId", order_id)
                .replace("$UUID", "")
                .replace("$amount", total);
        Response response = AutoBINBasedOMS.generateToken(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        payment_id = response.body().path("customer_payment_id").toString();
        log.info("payment_id is " +payment_id);
        token_amount =  response.body().path("amount").toString();
    }



    @Test(description = "initialise payment", groups = {"sanity", "regression"}, priority = 14)
    /**
     * Payment initializing[WEB] with discount of Manual BIN only
     */
    public void initialisePaymentManualBIN() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, INITIALIZE_PAYMENT_JSON)
                .replace("$orderId", order_id)
                .replace("$UUID", "")
                .replace("$amount", total)
                .replace("$bin", BIN_VISA);
        Response response = AutoBINBasedOMS.paymentInitialize(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        amount_initialise =  response.body().path("amount").toString();
        customer_payment_id_initialise = response.body().path("customer_payment_id").toString();
        log.info("payment_id is " +customer_payment_id_initialise);

    }



    @Test(description = "generate token invalid", groups = {"regression"},  priority = 15)

    /**
     * Applied VISA BIN coupon and generating payment token passing Master BIN
     */

    public void orderGenerateTokenForManualBINInvalid() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, GENERATE_TOKEN_MANUAL)
                .replace("$method", "card")
                .replace("$bin", BIN_MASTER)
                .replace("$service", "contact_doctor")
                .replace("$orderId", order_id)
                .replace("$UUID", "")
                .replace("$amount", total);
        Response response = AutoBINBasedOMS.generateToken(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }



    @Test(description = "Apply BIN Promotions", groups = {"sanity", "regression"}, priority = 16)
    public void orderApplyAutoCouponBIN() throws IOException, InterruptedException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        headers.put("Authorization", customer_access_token);
        headers.put("User-Agent", user_agent);
        String jsonString = timorHelper.getRequestFixture("orders", "auto_apply_bin.json");
        Response response = AutoBINBasedOMS.autoApplyBIN(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        int length = response.body().path("applicable_adjustments.size()");
        for (int i=0;i<length;i++)
        {
            if (response.body().path("applicable_adjustments["+i+"].reason").equals("bin_based"))
            {

                UUID =  response.body().path("applicable_adjustments["+i+"].attributes.applicable_adjustment_uuid");

            }

            log.info("UUID is" +UUID);
        }
        Thread.sleep(10000);

    }



    @Test(description = "generate token", groups = {"sanity", "regression"},  priority = 17, enabled = false)
    /**
     * Generate Token[APP] with discount of Manual BIN and Auto BIN.
     */
    public void orderGenerateTokenForManualAndAutoBINValid() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, GENERATE_TOKEN_MANUAL)
                .replace("$method", METHOD_CARD)
                .replace("$bin", BIN_VISA)
                .replace("$service", SERVICE_PD)
                .replace("$orderId", order_id)
                .replace("$UUID", UUID)
                .replace("$amount", total);
        Response response = AutoBINBasedOMS.generateToken(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        payment_id = response.body().path("customer_payment_id").toString();
        log.info("payment_id is " + payment_id);
        response.getBody().print().contains("ORDER");
        token_amount_after_both_discounts = response.body().path("amount").toString();
        Assert.assertNotEquals(token_amount, token_amount_after_both_discounts);
    }




    @Test(description = "Confirm consultation with wallet", groups = {"sanity", "regression"}, priority = 18)
    /**
     * Applying manual BIN discount and confirming consultation with wallet
     */
    public void confirmOrderWithWallet() {
        log.info("Running Test : " +Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, CONFIRM_ORDER_WALLET).
                replace("$amount",  total);
        Response response = AutoBINBasedOMS.confirmOrder(headers,jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
        Assert.assertEquals(response.getBody().path("message"), "Order contains bin based manual adjustments. Please pay by valid card");

    }



    @Test(description = "Pay for order with GoPay", groups = {"sanity", "regression"}, priority = 19)
    /**
     * Applying manual BIN discount and charge with gopay.
     */
    public void chargeWithGoPayForOrder() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, GOPAY_CHARGE_JSON)
                .replace("$entity_type", entity_type)
                .replace("$entity_id", USER_ENTITY_ID)
                .replace("$service_type", GOPAY_SERVICE_TYPE_PHARMACY_DEVLIVERY)
                .replace("$orderId", order_id)
                .replace("$currency", currency)
                .replace("$amount", total)
                .replace("$payment_provider", payment_provider)
                .replace("$payment_method", METHOD_GOPAY)
                .replace("$payment_option_attribute_key", payment_option_attribute_key)
                .replace("$payment_option", payment_option)
                .replace("$user_instrument_id_attribute_key", user_instrument_id_attribute_key)
                .replace("$user_instrument_id", gopay_instrument);
        Response response = GopayTokenization.payForOrder(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
        Assert.assertEquals(response.getBody().path("message"), "Payment token already generated for "+order_id);
    }



    @Test(description = "initialise payment", groups = {"sanity", "regression"}, priority = 20)
    /**
     * Payment initializing[WEB] with discount of Manual and Auto BIN
     */
    public void initialisePaymentManualAndAutoBIN() throws IOException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        headers.put("Content-Type", Constants.CONTENT_TYPE);
        headers.put("X-APP-TOKEN", OMS_X_APP_TOKEN);
        String jsonString = timorHelper.getRequestFixture(MANUAL_BIN_JSON_FOLDER, INITIALIZE_PAYMENT_JSON)
                .replace("$orderId", order_id)
                .replace("$UUID", UUID)
                .replace("$amount", total)
                .replace("$bin", BIN_VISA);
        Response response = AutoBINBasedOMS.paymentInitialize(headers, jsonString, order_id);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        amount_initialise =  response.body().path("amount").toString();
        amount_after_both_discounts = response.body().path("customer_payment_id").toString();
        log.info("payment_id is " +customer_payment_id_initialise);
        Assert.assertNotEquals(amount_initialise, amount_after_both_discounts);

    }




}

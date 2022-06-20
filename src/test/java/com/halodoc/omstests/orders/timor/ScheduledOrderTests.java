/**
 * @author arjun.shetty@halodoc.com
 */
package com.halodoc.omstests.orders.timor;
import static com.halodoc.oms.orders.utilities.constants.Constants.PRICE;
import static com.halodoc.oms.orders.utilities.constants.Constants.PRODUCT_LISTING_SCHEDULED;
import static com.halodoc.oms.orders.utilities.constants.Constants.USER_ENTITY_ID;
import static com.halodoc.oms.orders.utilities.constants.Constants.merchant_locationId;
import static com.halodoc.oms.orders.utilities.constants.Constants.merchantId;
import static com.halodoc.oms.orders.utilities.constants.Constants.reallocating_merchant_locationId;
import static com.halodoc.oms.orders.utilities.constants.Constants.reallocating_merchantId;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
public class ScheduledOrderTests extends OrdersBaseTest {
    String External_id;
    Double user_lat, user_long;
    @Test
    public void createAndConfirmInstantOrderWithDeliveryOptionAsInstant() throws InterruptedException {
        user_lat = -6.212672;
        user_long = 106.825944;
        String current_day = timorHelper.getCurrentDay("today");
        buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, true);
        Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED, user_lat, user_long);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        checkIfDeliveryOptGenerated(orderID);
        orderResponse = timorHelper.getOrderDetails(orderID);
        if (orderResponse.path("delivery_options[0].delivery_type").equals("instant")) {
            External_id = orderResponse.path("delivery_options[1].external_id");
        } else {
            External_id = orderResponse.path("delivery_options[0].external_id");
        }
        orderResponse = timorHelper.deliveryOption(orderID, External_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
        orderResponse = timorHelper.applyCoupon(orderID, "dop_backend");
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Apply Valid Coupon - Failed");
        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
    }
        @Test
        public void VerifyInstantOrderForMoreThan20Km() throws InterruptedException{
            user_lat = -6.416161;
            user_long = 106.794706;
            String current_day =timorHelper.getCurrentDay("currentDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, true);
            Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            String orderID = orderResponse.path("customer_order_id").toString();
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
            Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            External_id = orderResponse.path("delivery_options[0].external_id");
            orderResponse =timorHelper.deliveryOption(orderID,External_id);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
            orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        }

        @Test (enabled = false)
        public void VerifyDelayedInstantOrderForMoreThan400Km() throws InterruptedException{
            user_lat = -7.795759;
            user_long = 110.368463;
            String current_day =timorHelper.getCurrentDay("currentDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, true);
            Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            String orderID = orderResponse.path("customer_order_id").toString();
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
            Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            External_id = orderResponse.path("delivery_options[0].external_id");
            orderResponse =timorHelper.deliveryOption(orderID,External_id);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
            orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        }
        @Test
        public void createAndConfirmInstantOrderWithDeliveryOptionAsDelayedInstant() throws InterruptedException {
            user_lat = -6.212672;
            user_long = 106.825944;
            String current_day =timorHelper.getCurrentDay("currentDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId,current_day,true);
            Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            String orderID = orderResponse.path("customer_order_id").toString();
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
            Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            if (orderResponse.path("delivery_options[1].delivery_type").equals("instant")){
                External_id = orderResponse.path("delivery_options[0].external_id");
            }else{
                External_id = orderResponse.path("delivery_options[1].external_id");
            }
            orderResponse =timorHelper.deliveryOption(orderID,External_id);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
            orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
            Assert.assertEquals(orderResponse.path("attributes.delivery_type"),"delayed_instant", "unable to create delayed instant order");
        }

        @Test(groups = { "sanity" })
        public void createAndConfirmScheduledOrderWithDeliveryOptionAsScheduledInstant() throws InterruptedException {
            user_lat = -6.212672;
            user_long = 106.825944;
            String current_day =timorHelper.getCurrentDay("currentDay");
            String next_day =timorHelper.getCurrentDay("nextDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, false);
            buruHelper.updatePharmacy(merchantId, merchant_locationId, next_day, true);
            buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, current_day, false);
            buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, next_day, true);
            Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            String orderID = orderResponse.path("customer_order_id").toString();
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
            Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            if (orderResponse.path("delivery_options[1].delivery_type").equals("scheduled_instant")){
                External_id = orderResponse.path("delivery_options[1].external_id");
            }else{
                External_id = orderResponse.path("delivery_options[0].external_id");
            }
            orderResponse =timorHelper.deliveryOption(orderID,External_id);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
            orderResponse = timorHelper.applyCoupon(orderID, "dop_backend");
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Apply Valid Coupon - Failed");
            orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
            Assert.assertEquals(orderResponse.path("attributes.delivery_type"),"scheduled_instant", "unable to create scheduled instant order");

        }

        @Test(groups = { "sanity" })
        public void createAndConfirmScheduledOrderwithdeliveryOptionAsDelayedInstant() throws InterruptedException {
            user_lat = -6.212672;
            user_long = 106.825944;
            String current_day =timorHelper.getCurrentDay("currentDay");
            String next_day=timorHelper.getCurrentDay("nextDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, false);
            buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, current_day, false);
            buruHelper.updatePharmacy(merchantId, merchant_locationId, next_day, true);
            buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, next_day, true);
            Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            String orderID = orderResponse.path("customer_order_id").toString();
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
            checkIfGroupGenerated(orderID);
            orderResponse = timorHelper.getOrderDetails(orderID);
            if (orderResponse.path("delivery_options[1].delivery_type").equals("scheduled_instant")){
                External_id = orderResponse.path("delivery_options[0].external_id");
            }else{
                External_id = orderResponse.path("delivery_options[1].external_id");
            }
            orderResponse =timorHelper.deliveryOption(orderID,External_id);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
            orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
            Assert.assertEquals(orderResponse.path("attributes.delivery_type"),"delayed_instant", "unable to create scheduled instant order");
        }

        @Test
        public void createAndConfirmOrderWithDeliveryOptionAsEmpty() throws InterruptedException {
            user_lat = -6.212672;
            user_long = 106.825944;
            String current_day =timorHelper.getCurrentDay("currentDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, true);
            Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            String orderID = orderResponse.path("customer_order_id").toString();
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
            Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
            External_id = "";
            orderResponse =timorHelper.deliveryOption(orderID,External_id);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, orderResponse), "cannot add delivery options");
        }


        @Test
        public void CreatAndConfirmDelayedInstantorder() throws InterruptedException {
            user_lat = -6.416161;
            user_long = 106.794706;
            String current_day =timorHelper.getCurrentDay("currentDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, true);
            Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            String orderID = orderResponse.path("customer_order_id").toString();
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
            Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
            orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
            Assert.assertEquals(orderResponse.path("delivery_options[0].delivery_type"),"delayed_instant", "unable to create delayed instant order");

        }

        @Test
        public void createAndConfirmOrderforStores() throws InterruptedException {
            user_lat = -6.212672;
            user_long = 106.825944;
            boolean status = true;
            String current_day =timorHelper.getCurrentDay("currentDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, status);
            Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            String orderID = orderResponse.path("customer_order_id").toString();
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
            Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
            External_id = orderResponse.path("delivery_options[0].external_id");
            orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        }

        @Test
        public void createAndConfirmOrderForReallocationforInstantwithDeliveryOptionAsInstant() throws InterruptedException {
            Response orderResponse;
            user_lat = -6.212672;
            user_long = 106.825944;
            String current_day =timorHelper.getCurrentDay("currentDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, true);
            buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, current_day, true);
            orderResponse = timorHelper.createScheduledOrderForReallocation(USER_ENTITY_ID,PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Create Order Failed");
            String orderId = orderResponse.path("customer_order_id").toString();
            timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            String price = orderResponse.path("item_total").toString();
            if (orderResponse.path("delivery_options[1].delivery_type").equals("instant")){
                External_id = orderResponse.path("delivery_options[1].external_id");
            }else{
                External_id = orderResponse.path("delivery_options[0].external_id");
            }
            orderResponse =timorHelper.deliveryOption(orderId,External_id);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
            orderResponse = timorHelper.applyCoupon(orderId, "dop_backend");
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Apply Valid Coupon - Failed");
            orderResponse = timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status is not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            String MERCHANT_LOCATION_ID = orderResponse.path("delivery_options[0].merchant_location_id").toString();
            System.out.println("mechant loc ID :" + MERCHANT_LOCATION_ID);
            System.out.println("Cancelling order Via internal API");
            orderResponse = timorHelper.cancelOrder(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Cancel Order Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(checkStatusUntil("confirmed", orderId), "Order Status in not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Boolean verifyMerchant = timorHelper.validateSimple(MERCHANT_LOCATION_ID, orderResponse.path("attributes.reallocated_merchant_location_id").toString());
            Assert.assertTrue((!verifyMerchant), "Merchant Location ID not changed after Reject -  Failed in Reallocation");
            Assert.assertEquals(orderResponse.path("attributes.delivery_type"),"instant","reallocated delivery_type is not same as initial allocation");

        }

        @Test
        public void createAndConfirmOrderForReallocationforInstantwithDeliveryOptionAsDelayedInstant() throws InterruptedException {
            Response orderResponse;
            user_lat = -6.212672;
            user_long = 106.825944;
            String current_day =timorHelper.getCurrentDay("currentDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, true);
            buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, current_day, true);
            orderResponse = timorHelper.createScheduledOrderForReallocation(USER_ENTITY_ID,PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Create Order Failed");
            String orderId = orderResponse.path("customer_order_id").toString();
            timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            String price = orderResponse.path("item_total").toString();

            if (orderResponse.path("delivery_options[1].delivery_type").equals("instant")){
                External_id = orderResponse.path("delivery_options[0].external_id");
            }else{
                External_id = orderResponse.path("delivery_options[1].external_id");
            }
            orderResponse =timorHelper.deliveryOption(orderId,External_id);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
            orderResponse = timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status is not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            String MERCHANT_LOCATION_ID = orderResponse.path("delivery_options[0].merchant_location_id").toString();
            System.out.println("mechant loc ID :" + MERCHANT_LOCATION_ID);
            System.out.println("Cancelling order Via internal API");
            orderResponse = timorHelper.cancelOrder(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Cancel Order Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(checkStatusUntil("confirmed", orderId), "Order Status in not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Boolean verifyMerchant = timorHelper.validateSimple(MERCHANT_LOCATION_ID, orderResponse.path("attributes.reallocated_merchant_location_id").toString());
            Assert.assertTrue((!verifyMerchant), "Merchant Location ID not changed after Reject -  Failed in Reallocation");
            Assert.assertEquals(orderResponse.path("attributes.delivery_type"),"delayed_instant","reallocated delivery_type is not same as initial allocation");
        }

        @Test
        public void createAndConfirmOrderForReallocationforScheduledInstantwithDeliveryOptionAsScheduledInstant() throws InterruptedException {
            Response orderResponse;
            user_lat = -6.212672;
            user_long = 106.825944;
            String current_day =timorHelper.getCurrentDay("currentDay");
            String next_day =timorHelper.getCurrentDay("nextDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, false);
            buruHelper.updatePharmacy(merchantId, merchant_locationId, next_day, true);
            buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, current_day, false);
            buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, next_day, true);
            orderResponse = timorHelper.createScheduledOrderForReallocation(USER_ENTITY_ID,PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Create Order Failed");
            String orderId = orderResponse.path("customer_order_id").toString();
            timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            String price = orderResponse.path("item_total").toString();
            if (orderResponse.path("delivery_options[1].delivery_type").equals("scheduled_instant")){
                External_id = orderResponse.path("delivery_options[1].external_id");
            }else{
                External_id = orderResponse.path("delivery_options[0].external_id");
            }
            orderResponse =timorHelper.deliveryOption(orderId,External_id);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
            orderResponse = timorHelper.applyCoupon(orderId, "dop_backend");
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Apply Valid Coupon - Failed");
            orderResponse = timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status is not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            String MERCHANT_LOCATION_ID = orderResponse.path("delivery_options[0].merchant_location_id").toString();
            System.out.println("mechant loc ID :" + MERCHANT_LOCATION_ID);
            System.out.println("Cancelling order Via internal API");
            orderResponse = timorHelper.cancelOrder(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Cancel Order Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(checkStatusUntil("confirmed", orderId), "Order Status in not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Boolean verifyMerchant = timorHelper.validateSimple(MERCHANT_LOCATION_ID, orderResponse.path("attributes.reallocated_merchant_location_id").toString());
            Assert.assertTrue((!verifyMerchant), "Merchant Location ID not changed after Reject -  Failed in Reallocation");
            Assert.assertEquals(orderResponse.path("attributes.delivery_type"),"scheduled_instant","reallocated delivery_type is not same as initial allocation");
        }

        @Test
        public void createAndConfirmOrderForReallocationforScheduledInstantwithDeliveryOptionAsDelayedInstant() throws InterruptedException {
            Response orderResponse;
            user_lat = -6.212672;
            user_long = 106.825944;
            String current_day =timorHelper.getCurrentDay("currentDay");
            String next_day =timorHelper.getCurrentDay("nextDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, false);
            buruHelper.updatePharmacy(merchantId, merchant_locationId, next_day, true);
            buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, current_day, false);
            buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, next_day, true);
            orderResponse = timorHelper.createScheduledOrderForReallocation(USER_ENTITY_ID,PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Create Order Failed");
            String orderId = orderResponse.path("customer_order_id").toString();
            timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            String price = orderResponse.path("item_total").toString();

            if (orderResponse.path("delivery_options[1].delivery_type").equals("scheduled_instant")){
                External_id = orderResponse.path("delivery_options[0].external_id");
            }else{
                External_id = orderResponse.path("delivery_options[1].external_id");
            }
            orderResponse =timorHelper.deliveryOption(orderId,External_id);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
            orderResponse = timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status is not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            String MERCHANT_LOCATION_ID = orderResponse.path("delivery_options[0].merchant_location_id").toString();
            System.out.println("merchant loc ID :" + MERCHANT_LOCATION_ID);
            System.out.println("Cancelling order Via internal API");
            buruHelper.updatePharmacy(merchantId,merchant_locationId, current_day, true);
            orderResponse = timorHelper.cancelOrder(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Cancel Order Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(checkStatusUntil("confirmed", orderId), "Order Status in not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Boolean verifyMerchant = timorHelper.validateSimple(MERCHANT_LOCATION_ID, orderResponse.path("attributes.reallocated_merchant_location_id").toString());
            Assert.assertTrue((!verifyMerchant), "Merchant Location ID not changed after Reject -  Failed in Reallocation");
            Assert.assertEquals(orderResponse.path("attributes.delivery_type"),"delayed_instant","reallocated delivery_type is not same as initial allocation");

        }

        @Test
        public void createAndConfirmOrderForReallocationforDelayedInstantwithDeliveryOptionAsDelayedInstant() throws InterruptedException {
            Response orderResponse;
            user_lat = -6.416161;
            user_long = 106.794706;
            String current_day =timorHelper.getCurrentDay("currentDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, true);
            buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, current_day, true);
            orderResponse = timorHelper.createScheduledOrderForReallocation(USER_ENTITY_ID,PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Create Order Failed");
            String orderId = orderResponse.path("customer_order_id").toString();
            timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            String price = orderResponse.path("item_total").toString();
            External_id = orderResponse.path("delivery_options[0].external_id");
            orderResponse =timorHelper.deliveryOption(orderId,External_id);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
            orderResponse = timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status is not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            String MERCHANT_LOCATION_ID = orderResponse.path("delivery_options[0].merchant_location_id").toString();
            System.out.println("merchant loc ID :" + MERCHANT_LOCATION_ID);
            System.out.println("Cancelling order Via internal API");
            orderResponse = timorHelper.cancelOrder(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Cancel Order Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(checkStatusUntil("confirmed", orderId), "Order Status in not Confirmed");
            orderResponse = timorHelper.getOrderDetails(orderId);
            Boolean verifyMerchant = timorHelper.validateSimple(MERCHANT_LOCATION_ID, orderResponse.path("attributes.reallocated_merchant_location_id").toString());
            Assert.assertTrue((!verifyMerchant), "Merchant Location ID not changed after Reject -  Failed in Reallocation");
        }

        @Test
        public void cancelOrder() throws InterruptedException {
            user_lat = -6.212672;
            user_long = 106.825944;
            String current_day =timorHelper.getCurrentDay("currentDay");
            buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, true);
            Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            String orderID = orderResponse.path("customer_order_id").toString();
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
            Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            External_id = orderResponse.path("delivery_options[0].external_id");
            orderResponse =timorHelper.deliveryOption(orderID,External_id);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
            orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
            orderResponse = timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
            Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
            orderResponse = timorHelper.cancelOrder(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Cancel Order Failed");
        }


        // Following tests also impact with default shipping option as regular
    @Test
    public void VerifyServiceTypeandFee_InstantOrderWithDeliveryOptionAsInstant() throws InterruptedException {
        user_lat = -6.212672;
        user_long = 106.825944;
        String current_day =timorHelper.getCurrentDay("currentDay");
        buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, true);
        Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED, user_lat, user_long);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        if (orderResponse.path("delivery_options[1].delivery_type").equals("instant")) {
            External_id = orderResponse.path("delivery_options[1].external_id");
        } else {
            External_id = orderResponse.path("delivery_options[0].external_id");
        }
        orderResponse = timorHelper.deliveryOption(orderID, External_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
        orderResponse = timorHelper.applyCoupon(orderID, "dop_backend");
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Apply Valid Coupon - Failed");
        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        Assert.assertEquals(orderResponse.path("attributes.delivery_fee"),"0.0","wrong delivery fee populated");
        Assert.assertNull(orderResponse.path("attributes.service_type"));

    }
    @Test
    public void verifyServiceTypeandDeliveryFee_InstantOrderWithDeliveryOptionAsDelayedInstant() throws InterruptedException {
        user_lat = -6.212672;
        user_long = 106.825944;
        String current_day =timorHelper.getCurrentDay("currentDay");
        buruHelper.updatePharmacy(merchantId, merchant_locationId,current_day,true);
        Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        if (orderResponse.path("delivery_options[1].delivery_type").equals("instant")){
            External_id = orderResponse.path("delivery_options[0].external_id");
        }else{
            External_id = orderResponse.path("delivery_options[1].external_id");
        }
        orderResponse =timorHelper.deliveryOption(orderID,External_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        Assert.assertEquals(orderResponse.path("attributes.delivery_type"),"delayed_instant", "unable to create delayed instant order");
        Assert.assertEquals(orderResponse.path("attributes.service_type"),"regular","wrong service type populated");
        Assert.assertEquals(orderResponse.path("attributes.delivery_fee"),"5000.0","wrong delivery fee populated");
    }

    @Test
    public void VerifyServiceTypeandDeliveryFee_DelayedInstantorder() throws InterruptedException {
        user_lat = -6.416161;
        user_long = 106.794706;
        String current_day =timorHelper.getCurrentDay("currentDay");
        buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, true);
        Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        Assert.assertEquals(orderResponse.path("delivery_options[0].delivery_type"),"delayed_instant", "unable to create delayed instant order");
        Assert.assertEquals(orderResponse.path("delivery_options[0].service_type"),"regular","wrong service type populated");
        String delivery_fee = String.valueOf(5000.0);
        Assert.assertEquals(orderResponse.path("delivery_options[0].delivery_fee").toString(),delivery_fee,"wrong delivery fee populated");
    }

    @Test
    public void VerifyPharmacyAvailableTime_ScheduledInstantwithDeliveryOptionAsScheduledInstant() throws InterruptedException {
        user_lat = -6.212672;
        user_long = 106.825944;
        String current_day =timorHelper.getCurrentDay("currentDay");
        String next_day =timorHelper.getCurrentDay("nextDay");
        buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, false);
        buruHelper.updatePharmacy(merchantId, merchant_locationId, next_day, true);
        buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, current_day, false);
        buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, next_day, true);
        Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        if (orderResponse.path("delivery_options[1].delivery_type").equals("scheduled_instant")){
            External_id = orderResponse.path("delivery_options[1].external_id");
        }else{
            External_id = orderResponse.path("delivery_options[0].external_id");
        }
        orderResponse =timorHelper.deliveryOption(orderID,External_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        Assert.assertEquals(orderResponse.path("attributes.delivery_type"),"scheduled_instant", "unable to create scheduled instant order");
        Assert.assertNotNull(orderResponse.path("attributes.estimate_delivery_time"));

    }

    @Test
    public void VerifyServiceTypeDeliveryFee_ScheduledOrderWithDeliveryOptionAsScheduledInstant() throws InterruptedException {
        user_lat = -6.212672;
        user_long = 106.825944;
        String current_day =timorHelper.getCurrentDay("currentDay");
        String next_day =timorHelper.getCurrentDay("nextDay");
        buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, false);
        buruHelper.updatePharmacy(merchantId, merchant_locationId, next_day, true);
        buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, current_day, false);
        buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, next_day, true);
        Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        if (orderResponse.path("delivery_options[1].delivery_type").equals("scheduled_instant")){
            External_id = orderResponse.path("delivery_options[1].external_id");
        }else{
            External_id = orderResponse.path("delivery_options[0].external_id");
        }
        orderResponse =timorHelper.deliveryOption(orderID,External_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
        orderResponse = timorHelper.applyCoupon(orderID, "dop_backend");
        Assert.assertTrue(timorHelper.validateStatusCodes(new HttpStatus[] {HttpStatus.OK, HttpStatus.CREATED}, orderResponse));
        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        Assert.assertEquals(orderResponse.path("attributes.delivery_type"),"scheduled_instant", "unable to create scheduled instant order");
        Assert.assertEquals(orderResponse.path("attributes.delivery_fee"),"0.0", "wrong delivery fee");
        Assert.assertNull(orderResponse.path("attributes.service_type"));
    }
    @Test
    public void VerifyServiceTypeandFee_ScheduledInstantOrderwithdeliveryOptionAsDelayedInstant() throws InterruptedException {
        user_lat = -6.212672;
        user_long = 106.825944;
        String current_day =timorHelper.getCurrentDay("currentDay");
        String next_day =timorHelper.getCurrentDay("nextDay");
        buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, false);
        buruHelper.updatePharmacy(merchantId, merchant_locationId, next_day, true);
        buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, current_day, false);
        buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, next_day, true);
        Response orderResponse = timorHelper.createScheduledOrder(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", orderID), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        if (orderResponse.path("delivery_options[1].delivery_type").equals("scheduled_instant")){
            External_id = orderResponse.path("delivery_options[0].external_id");
        }else{
            External_id = orderResponse.path("delivery_options[1].external_id");
        }
        orderResponse =timorHelper.deliveryOption(orderID,External_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
        orderResponse = timorHelper.confirmOrder(orderResponse.path("customer_order_id").toString(), USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getOrderDetails(orderID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        Assert.assertEquals(orderResponse.path("attributes.delivery_type"),"delayed_instant", "unable to create scheduled instant order");
        Assert.assertEquals(orderResponse.path("attributes.service_type"),"regular","wrong service type populated");
        Assert.assertEquals(orderResponse.path("attributes.delivery_fee"),"5000.0","wrong delivery fee populated");
    }
    @Test
    public void VerifyReallocationFee_ScheduledInstantwithDeliveryOptionAsScheduledInstant() throws InterruptedException {
        Response orderResponse;
        user_lat = -6.212672;
        user_long = 106.825944;
        String current_day =timorHelper.getCurrentDay("currentDay");
        String next_day =timorHelper.getCurrentDay("nextDay");
        buruHelper.updatePharmacy(merchantId, merchant_locationId, current_day, false);
        buruHelper.updatePharmacy(merchantId, merchant_locationId, next_day, true);
        buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, current_day, false);
        buruHelper.updatePharmacy(reallocating_merchantId, reallocating_merchant_locationId, next_day, true);
        orderResponse = timorHelper.createScheduledOrderForReallocation(USER_ENTITY_ID,PRODUCT_LISTING_SCHEDULED,user_lat,user_long);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Create Order Failed");
        String orderId = orderResponse.path("customer_order_id").toString();
        timorHelper.getOrderDetails(orderId);
        Assert.assertTrue(checkStatusUntil("approved", orderId), "Order Creation Status in not Confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        String price = orderResponse.path("item_total").toString();
        if (orderResponse.path("delivery_options[1].delivery_type").equals("scheduled_instant")){
            External_id = orderResponse.path("delivery_options[1].external_id");
        }else{
            External_id = orderResponse.path("delivery_options[0].external_id");
        }
        orderResponse =timorHelper.deliveryOption(orderId,External_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "cannot add delivery options");
        orderResponse = timorHelper.applyCoupon(orderId, "dop_backend");
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Apply Valid Coupon - Failed");
        orderResponse = timorHelper.confirmOrder(orderId, USER_ENTITY_ID, price);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status is not Confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        String MERCHANT_LOCATION_ID = orderResponse.path("delivery_options[0].merchant_location_id").toString();
        System.out.println("mechant loc ID :" + MERCHANT_LOCATION_ID);
        System.out.println("Cancelling order Via internal API");
        orderResponse = timorHelper.cancelOrder(orderId);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Cancel Order Failed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(checkStatusUntil("confirmed", orderId), "Order Status in not Confirmed");
        orderResponse = timorHelper.getOrderDetails(orderId);
        Boolean verifyMerchant = timorHelper.validateSimple(MERCHANT_LOCATION_ID, orderResponse.path("attributes.reallocated_merchant_location_id").toString());
        Assert.assertTrue((!verifyMerchant), "Merchant Location ID not changed after Reject -  Failed in Reallocation");
        Assert.assertEquals(orderResponse.path("attributes.delivery_type"),"scheduled_instant","reallocated delivery_type is not same as initial allocation");
        Assert.assertNotNull("partner_total.collect_from_halodoc");
    }

}
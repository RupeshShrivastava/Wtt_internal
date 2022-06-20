package com.halodoc.omstests.updateCart;

import static com.halodoc.oms.orders.utilities.constants.Constants.PRICE;
import static com.halodoc.oms.orders.utilities.constants.Constants.PRODUCT_LISTING_SCHEDULED;
import static com.halodoc.oms.orders.utilities.constants.Constants.USER_ENTITY_ID;
import static com.halodoc.omstests.Constants.FILE_LOCATION;
import static com.halodoc.omstests.Constants.X_APP_TOKEN;
import static com.halodoc.omstests.Constants.order_base_url;
import static com.halodoc.omstests.Constants.update_cart_url;
import static com.halodoc.omstests.Constants.user_agent;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import com.halodoc.omstests.BaseOmsTest;
import com.halodoc.omstests.TimorHelper;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by nageshkumar
 * since  03/05/17.
 */
@Slf4j
public class UpdateCartTest extends OrdersBaseTest {

    @Test
    public void verifyUpdateCare() throws InterruptedException {
        Response orderResponse;
        orderResponse =  timorHelper.createOrderForReallocation(USER_ENTITY_ID, PRODUCT_LISTING_SCHEDULED);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse),"Create Order Failed");
        String orderId = orderResponse.path("customer_order_id").toString();
        String price = orderResponse.path("item_total").toString();
        timorHelper.getOrderDetails(orderId );
        Assert.assertTrue(checkStatusUntil("approved",orderId),"Order Creation Status in not Confirmed");
        orderResponse =  timorHelper.updateCart(orderId);
        Assert.assertTrue( timorHelper.validateStatusCode(HttpStatus.OK, orderResponse),"Order Get - Failed");
        String quantity = orderResponse.path("items.quantity").toString().replace("[","").replace("]","");
        Assert.assertEquals(quantity,"5");
        timorHelper.confirmOrder(orderId,USER_ENTITY_ID,price);
        orderResponse=timorHelper.getOrderDetails(orderId);
        Assert.assertTrue( timorHelper.verifyOrderStatus("confirmed", orderResponse),"Order Creation Status is not Confirmed");
    }

//
//    @Test
//    public void orderStateApproved() {
//        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
//        Response response = given().
//                                           contentType("application/json").
//                                           header("X-APP-TOKEN", X_APP_TOKEN).
//                                           header("Authorization", "Bearer " + access_token_customer).
//                                           header("User-Agent", user_agent).
//                                           body(fixture(FILE_LOCATION + "/update_cart_valid.json")).
//                                           expect().
//                                           log().all().
//                                           statusCode(200).
//                                           body("items.requested_quantity", hasItems(5)).
//                                           when().
//                                           put(order_base_url + update_cart_url.replace("$orderId", order_id));
//
//        log.info("orderStateApproved response : \n" + response.prettyPrint());
//    }
//
//    @Test (priority = 2)
//    public void invalidOrder() {
//        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
//        Response response = given().
//                                           contentType("application/json").
//                                           header("X-APP-TOKEN", X_APP_TOKEN).
//                                           header("Authorization", "Bearer " + access_token_customer).
//                                           header("User-Agent", user_agent).
//                                           body(fixture(FILE_LOCATION + "/update_cart_valid.json")).
//                                           expect().
//                                           body("code", containsString("3009")).
//                                           body("message", containsString("Order was not found")).
//                                           statusCode(404).
//                                           when().
//                                           put(order_base_url + update_cart_url.replace("$orderId", "invalid_order_id"));
//
//        log.info("invalidOrder response : \n" + response.prettyPrint());
//    }
//
//    @Test (priority = 2)
//    public void updateByDifferentUser() {
//        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
//        String access_token = timorHelper.getPharmacyAccessToken();
//        Response response = given().
//                                           contentType("application/json").
//                                           header("X-APP-TOKEN", X_APP_TOKEN).
//                                           header("Authorization", "Bearer " + access_token).
//                                           header("User-Agent", user_agent).
//                                           body(fixture(FILE_LOCATION + "/update_cart_valid.json")).
//                                           expect().
//                                           body("code", containsString("3009")).
//                                           body("message", containsString("Order was not found")).
//                                           statusCode(404).
//                                           when().
//                                           put(order_base_url + update_cart_url.replace("$orderId", order_id));
//
//        log.info("updateByDifferentUser response : \n" + response.prettyPrint());
//    }
//
//    @Test
//    public void invalidListingId() throws InterruptedException {
//        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
//        Thread.sleep(5000);
//        Response response = given().
//                                           contentType("application/json").
//                                           header("Authorization", "Bearer " + access_token_customer).
//                                           header("User-Agent", user_agent).
//                                           body(fixture(FILE_LOCATION + "/invalid_listing_id.json")).
//                                           expect().
//                                           log().all().
//                                           body("code", containsString("3009")).
//                                           body("message", containsString("Invalid Listing ID Passed")).
//                                           statusCode(404).
//                                           when().
//                                           put(order_base_url + update_cart_url.replace("$orderId", order_id));
//        log.info("invalidListingId response : \n" + response.prettyPrint());
//    }
//
//    @Test (priority = 2)
//    public void emptyItemsList() {
//        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
//        Response response = given().
//                                           contentType("application/json").
//                                           header("X-APP-TOKEN", X_APP_TOKEN).
//                                           header("Authorization", "Bearer " + access_token_customer).
//                                           header("User-Agent", user_agent).
//                                           body(fixture(FILE_LOCATION + "/empty_items_list.json")).
//                                           expect().
//                                           body("errors", hasItems("items may not be empty")).
//                                           statusCode(422).
//                                           when().
//                                           put(order_base_url + update_cart_url.replace("$orderId", order_id));
//
//        log.info("emptyItemsList response : \n" + response.prettyPrint());
//    }
//
//    @Test (priority = 2)
//    public void requestedQuantityNull() {
//        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
//        Response response = given().
//                                           contentType("application/json").
//                                           header("X-APP-TOKEN", X_APP_TOKEN).
//                                           header("Authorization", "Bearer " + access_token_customer).
//                                           header("User-Agent", user_agent).
//                                           body(fixture(FILE_LOCATION + "/request_quantity_null.json")).
//                                           expect().
//                                           body("errors", hasItems("items[0].requestedQuantity Requested Quantity cannot be null")).
//                                           statusCode(422).
//                                           when().
//                                           put(order_base_url + update_cart_url.replace("$orderId", order_id));
//
//        log.info("emptyItemsList response : \n" + response.prettyPrint());
//    }
//
//    @Test (priority = 3)
//    public void orderStateConfirmed() {
//        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
//        timorHelper.confirmOrder(access_token_customer, order_id);
//        Response response = given().
//                                           contentType("application/json").
//                                           header("X-APP-TOKEN", X_APP_TOKEN).
//                                           header("Authorization", "Bearer " + access_token_customer).
//                                           header("User-Agent", user_agent).
//                                           body(fixture(FILE_LOCATION + "/update_cart_valid.json")).
//                                           expect().
//                                           log().all().
//                //assert this needs to be fixed after HBE-45
//                        statusCode(200).
//                        body("items.requested_quantity", hasItems(5)).
//                        when().
//                        put(order_base_url + update_cart_url.replace("$orderId", order_id));
//
//        log.info("orderStateConfirmed response : \n" + response.prettyPrint());
//    }
//
//    @AfterClass
//    public void tearDown() {
//        timorHelper.logoutPatient(access_token_customer);
//    }

}

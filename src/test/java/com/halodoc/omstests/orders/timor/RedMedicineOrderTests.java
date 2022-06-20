package com.halodoc.omstests.orders.timor;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.komodo.KomodoUtil;
import com.halodoc.oms.orders.utilities.timor.TimorUtil;
import com.halodoc.omstests.orders.OrdersBaseTest;
import ch.qos.logback.classic.Level;
import io.restassured.response.Response;
import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RedMedicineOrderTests {
    TimorUtil timorHelper = new TimorUtil();
    KomodoUtil komodoHelper = new KomodoUtil();
    Response orderResponse;
    JSONObject jsonObject;

    private List<String> expectedRedMedicines = new ArrayList<>();
    private List<String> actualRedMedicine = new ArrayList<>();

    @BeforeTest (alwaysRun = true)
    public void setUp() throws IOException {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
        ch.qos.logback.classic.Logger root1 = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("io.netty");
        ch.qos.logback.classic.Logger root2 = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.redisson");
        root.setLevel(Level.OFF);
        root1.setLevel(Level.OFF);
        root2.setLevel(Level.OFF);
    }

    @Test (groups = { "regression" }, description = "Verify create and confirm order when red medicine does not exist.")
    public void createAndConfirmOrderNoRedMedicine() {
        Response orderResponse = timorHelper.createOrderV4OneMedicine(USER_ENTITY_ID, PRODUCT_LISTING_NOT_RED_MEDICINE);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.checkOrderStatus(orderID,"approved");
        Assert.assertEquals(orderResponse.path("items[0].attributes.prescription_required"), FALSE);

        orderResponse = timorHelper.confirmOrder(orderID, USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        timorHelper.checkOrderStatus(orderID,"confirmed");
    }

    @Test (groups = { "sanity", "regression" }, description = "Verify create and confirm order with one red medicine")
    public void createAndConfirmOrderOneRedMedicine() {
        Response orderResponse = timorHelper.createOrderV4OneMedicine(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.checkOrderStatus(orderID,"approved");
        Assert.assertEquals(orderResponse.path("items[0].attributes.prescription_required"), TRUE);

        orderResponse = timorHelper.confirmOrder(orderID, USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        timorHelper.checkOrderStatus(orderID,"confirmed");
    }


    @Test (groups = { "sanity", "regression" }, description = "Verify submitting of medicine form with one symptom")
    public void verifySubmissionOfMedicineFormWithOneSymptom() {
        expectedRedMedicines.add(TRUE);
        expectedRedMedicines.add(FALSE);
        Response orderResponse = timorHelper.createOrderV4TwoMedicine(USER_ENTITY_ID, PRODUCT_LISTING, PRODUCT_LISTING_NOT_RED_MEDICINE);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.checkOrderStatus(orderID,"approved");
        try{
            String firstMedicine = orderResponse.path("items[0].attributes.prescription_required");
            String secondMedicine = orderResponse.path("items[1].attributes.prescription_required");
            actualRedMedicine.add(firstMedicine);
            actualRedMedicine.add(secondMedicine);
            Assert.assertEquals(actualRedMedicine,expectedRedMedicines);
       }catch (Exception e){ }
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        timorHelper.checkOrderStatus(orderID,"confirmed");
        orderResponse = komodoHelper
                .submitMedicineForm(KOMODO_MEDICINE_FORM_SUBMIT_ONE_SYMPTOM_JSON, USER_ENTITY_ID, orderID, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, orderResponse));
        orderResponse = timorHelper.getOrderDetailsV2(orderID);
        Assert.assertEquals(orderResponse.path("attributes.verified"), TRUE);
    }

    /*
    @Test (groups = { "regression" }, description = "Verify submitting of medicine form with three symptom")
    public void verifySubmissionOfMedicineFormWithThreeSymptom() {
        Response orderResponse = timorHelper.createOrderV4TwoMedicine(USER_ENTITY_ID, PRODUCT_LISTING, PRODUCT_LISTING_NOT_RED_MEDICINE);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String customer_order_id = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", customer_order_id), "Order Creation Failed");
        orderResponse = timorHelper.confirmOrder(customer_order_id, USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        orderResponse = komodoHelper
                .submitMedicineForm(KOMODO_MEDICINE_FORM_SUBMIT_THREE_SYMPTOM_JSON, USER_ENTITY_ID, customer_order_id, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, orderResponse));
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertEquals(orderResponse.path("attributes.verified"), TRUE);
    }

    @Test (groups = { "sanity", "regression" }, description = "Verify submitting of medicine form with other symptom")
    public void verifySubmissionOfMedicineFormWithOtherSymptom() {
        Response orderResponse = timorHelper.createOrderV4TwoMedicine(USER_ENTITY_ID, PRODUCT_LISTING, PRODUCT_LISTING_NOT_RED_MEDICINE);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String customer_order_id = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", customer_order_id), "Order Creation Failed");
        orderResponse = timorHelper.confirmOrder(customer_order_id, USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        orderResponse = komodoHelper
                .submitMedicineForm(KOMODO_MEDICINE_FORM_SUBMIT_OTHER_SYMPTOM_JSON, USER_ENTITY_ID, customer_order_id, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, orderResponse));
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertEquals(orderResponse.path("attributes.symptoms_comment"), KOMODO_MEDICINE_FORM_OTHER_COMMENT);
    }

    @Test (groups = { "regression" }, description = "Verify submitting of medicine form when order has been cancelled")
    public void verifySubmissionOfMedicineWhenOrderCancelled() throws InterruptedException {
        Response orderResponse = timorHelper.createOrderV4TwoMedicine(USER_ENTITY_ID, PRODUCT_LISTING, PRODUCT_LISTING_NOT_RED_MEDICINE);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String customer_order_id = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", customer_order_id), "Order Creation Failed");
        orderResponse = timorHelper.confirmOrder(customer_order_id, USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        komodoHelper.cancelOrder(USER_ENTITY_ID, customer_order_id, false, false);
        orderResponse = komodoHelper
                .submitMedicineForm(KOMODO_MEDICINE_FORM_SUBMIT_OTHER_SYMPTOM_JSON, USER_ENTITY_ID, customer_order_id, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, orderResponse));
        Assert.assertEquals(orderResponse.path("code"), KOMODO_RED_MEDICINE_ERROR_CODE);
    }

    @Test (groups = { "regression" }, description = "Verify submitting of medicine form when order has been shipped")
    public void verifySubmissionOfMedicineWhenOrderShipped() {
        Response orderResponse = timorHelper.createOrderV4TwoMedicine(USER_ENTITY_ID, PRODUCT_LISTING, PRODUCT_LISTING_NOT_RED_MEDICINE);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String customer_order_id = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", customer_order_id), "Order Creation Failed");
        orderResponse = timorHelper.confirmOrder(customer_order_id, USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        int shipping_id = orderResponse.path("shipments[0].id");
        timorHelper.markOrderAsShipped(customer_order_id, shipping_id);
        orderResponse = komodoHelper
                .submitMedicineForm(KOMODO_MEDICINE_FORM_SUBMIT_OTHER_SYMPTOM_JSON, USER_ENTITY_ID, customer_order_id, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.OK, orderResponse));
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertEquals(orderResponse.path("attributes.verified"), TRUE);
    }

    @Test (groups = { "regression" }, description = "Verify submitting of medicine form when order has been delivered")
    public void verifySubmissionOfMedicineWhenOrderDelivered() {
        Response orderResponse = timorHelper.createOrderV4TwoMedicine(USER_ENTITY_ID, PRODUCT_LISTING, PRODUCT_LISTING_NOT_RED_MEDICINE);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String customer_order_id = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", customer_order_id), "Order Creation Failed");
        orderResponse = timorHelper.confirmOrder(customer_order_id, USER_ENTITY_ID, orderResponse.path("item_total").toString());
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NO_CONTENT, orderResponse), "Order Confirm Failed");
        orderResponse = timorHelper.getOrderDetails(customer_order_id);
        int shipping_id = orderResponse.path("shipments[0].id");
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        Assert.assertTrue(timorHelper.verifyOrderStatus("confirmed", orderResponse), "Order Creation Status in not Confirmed");
        timorHelper.markOrderAsDelivered(customer_order_id, shipping_id);
        orderResponse = komodoHelper
                .submitMedicineForm(KOMODO_MEDICINE_FORM_SUBMIT_OTHER_SYMPTOM_JSON, USER_ENTITY_ID, customer_order_id, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, orderResponse));
        Assert.assertEquals(orderResponse.path("code"), KOMODO_RED_MEDICINE_ERROR_CODE);
    }

    @Test (groups = { "regression" }, description = "Verify submitting of medicine form when order state cannot be changed")
    public void verifySubmitFormForAbandonedOrder() {
        Response orderResponse = timorHelper.createOrderV4OneMedicine(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String customer_order_id = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", customer_order_id), "Order Creation Failed");
        timorHelper.abandonOrder(customer_order_id);
        orderResponse = komodoHelper
                .submitMedicineForm(KOMODO_MEDICINE_FORM_SUBMIT_ONE_SYMPTOM_JSON, USER_ENTITY_ID, customer_order_id, false, false);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.BAD_REQUEST, orderResponse));
        Assert.assertEquals(orderResponse.path("code"), KOMODO_RED_MEDICINE_ERROR_CODE);
    }

    @Test (groups = { "sanity", "regression" }, description = "Verify CMS consists of prescription_required attribute")
    public void verifyPrescriptionRequiredAttribute() {
        Response productDescription = timorHelper.getProductDescription(PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, productDescription));
        Assert.assertEquals(productDescription.path("[0].prescription_required").toString(), TRUE);
    }

    @Test (groups = { "sanity", "regression" }, description = "Verify change of verification status")
    public void verifyChangeVerificationStatus() {
        Response orderResponse = timorHelper.createOrderV4OneMedicine(USER_ENTITY_ID, PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String customer_order_id = orderResponse.path("customer_order_id").toString();
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Get - Failed");
        Assert.assertTrue(checkStatusUntil("approved", customer_order_id), "Order Creation Failed");
        orderResponse = timorHelper.changeVerificationStatus(customer_order_id);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse));
        orderResponse = timorHelper.getOrderDetailsV2(customer_order_id);
        Assert.assertEquals(orderResponse.path("attributes.verified"), TRUE);
    }*/
}
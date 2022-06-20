package com.halodoc.omstests.orders.buru;

import com.halodoc.oms.orders.utilities.buru.BuruUtil;
import com.halodoc.oms.orders.utilities.timor.TimorUtil;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static com.halodoc.oms.orders.utilities.constants.Constants.PHONE_NUMBER_CUSTOMER;

public class BuruRegression extends OrdersBaseTest {
    @Test(priority = 1)
    public void getOrderDetails() throws InterruptedException {
        Response orderDetails = buruHelper.createAndConfirmOrder(timorHelper);
        Response getOrderDetails =  buruHelper.getOrderDetails(orderDetails.path("customer_order_id").toString(),orderDetails.path("items[0].group_id"));
        Assert.assertTrue( buruHelper.validateStatusCode(HttpStatus.OK, getOrderDetails),"Get order details failed");
    }

    @Test (priority = 2)
    public void markReadyForShipment() throws InterruptedException {
        Response orderDetails = buruHelper.createAndConfirmOrder(timorHelper);
        Response readyResponse =  buruHelper.markReady(orderDetails.path("customer_order_id").toString(),orderDetails.path("items[0].group_id"));
        Assert.assertTrue( buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, readyResponse),"Mark for Ready Order - Failed");
    }


    @Test (priority = 3)
    public void markReadyForDispatch() throws InterruptedException {
        Response orderDetails = buruHelper.createAndConfirmOrder(timorHelper);
        Response readyResponse =  buruHelper.markReady(orderDetails.path("customer_order_id").toString(),orderDetails.path("items[0].group_id"));
        Assert.assertTrue( buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, readyResponse),"Mark for Ready Order - Failed");
        Response dispatchResponse =  buruHelper.markDispatch(orderDetails.path("customer_order_id").toString(),orderDetails.path("items[0].group_id"));
        Assert.assertTrue( buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, dispatchResponse),"Mark for Reject Order - Failed");
    }
    @Test (priority = 4)
    public void rejectOrder() throws InterruptedException {
        Response orderDetails = buruHelper.createAndConfirmOrder(timorHelper);
        Response rejectResponse =  buruHelper.markReject(orderDetails.path("customer_order_id").toString(),orderDetails.path("items[0].group_id"));
        Assert.assertTrue( buruHelper.validateStatusCode(HttpStatus.NO_CONTENT, rejectResponse),"Mark for Reject Order - Failed");
    }

    @Test(priority =5)
    public void fetchBuruConfig(){
        Response config=buruHelper.getConfig();
        Assert.assertNotNull(config);

    }
    @Test(priority = 6)
    public void verifyDoctorSpecialities(){

        Response config=buruHelper.getConfig();
        List<String> docSpecialities=config.path("doctor_specialities");
        Assert.assertTrue(docSpecialities.contains("Neurosurgeon"));
    }
    @Test
    public void verifyPrescriptionConfig(){
        Response config=buruHelper.getConfig();
        HashMap docSpecialities=config.path("prescription_configuration");
        Assert.assertTrue(docSpecialities.containsKey("max_quantity"));
    }
    @Test
    public void verifyPrescriptionValues(){
        Response config=buruHelper.getConfig();
        HashMap<String,Integer>docSpecialities=config.path("prescription_configuration");
        Assert.assertTrue(!docSpecialities.get("max_quantity").toString().isEmpty());
    }
    @Test
    public void verifyPrescriptionValuesMaxQuantity(){
        Response config=buruHelper.getConfig();
        HashMap<String,Integer>docSpecialities=config.path("prescription_configuration");
        Assert.assertEquals(docSpecialities.get("max_quantity").toString(),"999");
    }
    @Test
    public void verifyPrescriptionValuesMinQuantity(){
        Response config=buruHelper.getConfig();
        HashMap<String,Integer>docSpecialities=config.path("prescription_configuration");
        Assert.assertEquals(docSpecialities.get("min_quantity").toString(),"1");
    }
    @Test
    public void verifyPrescriptionValuesDosage_step_value(){
        Response config=buruHelper.getConfig();
        HashMap<String,Float>docSpecialities=config.path("prescription_configuration");
        Assert.assertEquals(docSpecialities.get("dosage_step_value").toString(),"0.5");
    }

    @Test
    public void verifyPrescriptionValuesMax_dosage_value(){
        Response config=buruHelper.getConfig();
        HashMap<String,Integer>docSpecialities=config.path("prescription_configuration");
        Assert.assertEquals(docSpecialities.get("max_dosage_value").toString(),"20");
    }

    @Test
    public void verifyPrescriptionValuesMin_frequency_value(){
        Response config=buruHelper.getConfig();
        HashMap<String,Integer>docSpecialities=config.path("prescription_configuration");
        Assert.assertEquals(docSpecialities.get("min_frequency_value").toString(),"1");
    }
    @Test
    public void verifygeoServiceable(){
        Response config=buruHelper.fetchGeo("-6.6488614","106.7309988");
        Assert.assertEquals(config.statusCode(),204);
    }
    @Test
    public void verifygeoUnServiceable(){
        Response config=buruHelper.fetchGeo("13.0237973","77.5963265");
        Assert.assertEquals(config.statusCode(),404);
    }
    @Test
    public void verifyEmptyLatLong(){
        Response config=buruHelper.fetchGeo("","");
        Assert.assertEquals(config.statusCode(),400);
    }

    @SneakyThrows
    @Test
    public void searchOrders(){
        Response orderResponse=buruHelper.createAndConfirmOrder(timorHelper);
        String orderID = orderResponse.path("customer_order_id").toString();
        Response config=buruHelper.searchInternalOrders(orderID);
        Assert.assertNotNull(config);
    }
    @SneakyThrows
    @Test
    public void verifysearchOrdersIsInNew_Order_State(){
        Response orderResponse=buruHelper.createAndConfirmOrder(timorHelper);
        String orderID = orderResponse.path("customer_order_id").toString();
        Response config=buruHelper.searchInternalOrders(orderID);
        Assert.assertEquals(config.path("status[0]"),"new_order");
    }

    @SneakyThrows
    @Test
    public void verifysearchOrdersIsUnread(){
        Response orderResponse=buruHelper.createAndConfirmOrder(timorHelper);
        String orderID = orderResponse.path("customer_order_id").toString();
        Response config=buruHelper.searchInternalOrders(orderID);
        Assert.assertEquals(config.path("attributes[0].read"),"false");
    }
    @Test
    public void getPharmacyUsers(){
        Response config=buruHelper.getAllPharmacyUsers("68f72f0b-713a-4e65-84a4-81cc585a96a9","a5eef771-1289-45f8-8825-6b80943f594f");
        Assert.assertEquals(config.statusCode(),200);
    }

    @Test
    public void verifyPharmacyUsersDetails(){
        Response config=buruHelper.getAllPharmacyUsers("68f72f0b-713a-4e65-84a4-81cc585a96a9","a5eef771-1289-45f8-8825-6b80943f594f");
        Assert.assertEquals(config.statusCode(),200);
        Assert.assertNotNull(config.path("gpid"));
    }
    @Test(enabled = false)
    public void getPharmacyProducts(){
        Response config=buruHelper.getAllPharmacyProducts("20","1","ACTAL");
        Assert.assertEquals(config.statusCode(),200);

    }

    @Test
    public void verifyCustomerDetails(){
        Response details=buruHelper.getCustomerDetails(PHONE_NUMBER_CUSTOMER);
        Assert.assertEquals(details.statusCode(),200);
    }
    @Test
    public void verifyCustomerDetailsName(){
        Response details=buruHelper.getCustomerDetails(PHONE_NUMBER_CUSTOMER);
        Assert.assertEquals(details.path("name"),"Manoj ");
    }
    @Test
    public void verifyCustomerDetailsUtmAttributes(){
        Response details=buruHelper.getCustomerDetails(PHONE_NUMBER_CUSTOMER);
        Assert.assertEquals(details.path("utm_attributes.utm_source"),"halodoc");
    }
    @Test
    public void verifyCustomerDetailsInvalidNumber(){
        Response details=buruHelper.getCustomerDetails("+91991647911");
        Assert.assertEquals(details.statusCode(),404);
    }

}

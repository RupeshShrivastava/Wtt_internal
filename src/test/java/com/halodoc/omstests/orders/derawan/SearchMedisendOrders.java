package com.halodoc.omstests.orders.derawan;

import io.restassured.path.json.JsonPath;
import com.halodoc.oms.orders.utilities.derawan.DerawanUtil;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SearchMedisendOrders {
    String shipment_id;
    String invalid_shipment_id;
    String shipment_statuses;
    String per_page;
    String sort_order;
    String startDate;
    String distributorBranchId;
    String InvalidDistributorBranchId;
    String distributorId;
    String invalidistributorId;
    String actualexternalID;
    String expectedexternalID;
    String actualshipmentstatus;
    String[] shipmentValues;
    String actualshipmentID;
    String actualdistributorbranchID;
    String actualdistributorID;
    String multipledistributorbranchids;
    String multipledistributorids;

    final String testDataResourcePath="medisendOrdersinfo.properties";
    Properties prop=new Properties();
    @BeforeClass
    public void setPrerequisite() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceStream = loader.getResourceAsStream(testDataResourcePath)) {
            prop.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        shipment_statuses=prop.getProperty("shipment_statuses");
        per_page=prop.getProperty("per_page");
        sort_order=prop.getProperty("sort_order");
        startDate=prop.getProperty("startDate");
        shipment_id=prop.getProperty("shipment_id");
        invalid_shipment_id=prop.getProperty("invalid_shipment_id");
        distributorBranchId=prop.getProperty("distributorBranchId");
        InvalidDistributorBranchId=prop.getProperty("InvalidDistributorBranchId");
        distributorId=prop.getProperty("distributorId");
        invalidistributorId=prop.getProperty("invalidistributorId");
        expectedexternalID=prop.getProperty("expectedexternalid");
        shipmentValues = prop.get("shipment_statuses").toString().split(",");
        multipledistributorbranchids = prop.get("multipledistributorbranchids").toString();
        multipledistributorids=prop.get("multipledistributorids").toString();
    }

    @Test(description = "Get order details along with shipmentID")
    public void getShipmentIDInfo(){

        Response response=DerawanUtil.getShipmentIdInfo(startDate,sort_order,per_page);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,response.prettyPrint());
        JsonPath obj=response.jsonPath();
        actualexternalID=obj.get("result[0].shipments[0].external_id");
        Assert.assertNotNull(actualexternalID);
    }

    @Test(description = "Get order details along with shipment status")
    public void getOrderStatus(){
        Response response=DerawanUtil.getOrderStatus(startDate,sort_order,per_page,shipment_statuses);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,response.prettyPrint());
        JsonPath obj=response.jsonPath();
        actualshipmentstatus=obj.get("result[0].shipments[0].status");
        Assert.assertEquals(actualshipmentstatus,shipmentValues[0]);

    }

    @Test(description = "Fetching Details of order using shipment ID")
    public void getOrderByShipmentId(){
        Response response=DerawanUtil.getOrderByShipmentId(shipment_id);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,response.prettyPrint());
        JsonPath obj=response.jsonPath();
        actualshipmentID=obj.get("result[0].shipments[0].external_id");
        Assert.assertEquals(actualshipmentID,shipment_id);
    }

    @Test(description = "Fetching shipment details with invalid shipmentID")
    public void getOrderByInvalidShipmentId(){
        Response response=DerawanUtil.getOrderByShipmentId(invalid_shipment_id);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,response.prettyPrint());
    }

    @Test(description = "Searching orders by the ID of distributor branches")
    public void orderByDistributorBranchId(){
        Response response=DerawanUtil.getOrderByDistributorBranchId(startDate,sort_order,per_page,distributorBranchId);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,response.prettyPrint());
        JsonPath obj=response.jsonPath();
        actualdistributorbranchID=obj.get("result[0].shipments[0].distributor_entity_id");
        Assert.assertEquals(actualdistributorbranchID,distributorBranchId);
    }

    @Test(description = "Searching orders by the invalid ID of distributor branches")
    public void orderByInvalidDistributorBranchId(){
        Response response=DerawanUtil.getOrderByDistributorBranchId(startDate,sort_order,per_page,InvalidDistributorBranchId);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,response.prettyPrint());
    }

    @Test(description = "Searching orders by ID of distributor ")
    public void orderByDistributorId(){
        Response response=DerawanUtil.getOrderByDistributorId(startDate,sort_order,per_page,distributorId);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,response.prettyPrint());
        JsonPath obj=response.jsonPath();
        actualdistributorID=obj.get("result[0].shipments[0].distributor_id");
        Assert.assertEquals(actualdistributorID,distributorId);
    }

    @Test(description = "Searching orders by invalid ID of distributor")
    public void orderByInvalidDistributorId(){
        Response response=DerawanUtil.getOrderByDistributorId(startDate,sort_order,per_page,invalidistributorId);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,response.prettyPrint());
    }

    @Test(description = "Searching orders by the multiple distributor branches IDs")
    public void orderByMultipleDistributorBranchId(){
        Response response=DerawanUtil.getOrderByMultipleDistributorBranchId(startDate,sort_order,per_page,multipledistributorbranchids);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,response.prettyPrint());
        JsonPath obj=response.jsonPath();
        String actualDistributerIdFirst=obj.get("result[0].shipments[0].distributor_entity_id");
        String actualDistributerIdSecond=obj.get("result[4].shipments[0].distributor_entity_id");
       String [] multipledistributorbranchid = prop.get("multipledistributorbranchids").toString().split(",");
        Assert.assertEquals(actualDistributerIdFirst,multipledistributorbranchid[1]);
        Assert.assertEquals(actualDistributerIdSecond,multipledistributorbranchid[0]);
  }
    @Test(description = "Searching orders by the distributor IDs")
    public void orderByMultipleDistributorId(){
        Response response=DerawanUtil.getOrderByMultipleDistributorId(startDate,sort_order,per_page,multipledistributorbranchids);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,response.prettyPrint());

    }
}

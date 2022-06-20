package com.halodoc.omstests;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.opencsv.CSVReader;
import ch.qos.logback.classic.Level;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class MultiShipmentOrderCancel {

   String file = "/Users/amitsingh/Desktop/workspace/git/oms-backend-test/src/main/resources/multipleshipmentOrder.csv" ;
   String BASE_URI = "http://internal-c268e291-timor-timoromspro-eda5-1341999345.ap-southeast-1.elb.amazonaws.com:8010/v1/orders/internal/";
   HashMap<String,String> headers = new HashMap<>();
   String cancelRequest = "{\n" + "    \"comments\": \"\",\n" + "    \"key\": \"driver_too_far\",\n" + "    \"reason\": \"cancelled from backend\",\n" + "    \"type\": \"system_cancelled\"\n" + "}";

   @BeforeClass
   public void setup(){
       ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
       root.setLevel(Level.OFF);
   }


    @DataProvider (name = "orderId")
    public Object[] completedCardPayment() throws SQLException, IOException {
        FileReader filereader = new FileReader(file);
        CSVReader csvReader = new CSVReader(filereader);
        String[] nextRecord;
        ArrayList orderIds = new ArrayList<>();
        while ((nextRecord = csvReader.readNext()) != null) {
            for (int i = 0; i < nextRecord.length; i++) {
                orderIds.add(nextRecord[i]);
            }
        }

        String [] list = new String[orderIds.size()];
        for(int i=0;i<orderIds.size();i++){
            list[i]=orderIds.get(i).toString();
        }

        return list;
    }


    @Test(dataProvider = "orderId")
    public void cancelOrders(String orderId) throws IOException {
        headers.put("X-APP-TOKEN","4017351e-1832-4d81-ba02-5935194ce8b8");
        Response response = getApiStatusNoQueryParams(BASE_URI+orderId,headers);
        JSONObject jsonObject = new JSONObject(response.asString());
        int len = jsonObject.getJSONArray("shipments").length();
        for(int i =0;i<len;i++){
            //JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONArray("shipments").get(i));
            System.out.println(jsonObject.getJSONArray("shipments").getJSONObject(i).get("status").toString());
            if(!(jsonObject.getJSONArray("shipments").getJSONObject(i).get("status").toString()).equals("delivered")
            && !(jsonObject.getJSONArray("shipments").getJSONObject(i).get("status").toString()).equals("cancelled")){
                System.out.println(jsonObject.getJSONArray("shipments").getJSONObject(i).get("group_id").toString());

                putApiStatus(cancelRequest,
                        BASE_URI+orderId+"/groups/"+jsonObject.getJSONArray("shipments").getJSONObject(i).get("group_id").toString()+"/cancel",headers);
            }
        }


    }


    public Response putApiStatus(String jsonBody, String pathParams, HashMap<String, String> headers) {

        Response response;
        response = given().log().uri()
                          .headers(headers)
                          .when()
                          .body(jsonBody)
                          .put(pathParams)
                          .then()
                          .extract()
                          .response();
        if(response.getStatusCode()!= HttpStatus.SC_OK  && response.getStatusCode()!= HttpStatus.SC_CREATED && response.getStatusCode()!= HttpStatus.SC_NO_CONTENT)
            Assert.fail("Response obtained is " + response.getStatusCode()
                    +"\nResponse body "+ response.getBody().asString());
        else if(response.getBody().toString().isEmpty())
            Assert.fail("Response body is empty");
        return response;
    }

    public Response getApiStatusNoQueryParams(String pathParams,HashMap<String, String> headers) {
        Response response;
        headers.put("Content-Type","application/json");

        response = given().log().uri()
                          .headers(headers)
                          .when()
                          .get(pathParams)
                          .then()
                          .extract()
                          .response();
        if(response.getStatusCode()!=200)
            Assert.fail("Response obtained is " + response.getStatusCode());
        else if(response.getBody().toString().isEmpty())
            Assert.fail("Response body is empty");
        return response;
    }
}

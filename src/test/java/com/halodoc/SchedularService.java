package com.halodoc;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.JsonBuilderUtil;
import com.opencsv.CSVReader;
import ch.qos.logback.classic.Level;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class SchedularService {



    String file = "/Users/amitsingh/Desktop/workspace/git/oms-backend-test/src/main/resources/multipleshipmentOrder.csv" ;
    String BASE_URI = "http://acara-scheduler-service.stage-k8s.halodoc.com/v1/tasks";
    HashMap<String,String> headers = new HashMap<>();

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


    @Test (dataProvider = "orderId")
    public void cancelOrders(String orderId) throws IOException {
        headers.put("X-APP-TOKEN","d7095818-70ec-4e14-a182-9b1223acbc22");
        headers.put("Content-type","application/json");
        HashMap data = new HashMap();
        data.put("order_id","/v2/orders/internal/"+orderId+"/cancel");
        String body = JsonBuilderUtil.generateDataFromMap("pd-subscription","createTaskAcara",data);
        postApiStatus(body,BASE_URI,headers);

    }

    @Step ("{0}")
    public Response postApiStatus(String jsonBody,String pathParams,HashMap<String, String> headers) {
        Response response;
        response = given().log().all()
                          .headers(headers)
                          .when()
                          .body(jsonBody)
                          .post(pathParams)
                          .then()
                          .extract()
                          .response();
        if(response.getStatusCode()!= HttpStatus.SC_OK  && response.getStatusCode()!= HttpStatus.SC_CREATED
                && response.getStatusCode()!= HttpStatus.SC_ACCEPTED && response.getStatusCode()!= HttpStatus.SC_NO_CONTENT) {
            Assert.fail("Response obtained is " + response.getStatusCode() + "\nResponse body " + response.getBody().asString());
        }
        else if(response.getBody().toString().isEmpty())
            Assert.fail("Response body is empty");
        return response;
    }
}

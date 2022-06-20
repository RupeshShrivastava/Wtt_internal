package com.halodoc.subscripation.timor;

import static io.restassured.RestAssured.given;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import com.halodoc.oms.orders.utilities.pdSubscription.TimorSubscriptionUtils;
import com.opencsv.CSVReader;
import ch.qos.logback.classic.Level;
import io.restassured.response.Response;

public class LoadTesting {

    String file = "/Users/amitsingh/Desktop/workspace/git/oms-backend-test/src/main/resources/subscriptionUser.csv" ;
    HashMap<String,String> headers = new HashMap<>();
    CommonUtils commonUtils = new CommonUtils();
    TimorSubscriptionUtils timorSubscriptionUtils = new TimorSubscriptionUtils();
    String orderId;

    @BeforeClass
    public void setup(){
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
        root.setLevel(Level.OFF);
    }


    @DataProvider (name = "userId")
    public Object[] userId() throws SQLException, IOException {
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


    @Test(dataProvider = "userId")
    public void confirmSubcribeOrder(String userId) throws InterruptedException {
         /*orderId = timorSubscriptionUtils.createOrder(userId,"3be0ef01-9c8f-4069-82c2-048a22e4bca7","e098d4ff-2082-469e-81ef-828caab33d17",
                "7f3e2dfd-79f7-4eca-9b4d-68db4b9e7e12");
        timorSubscriptionUtils.updateCart(timorSubscriptionUtils.getOrderDetails(orderId,"approved"));
        timorSubscriptionUtils.confirmGopayWalletPayment(orderId);
        timorSubscriptionUtils.getOrderDetails(orderId,"confirmed");*/

    }
}

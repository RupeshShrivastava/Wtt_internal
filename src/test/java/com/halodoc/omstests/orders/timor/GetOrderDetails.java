package com.halodoc.omstests.orders.timor;

import com.halodoc.oms.orders.utilities.timor.TimorUtil;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.halodoc.oms.orders.utilities.constants.Constants.USER_ENTITY_ID;
@Slf4j
public class GetOrderDetails {

    public TimorUtil timorHelper = new TimorUtil();
    private int retryCount=3;

    public long getOrderResponse(String orderID) {
        long allocationTime=0;
        if(retryLogicAllocationTime(orderID)){
            Response orderResponse=timorHelper.getOrderDetails(orderID);
            Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Failed to get Order details");
            allocationTime = fetchDiff(orderResponse);
        }
        return allocationTime;
    }

    public long fetchDiff(Response response) {
        JsonPath jsonPathEvaluator;
        jsonPathEvaluator = response.jsonPath();
        long startTime = jsonPathEvaluator.get("created_at");

        long endTime = jsonPathEvaluator.get("history.created_at[0]");
        long diffInMillies = Math.abs(startTime - endTime);
        log.info("TIME TAKEN IN SECS : " + TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS));
        return TimeUnit.MILLISECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);


    }


    @SneakyThrows
    public boolean retryLogicAllocationTime(String  orderID) {

        int n = 0;
        while (n <= retryCount) {
            Response orderResponse = timorHelper.getOrderDetails(orderID);
            JsonPath jsonPath=orderResponse.jsonPath();
            List<String> history=jsonPath.getList("history.created_at");

            try {
                if(history.size()>1){
                    if (null != jsonPath.get("history.created_at[1]")) {
                        return true;
                    }
                }
                else  if(1==history.size()){
                if((null != jsonPath.get("history.created_at[0]"))) {
                    System.out.println("Single history seen");
                    return true;
                }
                }
            } catch (Exception ex) {

            }

            n++;
            log.info("retry logic triggered");
            Thread.sleep(5000);
        }
        return false;
    }
}

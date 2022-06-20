package com.halodoc.omstests.orders.timor;

import com.halodoc.oms.orders.utilities.timor.TimorUtil;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.halodoc.oms.orders.utilities.constants.Constants.PRODUCT_LISTING;
import static com.halodoc.oms.orders.utilities.constants.Constants.USER_ENTITY_ID;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class FulfilmentLoadTest implements Runnable {
    public List<String> orderIds;
    public FulfilmentLoadTest(List<String> orderIds){
        this.orderIds=orderIds;
    }
    public TimorUtil timorHelper = new TimorUtil();

    @Override
    public void run() {
        Response orderResponse = timorHelper.createOrder(USER_ENTITY_ID,PRODUCT_LISTING);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, orderResponse), "Order Creation Failed");
        String orderID = orderResponse.path("customer_order_id").toString();
        log.info(orderID);
        orderIds.add(orderID);
    }


}

package com.halodoc.omstests.orders.timor;

import com.halodoc.omstests.orders.OrdersBaseTest;

import lombok.SneakyThrows;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class TriggerOrderThreads extends OrdersBaseTest {

    private List<String> orderIds = new ArrayList<>();
    GetOrderDetails getOrderDetails = new GetOrderDetails();
    public static final int batchSize = 2000;

    @Test
    public void initLoad() {
        System.setProperty("no_ofOrders","1");
        ExecutorService executor = Executors.newFixedThreadPool(batchSize);
        for (int i = 0; i < Integer.valueOf(System.getProperty("no_ofOrders")); i++) {
            Runnable fulfilmentLoadTest = new FulfilmentLoadTest(orderIds);
            executor.execute(fulfilmentLoadTest);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        log.info("Finished all threads .. Created orders :  " + orderIds.size());
    }


    @SneakyThrows
    @Test(dependsOnMethods = "initLoad")
    public void initAverage() {
        List<Long> fulfilmentTime = new ArrayList<>();
        log.info("Getting average time ");
        for (int i = 0; i < orderIds.size(); i++) {
            long fulfilTime = getOrderDetails.getOrderResponse(orderIds.get(i));
            if (0 != fulfilTime) {
                fulfilmentTime.add(fulfilTime);
            }
        }
        fulfilmentTime.removeAll(Arrays.asList(Integer.valueOf(0)));
        log.info("TIME QTY : " + fulfilmentTime.size());
        log.info("TIME DATA : " + fulfilmentTime.toString());
        OptionalDouble average = fulfilmentTime
                .stream()
                .mapToLong(a -> a)
                .average();
        if(fulfilmentTime.size()==orderIds.size()) {
            for (int index = 0; index < fulfilmentTime.size(); index++) {
                System.out.println("Time taken for order " + orderIds.get(index) + " is " + fulfilmentTime.get(index));
            }
        }else{

            for(int temp=0;temp<fulfilmentTime.size();temp++){

                System.out.println(fulfilmentTime.get(temp));
            }
        }
        log.info("Number of orders FAILED to move to approved state are "+(orderIds.size()-fulfilmentTime.size()));
        log.info("AVERAGE TIME TAKEN FOR Fulfilment of   " + orderIds.size() +" Orders "+ " IS " + average.getAsDouble());
    }
}

package com.halodoc.omstests;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//import com.halodoc.commonstests.OrdersBaseTest;

/**
 * Created by nageshkumar
 * since  10/04/17.
 */
@Slf4j
public class BaseOmsTest {
    public String accessToken;
    public TimorHelper timorHelper;
    public String userAgent;
    public final Properties properties = new Properties();
    public InputStream inputStream = null;

    @BeforeSuite
    public void beforeSuite() throws IOException, InterruptedException {

        log.info("Setting up OMS api test suite..");


    }


    @AfterSuite
    public void afterSuite() {
        log.info("Teardown after OMS api test suite.");
    }




    public boolean validateStatusCode(int statusCode, Response response) {
        boolean res = false;
        try {
            Thread.sleep(2000);


            if (response.statusCode() == statusCode) {
                log.info("Success!!!! Expected status code " + statusCode + " matches with actual status code " + response.statusCode());
                res = true;
            } else {
                log.info("Failure!!! Expected status code " + statusCode + " does not match with actual status code " + response.statusCode());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
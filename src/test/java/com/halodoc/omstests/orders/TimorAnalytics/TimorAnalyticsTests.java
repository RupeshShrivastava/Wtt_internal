package com.halodoc.omstests.orders.TimorAnalytics;

import com.halodoc.oms.orders.utilities.timor.AnalyticsUtil;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.HashMap;
import static com.halodoc.oms.orders.utilities.constants.TimorAnalyticsConstants.*;

@Slf4j
public class TimorAnalyticsTests {
    private AnalyticsUtil utils;
    private Response response;

    @BeforeTest
    public void setUp() {
        utils = new AnalyticsUtil();
        response = utils.getProductPerformance("valid");
    }

    @Test(description = "Verify Product Performance based on Stock")
    public void verifyProductPerformance() {
        HashMap performance = utils.getPerformanceObject(response);

        int size = performance.size();
        log.info("Response Size: " + size);
        Assert.assertEquals(size, 4, "Non inventory Products should not  be visible in response");
    }

    @Test(description = "Verifying Visual Cues based on Product Stock")
    public void verifyVisualCues() {
        HashMap performance = utils.getPerformanceObject(response);

        //Verifying Low Inventory Visual Cue
        utils.verifyVisualCuesFromPerformanceObject(performance, LOW_INVENTORY_STOCK_PRODUCT_ID, LOW_INVENTORY_FILTER);

        //Verifying Out of stock Visual Cue
        utils.verifyVisualCuesFromPerformanceObject(performance, OUT_OF_STOCK_PRODUCT_ID, OUT_OF_STOCK_FILTER);

        //Verifying On Demand Visual Cue
        utils.verifyVisualCuesFromPerformanceObject(performance, ON_DEMAND_PRODUCT_ID, ON_DEMAND_FILTER);

        //Verifying High Inventory Visual Cue
        ArrayList visualCues = utils.getVisualCues(performance, HIGH_INVENTORY_STOCK_PRODUCT_ID);
        int visualCuesSize = visualCues.size();
        log.info("Visual Cue Size for High inventory products: " + visualCuesSize);
        Assert.assertEquals(visualCuesSize, 0, "Visual Cues should  not Present for High inventory stock products");
    }

    @Test(description = "Verifying Invalid ProductPerformance Response")
    public void verifyInvalidProductPerformance() {
        Response invalidResponse = utils.getProductPerformance("Invalid");

        int performanceSize = utils.getPerformanceObject(invalidResponse).size();
        log.info("Invalid Performance Size: " + performanceSize);
        Assert.assertEquals(performanceSize, 0, "Expected Response should be empty");
    }

    @Test(description = "Verifying valid performance filters visualCues")
    public void verifyValidFilterTypes() {
        //verifying On Demand Filter
        utils.verifyFilterVisualCues(ON_DEMAND_FILTER);

        //verifying Low Inventory Filter
        utils.verifyFilterVisualCues(LOW_INVENTORY_FILTER);

        //verifying Out Of Stock Filter
        utils.verifyFilterVisualCues(OUT_OF_STOCK_FILTER);
    }

    @Test(description = "Verifying invalid performance filters")
    public void verifyInvalidFilters() {
        //Filtering with invalid filterType
        Response filterResponse = utils.getPerformanceFilters(MERCHANT_LOCATION_ID, RandomStringUtils.randomAlphanumeric(5), "name", "asc");
        Assert.assertTrue(utils.validateStatusCode(HttpStatus.BAD_REQUEST,  filterResponse), "Status Codes Match");

        //Filtering with invalid sortBy
        filterResponse =  utils.getPerformanceFilters(MERCHANT_LOCATION_ID, ON_DEMAND_FILTER, RandomStringUtils.randomAlphabetic(5), "asc");
        Assert.assertTrue(utils.validateStatusCode(HttpStatus.BAD_REQUEST,  filterResponse), "Status Codes Match");

        //Filtering with invalid sortType
        filterResponse =  utils.getPerformanceFilters(MERCHANT_LOCATION_ID, ON_DEMAND_FILTER, "name", RandomStringUtils.randomAlphabetic(5));
        Assert.assertTrue(utils.validateStatusCode(HttpStatus.BAD_REQUEST,  filterResponse), "Status Codes Match");

        //Filtering with invalid MERCHANT_LOCATION_ID
        filterResponse =  utils.getPerformanceFilters(RandomStringUtils.randomAlphabetic(5), ON_DEMAND_FILTER, "name", "asc");
        Assert.assertTrue(utils.validateStatusCode(HttpStatus.OK,  filterResponse), "Status Codes Match");
        int resultSize = filterResponse.jsonPath().getList("result").size();
        Assert.assertEquals(resultSize, 0, "Result should be empty for invalid merchant location Id");
    }
}
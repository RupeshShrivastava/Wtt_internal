package com.halodoc.omstests.orders.buru;

import com.halodoc.oms.orders.utilities.buru.BuruStockImprovementsUtil;
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
import static com.halodoc.oms.orders.utilities.constants.TimorAnalyticsConstants.ON_DEMAND_FILTER;

@Slf4j
public class BuruStockImprovements {

    private BuruStockImprovementsUtil utils;
    private Response response;

    @BeforeTest
    public void setUp() {

        utils = new BuruStockImprovementsUtil();
        response = utils.getBuruProductPerformance("valid");
        Assert.assertTrue(utils.validateStatusCode(HttpStatus.OK, response), "Put Performance Request Passed");

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

        Response invalidResponse = utils.getBuruProductPerformance("Invalid");
        Assert.assertTrue(utils.validateStatusCode(HttpStatus.OK, response), "Put Performance Request Passed");

        int performanceSize = utils.getPerformanceObject(invalidResponse).size();
        log.info("Invalid Performance Size: " + performanceSize);
        Assert.assertEquals(performanceSize, 0, "Expected Response should be empty");

    }

    @Test(description = "Verifying empty ProductID Response")
    public void verifyEmptyProductPerformance() {

        Response invalidResponse = utils.getBuruProductPerformance("empty");
        Assert.assertTrue(utils.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, invalidResponse), "Empty Products List throws error");

    }

    @Test(description = "Verifying valid performance filters visualCues")
    public void verifyValidFilterTypes() {

        //verifying On Demand Filter
        utils.verifyBuruFilterVisualCues(ON_DEMAND_FILTER);

        //verifying Low Inventory Filter
        utils.verifyBuruFilterVisualCues(LOW_INVENTORY_FILTER);

        //verifying Out Of Stock Filter
        utils.verifyBuruFilterVisualCues(OUT_OF_STOCK_FILTER);

    }

    @Test(description = "Verifying invalid performance filters")
    public void verifyInvalidFilters() {

        //Filtering with invalid filterType
        Response filterResponse = utils.getBuruPerformanceFilters(RandomStringUtils.randomAlphanumeric(5), "name", "asc");
        Assert.assertTrue(utils.validateStatusCode(HttpStatus.BAD_REQUEST,  filterResponse), "Status Codes Match");

        //Filtering with invalid sortBy
        filterResponse =  utils.getBuruPerformanceFilters(ON_DEMAND_FILTER, RandomStringUtils.randomAlphabetic(5), "asc");
        Assert.assertTrue(utils.validateStatusCode(HttpStatus.BAD_REQUEST,  filterResponse), "Status Codes Match");

        //Filtering with invalid sortType
        filterResponse =  utils.getBuruPerformanceFilters(ON_DEMAND_FILTER, "name", RandomStringUtils.randomAlphabetic(5));
        Assert.assertTrue(utils.validateStatusCode(HttpStatus.BAD_REQUEST,  filterResponse), "Status Codes Match");

    }



}

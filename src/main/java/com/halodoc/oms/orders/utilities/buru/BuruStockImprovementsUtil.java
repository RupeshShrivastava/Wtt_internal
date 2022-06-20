package com.halodoc.oms.orders.utilities.buru;

import com.halodoc.oms.orders.utilities.timor.AnalyticsUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.testng.Assert;

import java.util.HashMap;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import static com.halodoc.oms.orders.utilities.constants.SearchConstants.CONTENT_TYPE;
import static com.halodoc.oms.orders.utilities.constants.TimorAnalyticsConstants.*;

@Slf4j
public class BuruStockImprovementsUtil extends AnalyticsUtil {

    public HashMap<String, String> loginHeaders;

    public BuruStockImprovementsUtil() {
        loginHeaders = getLoginHeaders(CONTENT_TYPE, MERCHANT_LOCATION_PH_NO, PHARMACY_PORT_AUTH, PHARMACY_USER_AGENT, BURU_BASEURL);
    }

    public Response getBuruProductPerformance(String ProductType) {

        String productPerformanceJson = getJson(JSON_LOCATION_BURU, PRODUCT_PERFORMANCE_JSON_FILENAME);

        if (ProductType.equalsIgnoreCase("valid")) {
            log.info("Valid Product Type");
            productPerformanceJson = productPerformanceJson.replace("$LOW_STOCK_PRODUCT_ID", LOW_INVENTORY_STOCK_PRODUCT_ID)
                    .replace("$OUT_OF_STOCK_PRODUCT_ID", OUT_OF_STOCK_PRODUCT_ID)
                    .replace("$ON_DEMAND_PRODUCT_ID", ON_DEMAND_PRODUCT_ID)
                    .replace("$HIGH_STOCK_PRODUCT_ID", HIGH_INVENTORY_STOCK_PRODUCT_ID)
                    .replace("$NO_INVENTORY_PRODUCT_ID", NO_INVENTORY_PRODUCT_ID);
        } else if (ProductType.equalsIgnoreCase("empty")){
            productPerformanceJson = productPerformanceJson.replaceAll(",", "")
                    .replace("\"$LOW_STOCK_PRODUCT_ID\"", "")
                    .replace("\"$OUT_OF_STOCK_PRODUCT_ID\"", "")
                    .replace("\"$ON_DEMAND_PRODUCT_ID\"", "")
                    .replace("\"$HIGH_STOCK_PRODUCT_ID\"", "")
                    .replace("\"$NO_INVENTORY_PRODUCT_ID\"", "");
        }

        log.info("ProductPerformance Json: " + productPerformanceJson);
        return RestAssured.given()
                .headers(loginHeaders)
                .body(productPerformanceJson)
                .expect().log()
                .all()
                .when()
                .put(BURU_BASEURL + INVENTORIES);

    }

    public Response getBuruPerformanceFilters(String filterType, String sortBy, String sortType) {

        HashMap<String, String> params = new HashMap<>();
        params.put("per_page", "10");
        params.put("page_number", "1");
        params.put("sort_by", sortBy);
        params.put("sort_type", sortType);
        params.put("filter_type", filterType);

        return RestAssured.given()
                .headers(loginHeaders)
                .params(params)
                .expect().log()
                .all()
                .when()
                .patch(BURU_BASEURL + INVENTORIES);

    }

    public void verifyBuruFilterVisualCues(String FilterType) {

        Response filterResponse = getBuruPerformanceFilters(FilterType, "name", "asc");
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, filterResponse), "Status Codes Match");

        int resultSize = filterResponse.jsonPath().getList("result").size();
        log.info("Result Size: " + resultSize);

        for (int i = 0; i < resultSize; i++) {
            String visualCue = filterResponse.path("result[" + i + "].visual_cues").toString();
            log.info("On Demand VisualCue: " + visualCue);
            Assert.assertTrue(visualCue.contains(FilterType), FilterType + " Visual Cues Present");
        }

    }

}

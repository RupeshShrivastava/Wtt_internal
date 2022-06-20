package com.halodoc.oms.orders.utilities.timor;

import com.halodoc.oms.orders.library.BaseUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;

import static com.halodoc.oms.orders.utilities.constants.TimorAnalyticsConstants.*;

@Slf4j
public class AnalyticsUtil extends BaseUtil {

    public Response getProductPerformance(String ProductType) {

        String ProductPerformanceJson = getJson(PRODUCT_PERFORMANCE_JSON_PATH, PRODUCT_PERFORMANCE_JSON_FILENAME);

        if (ProductType.equalsIgnoreCase("valid")) {
            log.info("Valid Product Type");
            ProductPerformanceJson = ProductPerformanceJson.replace("$MERCHANT_LOCATION_ID", MERCHANT_LOCATION_ID)
                    .replace("$LOW_STOCK_PRODUCT_ID", LOW_INVENTORY_STOCK_PRODUCT_ID)
                    .replace("$OUT_OF_STOCK_PRODUCT_ID", OUT_OF_STOCK_PRODUCT_ID)
                    .replace("$ON_DEMAND_PRODUCT_ID", ON_DEMAND_PRODUCT_ID)
                    .replace("$HIGH_STOCK_PRODUCT_ID", HIGH_INVENTORY_STOCK_PRODUCT_ID)
                    .replace("$NO_INVENTORY_PRODUCT_ID", NO_INVENTORY_PRODUCT_ID);
        }

        log.info("ProductPerformance Json: " + ProductPerformanceJson);
        return RestAssured.given()
                .headers(getXappHeaders("application/json", TIMOR_ANALYTICS_TOKEN))
                .body(ProductPerformanceJson)
                .expect().log()
                .all()
                .statusCode(200)
                .when()
                .put(TIMOR_ANALYTICS_BASE_URL + INVENTORIES);

    }

    public Response getPerformanceFilters(String MerchantLocId, String filterType, String sortBy, String sortType) {

        HashMap<String, String> params = new HashMap<>();
        params.put("merchant_location_id", MerchantLocId);
        params.put("per_page", "10");
        params.put("page_number", "1");
        params.put("sort_by", sortBy);
        params.put("sort_type", sortType);
        params.put("filter_type", filterType);

        return RestAssured.given()
                .headers(getXappHeaders("application/json", TIMOR_ANALYTICS_TOKEN))
                .params(params)
                .expect().log()
                .all()
                .when()
                .patch(TIMOR_ANALYTICS_BASE_URL + INVENTORIES);

    }

    public HashMap getPerformanceObject(Response response) {

        return response.jsonPath().getJsonObject("performance");

    }

    public ArrayList getVisualCues(HashMap performance, String ProductId) {

        return ((ArrayList) ((HashMap) performance.get(ProductId)).get("visual_cues"));

    }

    public void verifyFilterVisualCues(String FilterType) {

        Response filterResponse = getPerformanceFilters(MERCHANT_LOCATION_ID, FilterType, "name", "asc");
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, filterResponse), "Status Codes Match");

        int resultSize = filterResponse.jsonPath().getList("result").size();
        log.info("Result Size: " + resultSize);

        for (int i = 0; i < resultSize; i++) {
            String visualCue = filterResponse.path("result[" + i + "].visual_cues").toString();
            log.info("On Demand VisualCue: " + visualCue);
            Assert.assertTrue(visualCue.contains(FilterType), FilterType + " Visual Cues Present");
        }

    }

    public void verifyVisualCuesFromPerformanceObject(HashMap performance, String ProductId, String FilterType) {

        Boolean bool = false;
        ArrayList visualCues = getVisualCues(performance, ProductId);
        for (int i = 0; i < visualCues.size(); i++) {
            String visualCuesData = visualCues.get(i).toString();
            log.info("VisualCuesData for " + FilterType + ": " + visualCuesData);
            if (visualCuesData.equalsIgnoreCase(FilterType)) {
                bool = true;
                break;
            }
        }
        Assert.assertTrue(bool, FilterType + " Visual Cues Present");

    }

}

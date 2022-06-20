package com.halodoc.omstests.geoZone;

import static com.halodoc.omstests.Constants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import com.halodoc.omstests.BaseOmsTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

/**
 * Created by nageshkumar
 * since  10/04/17.
 */
@Slf4j
public class GeoTest extends BaseOmsTest {

    @Test
    public void getGeoZone() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Response response = given().
                                           contentType("application/json").
                                           header("X-APP-TOKEN", X_APP_TOKEN).
                                           expect().
                                           body(containsString("id")).
                                           body(containsString("name")).
                                           body(containsString("city")).
                                           statusCode(200).
                                           when().
                                           get(base_geo_url + geo_path);
        log.info("getGeoZone response : \n"+ response.prettyPrint());

    }

    @Test
    public void getGeoZoneByName() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Response response = given().
                                           contentType("application/json").
                                           header("X-APP-TOKEN", X_APP_TOKEN).
                                           queryParam("name", "Jakarta Central").
                                           expect().
                                           body("id", hasItems(2)).
                                           body("name", hasItems("Jakarta Central")).
                                           statusCode(200).
                                           when().
                                           get(base_geo_url + geo_path);
        log.info("getGeoZoneByName response : \n"+ response.prettyPrint());

    }

    @Test
    public void getGeoZoneForParticularCity() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Response response = given().
                                           contentType("application/json").
                                           header("X-APP-TOKEN", X_APP_TOKEN).
                                           queryParam("city", "Jakarta").
                                           expect().
                                           body("id", hasItems(1)).
                                           body("city", hasItems("jakarta")).
                                           statusCode(200).
                                           when().
                                           get(base_geo_url + geo_path);
        log.info("getGeoZoneForParticularCity response  :"+ response.prettyPrint());

    }

    @Test
    public void getGeoZoneCache() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Response response = given().
                                           contentType("application/json").
                                           header("X-APP-TOKEN", X_APP_TOKEN).
                                           expect().
                                           body(containsString("id")).
                                           body(containsString("name")).
                                           body(containsString("city")).
                                           statusCode(200).
                                           when().
                                           get(base_geo_url + geo_cache_path);
        log.info("getGeoZoneCache response  :"+ response.prettyPrint());
    }

    @Test
    public void getGeoCities() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Response response = given().
                                           contentType("application/json").
                                           header("X-APP-TOKEN", X_APP_TOKEN).
                                           expect().
                                           body(containsString("jakarta")).
                                           statusCode(200).
                                           when().
                                           get(base_geo_url + geo_cities_path);
        log.info("getGeoCities response  :"+ response.prettyPrint());

    }

    @Test
    public void getGeoZoneForSingleId() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Response response = given().
                                           contentType("application/json").
                                           header("X-APP-TOKEN", X_APP_TOKEN).
                                           expect().
                                           body("id", hasItems(TEST_GEO_ZONE_ID_01)).
                                           body("city", hasItems("jakarta")).
                                           body("name",hasItems("Jakarta South")).
                                           statusCode(200).
                                           when().
                                           get(base_geo_url + geo_path + TEST_GEO_ZONE_ID_01);
        log.info("getGeoZoneForSingleId response  :"+ response.prettyPrint());

    }

    @Test
    public void getGeoZoneForMultipleIds() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Response response = given().
                                           contentType("application/json").
                                           header("X-APP-TOKEN", X_APP_TOKEN).
                                           expect().
                                           body("id", hasItems(TEST_GEO_ZONE_ID_01, TEST_GEO_ZONE_ID_02)).
                                           body("city", hasItems("jakarta", "jakarta")).
                                           body("name", hasItems("Jakarta South", "Jakarta Central")).
                                           statusCode(200).
                                           when().
                                           get(base_geo_url + geo_path + TEST_GEO_ZONE_ID_01 + "," + TEST_GEO_ZONE_ID_02);
        log.info("getGeoZoneForMultipleIds response  :"+ response.prettyPrint());
    }

    @Test
    public void getGeoZoneForInvalidId() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Response response = given().
                                           contentType("application/json").
                                           header("X-APP-TOKEN", X_APP_TOKEN).
                                           expect().
                                           body("", Matchers.hasSize(0)).
                                           statusCode(200).
                                           when().
                                           get(base_geo_url + geo_path + INVALID_STRING);
        log.info("getGeoZoneForInvalidId response  :"+ response.prettyPrint());

    }

    @Test
    public void getGeoZoneForInvalidAppToken() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Response response = given().
                                           contentType("application/json").
                                           header("X-APP-TOKEN", INVALID_STRING).
                                           expect().
                                           body("header", containsString("Not Authorised")).
                                           body("code", containsString("1103")).
                                           statusCode(401).
                                           when().
                                           get(base_geo_url + geo_path + TEST_GEO_ZONE_ID_01);
        log.info("getGeoZoneForInvalidAppToken response  :"+ response.prettyPrint());
    }

    @Test
    public void getGeoZoneForInvalidCity() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Response response = given().
                                           contentType("application/json").
                                           header("X-APP-TOKEN", X_APP_TOKEN).
                                           queryParam("city", INVALID_STRING).
                                           expect().
                                           statusCode(404).
                                           when().
                                           get(base_geo_url + geo_path);
        log.info("getGeoZoneForInvalidCity response  :"+ response.prettyPrint());

    }

    @Test
    public void getGeoZoneForValidAndInvalidIds() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Response response = given().
                                           contentType("application/json").
                                           header("X-APP-TOKEN", X_APP_TOKEN).
                                           expect().
                                           body("id", hasItems(TEST_GEO_ZONE_ID_01)).
                                           body("city", hasItems("jakarta")).
                                           body("name",hasItems("Jakarta South")).
                                           statusCode(200).
                                           when().
                                           get(base_geo_url + geo_path + TEST_GEO_ZONE_ID_01 + "," + INVALID_STRING);
        log.info("getGeoZoneForValidAndInvalidIds response  :"+ response.prettyPrint());
    }



}

package com.halodoc.subscripation.wakatobi.catalog;

import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.active;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.constant;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.external_id;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.name;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.scheme_type;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.status;
import java.util.HashMap;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.halodoc.oms.orders.utilities.pdSubscription.CatalogUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;

@Feature ("Get Scheme")
public class GetScheme {

    CatalogUtils catalogUtils = new CatalogUtils();
    String externalId = "b171525d-a173-421c-a1a0-55d3327961d0";

    @Test (priority = 1)
    @Description ("Get schemes")
    @Severity (SeverityLevel.BLOCKER)
    public void vGetSchemes() {
            Response response = catalogUtils.vGetAllSchemes();
            catalogUtils.vGetSchemeResponse(response);
    }

    @Test (priority = 2)
    @Description ("Get schemes By correct external Id")
    @Severity (SeverityLevel.BLOCKER)
    public void vGetSchemesByCorrectExternalId() {
        Response response = catalogUtils.vGetSchemeByExtId(externalId);
        catalogUtils.vGetSchemeExtIdResponse(response);
    }

    @Test (priority = 3)
    @Description ("Get schemes By incorrect external Id")
    @Severity (SeverityLevel.BLOCKER)
    public void vGetSchemesByInCorrectExternalId() {
        Response response = catalogUtils.vGetSchemeByExtId(externalId.substring(0,externalId.length()-2));
        Assert.assertTrue(response.statusCode()==HttpStatus.SC_NOT_FOUND,"Wrong external Id");
    }


}

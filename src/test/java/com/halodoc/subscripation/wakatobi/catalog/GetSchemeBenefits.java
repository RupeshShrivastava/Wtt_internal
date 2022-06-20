package com.halodoc.subscripation.wakatobi.catalog;

import java.util.HashMap;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.CatalogUtils;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.*;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;

@Feature ("Get Scheme Benefits")
public class GetSchemeBenefits {

    CatalogUtils catalogUtils = new CatalogUtils();
    String externalId = "b171525d-a173-421c-a1a0-55d3327961d0";
    String schemeName = "get-scheme-benefits";

    @BeforeTest
    public void setup(){
        externalId = catalogUtils.createScheme(schemeName);
        catalogUtils.createSchemeBenefits(externalId,item.toString());
        catalogUtils.createSchemeBenefits(externalId,item.toString());
        catalogUtils.createSchemeBenefits(externalId,delivery.toString());
    }


    @Test (priority = 1)
    @Description ("Get benefits schemes")
    @Severity (SeverityLevel.BLOCKER)
    public void vGetSchemes() {
        Response response = catalogUtils.vGetAllSchemes();
        catalogUtils.vGetSchemeResponse(response);
    }

    @Test (priority = 2)
    @Description ("Get benefits schemes By correct external Id")
    @Severity (SeverityLevel.BLOCKER)
    public void vGetSchemesByCorrectExternalId() {
        Response response = catalogUtils.vGetSchemeByExtId(externalId);
        catalogUtils.vGetSchemeExtIdResponse(response);
    }

    @Test (priority = 3)
    @Description ("Get benefits schemes By incorrect external Id")
    @Severity (SeverityLevel.BLOCKER)
    public void vGetSchemesByInCorrectExternalId() {
        Response response = catalogUtils.vGetSchemeByExtId(externalId.substring(0,externalId.length()-2));
        Assert.assertTrue(response.statusCode()== HttpStatus.SC_NOT_FOUND,"Wrong external Id");
    }
}

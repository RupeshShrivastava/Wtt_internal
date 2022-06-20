package com.halodoc.subscripation.wakatobi.catalog;

import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.name;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.scheme_type;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.status;
import java.util.HashMap;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.halodoc.oms.orders.library.BaseUtil;
import com.halodoc.oms.orders.utilities.pdSubscription.CatalogUtils;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.*;

@Feature("Create Scheme")
public class CreateScheme extends CommonUtils {

    CatalogUtils catalogUtils = new CatalogUtils();
    HashMap<String,String> data = new HashMap<>();
    String schemeName = "PD-automation-scheme";

    @Test(priority = 1)
    @Description ("create scheme with valid name ,scheme-type constant and active state")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreateSchemeWithConstantType() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),schemeName);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),active.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vCreateScheme(user.toString());
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        JSONObject jsonObject = new JSONObject(response.asString());
        catalogUtils.vCreateSchemeResponse(response,user.toString());

    }

    @Test(priority = 2)
    @Description ("create scheme with valid name ,scheme-type accured and active state")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreateSchemeWithAccuredType() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),schemeName);
        user.put(scheme_type.toString(),accured.toString());
        user.put(status.toString(),active.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vCreateScheme(user.toString());
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        catalogUtils.vCreateSchemeResponse(response,user.toString());

    }


//https://m-health.atlassian.net/browse/DOP-3333  - verifed
    @Test(priority = 3)
    @Description ("create scheme with valid name ,scheme-type constant and inactive state")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreateSchemeWithInactiveState() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),schemeName);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),inactive.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vCreateScheme(user.toString());
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_BAD_REQUEST,
                "Expected status code is "+HttpStatus.SC_BAD_REQUEST+ " found "+response.statusCode());


    }

    //https://m-health.atlassian.net/browse/DOP-3326

    @Test(priority = 4)
    @Description ("create scheme with invalid name(integer) ,scheme-type constant and active state")
    @Severity (SeverityLevel.CRITICAL)
    public void vCreateSchemeWithInvalidName() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),3);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),inactive.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vCreateScheme(user.toString());
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_BAD_REQUEST,
                "Expected status code is "+HttpStatus.SC_BAD_REQUEST+ " found "+response.statusCode());

    }

    @Test(priority = 5)
    @Description ("create scheme with valid name ,scheme-type Others and inactive state")
    @Severity (SeverityLevel.CRITICAL)
    public void vCreateSchemeWithInvalidSchemeType() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),schemeName);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),others.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vCreateScheme(user.toString());
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_BAD_REQUEST,
                "Expected status code is "+HttpStatus.SC_BAD_REQUEST+ " found "+response.statusCode());

    }

    @Test(priority = 6)
    @Description ("create scheme with unauthorized app token")
    @Severity (SeverityLevel.CRITICAL)
    public void vCreateSchemeWithUnauthorizedAppToken() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),schemeName);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),others.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vCreateSchemeHeader(user.toString(),"x-app-token");
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_UNAUTHORIZED,
                "Expected status code is "+HttpStatus.SC_UNAUTHORIZED+ " found "+response.statusCode());

    }

    @Test(priority = 7)
    @Description ("create scheme with invalid content type")
    @Severity (SeverityLevel.CRITICAL)
    public void vCreateSchemeWithInvalidTokenType() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),schemeName);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),others.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vCreateSchemeHeader(user.toString(),"Content-Type");
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_BAD_REQUEST,""
                + "Expected status code is "+HttpStatus.SC_BAD_REQUEST+ " found "+response.statusCode());

    }

    @AfterClass
    public void tearDown(){
        catalogUtils.schemeDbCleanup(schemeName);
    }

}

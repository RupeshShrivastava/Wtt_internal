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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
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

@Feature("Update Scheme")
public class UpdateScheme extends CommonUtils {

    CatalogUtils catalogUtils = new CatalogUtils();
    HashMap<String,String> data = new HashMap<>();
    String schemeName = "PD-automation-scheme",updateSchemeName = "PD-automation-scheme-update";
    String externalId = null;

    @BeforeMethod
    public void setup(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),schemeName);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),active.toString());
        Reporter.log(user.toString(),true);
        catalogUtils.schemeDbCleanup(schemeName);
        Response response = catalogUtils.vCreateScheme(user.toString());
        JSONObject jsonObject = new JSONObject(response.asString());
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        externalId = jsonObject.get(external_id.toString()).toString();
    }

    @Test(priority = 1)
    @Description ("update scheme with valid name ,scheme-type constant and active state")
    @Severity (SeverityLevel.BLOCKER)
    public void vUpdateSchemeWithConstantType() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),updateSchemeName);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),active.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vUpdateScheme(user.toString(),externalId);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        catalogUtils.vCreateSchemeResponse(response,user.toString());

    }

    @Test(priority = 2)
    @Description ("update scheme with valid name ,scheme-type accured and active state")
    @Severity (SeverityLevel.BLOCKER)
    public void vUpdateSchemeWithAccuredType() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),updateSchemeName);
        user.put(scheme_type.toString(),accured.toString());
        user.put(status.toString(),active.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vUpdateScheme(user.toString(),externalId);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"Expected status code is "+HttpStatus.SC_OK+
                " found "+response.statusCode());
        catalogUtils.vCreateSchemeResponse(response,user.toString());

    }

    @Test(priority = 3)
    @Description ("update scheme with valid name ,scheme-type constant and inactive state")
    @Severity (SeverityLevel.BLOCKER)
    public void vUpdateSchemeWithInactiveState() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),updateSchemeName);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),inactive.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vUpdateScheme(user.toString(),externalId);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"Expected status code is "+HttpStatus.SC_OK+
                " found "+response.statusCode());
        catalogUtils.vCreateSchemeResponse(response,user.toString());

    }

    //https://m-health.atlassian.net/browse/DOP-3326
    @Test(priority = 4)
    @Description ("update scheme with invalid name(integer) ,scheme-type constant and active state")
    @Severity (SeverityLevel.CRITICAL)
    public void vUpdateSchemeWithInvalidName() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),3);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),inactive.toString());
        Reporter.log(user.toString(),true);
        Response response = catalogUtils.vUpdateScheme(user.toString(),externalId);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_BAD_REQUEST,
                "Expected status code is "+HttpStatus.SC_BAD_REQUEST+ " found "+response.statusCode());

    }

    @Test(priority = 5)
    @Description ("update scheme with valid name ,scheme-type Others and inactive state")
    @Severity (SeverityLevel.CRITICAL)
    public void vUpdateSchemeWithInvalidSchemeType() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),updateSchemeName);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),others.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vUpdateScheme(user.toString(),externalId);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_BAD_REQUEST,
                "Expected status code is "+HttpStatus.SC_BAD_REQUEST+ " found "+response.statusCode());

    }

    @Test(priority = 6)
    @Description ("update scheme with unauthorized app token")
    @Severity (SeverityLevel.CRITICAL)
    public void vUpdateSchemeWithUnauthorizedAppToken() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),updateSchemeName);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),others.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vCreateSchemeHeader(user.toString(),"x-app-token");
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_UNAUTHORIZED,
                "Expected status code is "+HttpStatus.SC_UNAUTHORIZED+ " found "+response.statusCode());

    }

    @Test(priority = 7)
    @Description ("update scheme with invalid content type")
    @Severity (SeverityLevel.CRITICAL)
    public void vUpdateSchemeWithInvalidTokenType() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),updateSchemeName);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),others.toString());
        Reporter.log(user.toString(),true);

        Response response = catalogUtils.vCreateSchemeHeader(user.toString(),"Content-Type");
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_BAD_REQUEST,""
                + "Expected status code is "+HttpStatus.SC_BAD_REQUEST+ " found "+response.statusCode());

    }

    @AfterClass(alwaysRun = true)
    public void tearDown(){
        catalogUtils.schemeDbCleanup("delete from schemes where name = '"+schemeName+"'");
        catalogUtils.schemeDbCleanup("delete from scheme_benefits where external_id = '"+updateSchemeName+"'");
    }

}

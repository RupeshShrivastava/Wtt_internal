package com.halodoc.subscripation.wakatobi.catalog;

import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.*;
import java.util.HashMap;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.JsonBuilderUtil;
import com.halodoc.oms.orders.utilities.pdSubscription.CatalogUtils;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;

@Feature ("Update Scheme Benefits")
public class UpdateSchemeBenefits extends CommonUtils {

    CatalogUtils catalogUtils = new CatalogUtils();
    HashMap<String,Object> data = new HashMap<>();
    String schemeName = "PD-automation-scheme", externalId = null,benefitExid;

    @BeforeTest
    public void setup(){
        externalId = catalogUtils.createScheme(schemeName);
        benefitExid = catalogUtils.createSchemeBenefits(externalId);
    }


    @Test (priority = 1)
    @Description ("create scheme benefits with valid status ,description constant ,benefit-type item and benefit condition status active")
    @Severity (SeverityLevel.BLOCKER)
    public void vUpdateSchemeBenefitsForPdItemType() {
        data.put(status.toString(),active.toString());
        data.put(description.toString(),constant.toString());
        data.put(benefit_type.toString(),item.toString());
        data.put(benefit_conditions_status.toString(),active.toString());
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","create-scheme-benefits",data);
        Response response = catalogUtils.vUpdateSchemeBenefits(request,externalId,benefitExid);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        benefitExid = catalogUtils.vSchemeBenefitsResponse(response,request);

    }


    // need to work on others ,after bug fix https://m-health.atlassian.net/browse/DOP-3340


    @Test (priority = 2)
    @Description ("create scheme benefits with valid status ,description constant ,benefit-type delivery and benefit condition status active")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreateSchemeBenefitsForPdDeliveryType() {
        data.put(status.toString(),active.toString());
        data.put(description.toString(),constant.toString());
        data.put(benefit_type.toString(),delivery.toString());
        data.put(benefit_conditions_status.toString(),active.toString());
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","create-scheme-benefits",data);
        Response response = catalogUtils.vCreateSchemeBenefits(request,externalId);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        benefitExid = catalogUtils.vSchemeBenefitsResponse(response,request);

    }

    @Test (priority = 3)
    @Description ("create scheme benefits with valid status ,description constant ,benefit-type item and benefit condition status inactive")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreateSchemeBenefitsForPdItemTypeInactive() {
        data.put(status.toString(),inactive.toString());
        data.put(description.toString(),constant.toString());
        data.put(benefit_type.toString(),item.toString());
        data.put(benefit_conditions_status.toString(),inactive.toString());
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","create-scheme-benefits",data);
        Response response = catalogUtils.vCreateSchemeBenefits(request,externalId);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        benefitExid = catalogUtils.vSchemeBenefitsResponse(response,request);

    }

    @Test (priority = 4)
    @Description ("create scheme benefits with valid status ,description constant ,benefit-type delivery and benefit condition status active")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreateSchemeBenefitsForPdDeliveryTypeInactive() {
        data.put(status.toString(),inactive.toString());
        data.put(description.toString(),constant.toString());
        data.put(benefit_type.toString(),delivery.toString());
        data.put(benefit_conditions_status.toString(),inactive.toString());
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","create-scheme-benefits",data);
        Response response = catalogUtils.vCreateSchemeBenefits(request,externalId);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        benefitExid = catalogUtils.vSchemeBenefitsResponse(response,request);

    }


    @Test (priority = 5)
    @Description ("create scheme benefits with description accrued ,benefit-type item and benefit condition status active")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreateSchemeBenefitsForPdItemTypeDescAccrued() {
        data.put(value_type.toString(),percentage.toString());
        data.put(status.toString(),active.toString());
        data.put(description.toString(),accured.toString());
        data.put(benefit_type.toString(),item.toString());
        data.put(benefit_conditions_status.toString(),active.toString());
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","create-scheme-benefits",data);
        Response response = catalogUtils.vCreateSchemeBenefits(request,externalId);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        benefitExid = catalogUtils.vSchemeBenefitsResponse(response,request);

    }

    @Test (priority = 6)
    @Description ("create scheme benefits with valid status ,description accrued ,benefit-type delivery and benefit condition status active")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreateSchemeBenefitsForPdDeliveryTypeDescAccrued() {
        data.put(status.toString(),active.toString());
        data.put(description.toString(),accured.toString());
        data.put(benefit_type.toString(),delivery.toString());
        data.put(benefit_conditions_status.toString(),active.toString());
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","create-scheme-benefits",data);
        Response response = catalogUtils.vCreateSchemeBenefits(request,externalId);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        benefitExid = catalogUtils.vSchemeBenefitsResponse(response,request);

    }


    @Test(priority = 7)
    @Description ("update scheme benefit with unauthorized app token")
    @Severity (SeverityLevel.CRITICAL)
    public void vCreateBenefitSchemeWithUnauthorizedAppToken() {
        data.put(status.toString(),active.toString());
        data.put(description.toString(),constant.toString());
        data.put(benefit_type.toString(),delivery.toString());
        data.put(benefit_conditions_status.toString(),active.toString());
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","create-scheme-benefits",data);
        Response response = catalogUtils.vSchemeBenefitsHeader(request,externalId,"x-app-token");
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_UNAUTHORIZED,
                "Expected status code is "+HttpStatus.SC_UNAUTHORIZED+ " found "+response.statusCode());

    }

    @Test(priority = 8)
    @Description ("update scheme benefit with unauthorized app token")
    @Severity (SeverityLevel.CRITICAL)
    public void vCreateBenefitSchemeWithInvalidTokenType() {
        data.put(status.toString(),active.toString());
        data.put(description.toString(),constant.toString());
        data.put(benefit_type.toString(),delivery.toString());
        data.put(benefit_conditions_status.toString(),active.toString());
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","create-scheme-benefits",data);
        Response response = catalogUtils.vSchemeBenefitsHeader(request,externalId,"Content-Type");
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_BAD_REQUEST,""
                + "Expected status code is "+HttpStatus.SC_BAD_REQUEST+ " found "+response.statusCode());
    }






    @AfterClass (alwaysRun = true)
    public void tearDown(){
        catalogUtils.schemeDbCleanup("delete from schemes where name = '"+schemeName+"'");
        catalogUtils.schemeDbCleanup("delete from scheme_benefits where external_id = '"+benefitExid+"'");
    }

}

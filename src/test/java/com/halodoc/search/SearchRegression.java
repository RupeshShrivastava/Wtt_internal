package com.halodoc.search;

import com.halodoc.oms.orders.utilities.search.SearchUtil;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Feature("SEARCH REGRESSION TESTS")
public class SearchRegression   {

    public SearchUtil searchHelper;


    @BeforeClass
    public void setUp() {
        searchHelper = new SearchUtil();
    }

    //TODO:Insert discovery TC here

    @Severity(SeverityLevel.NORMAL)
    @Description("UPSERT PRODUCT INDEX WITH INVALID ENTITY ID")
    @Test
    public void upsertProductInIndexInvaldEntityID() {
        Response upsertProductResponse = searchHelper.upsertProductInvalidBody("3611c7da","12121");
        Assert.assertFalse(searchHelper.validateStatusCode(HttpStatus.NO_CONTENT, upsertProductResponse), "Upsert product failed");
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPSERT PRODUCT INDEX WITH INVALID PRODUCT NAME")
    @Test
    public void upsertProductInIndexInvaldProductName() {
        Response upsertProductResponse = searchHelper.upsertProductInvalidBody("PRENAMIA","GAURAV");
        Assert.assertTrue(searchHelper.validateStatusCode(HttpStatus.NO_CONTENT, upsertProductResponse), "Upsert product failed");
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPSERT PRODUCT INDEX WITH INVALID XAPP TOKEN")
    @Test
    public void upsertProductInIndexInvaldXappToken() {
        Response upsertProductResponse = searchHelper.upsertProductInvalidHeaders();
        Assert.assertFalse(searchHelper.validateStatusCode(HttpStatus.NO_CONTENT, upsertProductResponse), "Upsert product failed");
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("UPSERT PRODUCT INDEX WITH INVALID PAYLOAD DATA")
    @Test
    public void upsertProductInIndexInvaldPayLoadData() {
        Response upsertProductResponse = searchHelper.upsertProductInvalidBody("3030227614","Nothing");
        Assert.assertFalse(searchHelper.validateStatusCode(HttpStatus.NO_CONTENT, upsertProductResponse), "Upsert product failed");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("CREATE PRODUCT INDEX")
    @Test
    public void createProductIndex() {
        Response upsertProductResponse = searchHelper.createProductIndex();
        Assert.assertTrue(upsertProductResponse.path("message").toString().contains("already exists"));
        Assert.assertFalse(searchHelper.validateStatusCode(HttpStatus.NO_CONTENT, upsertProductResponse), "Upsert product failed");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("CREATE INDEX PATCH")
    @Test
    public void createIndexPatch() {
        Response upsertProductResponse = searchHelper.patchProductIndex();
       Assert.assertTrue(upsertProductResponse.path("message").toString().contains("Error while updating settings in the index in elasticsearch"));
        Assert.assertFalse(searchHelper.validateStatusCode(HttpStatus.NO_CONTENT, upsertProductResponse), "Upsert product failed");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("REPAIR PRODUCT IN INDEX")
    @Test
    public void repairProductInIndex() {
        Response repairProductResponse = searchHelper.repairProduct();
        Assert.assertTrue(searchHelper.validateStatusCode(HttpStatus.OK, repairProductResponse), "Repair product in index failed");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("REPAIR PRODUCT IN INDEX WITH INVALID EXTERNAL ID")
    @Test
    public void repairProductInIndexInvalidExternalID() {
        Response repairProductResponse = searchHelper.repairProductModifyPayload("3611c7da-d054-","12313");
        Assert.assertTrue(searchHelper.validateStatusCode(HttpStatus.OK, repairProductResponse), "Repair product in index failed");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("SUPPORT API INDEX PRODUCTS")
    @Test
    public void supportAPIIndexProducts() {
        Response repairProductResponse = searchHelper.indexProduct();
        Assert.assertTrue(searchHelper.validateStatusCode(HttpStatus.NO_CONTENT, repairProductResponse), "Repair product in index failed");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("SUPPORT API INDEX PRODUCTS WITH INVALID PRODUCT ID")
    @Test
    public void supportAPIIndexProductsInvalidProductID() {
        Response repairProductResponse = searchHelper.indexProductWithInvalidProductID();
        Assert.assertTrue(searchHelper.validateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR, repairProductResponse), "Repair product in index failed");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VALIDATE PRODUCT IN INDEX")
    @Test
    public void validateProductInIndex() {
        Response validateProductResponse = searchHelper.validateProduct();
        Assert.assertTrue(searchHelper.validateStatusCode(HttpStatus.OK, validateProductResponse), "Validate product in index failed");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VALID PRODUCT IN INDEX WITH INVALID ENTITY ID")
    @Test
    public void validateProductInIndexWithInvalidEntityID() {
        Response validateProductResponse = searchHelper.validateProductModifyPayload("04902b16-","12222");
        Assert.assertEquals(validateProductResponse.path("message"),"Need to create");
        Assert.assertTrue(searchHelper.validateStatusCode(HttpStatus.OK, validateProductResponse), "Validate product in index failed");
    }
}


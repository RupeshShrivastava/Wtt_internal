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

/**
 * Created by nageshkumar
 * since  27/12/19.
 */

@Feature("SEARCH TESTS")
public class SearchTestCases {

    public SearchUtil searchHelper;


    @BeforeClass
    public void setUp() {
        searchHelper = new SearchUtil();
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("SEARCH PRODUCT")
    @Test
    public void searchProduct() {
        Response searchProductResponse = searchHelper.searchProduct();
        Assert.assertTrue(searchHelper.validateStatusCode(HttpStatus.OK, searchProductResponse), "Search product failed");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("UPSERT PRODUCT IN INDEX")
    @Test
    public void upsertProductInIndex() {
        Response upsertProductResponse = searchHelper.upsertProduct();
        Assert.assertTrue(searchHelper.validateStatusCode(HttpStatus.NO_CONTENT, upsertProductResponse), "Upsert product failed");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("REPAIR PRODUCT IN INDEX")
    @Test
    public void repairProductInIndex() {
        Response repairProductResponse = searchHelper.repairProduct();
        Assert.assertTrue(searchHelper.validateStatusCode(HttpStatus.OK, repairProductResponse), "Repair product in index failed");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("VALIDATE PRODUCT IN INDEX")
    @Test
    public void validateProductInIndex() {
        Response validateProductResponse = searchHelper.validateProduct();
        Assert.assertTrue(searchHelper.validateStatusCode(HttpStatus.OK, validateProductResponse), "Validate product in index failed");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("INDEX PRODUCT")
    @Test
    public void indexProduct() {
        Response indexProductResponse = searchHelper.indexProduct();
        Assert.assertTrue(searchHelper.validateStatusCode(HttpStatus.NO_CONTENT, indexProductResponse), "Index product failed");
    }


}

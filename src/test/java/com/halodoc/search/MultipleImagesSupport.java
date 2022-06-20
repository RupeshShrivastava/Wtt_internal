package com.halodoc.search;

import com.halodoc.oms.orders.utilities.search.SearchUtil;
import com.halodoc.omstests.ErrorCodesAndMessages;
import com.halodoc.omstests.ResponseValidations;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.halodoc.omstests.Constants.INVALID_SEARCH_TEXT;
import static com.halodoc.omstests.Constants.PRODUCT_SEARCH_TEXT;

/**
 * @author praveenkumardn
 * This class contains the search products and categories api tests for multiple images support changes.
 */
@Feature("MULTIPLE IMAGES SUPPORT TESTS")
public class MultipleImagesSupport {
    private SearchUtil searchHelper;
    private Map<String, String> queryParamsProduct;
    private Map<String, String> queryParamsCategory;
    private ResponseValidations responseValidations;

    public MultipleImagesSupport() {
        searchHelper = new SearchUtil();
        queryParamsProduct = new HashMap<>();
        queryParamsCategory = new HashMap<>();
        responseValidations = new ResponseValidations();
    }

    @BeforeClass
    private void beforeClass(){
        queryParamsProduct.put("per_page", "10");
        queryParamsProduct.put("page_no", "1");
        queryParamsProduct.put("statuses", "active");
        queryParamsProduct.put("display", "true");
        queryParamsProduct.put("page_adjustment", "0");
        queryParamsProduct.put("exclude_products", "");
        queryParamsProduct.put("search_text", PRODUCT_SEARCH_TEXT);
        queryParamsCategory.put("per_page", "10");
        queryParamsCategory.put("page_no", "1");
        queryParamsCategory.put("status", "active");
        queryParamsCategory.put("display", "true");
        queryParamsCategory.put("sort_order", "asc");
        queryParamsCategory.put("sort_by", "display_order");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("SEARCH PRODUCTS FOR MULTIPLE IMAGES SUPPORT")
    @Test
    public void searchProductsForMultipleImagesSupport() {
        Response searchProductResponse = searchHelper.searchProducts(queryParamsProduct);
        responseValidations.verifyMulipleImages(searchProductResponse.getBody());
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("SEARCH PRODUCTS FOR MULTIPLE IMAGE SUPPORT INVALID SEARCH TEXT")
    @Test
    public void searchProductsForMultipleImagesSupportInvalidSearchText() {
        try {
            queryParamsProduct.put("search_text", INVALID_SEARCH_TEXT);
            Response searchProductResponse = searchHelper.searchProducts(queryParamsProduct);
            ResponseBody responseBody = searchProductResponse.getBody();
            responseValidations.validateExceptionCodeAndMessage(responseBody.path("status_code").toString(), responseBody.path("message").toString(), ErrorCodesAndMessages.NOT_FOUND_GENERIC_EXCEPTION);
        }finally {
            queryParamsProduct.put("search_text", PRODUCT_SEARCH_TEXT);
        }
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("SEARCH CATEGORIES FOR MULTIPLE IMAGE SUPPORT")
    @Test
    public void searchCategoriesForMultipleImagesSupport() {
        Response searchProductResponse = searchHelper.searchCategories(queryParamsCategory);
        responseValidations.verifyMulipleImages(searchProductResponse.getBody());
    }
}

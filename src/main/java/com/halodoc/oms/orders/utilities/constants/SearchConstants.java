package com.halodoc.oms.orders.utilities.constants;

/**
 * Created by nageshkumar
 * since  27/12/19.
 */
public class SearchConstants {

    public static final String SEARCH_BASE_URL = "http://timor-search.stage-k8s.halodoc.com";
    public static final String SEARCH_X_APP = "edcb217c-3ad1-44e0-b276-4187a932371c";
    public static final String CONTENT_TYPE = "application/json";
    public static final String JSON_FILE_PATH= "jsonFiles/search";


    public static final String PRODUCTS = "/v1/products";
    public static final String CATEGORY = "/v1/category";
    public static final String PRODUCT_INDEX = "/v1/support/product-index";

    public static final String PRODUCT_REPAIR = "/v1/support/products/repair";
    public static final String PRODUCT_SUPPORT_INDEX = "/v1/support/products/index";
    public static final String PRODUCT_VALIDATE = "/v1/support/products/validate";

    public static final String VALIDATE_JSON = "validateProductInIndex.json";
    public static final String INVALID_JSON   = "validateProductInIndexInvalidData.json";
    public static final String INDEX_PRODUCT_JSON = "indexProducts.json";
    public static final String UPSERT_PRODUCT_JSON = "upsertProduct.json";
    public static final String EMPTY_PRODUCT_JSON = "empty.json";


}

package com.halodoc.oms.orders.utilities.search;


import com.halodoc.oms.orders.library.BaseUtil;
import com.halodoc.utils.http.RestClient;
import com.squareup.moshi.Json;
import io.restassured.response.Response;

import static com.halodoc.oms.orders.utilities.constants.Constants.CREATE_ORDER_JSON;
import static com.halodoc.oms.orders.utilities.constants.Constants.JSON_LOCATION_TIMOR;
import static com.halodoc.oms.orders.utilities.constants.SearchConstants.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nageshkumar
 * since  27/12/19.
 */
public class SearchUtil extends BaseUtil {

    public HashMap<String, String> getHeaders(){
        HashMap<String, String> headers = new HashMap();
        headers.put("x-app-token", SEARCH_X_APP);
        headers.put("Content-Type",CONTENT_TYPE);
        return headers;
    }

    public Response searchProduct(){
        StringBuilder searchProductUrl = new StringBuilder();
        RestClient client = new RestClient(searchProductUrl.append(SEARCH_BASE_URL).append(PRODUCTS).toString(),getHeaders());
        Response response =client.executeGet();
        return response;
    }

    public Response searchProducts(Map<String, String> queryParams){
        StringBuilder searchProductUrl = new StringBuilder();
        RestClient client = new RestClient(searchProductUrl.append(SEARCH_BASE_URL).append(PRODUCTS).toString(),getHeaders());
        return client.executeGet(queryParams) ;
    }

    public Response searchCategories(Map<String, String> queryParams){
        StringBuilder searchProductUrl = new StringBuilder();
        RestClient client = new RestClient(searchProductUrl.append(SEARCH_BASE_URL).append(CATEGORY).toString(),getHeaders());
        return client.executeGet(queryParams) ;
    }

    public Response validateProduct(){
        StringBuilder searchProductUrl = new StringBuilder();
        RestClient client = new RestClient(searchProductUrl.append(SEARCH_BASE_URL).append(PRODUCT_VALIDATE).toString(),getHeaders());
        Response response =client.executePost(getJson(JSON_FILE_PATH,VALIDATE_JSON));
        return response;
    }
    public Response validateProductModifyPayload(String oldData,String newData){
        StringBuilder searchProductUrl = new StringBuilder();
        RestClient client = new RestClient(searchProductUrl.append(SEARCH_BASE_URL).append(PRODUCT_VALIDATE).toString(),getHeaders());
        Response response =client.executePost(getJson(JSON_FILE_PATH,VALIDATE_JSON).replace(oldData,newData));
        return response;
    }

    public Response indexProduct(){
        StringBuilder indexProductUrl = new StringBuilder();
        RestClient client = new RestClient(indexProductUrl.append(SEARCH_BASE_URL).append(PRODUCT_SUPPORT_INDEX).toString(),getHeaders());
        Response response =client.executePost(getJson(JSON_FILE_PATH,INDEX_PRODUCT_JSON));
        return response;
    }
    public Response indexProductWithInvalidProductID(){
        StringBuilder indexProductUrl = new StringBuilder();
        RestClient client = new RestClient(indexProductUrl.append(SEARCH_BASE_URL).append(PRODUCT_SUPPORT_INDEX).toString(),getHeaders());
        Response response =client.executePost(getJson(JSON_FILE_PATH,INDEX_PRODUCT_JSON).replace("04902b16","productID"));
        return response;
    }

    public Response repairProduct(){

        StringBuilder repairProductUrl = new StringBuilder();
        RestClient client = new RestClient(repairProductUrl.append(SEARCH_BASE_URL).append(PRODUCT_REPAIR).toString(),getHeaders());
        Response response =client.executePost(getJson(JSON_FILE_PATH,VALIDATE_JSON));
        return response;
    }
    public Response repairProductModifyPayload(String replaceText,String replaceWith){

        StringBuilder repairProductUrl = new StringBuilder();
        RestClient client = new RestClient(repairProductUrl.append(SEARCH_BASE_URL).append(PRODUCT_REPAIR).toString(),getHeaders());
        Response response =client.executePost(getJson(JSON_FILE_PATH,VALIDATE_JSON).replace(replaceText,replaceWith));
        return response;
    }
    public Response upsertProduct(){
        StringBuilder upsertProductUrl = new StringBuilder();
        RestClient client = new RestClient(upsertProductUrl.append(SEARCH_BASE_URL).append(PRODUCTS).toString(),getHeaders());
        Response response =client.executePost(getJson(JSON_FILE_PATH,UPSERT_PRODUCT_JSON));
        return response;
    }

    public Response upsertProductInvalidBody(String oldData,String newData){
        StringBuilder upsertProductUrl = new StringBuilder();
        RestClient client = new RestClient(upsertProductUrl.append(SEARCH_BASE_URL).append(PRODUCTS).toString(),getHeaders());
        Response response =client.executePost(getJson(JSON_FILE_PATH,UPSERT_PRODUCT_JSON).replace(oldData,newData));
        return response;
    }
    public Response upsertProductInvalidHeaders(){
        HashMap<String,String> invalidHashMap=new HashMap<>();
        invalidHashMap.put("x-app-token","abcsada");
        StringBuilder upsertProductUrl = new StringBuilder();
        RestClient client = new RestClient(upsertProductUrl.append(SEARCH_BASE_URL).append(PRODUCTS).toString(),invalidHashMap);
        Response response =client.executePost(getJson(JSON_FILE_PATH,UPSERT_PRODUCT_JSON));
        return response;
    }

    public Response createProductIndex(){
        StringBuilder indexProductUrl = new StringBuilder();
        RestClient client = new RestClient(indexProductUrl.append(SEARCH_BASE_URL).append(PRODUCT_INDEX).toString(),getHeaders());
        Response response =client.executePost(getJson(JSON_FILE_PATH,EMPTY_PRODUCT_JSON));

        return response;

    }
    public Response patchProductIndex(){
        StringBuilder indexProductUrl = new StringBuilder();
        RestClient client = new RestClient(indexProductUrl.append(SEARCH_BASE_URL).append(PRODUCT_INDEX).toString(),getHeaders());
        Response response =client.executePatch();

        return response;

    }

}

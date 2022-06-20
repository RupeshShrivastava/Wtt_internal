package com.halodoc.oms.orders.utilities.pdSubscription;

import java.util.HashMap;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.*;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.*;
import static org.testng.Assert.*;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.halodoc.oms.dbUtils.DbQueryExecution;
import com.halodoc.oms.orders.utilities.JsonBuilderUtil;
import io.restassured.response.Response;

public class CatalogUtils {

    HashMap<String, String> header = new HashMap<>();
    CommonUtils commonUtils = new CommonUtils();
    DbQueryExecution dbQueryExecution = new DbQueryExecution();


    public String createScheme(String schemeName){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode user = mapper.createObjectNode();
        user.put(name.toString(),schemeName);
        user.put(scheme_type.toString(),constant.toString());
        user.put(status.toString(),active.toString());
        Reporter.log(user.toString(),true);
        schemeDbCleanup(schemeName);
        Response response = vCreateScheme(user.toString());
        JSONObject jsonObject = new JSONObject(response.asString());
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        return jsonObject.get(external_id.toString()).toString();
    }

    public String createSchemeBenefits(String externalId){
        HashMap<String,Object> data = new HashMap<>();
        data.put(status.toString(),active.toString());
        data.put(description.toString(),constant.toString());
        data.put(benefit_type.toString(),item.toString());
        data.put(benefit_conditions_status.toString(),active.toString());
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","create-scheme-benefits",data);
        Response response = vCreateSchemeBenefits(request,externalId);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        return vSchemeBenefitsResponse(response,request);
    }

    public String createSchemeBenefits(String schemeExtId,String benefitType){
        HashMap<String,Object> data = new HashMap<>();
        data.put(status.toString(),active.toString());
        data.put(description.toString(),constant.toString());
        data.put(benefit_type.toString(),benefitType);
        data.put(benefit_conditions_status.toString(),active.toString());

        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","create-scheme-benefits",data);
        Response response = vCreateSchemeBenefits(request,schemeExtId);
        Reporter.log(response.prettyPrint());
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        return vSchemeBenefitsResponse(response,request);
    }

    public Response vCreateScheme(String input){
        header.put("Content-Type","application/json");
        header.put("x-app-token",X_APP_TOKEN_WAKATOBI_CATALOG);
        return commonUtils.postApiResponse(input,WAKATOBI_CATALOG_BASE_URL+ SCHEME,header);
    }

    public void vCreateSchemeResponse(Response response,String inp){
        JSONObject jsonObject = new JSONObject(response.asString());
        assertTrue(inp.contains(jsonObject.get(name.toString()).toString()),"name "+jsonObject.get(name.toString()).toString()+
                " doesn't match with request body "+inp);
        assertTrue(inp.contains(jsonObject.get(scheme_type.toString()).toString()),"scheme_type "+jsonObject.get(scheme_type.toString()).toString()+
                " doesn't match with request body "+inp);
        assertTrue(inp.contains(jsonObject.get(status.toString()).toString()),"status "+jsonObject.get(status.toString()).toString()+
                " doesn't match with request body "+inp);
        assertTrue(jsonObject.get(external_id.toString()).toString().length()>30,"respone external_id is not correct "+
                jsonObject.get(external_id.toString()).toString());

    }

    public void schemeDbCleanup(String query){
        dbQueryExecution.updateStageDbData(query);

    }

    public Response vCreateSchemeHeader(String input,String headerType){
        if(headerType.equals("x-app-token")) {
            header.put("x-app-token", X_APP_TOKEN_WAKATOBI_CATALOG + "1");
            header.put("Content-Type","application/json");
        }else if(headerType.equals("Content-Type")) {
            header.put("x-app-token", X_APP_TOKEN_WAKATOBI_CATALOG);
        }
        return commonUtils.postApiResponse(input,WAKATOBI_CATALOG_BASE_URL+ SCHEME,header);

    }
    public Response vUpdateScheme(String input,String external_id){
        header.put("Content-Type","application/json");
        header.put("x-app-token",X_APP_TOKEN_WAKATOBI_CATALOG);
        return commonUtils.putApiResponse(input,WAKATOBI_CATALOG_BASE_URL+ SCHEME +"/"+external_id,header);
    }

    public Response vGetSchemeByExtId(String external_id){
        header.put("x-app-token",X_APP_TOKEN_WAKATOBI_CATALOG);
        return commonUtils.getApiResponseNoQueryParams(WAKATOBI_CATALOG_BASE_URL+ SCHEME +"/"+external_id,header);
    }

    public Response vGetAllSchemes(){
        header.put("x-app-token",X_APP_TOKEN_WAKATOBI_CATALOG);
        return commonUtils.getApiResponseNoQueryParams(WAKATOBI_CATALOG_BASE_URL+ SCHEME ,header);
    }

    public void vGetSchemeResponse(Response response){
        JSONObject jsonObject = new JSONObject(response.asString());
        for(int i=0;i<jsonObject.getJSONArray("result").length();i++) {
            JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONArray("result").getJSONObject(i));
            assertTrue(jsonObject1.get(name.toString()).toString().length()>0,
                    "name " + jsonObject.get(name.toString()).toString() + "length isnot greater than 0 ");
            assertTrue(jsonObject1.get(scheme_type.toString()).toString().contains(constant.toString()) ||
                            jsonObject1.get(scheme_type.toString()).toString().contains(accured.toString()),
                    "scheme_type " + jsonObject1.get(scheme_type.toString()).toString() + " doesn't match with constant/accord");
            assertTrue(jsonObject1.get(status.toString()).toString().contains(active.toString()) ||
                            jsonObject1.get(status.toString()).toString().contains(inactive.toString()),
                    "status " + jsonObject1.get(status.toString()).toString() + " doesn't match with active/inactive ");
            assertTrue(jsonObject1.get(external_id.toString()).toString().length() > 30,
                    "respone external_id is not correct " + jsonObject1.get(external_id.toString()).toString());
        }

    }

    public Response vCreateSchemeBenefits(String input,String schemeExtId){
        header.put("Content-Type","application/json");
        header.put("x-app-token",X_APP_TOKEN_WAKATOBI_CATALOG);
        return commonUtils.postApiResponse(input,WAKATOBI_CATALOG_BASE_URL+ SCHEME+"/"+schemeExtId+"/"+BENEFIT,header);
    }

    public Response vUpdateSchemeBenefits(String input,String schemeExtId,String benefitExtId){
        header.put("Content-Type","application/json");
        header.put("x-app-token",X_APP_TOKEN_WAKATOBI_CATALOG);
        return commonUtils.putApiResponse(input,WAKATOBI_CATALOG_BASE_URL+ SCHEME+"/"+schemeExtId+"/"+BENEFIT+"/"+benefitExtId,header);
    }

    public String vSchemeBenefitsResponse(Response response,String inp){
        JSONObject jsonObject = new JSONObject(response.asString());
        assertTrue(inp.contains(jsonObject.get(value_type.toString()).toString()),"value_type "+jsonObject.get(value_type.toString()).toString()+
                " doesn't match with request body "+inp);
        assertTrue(inp.contains(jsonObject.get(status.toString()).toString()),"status "+jsonObject.get(status.toString()).toString()+
                " doesn't match with request body "+inp);
        assertTrue(inp.contains(jsonObject.get(benefit_type.toString()).toString()),"benefit_type "+jsonObject.get(benefit_type.toString()).toString()+
                " doesn't match with request body "+inp);
        assertTrue(jsonObject.get(external_id.toString()).toString().length()>30,"respone external_id is incorrect "+
                jsonObject.get(external_id.toString()).toString());
        assertTrue(inp.contains(jsonObject.get(description.toString()).toString()),"description "+jsonObject.get(description.toString()).toString()+
                " doesn't match with request body "+inp);

        return jsonObject.get(external_id.toString()).toString();
    }

    public Response vSchemeBenefitsHeader(String input,String schemeExtId,String headerType){
        if(headerType.equals("x-app-token")) {
            header.put("x-app-token", X_APP_TOKEN_WAKATOBI_CATALOG + "1");
            header.put("Content-Type","application/json");
        }else if(headerType.equals("Content-Type")) {
            header.put("x-app-token", X_APP_TOKEN_WAKATOBI_CATALOG);
        }
        return commonUtils.postApiResponse(input,WAKATOBI_CATALOG_BASE_URL+ SCHEME+"/"+schemeExtId+"/"+BENEFIT,header);

    }

    public void vGetSchemeExtIdResponse(Response response){
            JSONObject jsonObject = new JSONObject(response.asString());

            assertTrue(jsonObject.length()==1,
                    "Result length is grater than one");
            assertTrue(jsonObject.get(name.toString()).toString().length()>0,
                    "name " + jsonObject.get(name.toString()).toString() + "length isnot greater than 0 ");
            assertTrue(jsonObject.get(scheme_type.toString()).toString().contains(constant.toString()) ||
                            jsonObject.get(scheme_type.toString()).toString().contains(accured.toString()),
                    "scheme_type " + jsonObject.get(scheme_type.toString()).toString() + " doesn't match with constant/accord");
            assertTrue(jsonObject.get(status.toString()).toString().contains(active.toString()) ||
                            jsonObject.get(status.toString()).toString().contains(inactive.toString()),
                    "status " + jsonObject.get(status.toString()).toString() + " doesn't match with active/inactive ");
            assertTrue(jsonObject.get(external_id.toString()).toString().length() > 30,
                    "respone external_id is not correct " + jsonObject.get(external_id.toString()).toString());
        }

    public Response vGetSchemeBenefit(String Scheme_External_id){
        header.put("x-app-token",X_APP_TOKEN_WAKATOBI_CATALOG);
        return commonUtils.getApiResponseNoQueryParams(WAKATOBI_CATALOG_BASE_URL+ SCHEME +"/"+Scheme_External_id+"/"+BENEFIT,header);
    }

    public void vGetSchemeBenefitResponse(Response response){
        JSONObject jsonObject = new JSONObject(response.asString());
        for(int i=0;i<jsonObject.getJSONArray("result").length();i++) {
            JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONArray("result").getJSONObject(i));
            assertTrue(jsonObject1.get(name.toString()).toString().length()>0,
                    "name " + jsonObject.get(name.toString()).toString() + "length isnot greater than 0 ");
            assertTrue(jsonObject1.get(scheme_type.toString()).toString().contains(constant.toString()) ||
                            jsonObject1.get(scheme_type.toString()).toString().contains(accured.toString()),
                    "scheme_type " + jsonObject1.get(scheme_type.toString()).toString() + " doesn't match with constant/accord");
            assertTrue(jsonObject1.get(status.toString()).toString().contains(active.toString()) ||
                            jsonObject1.get(status.toString()).toString().contains(inactive.toString()),
                    "status " + jsonObject1.get(status.toString()).toString() + " doesn't match with active/inactive ");
            assertTrue(jsonObject1.get(external_id.toString()).toString().length() > 30,
                    "respone external_id is not correct " + jsonObject1.get(external_id.toString()).toString());
        }

    }

    public Response createPackage(String product_id,HashMap<String,Object> postBody) {
        header.put("Content-Type","application/json");
        header.put("x-app-token",X_APP_TOKEN_WAKATOBI_CATALOG);
        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","create-package",postBody);
        return commonUtils.postApiResponse(request,WAKATOBI_CATALOG_BASE_URL+ INTERNAL_PRODUCT_PATH+"/"+product_id+"/"+PACKAGE,header);
    }


   public void vCreatePackageResponse(Response response,HashMap<String,Object> postBody){
       JSONObject jsonObject = new JSONObject(response.asString());
       for(int i=0;i<jsonObject.getJSONArray("attibutes").length();i++) {

       }

   }
}

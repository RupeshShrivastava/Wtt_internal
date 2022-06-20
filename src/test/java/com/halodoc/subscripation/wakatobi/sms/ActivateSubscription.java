package com.halodoc.subscripation.wakatobi.sms;

import java.util.HashMap;
import java.util.UUID;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.JsonBuilderUtil;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;

public class ActivateSubscription {

    @DataProvider (name = "wakatobi-cancel-product")
    public static Object[][] inactiveProduct() {
        return new Object[][] { {"X4XE6Q-2418","8ee13f5d-3530-47db-96f2-0defd20e25f3"},
                {"X4XE6Q-2467","173ef302-f04f-48ba-a414-7191862700c5"},
                {"X4XE6Q-4760","b24204d0-18d5-46ed-87fd-3a3d53401d7a"},
                {"X4XE6Q-2191","ccde8be2-fc8f-4f49-9cd8-09884432ef5e"},
                {"X4XE6Q-1351","88f4327f-900c-4205-90e3-4cada043d913"},
                {"X4XE6Q-2847","c7a29866-1fc7-4c03-be20-f52654f9bebf"},
                {"X4XE6Q-3877","d300f296-cd89-468c-8b46-8b40ec1e4f8f"},
                {"X4XE6Q-5606","1e090525-e761-417f-8382-6d1bfff5363f"},
                {"X4XE6Q-3270","d13882c2-8265-45fe-8353-827b4b096fa3"},
                {"X4XE6Q-4999","dc578405-b3f5-43b3-8c03-ebcef258452d"},
                {"X4XE6Q-7623","95d60fa4-f912-4be0-928b-be74e9660303"},
                {"X4XE6Q-9726","3b5e9914-c5fb-45df-9a12-f44ddeba225a"},
                {"X4XE6Q-2203","0a771238-ff77-49ec-baaf-dae8dfff0503"},
                {"X4XE6Q-9867","bb691ccf-a507-4a57-ab95-19866e23d5c9"},
                {"X4XE6Q-6881","c9c7a18d-bced-4a35-b010-97722c1131eb"},
                {"X4XE6Q-4919","93764fd0-2ff5-4473-b501-b767618aa4a2"},
                {"X4XE6Q-0995","c34212e2-75fe-42fc-a0b0-0f443a54ac78"},
                {"X4XE6Q-5017","49bc16cc-3716-4728-abfd-63b06e430864"},
                {"X4XE6Q-4784","1b6d18a9-7d2c-421b-a6f3-29b27111495c"},
                {"X4XE6Q-6513","0901dc5c-227f-4e3f-b209-98062ceea56e"},
                {"X4XE6Q-8242","8dc0fc7f-9f7f-4e46-9121-91b51922941a"},
                {"X4XE6Q-1700","b28ecbaa-7e7d-4dca-878d-e4f3d4b99680"},
                {"X4XE6Q-8616","b28ecbaa-7e7d-4dca-878d-e4f3d4b99680"},
                {"X4XE6Q-8990","13ec1ae4-037f-4f70-ad5d-7d2be6040a56"},
                {"X4XE6Q-4177","AA20027"},
                {"X4XE6Q-9738","63e1e9d6-169b-4bea-9321-2ddac5f242e4"},
                {"X4XE6Q-6654","63e1e9d6-169b-4bea-9321-2ddac5f242e4"},
                {"X4XE6Q-0112","cd884989-fb60-4df5-a598-d3d6525961e4"},
                {"X4XE6Q-1841","3383cd56-4d2a-4bb1-8914-628b6a871e58"},
                {"X4XE6Q-3570","43f0cfae-6ad9-494e-96ef-0c823e084398"},
                {"X4XE6Q-7028","b35e754f-4a2c-4032-a366-d3f84da20462"},
                {"X4XE6Q-8757","733c0da6-99c3-447b-8727-39498d3a845a"},
                {"X4XE6Q-6421","7709c09c-335e-4270-a572-39f2221935cf"},
                {"X4XE6Q-8150","4fbf00b7-f194-4ff9-a9ee-da5fda2323c0"},
                {"X4XE6Q-9879","539832d7-d7e9-44bf-b942-33d0a961fb27"},
                {"X4XE6Q-0253","eef137b3-3290-40ba-990a-53d07c33b9a6"},
                {"X4XE6Q-7917","f8615ce4-2fc0-43ae-911a-14ed7153a4e7"},
                {"X4XE6Q-4833","a52c6857-4aab-4dd0-8dd9-3e594570c167"},
                {"X4XE6Q-0020","fedbb9c6-420f-46fc-9167-b73181b9bca0"},
                {"X4XE6Q-4600","899be81b-e183-45dd-9622-473ffb6b3d89"},
                {"X4XE6Q-4974","61b5f522-8b13-49b2-bea0-f01a3d6e7417"},
                {"X4XE6Q-0161","308f7fea-ab75-4130-af2b-78eea721b38a"},
                {"X4XE6Q-7077","02cb4ff5-05fd-4b27-93f1-71406a576a79"},
                {"X4XE6Q-5722","20830ce7-f320-4fe8-b7a5-fb86b4ca30cb"},
                {"X4XE6Q-9928","240d8630-d7c8-4565-9027-fe55d18afc2b"},
                {"X4XE6Q-1657","426c10e8-71eb-4c37-99c4-15a0f067afc7"},
                {"X4XE6Q-3386","c08e0eb4-c3a9-4243-8465-6314cd02422f"},
                {"X4XE6Q-7218","ea58300b-f47d-429b-83fa-0c812683f99d"},
                {"X4XE6Q-2405","6dbacd71-f526-4329-b7ce-300ef2545106"}
        };
    }

    @Test (dataProvider= "wakatobi-cancel-product")
    @Description ("activate and cancel subscription with valid data-2")
    @Severity (SeverityLevel.BLOCKER)
    public void vActivateAndCancelSubscription(String orderId,String userId) throws InterruptedException {

        HashMap<String,String> headers = new HashMap<>();
        headers.put("x-app-token","c29f9b1c-d8a5-416e-9f7c-32d0ce0843db");
        headers.put("Content-Type","application/json");
        String url = "http://wakatobi-sms.prod-k8s.halodoc.com:8560/v1/subscriptions/"+orderId+"/activate";

/*
        HashMap<String, Object> data=new HashMap<>();
        data.put("userId", userId);

        String request = JsonBuilderUtil.generateDataFromMap("pd-subscription","wakatobi-sms",data);
*/

        String request = "{\"patient_id\" : \""+userId +"\" ,\"attributes\": {},\"payments\" : [] }";

        Response response = CommonUtils.putApiResponse(request,url,headers);
        Thread.sleep(1000);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"getting error \n"+response.asString());

        String url1 = "http://wakatobi-sms.prod-k8s.halodoc.com:8560/v1/subscriptions/"+orderId+"/cancel";

        Response response1 = CommonUtils.putApiResponse("",url1,headers);
        Thread.sleep(1000);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"getting error \n"+response.asString());


    }

    @DataProvider (name = "wakatobi-cancel-product1")
    public static Object[][] cancelSubscription() {
        return new Object[][] {
                {"X4XE6Q-4760"},
                {"X4XE6Q-2191"},
                {"X4XE6Q-1351"},
                {"X4XE6Q-2847"},
                {"X4XE6Q-3877"},
                {"X4XE6Q-5606"},
                {"X4XE6Q-3270"},
                {"X4XE6Q-4999"},
                {"X4XE6Q-7623"},
                {"X4XE6Q-9726"},
                {"X4XE6Q-2203"},
                {"X4XE6Q-9867"},
                {"X4XE6Q-6881"},
                {"X4XE6Q-2816"},
                {"X4XE6Q-4919"},
                {"X4XE6Q-0995"},
                {"X4XE6Q-5017"},
                {"X4XE6Q-4784"},
                {"X4XE6Q-6513"},
                {"X4XE6Q-8242"},
                {"X4XE6Q-1700"},
                {"X4XE6Q-8616"},
                {"X4XE6Q-8990"},
                {"X4XE6Q-4177"},
                {"X4XE6Q-9738"},
                {"X4XE6Q-6654"},
                {"X4XE6Q-0112"},
                {"X4XE6Q-1841"},
                {"X4XE6Q-3570"},
                {"X4XE6Q-7028"},
                {"X4XE6Q-8757"},
                {"X4XE6Q-6421"},
                {"X4XE6Q-8150"},
                {"X4XE6Q-9879"},
                {"X4XE6Q-0253"},
                {"X4XE6Q-7917"},
                {"X4XE6Q-4833"},
                {"X4XE6Q-0020"},
                {"X4XE6Q-4600"},
                {"X4XE6Q-4974"},
                {"X4XE6Q-0161"},
                {"X4XE6Q-7077"},
                {"X4XE6Q-5722"},
                {"X4XE6Q-9928"},
                {"X4XE6Q-1657"},
                {"X4XE6Q-3386"},
                {"X4XE6Q-7218"},
                {"X4XE6Q-2405"} };
    }

    @Test (dataProvider= "wakatobi-cancel-product1")
    @Description ("create package with valid data-2")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreatePackageValidData2(String orderId) throws InterruptedException {

        HashMap<String,String> headers = new HashMap<>();
        headers.put("x-app-token","c29f9b1c-d8a5-416e-9f7c-32d0ce0843db");
        headers.put("Content-Type","application/json");

        String url1 = "http://wakatobi-sms.prod-k8s.halodoc.com:8560/v1/subscriptions/"+orderId+"/cancel";
        String body = "{\"cancellation_type\": \"system_cancelled\",\"reason\": \"\"" + "}";

        Response response = CommonUtils.putApiResponse(body,url1,headers);
        Thread.sleep(1000);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"getting error \n"+response.asString());



    }

}

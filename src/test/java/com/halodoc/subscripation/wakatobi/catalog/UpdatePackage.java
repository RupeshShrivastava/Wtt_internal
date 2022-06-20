package com.halodoc.subscripation.wakatobi.catalog;

import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.active;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.name;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.status;
import java.util.HashMap;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.CommonUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;

public class UpdatePackage {





    @DataProvider (name = "wakatobi-inacitve product")
    public static Object[][] inactiveProduct() {
        return new Object[][] { { "9827571d-3c1b-4674-a1a9-22d63c2a81a8","5d2c918e-19ae-4cad-9af0-8ea1ec28ac2c"},
                {"7a0b3c69-6f52-4cf6-9446-e10aa0671b61","e11b88b1-7169-44b7-b891-c3f85a74b652"},
                {"39a3ead1-301b-4d1d-b9cc-502b4e8ba754","afa6536c-1c92-4dad-98c0-cdc85a9c6df2"},
                {"602d4479-4270-4a61-8083-0035dcfd06cd","c71d5473-36df-42de-becc-08078c9ff897"},
                {"2180f52e-1ef3-4b11-8511-049615ab3fd8","d2fc2d9c-3805-4e52-8188-ac880aab5af0"},
                {"e1105ed2-7a4e-4527-aae9-aed1fde4f7c5","39d1c349-e65c-47bf-8428-67e3c3d7e9ad"},
                {"9f047e6a-b081-4a71-890e-e89f3ec0c2f2","4c50aea3-217f-4909-b2fe-63513456391a"},
                {"d52891c1-563e-4d24-abc9-ab02d6bb443a","d29ad3bd-ea95-4722-a369-e689340c9a5d"},
                {"512f1867-d1d5-4a85-b894-0d54cd0a465b","215c90d4-99f9-482f-9135-322d96ce6cf4,"},
                {"01adfd51-1b7a-4cb8-90f1-d1201f69e3a8","cba927e3-dc80-4df3-988e-077384b39863"},
                {"b0c763b6-601b-4780-9880-d91a20dc9768","2b6b989b-e63c-4bf0-8a7a-6131e09fbc49"},
                {"f3d0662f-1c6c-4e49-8569-85515cfc9cea","16668272-514c-4cfb-895a-c6c424f42c5a"}};
    }

    @Test (dataProvider= "wakatobi-inacitve product")
    @Description ("create package with valid data-2")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreatePackageValidData2(String productId,String packageId) throws InterruptedException {

        HashMap<String,String> headers = new HashMap<>();
        headers.put("x-app-token","8d0ccbf8-d2ca-4e8e-8754-24a81f12c30a");
        headers.put("Content-Type","application/json");
        String url = "http://wakatobi-catalog.prod-k8s.halodoc.com:8558/v1/internal/product/"+productId+"/package/"+packageId;
        String payload = "{\n" + "    \"status\": \"inactive\"\n" + "}\n";
        Response response = CommonUtils.putApiResponse(payload,url,headers);
        Thread.sleep(1000);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_OK,"getting error \n"+response.asString());



    }
}

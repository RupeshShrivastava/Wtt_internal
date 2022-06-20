package com.halodoc.subscripation.wakatobi.catalog;

import java.util.HashMap;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.pdSubscription.CatalogUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import static com.halodoc.oms.orders.utilities.pdSubscription.Constants.wakatobi.*;

@Feature("CREATE PACKAGE")
public class CreatePackage {

    CatalogUtils catalogUtils = new CatalogUtils();
    HashMap<String,Object> data = new HashMap<>();

    @Test(priority = 1)
    @Description ("create package with valid data-1")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreatePackageValidData1() {
        String product_id = "ba71d093-96a5-4001-a1c9-cf3e5dbf71dc";
        String packageName = "backend-automation-package";
        data.put(status.toString(),active.toString());
        data.put(name.toString(),packageName);
        data.put("subscription_price_value","20000");
        data.put("subscription_fulfilment_provider_value","");
        data.put("scheme_id_value","940ac646-7a4d-4523-b0c6-1de119802bdf");
        Response response = catalogUtils.createPackage(product_id,data);
        catalogUtils.vCreatePackageResponse(response,data);


    }

    //need to change test data for that
    @Test(priority = 2)
    @Description ("create package with valid data-2")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreatePackageValidData2() {
        String product_id = "ba71d093-96a5-4001-a1c9-cf3e5dbf71dc";
        String packageName = "backend-automation-package";
        data.put(status.toString(),active.toString());
        data.put(name.toString(),packageName);
        data.put("subscription_price_value","20000");
        data.put("subscription_fulfilment_provider_value","");
        data.put("scheme_id_value","940ac646-7a4d-4523-b0c6-1de119802bdf");
        Response response = catalogUtils.createPackage(product_id,data);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_CREATED,"Expected status code is "+HttpStatus.SC_CREATED+
                " found "+response.statusCode());
        catalogUtils.vCreatePackageResponse(response,data);

    }

    //need to change test data for that
    @Test(priority = 3)
    @Description ("create package with inactive state")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreatePackageWithInactiveState() {
        String product_id = "ba71d093-96a5-4001-a1c9-cf3e5dbf71dc";
        String packageName = "backend-automation-package";
        data.put(status.toString(),inactive.toString());
        data.put(name.toString(),packageName);
        data.put("subscription_price_value","20000");
        data.put("subscription_fulfilment_provider_value","");
        data.put("scheme_id_value","940ac646-7a4d-4523-b0c6-1de119802bdf");
        Response response = catalogUtils.createPackage(product_id,data);
        Assert.assertTrue(response.statusCode() == HttpStatus.SC_BAD_REQUEST,
                "Expected status code is "+HttpStatus.SC_BAD_REQUEST+ " found "+response.statusCode());


    }

    //need to change test data for that
    @Test (priority = 4)
    @Description ("create package with blank subcription price and scheme id")
    @Severity (SeverityLevel.BLOCKER)
    public void vCreatePackageWithBlankSubscriptionAndSchemeId() {
        String product_id = "ba71d093-96a5-4001-a1c9-cf3e5dbf71dc";
        String packageName = "backend-automation-package";
        data.put(status.toString(),inactive.toString());
        data.put(name.toString(),packageName);
        data.put("subscription_price_value","");
        data.put("subscription_fulfilment_provider_value","");
        data.put("scheme_id_value","");
        Response response = catalogUtils.createPackage(product_id,data);
        catalogUtils.vCreatePackageResponse(response,data);

    }
}

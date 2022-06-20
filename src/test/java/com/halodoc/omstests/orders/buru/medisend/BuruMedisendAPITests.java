package com.halodoc.omstests.orders.buru.medisend;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import org.junit.Assert;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.buru.BuruUtil;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BuruMedisendAPITests extends BuruUtil {

    @Test(groups = { "sanity", "regression" }, priority = 1)
    public void getDistributorSlugAPI(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = getDistributorSlug(DISTRIBUTOR_SLUG);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, NAME_PATH, DISTRIBUTOR_NAME));
    }

    @Test(groups = { "sanity", "regression" }, priority = 2)
    public void getInvalidDistributorSlugAPI(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = getDistributorSlug(merchantId);
        Assert.assertTrue(validateStatusCode(HttpStatus.NOT_FOUND, response));
    }
}

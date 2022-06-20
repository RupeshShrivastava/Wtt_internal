package com.halodoc.omstests.orders.fulfillment;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.fulfillment.FulfillmentUtil;
import com.halodoc.oms.orders.utilities.komodo.KomodoUtil;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReallocationOrderTests  {

    FulfillmentUtil fulfillmentHelper = new FulfillmentUtil();
    KomodoUtil komodoHelper = new KomodoUtil();
    @Test
    public void verifyCheckReallocationCustomerCancel() throws InterruptedException, ParseException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = fulfillmentHelper.checkReallocation(USER_ENTITY_ID, TYPE_CANCELLED_PHARMACY, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test
    public void verifyCheckReallocationCustomerCancelReallocatedTrue() throws InterruptedException, ParseException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = fulfillmentHelper.checkReallocation(USER_ENTITY_ID, TYPE_CANCELLED_PHARMACY, true, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test
    public void verifyCheckReallocationMerchantCancel() throws InterruptedException, ParseException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = fulfillmentHelper.checkReallocation(USER_ENTITY_ID, TYPE_MERCHANT_CANCELLED, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.NO_CONTENT, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test
    public void verifyCheckReallocationMerchantCancelReallocatedTrue() throws InterruptedException, ParseException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = fulfillmentHelper.checkReallocation(USER_ENTITY_ID, TYPE_MERCHANT_CANCELLED, true, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test
    public void verifyCheckReallocationWithEmptyBody() throws InterruptedException, ParseException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = fulfillmentHelper.checkReallocation(USER_ENTITY_ID, EMPTY_STRING, false, false, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test
    public void verifyCheckReallocationWithInvalidToken() throws InterruptedException, ParseException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = fulfillmentHelper.checkReallocation(USER_ENTITY_ID, TYPE_CANCELLED_PHARMACY, false, true, false);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test
    public void verifyCheckReallocationWithoutToken() throws InterruptedException, ParseException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = fulfillmentHelper.checkReallocation(USER_ENTITY_ID, TYPE_CANCELLED_PHARMACY, false, false, true);
        Assert.assertTrue(komodoHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
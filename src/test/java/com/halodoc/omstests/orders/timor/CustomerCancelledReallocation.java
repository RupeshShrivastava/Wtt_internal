package com.halodoc.omstests.orders.timor;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerCancelledReallocation extends OrdersBaseTest {
    @Test
    public void verifyReallocation() throws InterruptedException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = timorHelper.reallocateOrder(null, false, false, false);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(timorHelper.verifyOrderStatus(STATUS_APPROVED, response));
        Assert.assertTrue(timorHelper.validateReallocation(response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test
    public void verifyReallocationReallocatedTrue() throws InterruptedException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = timorHelper.reallocateOrder(null, true, false, false);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test
    public void verifyReallocationWithInvalidOrderId() throws InterruptedException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = timorHelper.reallocateOrder(INVALID_ORDER_ID, false, false, false);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.NOT_FOUND, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test
    public void verifyReallocationWithInvalidToken() throws InterruptedException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = timorHelper.reallocateOrder(null, false, true, false);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.UNAUTHORIZED, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test
    public void verifyReallocationWithoutToken() throws InterruptedException {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = timorHelper.reallocateOrder(null, false, false, true);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.BAD_REQUEST, response));

        log.info("Exit Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
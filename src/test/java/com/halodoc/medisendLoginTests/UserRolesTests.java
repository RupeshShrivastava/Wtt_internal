package com.halodoc.medisendLoginTests;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.buru.BuruUtil;
import com.halodoc.oms.orders.utilities.constants.Constants;
import com.halodoc.omstests.orders.OrdersBaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRolesTests extends BuruUtil {

    List<Constants.MedisendOrderItem> medisendOrderItemsArray = new ArrayList<MedisendOrderItem>();

    OrdersBaseTest ordersBaseTest = new OrdersBaseTest();

    @Severity (SeverityLevel.BLOCKER)
    @Description ("UPDATE USER ROLE")
    @Test (groups = { "sanity", "regression" }, priority = 1)
    public void updateUserRole() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = getUserDetails(USER_ROLE_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, ACCESS_ROLES_PATH, USER_ROLE_ADMIN));
        response = updateUserRole(USER_ROLE_GPID, STATUS_ACTIVE, USER_ROLE_SELLER);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));
        response = getUserDetails(USER_ROLE_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, ACCESS_ROLES_PATH, USER_ROLE_SELLER));
        response = updateUserRole(USER_ROLE_GPID, STATUS_ACTIVE, USER_ROLE_BUYER);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));
        response = getUserDetails(USER_ROLE_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, ACCESS_ROLES_PATH, USER_ROLE_BUYER));
        response = updateUserRole(USER_ROLE_GPID, STATUS_ACTIVE, USER_ROLE_ADMIN);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));

    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPDATE USER ROLE FOR EMPTY ARRAY")
    @Test (groups = { "regression" }, priority = 2, dependsOnMethods = "updateUserRole")
    public void updateUserRoleForEmptyArray() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = getUserDetails(USER_ROLE_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, ACCESS_ROLES_PATH, USER_ROLE_ADMIN));
        response = updateUserEmptyRole(USER_ROLE_GPID,STATUS_ACTIVE);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));
        response = getUserDetails(USER_ROLE_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertNull(response.path(ACCESS_ROLES_PATH));
        response = updateUserRole(USER_ROLE_GPID, STATUS_ACTIVE, USER_ROLE_ADMIN);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("GET ACCESS ROLES")
    @Test (groups = { "status", "regression" }, priority = 3, dependsOnMethods = "updateUserRoleForEmptyArray")
    public void getAccessRole() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = getAccessRoles();
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, FIRST_ARRAY_ELEMENT + TITLE_PATH, USER_ROLE_BUYER));
        Assert.assertTrue(validateResponseValue(response, SECOND_ARRAY_ELEMENT + TITLE_PATH, USER_ROLE_SELLER));
        Assert.assertTrue(validateResponseValue(response, THIRD_ARRAY_ELEMENT + TITLE_PATH, USER_ROLE_ADMIN));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("API ACCESS FOR ROLES ADMIN, BUYER AND SELLER")
    @Test (groups = { "status", "regression" }, priority = 4, dependsOnMethods = "getAccessRole")
    public void verifyAPIAccessForRolesForAdminBuyerSeller() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = getUserDetails(USER_ACCESS_ROLE_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, ACCESS_ROLES_PATH, USER_ROLE_ADMIN));

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        response = createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        response = getTimorOrderSearch();
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, RESULT_PATH + FIRST_ARRAY_ELEMENT + CUSTOMER_ORDER_ID_PATH, V1_ORDER_SEARCH_ID));

        response = updateUserRole(USER_ACCESS_ROLE_GPID, STATUS_ACTIVE, USER_ROLE_SELLER);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));


        response = createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(validateStatusCode(HttpStatus.FORBIDDEN, response));
        response = getTimorOrderSearch();
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, RESULT_PATH + FIRST_ARRAY_ELEMENT + CUSTOMER_ORDER_ID_PATH, V1_ORDER_SEARCH_ID));

        response = updateUserRole(USER_ACCESS_ROLE_GPID, STATUS_ACTIVE, USER_ROLE_BUYER);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = createMedisendOrder(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        response = getTimorOrderSearch();
        Assert.assertTrue(validateStatusCode(HttpStatus.FORBIDDEN, response));

        response = updateUserRole(USER_ACCESS_ROLE_GPID, STATUS_ACTIVE, USER_ROLE_ADMIN);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("API ACCESS FOR ROLE DELETED USER")
    @Test (groups = { "status", "regression" }, priority = 5, dependsOnMethods = "verifyAPIAccessForRolesForAdminBuyerSeller")
    public void verifyAPIAccessForRolesForDeletedUsers() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(Constants.MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        Response response = createMedisendOrderDeletedPharmacyUser(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(validateStatusCode(HttpStatus.UNAUTHORIZED, response));
        response = getTimorOrderSearchDeletedPharmacy();
        Assert.assertTrue(validateStatusCode(HttpStatus.UNAUTHORIZED, response));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("VERIFY API ACCESS ROLE REMOVED USER")
    @Test (groups = { "status", "regression" }, priority = 6, dependsOnMethods = "verifyAPIAccessForRolesForDeletedUsers")
    public void verifyAPIAccessForRoleRemovedUsers() {
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        medisendOrderItemsArray.clear();
        medisendOrderItemsArray.add(Constants.MedisendOrderItem.ProductAvailable1BranchNoMinimum1.getInstance(REQUESTED_QUANTITY));
        Response response = createMedisendOrderRemovedPharmacyUser(getUUID(), CURRENCY_IDR, medisendOrderItemsArray);
        Assert.assertTrue(validateStatusCode(HttpStatus.FORBIDDEN, response));
        response = getTimorOrderSearchRemovedPharmacy();
        Assert.assertTrue(validateStatusCode(HttpStatus.FORBIDDEN, response));
    }
}
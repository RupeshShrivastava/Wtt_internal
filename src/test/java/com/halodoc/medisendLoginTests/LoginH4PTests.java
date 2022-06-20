package com.halodoc.medisendLoginTests;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.halodoc.oms.orders.utilities.buru.BuruUtil;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginH4PTests extends BuruUtil {

    @Severity (SeverityLevel.BLOCKER)
    @Description ("GET USER DETAILS FOR ACTIVE MAPPED USER")
    @Test (groups = {"sanity", "regression"}, priority = 1)
    public void getUserDetailsForActiveMappedUser(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = getUserDetails(USER_ACTIVE_MAPPED_ADMIN_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, GPID_PATH, USER_ACTIVE_MAPPED_ADMIN_GPID));
        Assert.assertTrue(validateResponseValue(response, STATUS_PATH, STATUS_ACTIVE));
        Assert.assertTrue(validatePathContainsValueInPath(response, MERCHANT_ID_PATH, true));
        Assert.assertTrue(validatePathContainsValueInPath(response, MERCHANT_LOCATION_ID_PATH, true));
        Assert.assertTrue(validateResponseValue(response, PHONE_NUMBER_PATH, PHONE_NUMBER_MEDISEND_ACTIVE_PHARMACY));
        Assert.assertTrue(validateResponseValue(response, STATUS_DELETED, FALSE));
        Assert.assertNotNull(response.path(MAPPED_ENTITIES_PATH));
        Assert.assertTrue(validatePathContainsValueInPath(response, MAPPED_PATH, true));
        Assert.assertFalse(validateResponseValueIsNull(response, OWNER_NAME_PATH));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("GET USER DETAILS FOR PENDING UNMAPPED USER")
    @Test(groups = {"regression"}, priority = 2)
    public void getUserDetailsForPendingUnmappedUser(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = getUserDetails(USER_PENDING_UNMAPPED_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, GPID_PATH, USER_PENDING_UNMAPPED_GPID));
        Assert.assertTrue(validateResponseValue(response, STATUS_PATH, STATUS_PENDING));
        Assert.assertTrue(validatePathContainsValueInPath(response, MERCHANT_ID_PATH, true));
        Assert.assertTrue(validatePathContainsValueInPath(response, MERCHANT_LOCATION_ID_PATH, true));
        Assert.assertTrue(validateResponseValue(response, PHONE_NUMBER_PATH, PHONE_NUMBER_PENDING_USER));
        Assert.assertTrue(validateResponseValue(response, STATUS_DELETED, FALSE));
        Assert.assertTrue(validateArraySize(response, MAPPED_ENTITIES_PATH, EQUAL_KEY, ZERO_QUANTITY));
        Assert.assertTrue(validateResponseValue(response, MAPPED_PATH, FALSE));
        Assert.assertTrue(validateResponseValue(response, OWNER_NAME_PATH, OWNER_NAME_PENDING));
        Assert.assertNull(response.path("ACCESS_ROLES_PATH"));
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("GET USER DETAILS FOR INVALID GPID")
    @Test(groups = {"regression"}, priority = 3)
    public void getUserDetailsInvalidGPID(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = getUserDetails(INVALID_ID);
        Assert.assertTrue(validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("MERCHANT AND DISTRIBUTOR UNMAPPING AND MAPPING USER DETAILS")
    @Test(groups = {"regression"}, priority = 4)
    public void verifyMerchantAndDistributorUnmappingAndMappingUserDetails(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = getUserDetails(USER_UNMAPPING_MAPPING_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, STATUS_PATH, STATUS_ACTIVE));
        Assert.assertTrue(validateArraySize(response, MAPPED_ENTITIES_PATH, NOT_EQUAL_KEY, ZERO_QUANTITY));
        Assert.assertTrue(validatePathContainsValueInPath(response, MAPPED_PATH, true));

        response = updateMerchantMapping(DISTRIBUTOR_ID_LOGIN_USER, DISTRIBUTOR_BRANCH_LOGIN_USER, DISTRIBUTOR_ENTITY_MAPPING_EXTERNAL_LOGIN, USER_DETAIL_MERCHANT_LOCATION_ID, STATUS_INACTIVE);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = getUserDetails(USER_UNMAPPING_MAPPING_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, GPID_PATH, USER_UNMAPPING_MAPPING_GPID));
        Assert.assertTrue(validateResponseValue(response, STATUS_PATH, STATUS_ACTIVE));
        Assert.assertTrue(validateResponseValue(response, MERCHANT_ID_PATH, USER_DETAIL_MERCHANT_ID));
        Assert.assertTrue(validateResponseValue(response, MERCHANT_LOCATION_ID_PATH, USER_DETAIL_MERCHANT_LOCATION_ID));
        Assert.assertTrue(response.path(MAPPED_ENTITIES_PATH).toString().equals("[]"));
        Assert.assertTrue(validateResponseValue(response, MAPPED_PATH, FALSE));

        response = updateMerchantMapping(DISTRIBUTOR_ID_LOGIN_USER, DISTRIBUTOR_BRANCH_LOGIN_USER, DISTRIBUTOR_ENTITY_MAPPING_EXTERNAL_LOGIN, USER_DETAIL_MERCHANT_LOCATION_ID, STATUS_ACTIVE);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("UPDATE USER DETAILS FOR PENDING USER")
    @Test(groups = {"sanity", "regression"}, priority = 5)
    public void updateUserDetailsForPendingUser(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = updateUserDetailsValidData(USER_PENDING_UPDATE_GPID, OWNER_NAME_PENDING, merchantId, merchant_locationId);
        Assert.assertTrue(validateStatusCode(HttpStatus.CREATED, response));

        response = getUserDetails(USER_PENDING_UPDATE_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, GPID_PATH, USER_PENDING_UPDATE_GPID));
        Assert.assertTrue(validateResponseValue(response, STATUS_PATH, STATUS_PENDING));
        Assert.assertTrue(validateResponseValue(response, MERCHANT_ID_PATH, merchantId));
        Assert.assertTrue(validateResponseValue(response, MERCHANT_LOCATION_ID_PATH, merchant_locationId));
        Assert.assertTrue(validateResponseValue(response, STATUS_DELETED, FALSE));
        Assert.assertTrue(validateResponseValue(response, OWNER_NAME_PATH, OWNER_NAME_PENDING));

        response = updateUserDetailsValidData(USER_PENDING_UPDATE_GPID, OWNER_NAME_ACTIVE, USER_DETAIL_MERCHANT_ID, USER_DETAIL_MERCHANT_LOCATION_ID);
        Assert.assertTrue(validateStatusCode(HttpStatus.CREATED, response));
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPDATE USER DETAILS WITH EMTPY NAME, MERCHANT LOCATION AND MERCHANT ID")
    @Test(groups = {"regression"}, priority = 6)
    public void updateUserDetailsWithEmptyNameMerchantLocationAndId(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = updateUserDetailsValidData(USER_ACTIVE_MAPPED_ADMIN_GPID, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
        Assert.assertTrue(validateStatusCode(HttpStatus.UNPROCESSABLE_ENTITY, response));
    }

    //Error thrown only when owner_name/merchant_location_id/merchant_id is different.
    @Severity (SeverityLevel.NORMAL)
    @Description ("UPDATE USER DETAILS WITH STATUS ACTIVE AND ALREADY MAPPED")
    @Test(groups = {"regression"}, priority = 7)
    public void updateUserDetailsWithStateActiveAndAlreadyMapped(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = updateUserDetailsValidData(USER_ACTIVE_MAPPED_ADMIN_GPID, OWNER_NAME_PENDING, USER_DETAIL_MERCHANT_ID, USER_DETAIL_MERCHANT_LOCATION_ID);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPDATE DELETED USER DETAILS WITH STATUS PENDING")
    @Test(groups = {"regression"}, priority = 8)
    public void updateDeleteUserDetailsWithStatusPending(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = updateUserDetailsValidData(USER_DELETED_PENDING_GPID, OWNER_NAME_PENDING, USER_DETAIL_MERCHANT_ID, USER_DETAIL_MERCHANT_LOCATION_ID);
        Assert.assertTrue(validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("DELETE USER DETAILS WITH STATUS ACTIVE")
    @Test(groups = {"regression"}, priority = 9)
    public void updateDeleteUserDetailsWithStatusActive(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = updateUserDetailsValidData(USER_DELETED_ACTIVE_GPID, OWNER_NAME_ACTIVE, USER_DETAIL_MERCHANT_ID, USER_DETAIL_MERCHANT_LOCATION_ID);
        Assert.assertTrue(validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("UPDATE USER DETAILS WITH INACTIVE GPID")
    @Test(groups = {"regression"}, priority = 10)
    public void updateUserDetailsWithInvalidGPID(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = updateUserDetailsValidData(INVALID_ID, OWNER_NAME_ACTIVE, USER_DETAIL_MERCHANT_ID, USER_DETAIL_MERCHANT_LOCATION_ID);
        Assert.assertTrue(validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("VERIFY ACTIVATE USER")
    @Test(groups = {"sanity", "regression"}, priority = 11)
    public void verifyActivateUser(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = activateUser(USER_ACTIVATE_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = getUserDetails(USER_ACTIVATE_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, GPID_PATH, USER_ACTIVATE_GPID));
        Assert.assertTrue(validateResponseValue(response, STATUS_PATH, STATUS_ACTIVE));

        response = updateUserDetailsStatusCC(USER_ACTIVATE_GPID, STATUS_PENDING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("ACTIVE USER THAT IS ALREADY ACTIVATED")
    @Test(groups = {"regression"}, priority = 12)
    public void activateAlreadyActiveUser(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = activateUser(USER_ACTIVE_MAPPED_ADMIN_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("ACTIVATE USER WITH NO MERCHANT ID")
    @Test(groups = {"sanity", "regression"}, priority = 13)
    public void activateUserWithNoMerchantID(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = activateUser(USER_PENDING_NO_MERCHANT_ID);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("ACTIVE USER WITH DELETED TRUE")
    @Test(groups = {"sanity", "regression"}, priority = 14)
    public void activateUserWithDeletedTrue(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = activateUser(USER_DELETED_PENDING_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("DELETE AND RECOVER ACTIVE USER")
    @Test(groups = {"sanity", "regression"}, priority = 15)
    public void verifyDeleteAndRecoverActiveUser(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = deleteUser(USER_DELETE_RECOVER_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = getUserDetails(USER_DELETE_RECOVER_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, GPID_PATH, USER_DELETE_RECOVER_GPID));
        Assert.assertTrue(validateResponseValue(response, STATUS_PATH, STATUS_ACTIVE));
        Assert.assertTrue(validateResponseValue(response, STATUS_DELETED, TRUE));
        Assert.assertTrue(validatePathContainsValueInPath(response, MAPPED_PATH, true));

        response = recoverUser(USER_DELETE_RECOVER_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = getUserDetails(USER_DELETE_RECOVER_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, GPID_PATH, USER_DELETE_RECOVER_GPID));
        Assert.assertTrue(validateResponseValue(response, STATUS_PATH, STATUS_ACTIVE));
        Assert.assertTrue(validateResponseValue(response, STATUS_DELETED, FALSE));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("DELETE PENDING UNMAPPED USER")
    @Test(groups = {"regression"}, priority = 16)
    public void deletePendingUnmappedUser(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = deleteUser(USER_PENDING_UNMAPPED_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));

        response = getUserDetails(USER_PENDING_UNMAPPED_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateResponseValue(response, GPID_PATH, USER_PENDING_UNMAPPED_GPID));
        Assert.assertTrue(validateResponseValue(response, STATUS_PATH, STATUS_PENDING));
        Assert.assertTrue(validateResponseValue(response, STATUS_DELETED, TRUE));

        response = recoverUser(USER_PENDING_UNMAPPED_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.NO_CONTENT, response));
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("DELETE ALREADY DELETED USER")
    @Test(groups = {"regression"}, priority = 17)
    public void deleteAlreadyDeletedUser(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = deleteUser(USER_DELETED_PENDING_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.NOT_FOUND, response));
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("RECOVER USER WHICH IS NOT DELETED")
    @Test(groups = {"regression"}, priority = 18)
    public void recoverUserWhichIsNotDeleted(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = recoverUser(USER_PENDING_UNMAPPED_GPID);
        Assert.assertTrue(validateStatusCode(HttpStatus.BAD_REQUEST, response));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("SEARCH FOR REGISTERED USERS")
    @Test(groups = {"sanity", "regression"}, priority = 19)
    public void userSearchForRegisteredUsers(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = userSearch(STATUS_ACTIVE, FALSE);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateStatusAndDeletedInUserSearch(response, STATUS_ACTIVE, FALSE));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("SEARCH FOR UNREGISTERED USERS")
    @Test(groups = {"sanity", "regression"}, priority = 20)
    public void userSearchForUnregisteredUsers(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = userSearch(STATUS_PENDING, FALSE);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateStatusAndDeletedInUserSearch(response, STATUS_PENDING, FALSE));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("SEARCH FOR DELETED USERS")
    @Test(groups = {"sanity", "regression"}, priority = 21)
    public void userSearchForDeletedUsers(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        Response response = userSearch(STATUS_PENDING, TRUE);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateStatusAndDeletedInUserSearch(response, STATUS_PENDING, TRUE));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("SEARCH FOR REGISTERED USERS USING PHONE NUMBER")
    @Test(groups = {"sanity", "regression"}, priority = 22)
    public void userSearchForRegisteredUsersUsingPhoneNumber(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        String phoneNumber = getPhoneNumberFromUserDetails(USER_ACTIVE_MAPPED_ADMIN_GPID);
        Assert.assertNotNull(phoneNumber);

        Response response = userSearchUsingPhoneNumber(STATUS_ACTIVE, phoneNumber, FALSE);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateGPIDStatusAndDeletedInUserSearch(response, USER_ACTIVE_MAPPED_ADMIN_GPID, STATUS_ACTIVE, FALSE));
    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("SEARCH FOR UNREGISTERED USERS USING PHONE NUMBER")
    @Test(groups = {"sanity", "regression"}, priority = 23)
    public void userSearchForUnregisteredUsersUsingPhoneNumber(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        String phoneNumber = getPhoneNumberFromUserDetails(USER_PENDING_UNMAPPED_GPID);
        Assert.assertNotNull(phoneNumber);

        Response response = userSearchUsingPhoneNumber(STATUS_PENDING, phoneNumber, FALSE);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateGPIDStatusAndDeletedInUserSearch(response, USER_PENDING_UNMAPPED_GPID, STATUS_PENDING, FALSE));

    }

    @Severity (SeverityLevel.BLOCKER)
    @Description ("SEARCH FOR DELETED USERS USING PHONE NUMBER")
    @Test(groups = {"sanity", "regression"}, priority = 24)
    public void userSearchForDeletedUsersUsingPhoneNumber(){
        log.info("Running Test : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        String phoneNumber = getPhoneNumberFromUserDetails(USER_DELETED_PENDING_GPID);
        Assert.assertNotNull(phoneNumber);

        Response response = userSearchUsingPhoneNumber(STATUS_PENDING, phoneNumber, TRUE);
        Assert.assertTrue(validateStatusCode(HttpStatus.OK, response));
        Assert.assertTrue(validateGPIDStatusAndDeletedInUserSearch(response, USER_DELETED_PENDING_GPID, STATUS_PENDING, TRUE));
    }
}

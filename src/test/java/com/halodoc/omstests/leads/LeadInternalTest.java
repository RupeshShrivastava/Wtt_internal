package com.halodoc.omstests.leads;

import static com.halodoc.omstests.Constants.LEADS_INTERNAL;
import static com.halodoc.omstests.Constants.LEAD_DOCUMENTS;
import static com.halodoc.omstests.Constants.LEAD_NOTES;
import static com.halodoc.omstests.Constants.OMS_X_APP_TOKEN;
import static com.halodoc.omstests.Constants.ORDERS_INTERNAL;
import static com.halodoc.omstests.Constants.ORDER_BASE_URL;
import static com.halodoc.omstests.Constants.ORDER_CONFIRM_INTERNAL;
import static com.halodoc.omstests.Constants.REJECT_LEAD;
import static com.halodoc.omstests.Constants.SEARCH_LEADS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import com.halodoc.omstests.TimorHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by nageshkumar
 * since  20/10/17.
 */
@Slf4j
public class LeadInternalTest {
    private TimorHelper timorHelper;

    private String lead_id;

    private String processed_lead_id;

    private String order_id;

    @BeforeClass
    public void setUp() {
        timorHelper = new TimorHelper();
        RestAssured.baseURI = ORDER_BASE_URL;
    }

    @Test
    public void createLead() {
        log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        lead_id = given().
                                 header("X-APP-TOKEN", OMS_X_APP_TOKEN).
                                 contentType(ContentType.JSON).
                                 body(timorHelper.getRequestFixture("leads", "create_lead_internal.json")).
                                 expect().
                                 log().all().
                                 statusCode(201).
                                 when().
                                 post(LEADS_INTERNAL).then().extract().path("external_id");
    }

    @Test
    public void searchLeads() {
        log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        given().
                       header("X-APP-TOKEN", OMS_X_APP_TOKEN).
                       params("start_lead_date", 0).
                       expect().
                       log().all().
                       statusCode(200).
                       when().
                       get(SEARCH_LEADS);
    }

    @Test (priority = 1)
    public void getLeadById() {
        log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        given().
                       header("X-APP-TOKEN", OMS_X_APP_TOKEN).
                       expect().
                       log().all().
                       statusCode(200).
                       when().
                       get(LEADS_INTERNAL + "/" + lead_id);
    }

    @Test (priority = 1)
    public void createLeadNotes() {
        log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        given().
                       header("X-APP-TOKEN", OMS_X_APP_TOKEN).
                       contentType(ContentType.JSON).
                       body(timorHelper.getRequestFixture("leads", "lead_notes.json")).
                       expect().
                       log().all().
                       statusCode(201).
                       when().
                       post(LEAD_NOTES, lead_id);
    }

    @Test (priority = 1)
    public void createLeadDocuments() {
        log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        given().
                       header("X-APP-TOKEN", OMS_X_APP_TOKEN).
                       contentType(ContentType.JSON).
                       body(timorHelper.getRequestFixture("leads", "lead_documents.json")).
                       expect().
                       log().all().
                       statusCode(201).
                       when().
                       post(LEAD_DOCUMENTS, lead_id);
    }

    @Test (dependsOnMethods = "createLead")
    public void rejectLead() {
        log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        given().
                       header("X-APP-TOKEN", OMS_X_APP_TOKEN).
                       contentType(ContentType.JSON).
                       body(timorHelper.getRequestFixture("leads", "reject_lead.json")).
                       expect().
                       log().all().
                       statusCode(204).
                       when().
                       put(REJECT_LEAD, lead_id);
    }

    @Test (dependsOnMethods = "rejectLead")
    public void createOrderForRejectedLead() {
        log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        given().
                       header("X-APP-TOKEN", OMS_X_APP_TOKEN).
                       contentType(ContentType.JSON).
                       body(timorHelper.getLeadOrderFixture("leads", "create_lead_order.json").replace("$lead_id", lead_id)).
                       expect().
                       log().all().
                       statusCode(404).
                       body("message", equalTo("Lead not found.")).
                       when().
                       post(ORDERS_INTERNAL);
    }

    @Test
    public void createOrderForPendingLead() {
        log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        processed_lead_id = timorHelper.createLead();
        order_id = given().
                                  header("X-APP-TOKEN", OMS_X_APP_TOKEN).
                                  contentType(ContentType.JSON).
                                  body(timorHelper.getLeadOrderFixture("leads", "create_lead_order.json").replace("$lead_id", processed_lead_id)).
                                  expect().
                                  log().all().
                                  statusCode(201).
                                  when().
                                  post(ORDERS_INTERNAL).then().extract().path("customer_order_id");
    }

    @Test (dependsOnMethods = "createOrderForPendingLead")
    public void createOrderForProcessedLead() {
        log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        given().
                       header("X-APP-TOKEN", OMS_X_APP_TOKEN).
                       contentType(ContentType.JSON).
                       body(timorHelper.getLeadOrderFixture("leads", "create_lead_order.json").replace("$lead_id", processed_lead_id)).
                       expect().
                       log().all().
                       statusCode(404).
                       body("message", equalTo("Lead not found.")).
                       when().
                       post(ORDERS_INTERNAL);
    }

    @Test (dependsOnMethods = "createOrderForPendingLead")
    public void confirmOrder() throws InterruptedException {
        log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        Thread.sleep(5000);
        given().
                       contentType(ContentType.JSON).
                       header("X-APP-TOKEN", OMS_X_APP_TOKEN).
                       body(timorHelper.getRequestFixture("leads", "confirm_lead_order.json")).
                       expect().
                       log().all().
                       statusCode(204).
                       when().
                       put(ORDER_BASE_URL + ORDER_CONFIRM_INTERNAL.replace("$order_id", order_id));
    }

}

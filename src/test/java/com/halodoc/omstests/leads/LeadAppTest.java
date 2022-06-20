package com.halodoc.omstests.leads;

import static com.halodoc.omstests.Constants.LEADS;
import static com.halodoc.omstests.Constants.ORDER_BASE_URL;
import static com.halodoc.omstests.Constants.UPLOAD_LEADS_DOC;
import static com.halodoc.omstests.Constants.USER_AGENT_PATIENT;
import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.HttpClientConfig.httpClientConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.halodoc.omstests.BaseOmsTest;
import com.halodoc.omstests.TimorHelper;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by nageshkumar
 * since  18/10/17.
 */
@Slf4j
public class LeadAppTest extends BaseOmsTest {
    private String customer_access_token;

    private TimorHelper timorHelper;

    private RestAssuredConfig config;

    @BeforeClass
    public void setUp() {
        timorHelper = new TimorHelper();
        customer_access_token = timorHelper.getPatientAccessToken();
        RestAssured.baseURI = ORDER_BASE_URL;
        config = config().httpClient(httpClientConfig().setParam("CONNECTION_MANAGER_TIMEOUT", 50000));
        // config_dz = new CurlLoggingRestAssuredConfigBuilder().printMultiliner().build();
    }

    @Test
    public void uploadLeadDocument() throws IOException {
        log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        Path file_path = Paths.get("src/main/resources/paper_prescription.pdf");
        byte[] file_data = Files.readAllBytes(file_path);

            given().
                           config(config).
                    header("Authorization", "bearer " + customer_access_token).
                    header("x-file-type", "application/pdf").
                    header("User-Agent", USER_AGENT_PATIENT).
                    contentType(ContentType.BINARY).
                    body(file_data).
                    expect().
                    log().all().
                    statusCode(200).
                    when().
                           post(UPLOAD_LEADS_DOC);

        given().
                       config(config).
                       header("Authorization", "bearer " + customer_access_token).
                       header("x-file-type", "application/pdf").
                       header("User-Agent", USER_AGENT_PATIENT).
                       body(file_data).
                       expect().
                       log().all().
                       statusCode(200).
                       when().
                       post(UPLOAD_LEADS_DOC);

    }

    @Test
    public void createLead() {
        log.info("Running Test : {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        given().
                       contentType(ContentType.JSON).
                       header("Authorization", "bearer " + customer_access_token).
                       header("User-Agent", USER_AGENT_PATIENT).
                       body(timorHelper.getRequestFixture("leads", "create_lead.json")).
                       expect().
                       log().all().
                       statusCode(201).
                       when().
                       post(LEADS);
    }

    @AfterClass
    public void cleanUp() {
        log.info("Logging out user");
        timorHelper.logoutPatient(customer_access_token);
    }
}

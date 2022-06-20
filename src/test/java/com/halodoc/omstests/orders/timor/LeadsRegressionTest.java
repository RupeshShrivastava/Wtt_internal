package com.halodoc.omstests.orders.timor;

import com.halodoc.omstests.orders.OrdersBaseTest;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import lombok.extern.slf4j.Slf4j;
import static com.halodoc.oms.orders.utilities.constants.Constants.*;

@Slf4j
public class LeadsRegressionTest extends OrdersBaseTest {
    @Test
    public void searchLeadsOrder(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");

        String leadId = orderResponse.path("external_id").toString();
        Assert.assertEquals(timorHelper.searchLeads(leadId).getStatusCode(), HttpStatus.OK.value());
    }

    @Test
    public void searchInvalidLeadsOrder(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        String leadId = orderResponse.path("external_id").toString();
        Assert.assertEquals(timorHelper.searchLeads(leadId).getStatusCode(), HttpStatus.OK.value());
    }

    @Test
    public void uploadDocsLead(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        orderResponse=timorHelper.uploadDocLead(LEADS_DOC_UPLOAD);
        log.info(orderResponse.toString());
        Assert.assertEquals(orderResponse.getStatusCode(),200);

    }

    @Test
    public void verifyLeadByID(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        String leadId = orderResponse.path("external_id").toString();
        orderResponse=timorHelper.getLead(leadId);
        Assert.assertEquals(orderResponse.getStatusCode(),200);
    }

    @Test
    public void verifyLeadByInvalidID(){
        Response orderResponse;
        orderResponse=timorHelper.getLead("leadId");
        Assert.assertEquals(orderResponse.getStatusCode(),404);
    }

    @Test
    public void verifyLeadByEmptyID(){
        Response orderResponse;
        orderResponse=timorHelper.getLead("");
        Assert.assertEquals(orderResponse.getStatusCode(),405);
    }

    @Test
    public void verifyLeadUpdate(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        String leadId = orderResponse.path("external_id").toString();
        orderResponse=timorHelper.updateLead(leadId, DOCUMENT_ID, DOCUMENT_URL);
        Assert.assertEquals(orderResponse.getStatusCode(),204);
        orderResponse=timorHelper.getLead(leadId);
        Assert.assertEquals(orderResponse.path("source"),"customer_app");
    }

    @Test
    public void verifyLeadUpdateInvalidData(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        String leadId = orderResponse.path("external_id").toString();
        orderResponse=timorHelper.updateLead(INVALID_KEY, INVALID_KEY, INVALID_KEY);
        Assert.assertEquals(orderResponse.getStatusCode(),404);
        orderResponse=timorHelper.getLead(leadId);
        Assert.assertEquals(orderResponse.path("source"),"customer_app");
    }

    @Test
    public void verifyLeadStatus(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        String leadId = orderResponse.path("external_id").toString();
        orderResponse=timorHelper.getLead(leadId);
        Assert.assertEquals(orderResponse.path("status"),"pending");
    }

    @Test
    public void getLeadDocuments(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        String leadId = orderResponse.path("external_id").toString();
        orderResponse=timorHelper.uploadDocLead(LEADS_DOC_UPLOAD);
        Assert.assertEquals(orderResponse.getStatusCode(),200);
        orderResponse=timorHelper.getLeadDocuments(leadId);
        Assert.assertEquals(orderResponse.statusCode(),200);
    }

    @Test
    public void getLeadDocumentsInvalid(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        String leadId = orderResponse.path("external_id").toString();
        orderResponse=timorHelper.uploadDocLead(RANDOMIVALID_FILE);
        Assert.assertEquals(orderResponse.getStatusCode(),400);
        orderResponse=timorHelper.getLeadDocuments(leadId);
        Assert.assertEquals(orderResponse.statusCode(),200);
    }

    @Test
    public void getMultiDocsLeads(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        String leadId = orderResponse.path("external_id").toString();
        orderResponse=timorHelper.uploadDocLead(LEADS_DOC_UPLOAD);
        String document_id=orderResponse.path("document_id");
        String doc_url=orderResponse.path("thumbnail_url");
        log.info("DATEED"+orderResponse.prettyPrint());
        orderResponse=timorHelper.updateLead(leadId,document_id,doc_url);
        Assert.assertEquals(orderResponse.getStatusCode(),204);
        orderResponse=timorHelper.getLeadDocuments(leadId);
        Assert.assertEquals(orderResponse.statusCode(),200);
    }

    @Test
    public void getMultiGetDocs(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        String leadId = orderResponse.path("external_id").toString();
        orderResponse=timorHelper.uploadDocLead(LEADS_DOC_UPLOAD);
        String document_id=orderResponse.path("document_id");
        String doc_url=orderResponse.path("thumbnail_url");
        log.info("DATEED"+orderResponse.prettyPrint());
        orderResponse=timorHelper.updateLead(leadId,document_id,doc_url);
        Assert.assertEquals(orderResponse.statusCode(),204);
        orderResponse=timorHelper.getLeadMultiDocuments(leadId,document_id);
        Assert.assertEquals(orderResponse.statusCode(),200);
    }

    @Test(enabled = false)
    public void updateLeadInvalidDocId() {
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        String leadId = orderResponse.path("external_id").toString();
        orderResponse = timorHelper.uploadDocLead(LEADS_DOC_UPLOAD);
        String doc_url = orderResponse.path("thumbnail_url");
        log.info("DATEED" + orderResponse.prettyPrint());
        orderResponse = timorHelper.updateLead(leadId, "document_id", doc_url);
        Assert.assertEquals(orderResponse.statusCode(), 400);
    }

    @Test(enabled = false)
    public void updateLeadInvalidDocUrl(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        String leadId = orderResponse.path("external_id").toString();
        orderResponse=timorHelper.uploadDocLead(LEADS_DOC_UPLOAD);
        String document_id=orderResponse.path("document_id");
        orderResponse=timorHelper.updateLead(leadId,document_id,"doc_url");
        Assert.assertEquals(orderResponse.statusCode(),400);
    }

    @Test
    public void updateLeadInvalidLeadID(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        orderResponse=timorHelper.uploadDocLead(LEADS_DOC_UPLOAD);
        String document_id=orderResponse.path("document_id");
        log.info("DATEED"+orderResponse.prettyPrint());
        orderResponse=timorHelper.updateLead("leadId",document_id,"doc_url");
        Assert.assertEquals(orderResponse.statusCode(),404);
    }

    @Test
    public void verifySignedUrlLeads(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        orderResponse=timorHelper.uploadDocLead(LEADS_DOC_UPLOAD);
        String document_id=orderResponse.path("document_id");
        Assert.assertNotNull(document_id);
    }

    @Test
    public void verifySignedUrldocumentId(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        orderResponse=timorHelper.uploadDocLead(LEADS_DOC_UPLOAD);
        String document_id=orderResponse.path("document_id");
        Assert.assertTrue(document_id.length()>10);
    }

    @Test
    public void verifySignedUrlNotEmpty(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        orderResponse=timorHelper.uploadDocLead(LEADS_DOC_UPLOAD);
        String image_url=orderResponse.path("image_url");
        Assert.assertNotNull(image_url);
    }

    @Test
    public void verifySignedUrlValidURL(){
        Response orderResponse;
        orderResponse = timorHelper.createLead(USER_ENTITY_ID);
        Assert.assertTrue(timorHelper.validateStatusCode(HttpStatus.CREATED, orderResponse), "Create Lead - Failed");
        orderResponse=timorHelper.uploadDocLead(LEADS_DOC_UPLOAD);
        String image_url=orderResponse.path("image_url");
        Assert.assertNotNull(image_url.length()>10);
    }
}
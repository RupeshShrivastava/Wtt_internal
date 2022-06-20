package com.halodoc.omstests.orders;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.halodoc.oms.orders.utilities.LockUtil;
import com.halodoc.oms.orders.utilities.buru.BuruUtil;
import com.halodoc.oms.orders.utilities.derawan.DerawanUtil;
import com.halodoc.oms.orders.utilities.fulfillment.FulfillmentUtil;
import com.halodoc.oms.orders.utilities.komodo.KomodoUtil;
import com.halodoc.oms.orders.utilities.timor.TimorUtil;
import com.halodoc.transformers.havelock.service.LockService;
import com.halodoc.utils.http.RestClient;
import ch.qos.logback.classic.Level;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.testng.annotations.*;
import org.springframework.http.HttpStatus;
import static com.halodoc.oms.orders.utilities.constants.Constants.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import static org.awaitility.Awaitility.await;

//TODO : move all re usable methods and initialisations in base Test , U
@Slf4j
public class OrdersBaseTest {
    public TimorUtil timorHelper;
    public BuruUtil buruHelper;
    public KomodoUtil komodoHelper;
    public FulfillmentUtil fulfillmentHelper;
    public DerawanUtil derawanHelper;
    private LockService lockService;
    private String orderId = null;
    private String shipmentId = null;
    private String expectedStatus = null;
    private String pathAttribute = null;
    private Integer max_retry=30;


    @BeforeClass(alwaysRun = true)
    public void setUp() throws IOException {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
        ch.qos.logback.classic.Logger root1 = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("io.netty");
        ch.qos.logback.classic.Logger root2 = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.redisson");
        root.setLevel(Level.OFF);
        root1.setLevel(Level.OFF);
        root2.setLevel(Level.OFF);
        Awaitility.setDefaultPollInterval(1, TimeUnit.SECONDS);
        Awaitility.setDefaultPollDelay(4, TimeUnit.SECONDS);
        Awaitility.setDefaultTimeout(30, TimeUnit.SECONDS);

        //initializeLocking();

        timorHelper = new TimorUtil();
        buruHelper = new BuruUtil();
        komodoHelper = new KomodoUtil();
        fulfillmentHelper = new FulfillmentUtil();
        derawanHelper = new DerawanUtil();
        timorHelper.walletCredit(USER_ENTITY_ID, "1000000");
        timorHelper.walletCredit(USER_ENTITY_ID, "1000000");
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
       // lockService.getLocker().stop();
    }

    private void initializeLocking() throws IOException {
        String url = BASE_URL + TIMOR_ORDER_PORT + CONFIG_URI;
        HashMap<String, String> headers = new HashMap();

        RestClient restClient = new RestClient(url, headers);
        Response response = restClient.executeGet();
        String bodyResponse = response.getBody().asString()
                                      .replace("${REDIS_CLUSTER_NODE_ADDRESS}", REDIS_ADDRESS)
                                      .replace("${REDIS_CLUSTER_PASSWORD}", EMPTY_STRING);

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TimorOmsConfiguration timorOmsConfiguration = objectMapper.readValue(bodyResponse, TimorOmsConfiguration.class);

        lockService = timorOmsConfiguration.getHavelockConfiguration().getLockProvider().getLockService(timorOmsConfiguration.getHavelockConfiguration());
        lockService.getLocker().start();

        lockHelper = new LockUtil(lockService.getLocker());

    }

    private void checkLocking() {
        await().until(checkStatus(MODE_CHECK_LOCK));
    }

    public boolean checkStatusUntil(String expectedStatus, String orderId) {
        try {
            this.orderId = orderId;
            this.expectedStatus = expectedStatus;
            checkLocking();
            await().until(checkStatus(MODE_CHECK_STATUS));
            checkLocking();
        } catch(Exception e) {
            return false;
        }

        return true;
    }

    private Callable<Boolean> checkStatus(String mode) {
        return () -> {
            Response response;

            switch(mode) {
                case MODE_CHECK_STATUS:
                    response = timorHelper.getOrderDetails(orderId);
                    String actualStatus = response.path("status").toString();
                    return actualStatus.equals(expectedStatus);
                case MODE_CHECK_LOCK:
                    boolean isLockAcquired = false;
                    try {
                        isLockAcquired = lockHelper.lock(orderId);
                    } catch(Exception e) {
                        log.info("Error in acquiring lock : " + e.getMessage());
                    } finally {
                        if(isLockAcquired) {
                            lockHelper.unlock(orderId);
                        }
                        log.info("Inside Finally Locking Acquire: " + isLockAcquired);
                    }
                    log.info("Locking Acquire: " + isLockAcquired);
                    return isLockAcquired;
                case MODE_CHECK_ATTRIBUTE_SHIPMENT:
                    response =  derawanHelper.getOrderDetails(orderId);
                    boolean isAttributeNull = false;
                    boolean isContinueCheck = true;
                    int index = 0;
                    while(isContinueCheck) {
                        String tmpShipmentId = response.path(SHIPMENTS_PATH + "[" + index + "]." + EXTERNAL_ID_PATH);
                        if(tmpShipmentId.equals(shipmentId)) {
                            isAttributeNull = response.path(SHIPMENTS_PATH + "[" + index + "]." + pathAttribute) == null;
                            isContinueCheck = false;
                        }
                        index++;
                    }
                    return !isAttributeNull;
                default:
                    return false;
            }
        };
    }

    public boolean updateDelOption(Response response) {
        String orderID = response.path("customer_order_id").toString();
        JsonPath jsonPathEvaluator = response.jsonPath();
        List<String> externalIds = jsonPathEvaluator.getList("delivery_options.external_id");
        List<String> groupId = jsonPathEvaluator.getList("delivery_options.group_id");
        log.info("Found " + externalIds.size() + " number of delivery options ");
        for (int i = 0; i < externalIds.size(); i++) {
            if (timorHelper.confirmDelOptions(orderID, externalIds.get(i), groupId.get(i)).statusCode() == HttpStatus.OK.value()) {
                log.info("Updated delivery options successfully ");
                if(i==externalIds.size()-1){
                    return true;
                }
            }
        }
        return false;
    }

    public void updateItems(Response response,int requestedQty) {
        String orderID = response.path("customer_order_id").toString();
        String listingId = response.path("items.listing_id[0]").toString();
        String groupId = response.path("items.group_id[0]").toString();
        String externalId=response.path("delivery_options.external_id[0]");
        timorHelper.updateItems(orderID, listingId, groupId,requestedQty,externalId);
        log.info("Updated delivery options successfully ");
    }

    public boolean checkIfGroupGenerated(String orderID) {
        int i = 0;
        while (i < max_retry) {
            Response response = timorHelper.getInternalOrderDetails(orderID);
            JsonPath jsonPathEvaluator = response.jsonPath();
            List<String> externalIds = jsonPathEvaluator.getList("delivery_options.group_id");
             String groupId = jsonPathEvaluator.get("items.group_id[0]");
            if (!externalIds.isEmpty()&&groupId!=null) {
                return true;
            }
            i++;
            continue;
        }

        return false;
    }
    public boolean checkIfDeliveryOptGenerated(String orderID) {
        int i = 0;
        while (i < 50) {
            Response response = timorHelper.getInternalOrderDetails(orderID);
            JsonPath jsonPathEvaluator = response.jsonPath();
            List<String> externalIds = jsonPathEvaluator.getList("delivery_options");
            String groupId = jsonPathEvaluator.get("items.group_id[0]");
            if (!externalIds.isEmpty()&&groupId!=null) {
                return true;
            }
            i++;
            continue;
        }

        return false;
    }
    public boolean checkIfAllocated(String orderID) {
        int i = 0;
        while (i < 60) {
            Response response = timorHelper.getInternalOrderDetails(orderID);
            JsonPath jsonPathEvaluator = response.jsonPath();
            if(jsonPathEvaluator.get("status").toString().contains("approved"))
            {
                return true;
            }
             i++;
                continue;

        }

        return false;
    }

    public String getUUID() {
        return UUID.randomUUID().toString();
    }

    public boolean checkUntilAttributeExist(String orderId, String shipmentId, String pathAttribute) {
        try {
            this.orderId = orderId;
            this.shipmentId = shipmentId;
            this.pathAttribute = pathAttribute;
            await().until(checkStatus(MODE_CHECK_ATTRIBUTE_SHIPMENT));
        } catch(Exception e) {
            return false;
        }

        return true;
    }


}
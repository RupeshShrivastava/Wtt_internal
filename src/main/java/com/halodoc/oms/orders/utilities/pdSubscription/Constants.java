package com.halodoc.oms.orders.utilities.pdSubscription;

public class Constants {


    public static final String
            USER_AGENT_CUSTOMER_APP = "HD customer app/9.900/android 11";

    public static final String WAKATOBI_CATALOG_BASE_URL = "http://wakatobi-catalog.stage-k8s.halodoc.com",
            WAKATOBI_SMS_BASE_URL= "http://wakatobi-sms.stage-k8s.halodoc.com",
            PHONONIX_URL="http://phoenix.stage-k8s.halodoc.com/v1/authentication/login",
            TIMOR_BASE_URL ="http://timor-oms.stage-k8s.halodoc.com/",
            SCROOGE_BASE_URL= "http://scrooge-payment.stage-k8s.halodoc.com/",
            KOMODO_BASE_URL="http://komodo.stage-k8s.halodoc.com/",
            CUSTOMER_STAGE="https://customers.stage.halodoc.com/";


    public static final String PHONIX_APP_TOKEN = "1dd7965f-f557-42fe-a2c0-1e8f63d8cf84",TIMOR_APP_TOKEN="202740cf-46d4-4f95-bc10-9443c6a28659",
            X_APP_TOKEN_WAKATOBI_CATALOG = "7f85f853-6851-4aa7-868d-4b9e7283a6c5",SUBCRIPTION_ENTITY_ID = "aeb4d479-add8-49f0-8dde-e27c04bb79eb";

    public static final String INTERNAL_PRODUCT_PATH = "/v1/internal/product",timor_order="v6/orders",timorV2order="v2/orders/",timorV1order="v1/orders/",timorV5order="v5/orders/",
            timorV1orderInternal="v1/orders/internal/",SHIPMENT_TRACK_EVENT="v1/shipments/internal/track/events",SHIPPER_TRACK_EVENT = "v2/shipments/internal/track/events",
            userInstrument="v1/payments/user-instrument/",
            PD_PAY_AMOUNT_BY_GOPAY_WALLET = "/charge",
            PD_CONFIRM_PAYMENT_ORDER = "/users/confirm",
            UPDATE_MULTIPLE_PHARMACY = "v1/orders/${pdOrderId}/update";


    public static final String VERSION = "v1/",SCHEME = VERSION+"schemes",BENEFIT = "benefits",SUBSCRIPTIONS ="subscriptions",PACKAGE = "package",
            product_subscription = "product-subscriptions";

    public static final String GET_PRODUCT_SUBSCRIPTION = VERSION+product_subscription,
            CANCEL_PRODUCT_SUBSCRIPTION =VERSION+product_subscription+"/cancel",
            RENEW_PRODUCT_SUBSCRIPTION =VERSION+product_subscription+"/renew";

    public static enum wakatobi{
        name,scheme_type,status,constant,accured,active,inactive,external_id,others,benefit_type,benefit_conditions_status,item,delivery,value_type,
        description,percentage
    }

    public static enum timor{
        entity_id,patient_id,cart_id,package_id,subscription_fulfilled_by,listing_id
    }
}

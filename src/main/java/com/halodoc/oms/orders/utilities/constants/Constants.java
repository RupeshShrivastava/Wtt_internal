package com.halodoc.oms.orders.utilities.constants;

import com.halodoc.oms.orders.utilities.LockUtil;
import lombok.Getter;

public class Constants {
    //Common constants
    public static final String PRICE = "123000";
    public static final String SHIPMENT_ID="52812";
    public static final String BASE_URL = "http://timor-oms.stage-k8s.halodoc.com:";
    public static final String PROD_BASE_URL="http://internal-c268e291-timor-timoromspro-eda5-1341999345.ap-southeast-1.elb.amazonaws.com:8010";
    public static final String OLD_BASE_URL = "http://garuda-golang-stage.int.halodoc.com:";
    public static final String NEW_BASE_URL="http://komodo.stage-k8s.halodoc.com:";
    public static final String BURU_BASEURL = "https://pharmacies-api.stage.halodoc.com:";
    public static final String CMS_BASEURL = "http://timor-cms.stage-k8s.halodoc.com";
    public static final String DERAWAN_BASEURL = "http://derawan-oms.stage-k8s.halodoc.com";

    public static final String WALLET_BASE_URL = "http://wallet-service.stage-k8s.halodoc.com:";
    public static final String KOMODO_BASE_URL = "http://komodo.stage-k8s.halodoc.com";
    public static final String FULFILLMENT_BASE_URL = "http://internal-86f42140-timor-timorfulfil-3d5c-1788276037.ap-southeast-1.elb.amazonaws.com:9125";
    public static final String CONTENT_TYPE = "application/json";
    public static final String OTP_CODE_URI = "/communication_requests?destination=%2B628234512347&channel=1&limit=1";
    public static final String PHARMACY_PORT_AUTH = "9130";
    public static final String CUSTOMER_PORT_AUTH = "";
    public static final String PAYMENT_PORT = "9260";
    public static final String USER_ENTITY_ID = "bb1b9841-f34f-4d60-b12c-e255c7025ffa";//Benfits: "3e81b81e-703e-454f-99b4-2e1eda1bce51";//"2aac38ad-2f93-41cb-b4e6-73852498fd7d";//"34d852d7-829c-48bb-bd34-3ccd6d7969a2";//"c4578bab-5160-4edd-b987-46d67bcc84fd";
    public static final String X_APP_TOKEN_PAYMENT = "f648d8b6-b43e-4561-91fd-5f874e7453ad";//"4c29b1d5-1849-41ad-8ca8-4177cc522714";
    public static final String DEPENDENT_PATIENT_ID = "69f978b6-8546-43b1-abc2-1eac41c3425b";//"04a512dd-3897-4654-8298-ed77ffd0223c";
    public static final String COMMON_JSON = "jsonFiles/commonFiles";
    public static final String FEEDBACK_JSON = "jsonFiles/commonFiles/feedback.json";
    public static final String X_APP_TOKEN_CMS = "3496d7a8-6732-4536-8aa5-2d24c5af8bb5";

    public static final String X_APP_WALLET = "ac1711ae-9480-4bd9-ae92-e5215512672b";
    public static final String WALLET_PORT = "";
    public static final String REAJECT_URI = "/reject";
    public static final String PROMOTION_URI = "/promotions";
    public static final String UPDATE_CART_URL = "/items/update";
    public static final String PRODUCT_LISTING = "4ecfb0d9-2a1f-47ef-b9ac-06cf106683e9";
    public static final String PRODUCT_LISTING_PHARMACY = "3be0ef01-9c8f-4069-82c2-048a22e4bca7";
    public static final String PRODUCT_LISTING_NOT_RED_MEDICINE = "3be0ef01-9c8f-4069-82c2-048a22e4bca7";

    public static final String PRODUCT_LISTING_SCHEDULED = "b73c4634-b0fa-4e8c-8c9f-d6414a704866";
    public static final String LEADS_DOC_UPLOAD="/src/main/resources/fixtures/leads/randomInput.png";
    public static final String RANDOMIVALID_FILE="/src/main/resources/fixtures/leads/test.xlsx";
    public static final String MULTI_GET_LEADS_DOC="/v1/leads/{leadId}/documents";

    //Timor Constants
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String TIMOR_ADD_FEEDBACK = "/v1/orders/{customerOrderId}/feedbacks";
    public static final String TIMOR_ADD_DELIVERYOPTION = "/v1/orders/{customerOrderId}/delivery-option";
    public static final String TIMOR_ADD_FEEDBACK_INTERNAL = "/v1/orders/internal/{customerOrderId}/feedbacks";

    public static final String TIMOR_ORDER_PORT = "";
    public static final String TIMOR_ORDER = "/v1/orders/";
    public static final String TIMOR_LEADS = "/v1/leads/";
    public static final String TIMOR_LEADS_DOCUMENTS="/v1/leads/{leadId}/documents";
    public static final String UPDATE_LEAD="/v2/leads/{leadId}";
    public static final String TIMOR_LEADS_SEARCH="/v1/leads/internal/search?start_lead_date=1599501600000";
    public static final String TIMOR_LEADS_INTERNAL = "internal/";
    public static final String TIMOR_LEADS_DOC_UPLOAD="/v2/leads/documents/upload";
    public static final String CONFIRM_ORDER_URI = "/users/confirm";
    public static final String SIMULATE_VA_XENDIT="https://api.xendit.co/callback_virtual_accounts/external_id=";
    public static final String AUTH_KEY_XENDIT="eG5kX2RldmVsb3BtZW50X0R1ZGxBcEF4d1BCTzVJTUFWSzQ2YlJ3NjFxRDZ5d0hRTkVrWkNoTzB6MWtWWTdqd3Z6Y3FHYVVpRzYxRjNGTUQ6";
    public static final String COOKIE_KEY="incap_ses_738_2182539=xsazAHsQ1DpP8Jcgguc9Ctg29WAAAAAAqFmDdUNxt9SYYRoV/Elbug==; incap_ses_740_2182539=4wLmKVrrHUSQkgcYdgJFCnZg8GAAAAAAgQnYvmf+fete2/X71+HDWw==; nlbi_2182539=pK14ELUqxATkyzEZjjCKbQAAAACaFjl0dRVNhhO6wWOi+6pd; visid_incap_2182539=4zSbodCATx6jdkd37fFYoLkvlGAAAAAAQUIPAAAAAACbpBHH4V44+XYl4OQsWJG7; visid_incap_2182539=f0A/0YK5Si+vVLjfyJ0aInuAyGAAAAAAQUIPAAAAAAC1+ovDWm/pYpVlP5mhbQio; incap_ses_704_2182539=jSJMN4ZFUQLqjvsyxRzFCRCQAWEAAAAASkCs2BoMG8DpEIyvVEXnGA==; incap_ses_715_2182539=L3s2Th3xh2MtS0OtFTHsCdnUA2EAAAAAx1mbhVJTQjQk9YyVl8sczw==";
    public static final String TIMOR_ORDER_INTERNAL = "/v2/orders/internal/";
    public static final String PAYMENT_REFRESH="/v2/orders/{customer_order_id}/payments/refresh";
    public static final String TIMOR_ORDER_INTERNAL_V1 = "/v1/orders/internal";
    public static final String MARK_ORDER_SHIPPED="/v1/orders/internal/{customerOrderId}/shipments/{shipmentId}/ship";
    public static final String MARK_ORDER_DELIVERED="/v1/orders/internal/{customerOrderId}/shipments/{shipmentId}/deliver";
    public static final String MARK_ORDER_CANCELLED="/v1/orders/{customerOrderId}/cancel";
    public static final String TIMOR_CHANGE_VERIFICATION_STATUS = "internal/{customerOrderId}/verification-status";
    public static final String RECENT_PRODUCTS="/v1/recent_products";
    public static final String TIMOR_V1_ORDER_SEARCH = "/v1/orders/search?statuses=new_order";
    public static final String V1_ORDER_SEARCH_ID = "3LRJX3-7527";
    public static final String BULK_ORDER_CANCELLATION_XML_FILE_PATH="/src/main/resources/Bulk_Order_cancellation.xlsx";

    public static final String PAYMENT_ENDPOINT = "http://internal-86f42140-scrooge-scroogepa-fdeb-515319052.ap-southeast-1.elb.amazonaws.com:";
    public static final String CANCEL_ORDER_URI = "/cancel";
    public static final String ABANDON_ORDER_URI = "/abandon";
    public static final String TRACK_ORDER_URI = "/track";
    public static final String PHONE_NUMBER_CUSTOMER = "+628802851919";//"Benefits user : +628281554866";//"+628852286172";//"+628234512347";
    public static final String PHONE_NUMBER_OTHER_CUSTOMER = "+628972209180";
    public static final String CUSTOMER_USER_AGENT = "HD customer app/9.201/android 9";
    public static final String X_APP_TOKEN_TIMOR = "202740cf-46d4-4f95-bc10-9443c6a28659";
    public static final String PROD_X_APP_TOKEN_TIMOR = "4017351e-1832-4d81-ba02-5935194ce8b8";
    public static final String JSON_LOCATION_TIMOR = "fixtures/timor";
    public static final String PAYMENT_INITIALISE_TIMOR = "/v1/payments/internal/initialise";

    //BURU Constants
    public static final String PHARMACY_PORT = "9130";
    public static final String ORDERS = "/v1/orders/";
    public static final String BURU_GROUP_URI = "/groups/";
    public static final String BURU_READY_URI = "/ready";
    public static final String BURU_DISPATCH_URI = "/dispatch";
    public static final String COMMUNICATION_REQUEST = "/communication_requests";
    public static final String GPID_PATH = "gpid";
    public static final String ACCESS_ROLES_PATH = "access_roles[0]";
    public static final String USER_ROLE_ADMIN = "admin";
    public static final String USER_ROLE_BUYER = "buyer";
    public static final String USER_ROLE_SELLER = "seller";
    public static final String OWNER_NAME_PENDING = "unmappedpendinguser";
    public static final String OWNER_NAME_ACTIVE = "activeuser";
    public static final String X_APP_TOKEN_GARUDA = "1c0de9a5-cf8b-417f-a9af-90ca37c7cee2";
    public static final String PHONE_NUMBER_PHARMACY = "+62812812812";
    public static final String PHONE_NUMBER_NEW_PHARMACY = "+628187109866";
    public static final String USER_PENDING_UNMAPPED_PHONE_NUMBER_PHARMACY = "+628283999840";
    public static final String USER_ACTIVE_MAPPED_ADMIN_GPID = "e3741167-2593-4ea9-b30c-cfaed4bbc3b4";
    public static final String USER_DELETE_RECOVER_GPID = "d0d0ae75-24a5-460a-995e-072879c89afa";
    public static final String USER_PENDING_UNMAPPED_GPID = "902fd065-14b1-4f4f-afaf-c9f4215b698c";
    public static final String USER_DELETED_PENDING_GPID = "d99839c6-cd99-4a25-8568-669b374d5ef5";
    public static final String USER_UNMAPPING_MAPPING_GPID = "00069555-cfb6-447d-8659-bb373f1e1d76";
    public static final String USER_DELETED_ACTIVE_GPID = "85df4bcc-4fb8-4598-b96d-63b5c9de5abc";
    public static final String USER_PENDING_UPDATE_GPID = "828d6c88-e8fb-41af-bf65-b91e3c0bc576";
    public static final String USER_PENDING_NO_MERCHANT_ID = "e0e9a168-467d-44e9-8460-5c6822371630";
    public static final String USER_ACTIVATE_GPID = "5d704fee-611b-4d83-a354-7cccf9c88d1d";
    public static final String USER_ROLE_GPID = "06089b63-0e72-4107-9972-8bc8426abe23";
    public static final String USER_ACCESS_ROLE_GPID = "ab30d769-8e07-4c04-baea-145f9ed43120";
    public static final String USER_DETAIL_MERCHANT_ID = "f66fe958-54c5-4ab3-af58-da11181afa32";
    public static final String USER_DETAIL_MERCHANT_LOCATION_ID = "6cbbcb99-b4f8-4be2-8c98-f4e485f5bd43";
    public static final String PHARMACY_USER_AGENT = "HD pharmacy app/5.900.0/android 9.0.0";
    public static final String JSON_LOCATION_BURU = "jsonFiles/buru";
    public static final String BURU_CONFIG="/v1/configs";
    public static final String BURU_GEO_VALIDATE="/v1/geo/validate?latitude=$lat&longitude=$long&channel=halodoc";
    public static final String BURU_SEARCH_ORDERS="/v1/internal/orders/search?customer_order_id={customer_order_id}";
    public static final String BURU_GET_PHARMACY_USERS="/v1/merchants/{merchant_id}/locations/{merchant_location_id}/users";
    public static final String BURU_XAPP_TOKEN="ac882c71-e2ce-4a77-b3fc-d474f2be3b8f";
    public static final String BURU_GET_USER_ROLE_URL = "/v1/internal/roles";
    public static final String BURU_GET_USER_DETAILS = "/v1/users/gpid/";
    public static final String BURU_UPDATE_USER_DETAILS = "/v1/internal/users/";
    public static final String BURU_UPDATE_USER_DETAILS_CC = "/v1/users/gpid/";
    public static final String[] BURU_ACTIVATE_USER = {"/v1/internal/users/","/activate"};
    public static final String[] BURU_DELETE_USER = {"/v1/internal/users/","/delete"};
    public static final String[] BURU_RECOVER_USER = {"/v1/internal/users/","/recover"};
    public static final String BURU_INTERNAL_USER_SEARCH = "/v1/internal/users?per_page=10&page_no=1&status={status}&deleted={delete}";
    public static final String BURU_INTERNAL_USER_SEARCH_USING_PHONE_NUMBER = "/v1/internal/users?per_page=10&page_no=1&status={status}&deleted={deleted}&phone_number={phoneNumber}";
    public static final String BURU_PRODUCTS="/v2/products?page_no={page_no}&per_page={per_page}&search_text={search_text}";
    public static final String BURU_INVENTORY_STOCK_SEARCH = "/v1/inventories?filter_type=out_of_stock&sort_type=desc&sort_by=name&per_page=2&check_procurement=true";
    public static final String BURU_PATIENTS="/v1/patients?phone_number={phone_number}";
    public static final String BURU_GET_PRODUCT_DETAILS_URI = "/v2/products/";
    public static final String BURU_ADD_SHIPMENT_ITEM_NOTES_URI = "/v1/distributor-orders/{customer_order_id}/shipments/{shipment_id}/items/{items_id}/notes";
    public static final String BURU_DELETE_SHIPMENT_ITEM_NOTES_URI = "/v1/distributor-orders/{customer_order_id}/shipments/{shipment_id}/items/{items_id}/notes";
    public static final String MERCHANTS_LOCATION_BUSINESSHOURS= "/v1/merchants/{merchantId}/locations/{merchantLocationId}/business_hours";
    public static final String MERCHANTS_LOCATION_BUSINESSHOURS_WITH_ID= "/v1/merchants/{merchantId}/locations/{merchantLocationId}/business_hours/{slotId}";
    public static final String GET_DISTRIBUTOR_SLUG = "/v1/internal/distributors/slugs/";
    public static final String DISTRIBUTOR_SLUG = "login-slug-image";
    public static final String DISTRIBUTOR_NAME = "Login slug img";
    public static final String DELIVERY_OPTIONS="/v2/orders/internal/{customer_order_id}/delivery-options";
    public static final String CREATE_BURU_ORDER_JSON ="create_buru_order.json";
    public static final String CONFIRM_BURU_ORDER_JSON ="confirm_buru_order.json";
    //Json files
    public static final String CREATE_ORDER_JSON = "create_pd_order.json";
    public static final String CREATE_MULTIPLE_PHAR_ORDER_JSON="create_multiple_phar_order.json";
    public static final String CREATE_MULTIPLE_PHAR_REALLOCATION_ORDER_JSON="create_multiple_phar_order_reallocation.json";
    public static final String DELIVERY_OPTIONS_MP ="delivery_options.json";
    public static final String UPDATE_CART_ITEMS_MP ="updateItems.json";
    public static final String CREATE_ORDER_ONE_MEDICINE = "create_pd_order_one_medicine.json";
    public static final String CREATE_ORDER_TWO_MEDICINE = "create_pd_order_two_medicine.json";
    public static final String CREATE_ORDER_FOR_REALLOCATION_JSON = "create_pd_order_for_reallocation.json";
    public static final String CREATE_LEAD_ORDER_JSON = "create_pd_lead_order.json";
    public static final String CREATE_LEAD_JSON = "create_lead.json";
    public static final String REJECT_LEAD_JSON = "reject_lead.json";
    public static final String UPDATE_LEAD_JSON="update_lead.json";
    public static final String MULTI_GET_DOCS_LEADS="multi_get_lead_doc.json";
    public static final String CONFIRM_ORDER_JSON = "confirm_pd_order.json";
    public static final String CONFIRM_VA_ORDER="confirm_pd_order_va.json";
    public static final String PAYMENT_CHARGE="xendit_charge.json";
    public static final String SIMULATE_VA_JSON="simulate_va.json";
    public static final String CANCEL_ORDER_JSON = "cancel_order.json";
    public static final String SYSTEM_CANCEL_ORDER_JSON = "system_cancel_order.json";
    public static final String REJECT_SHIPMENT_JSON = "reject_shipment.json";
    public static final String ABANDON_ORDER_JSON = "abandon_order.json";
    public static final String UPDATE_CART_JSON = "update_cart_valid.json";
    public static final String UPDATE_PATIENT_JSON = "update_patient.json";
    public static final String PAYMENT_INITIALISE_JSON = "paymentInitialise.json";
    public static final String CREDIT_WALLET_JSON = "creditWallet.json";
    public static final String PROMOTION_JSON = "promotions.json";
    public static final String DEL_OPTION_JSON ="deliveryOption.json";
    public static final String MERCHANT_LOCATION_JSON ="merchantLocation.json";
    public static final String MERCHANT_LOCATION_JSON_FALSE ="merchantLocationFalse.json";
    public static final String CREATE_ORDER_JSON_SCHEDULED = "create_scheduled_order.json";
    public static final String MERCHANT_MAPPING_JSON = "merchant_mapping.json";
    public static final String AUTO_APPLY_BIN_JSON = "auto_apply_bin.json";
    public static final String PAYMENT_TOKEN_JSON = "payment_token.json";
    public static final String KOMODO_CREATE_ORDER_JSON = "komodo_create_order.json";
    public static final String EMPTY_JSON = "empty.json";
    public static final String KOMODO_UPDATE_CART_JSON = "update_cart.json";
    public static final String KOMODO_CREATE_NATIONWIDE_ORDER_JSON = "komodo_create_nationwide_order.json";
    public static final String KOMODO_CREATE_NON_STORE_ORDER_JSON = "komodo_create_non_store_order.json";
    public static final String FULFILLMENT_CHECK_REALLOCATION_JSON = "fulfillment_check_reallocation.json";
    public static final String KOMODO_MEDICINE_FORM_SUBMIT_ONE_SYMPTOM_JSON = "komodo_medicine_form_one_symptom.json";
    public static final String KOMODO_MEDICINE_FORM_SUBMIT_THREE_SYMPTOM_JSON = "komodo_medicine_form_three_symptoms.json";
    public static final String KOMODO_MEDICINE_FORM_SUBMIT_OTHER_SYMPTOM_JSON = "komodo_medicine_form_other_symptom.json";
    public static final String CMS_GET_PRODUCT_DESCRIPTION = "get_product_description.json";
    public static final String SHIPMENT_ITEM_NOTES_JSON = "shipment_item_notes_buru.json";
    public static final String SHIPMENT_ITEM_NOTES_DERAWAN_JSON = "shipment_item_notes_derawan.json";
    public static final String BATCH_NO_JSON = "batch_no.json";
    public static final String REJECT_PAYMENT_JSON = "reject_payment.json";
    public static final String USER_UPDATE_VALID_JSON = "update_user_details.json";
    public static final String USER_UPDATE_THROUGH_CC_JSON = "update_user_details_CC.json";
    public static final String USER_UPDATE_ROLE_JSON = "update_user_role.json";
    public static final String USER_UPDATE_EMPTY_ROLE_JSON = "update_user_empty_role.json";


    //BIN-BASED
    public static final String TIMOR_ORDER_V3 = "/v3/orders/";
    public static final String TIMOR_ORDER_V4 = "/v4/orders/";
    public static final String AUTO_APPLY_BIN = "/v1/orders/{order_id}/promotions/auto";
    public static final String GENERATE_TOKEN = "/v2/orders/{order_id}/payments/token";
    public static final String GET_ORDER = "/v1/orders/{order_id}";
    public static final String INITIALIZE_PAYMENT = "/v1/orders/{order_id}/payments/initialise";
    public static final String CONFIRM_ORDER_BIN = "/v1/orders/{order_id}/users/confirm";
    public static final String CANCEL_ORDER_BIN = "/v1/orders/{order_id}/cancel";
    public static final String REFRESH_PAYMENTS = "/v1/orders/{order_id}/payments/refresh";

    //GoPay Tokenization
    public static final String PAYMENTS_URL = "http://scrooge-payment.stage-k8s.halodoc.com";
    public static final String GET_INSTRUMENT = "/v1/payments/user-instrument";
    public static final String OMS_CHARGE = "/v1/orders/{order_id}/payments/accounts/charge";
    public static final String DELIVERED_ORDER = "6YGK0Z-3325";
    public static final String ABANDONED_ORDER = "6YGK0Z-9585";
    public static final String INVALID_INSTRUMENT_ID = "123";
    public static final String INVALID_ORDER_ID = "6YGK0Z";
    public static final String PAYMENT_METHOD_VALID = "gopay";
    public static final String PAYMENT_METHOD1 = "card";
    public static final String PAYMENT_METHOD2 = "wallet";
    public static final String PAYMENT_METHOD3 = "cash";

    //OMSRegression
    public static final String PRODUCT_QUANTITY_ZERO = "8346dc17-3e19-46c4-9224-36df027f724d";
    public static final String DOCUMENT_ID = "292f673e-bef1-4310-b7eb-2f347a821784";
    public static final String DOCUMENT_URL = "9g86%2Fodvj0QqsdQ4oNinHf63dYhgV5ok8dOn0kdhaGx9At68vHNatrzB5Z11jg4D";
    public static final String INVALID_KEY = "INVALID";

    //Komodo
    public static final String KOMODO_GET_ORDER_URI = "/v2/orders/{customerOrderId}";
    public static final String KOMODO_ABANDON_ORDER_URI = "/v1/orders/{customerOrderId}/abandon";
    public static final String KOMODO_UPDATE_ITEM_URI = "/v1/orders/{customerOrderId}/items/update";
    public static final String KOMODO_GET_ORDER_DOCUMENT_URI = "/v1/orders/{customerOrderId}/documents";
    public static final String KOMODO_PROMO_URI = "/v1/orders/{customerOrderId}/promotions";
    public static final String KOMODO_CANCEL_ORDER_URI = "/v1/orders/{customerOrderId}/cancel";
    public static final String KOMODO_UPDATE_PATIENT_URI = "/v1/orders/{customerOrderId}/patients/update";
    public static final String KOMODO_GET_CATEGORY_PRODUCTS_URI = "/v1/categories/{categoryId}/products";
    public static final String KOMODO_GET_CATEGORY_URI = "/v1/categories/search";
    public static final String KOMODO_GET_STORES_URI = "/v1/stores";
    public static final String KOMODO_GET_STORE_PRODUCTS_URI = "/v1/stores/{storeId}/products";
    public static final String KOMODO_SEARCH_PRODUCT_URI = "/v1/products/search";
    public static final String KOMODO_GET_PRODUCT_URI = "/v1/products/{productId}";
    public static final String KOMODO_CONFIRM_ORDER_URI = "/v4/orders/{customerOrderId}/users/confirm";
    public static final String KOMODO_REALLOCATE_ORDER_URI = "/v1/orders/{customerOrderId}/reallocate";
    public static final String KOMODO_SUBMIT_MEDICINE_FORM_URI = "/v1/orders/{customerOrderId}/medicine-form";
    public static final String KOMODO_MEDICINE_FORM_OTHER_COMMENT = "Other related problems";
    public static final String KOMODO_RED_MEDICINE_ERROR_CODE = "3046";
    public static final String JSON_LOCATION_ORDER = "fixtures/orders";
    public static final String JSON_LOCATION_KOMODO = "fixtures/komodo";
    public static final String JSON_LOCATION_SEARCH = "jsonFiles/search";
    public static final String CURRENT_DATE_KEY = "current_date";
    public static final String LATITUDE_KEY = "latitude";
    public static final String LONGITUDE_KEY = "longitude";
    public static final String NOT_EQUAL_KEY = "not_equal";
    public static final String RESULT_KEY = "result";
    public static final String SEARCH_TEXT_KEY = "search_text";
    public static final String NAME_KEY = "name";
    public static final String PER_PAGE_KEY = "per_page";
    public static final String EQUAL_KEY = "equal";
    public static final String CATEGORY_KEY = "category";
    public static final String INSTANT_KEY = "instant";
    public static final String DELAYED_INSTANT_KEY = "delayed_instant";
    public static final String SCHEDULED_INSTANT_KEY = "scheduled_instant";
    public static final String ATTRIBUTE_ORDER_MERCHANT_ID_PATH = "attributes.merchant_id";
    public static final String ATTRIBUTE_ORDER_MERCHANT_LOCATION_ID_PATH = "attributes.merchant_location_id";
    public static final String ATTRIBUTE_ORDER_STORE_ID_PATH = "attributes.store_id";
    public static final String ATTRIBUTE_DELIVERY_TYPE_PATH = "attributes.delivery_type";
    public static final String STATUS_CREATED = "created";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_APPROVED = "approved";
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String STATUS_ON_HOLD = "on_hold";
    public static final String STATUS_DRAFT = "draft";
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_DELETED = "deleted";
    public static final String STATUS_ABANDONED = "abandoned";
    public static final String STATUS_DELIVERED = "delivered";
    public static final String STATUS_REQUEST_CANCEL = "cancellation_requested";
    public static final String STATUS_CANCEL = "cancelled";
    public static final String STATUS_PROCESSED = "processed";
    public static final String STATUS_TRANSIT = "in_transit";
    public static final String STATUS_COMPLETED = "completed";
    public static final String FULFILLMENT_TYPE_PARTIAL = "partial";
    public static final String FULFILLMENT_TYPE_PENDING = "pending";
    public static final String EMPTY_STRING = "";
    public static final String ORDER_ID_WITH_DOCUMENT = "XYWMM3-5323";
    public static final String COUPON_CODE = "TC00003";
    public static final String LATITUDE_VALUE = "-6.6488614";
    public static final String LONGITUDE_VALUE = "106.7309988";
    public static final String CATEGORY_ID = "724183f6-f901-4513-8167-8ae6286bdf7c";
    public static final String STORE_ID = "2693b7db-b680-47c0-ba00-5e7eced5ba45";
    public static final String PRODUCT_SEARCHED = "ACTAL";
    public static final String ACTAL_MEDICINE = "ACTAL PLUS TABLET";
    public static final String INVALID_ID = "724183f6-f901-4513-8167";
    public static final String DUPLICATE_CART_ID = "95fb5cc7-33c2-42c4-b6d2-7971a0ef0793";
    public static final String INVALID_ID_WITH_SPECIAL_CHARS = "724183f6-f901-4513-8167-*&^";
    public static final String CATEGORY_VALUE = "all";
    public static final String OTHERS_ENTITY_ID = "96be3ef4-04f6-4df0-95cd-a7e0d566ad15";
    public static final String OTHERS_ORDER_ID = "2Y5V8Y-0071";
    public static final String NATIONWIDE_PRODUCT_ID = "bebabb83-241e-4c20-9a5e-ee74a9386a6e";
    public static final String STORE_NATIONWIDE_PHARMACY_OPEN_ID = "ede7a7de-60d7-44ac-9bb7-6ce86a447527";
    public static final String LATITUDE_INSIDE_GEOZONE = "-6.221134";
    public static final String LONGITUDE_INSIDE_GEOZONE = "106.823715";
    public static final String MERCHANT_NATIONWIDE_ID = "5fd5f1a0-1c30-4df5-80b3-a24ee6bf78de";
    public static final String PHARMACY_ALWAYS_OPEN_ID = "69d46b88-d517-4df4-8838-aa1a1271b20e";
    public static final String STORE_NATIONWIDE_NON_INSTANT_PHARMACY_OPEN_ID = "621eb625-f643-4437-aed0-f441ff431759";
    public static final String PHARMACY_ALWAYS_OPEN_NON_INSTANT_ID = "ce69e906-2890-444f-ae0e-3793f8478ba8";
    public static final String STORE_NATIONWIDE_INSTANT_PHARMACY_CLOSE_ID = "04b5cff4-49cd-47e7-baf2-b9191c2f1352";
    public static final String PHARMACY_ALWAYS_CLOSE_ID = "bb730fb2-d549-4c18-8728-ddeebb80cd36";
    public static final String LATITUDE_OUTSIDE_GEOZONE = "0.501603";
    public static final String LONGITUDE_OUTSIDE_GEOZONE = "101.453190";
    public static final String STORE_NON_NATIONWIDE_ID = "b86a81db-203c-4d8d-8b34-559e583dafd2";
    public static final String PRODUCT_NO_REALLOC_ID = "bacd80db-9df2-409d-b7c5-eb43ab3000e7";
    public static final String STORE_INACTIVE_ID = "21946c6a-bfb4-42de-be90-801c5f9c1884";
    public static final String MESSAGE_CHECK_STATUS_FAILED = "check status failed";
    public static final String STORE_NON_NATIONWIDE_SCHEDULED_ID = "bc9bcfa1-d96b-4379-a2ee-b5b89af3dd72";
    public static final String PRODUCT_NO_REALLOC_SCHEDULED_ID = "c4bae13b-3625-41f0-9f62-f3305cbdfb86";
    public static final String PHARMACY_ALWAYS_OPEN_SCHEDULED_ID = "dbbe9bfd-4c78-4463-aff1-29261bd3ff85";
    public static final int PAGE_LIMIT = 5;

    public static final String payment_option_attribute_key="payment_option" ;
    public static final String payment_option="GOPAY_WALLET" ;
    public static final String user_instrument_id_attribute_key="user_instrument_id" ;
    public static final String merchantId = "1";
    public static final String merchant_locationId = "349";
    public static final String reallocating_merchantId ="3";
    public static final String reallocating_merchant_locationId ="3";

    //===============================SQL DB Queries================================================
    public static final String DB_PROPERTIES_FILE_PATH = "config/db.properties" ;
    public static final String SQL_PAYMENTS_BY_SERVICE_REF_ID = "select * from `payments` where `service_reference_id`='${service_reference_id}'";
    public static final String SQL_PAYMENT_ATTRIBUTES_BY_ID_AND_KEY = "select * from `payment_attributes` where `payment_id`='${payment_id}' and `attribute_key`='${attribute_key}'";

    //Fulfillment
    public static final String FULFILLMENT_CHECK_REALLOCATION_URI = "/v1/fulfilment/verify_reallocation_eligibility";
    public static final String INTERNAL_OMS_REALLOCATION_URI = "/{customerOrderId}/reallocate";
    public static final String JSON_LOCATION_FULFILLMENT = "fixtures/fulfillment";
    public static final String TYPE_CANCELLED_PHARMACY = "cancelled_pharmacy";
    public static final String TYPE_CANCELLED_DISTRIBUTOR = "cancelled_distributor";
    public static final String TYPE_MERCHANT_CANCELLED = "merchant_cancelled";
    public static final String X_APP_TOKEN_FULFILLMENT = "2755fc8d-7330-4f0c-8c8f-73499a8b2910";

    //Awaitility
    public static final String MODE_CHECK_STATUS = "mode_check_status";
    public static final String MODE_CHECK_LOCK = "mode_check_lock";
    public static final String MODE_CHECK_ATTRIBUTE_SHIPMENT = "mode_check_attribute_shipment";
    public static final String CONFIG_URI = "/v1/config";
    public static final String REDIS_ADDRESS = "redis://halodoc-stage-redis.int.halodoc.com:6379";
    public static LockUtil lockHelper;


    //Manual BIN
    public static final String MANUAL_BIN_JSON_FOLDER= "orders";
    public static final String APPLY_MANUAL_PROMO = "apply_manual_bin.json";
    public static final String GENERATE_TOKEN_MANUAL = "generate_token_manual.json";
    public static final String CONFIRM_ORDER_WALLET = "confirm_order_wallet.json";
    public static final String CONFIRM_ORDER_GOPAY = "confirm_order_gopay.json";
    public static final String GOPAY_CHARGE_JSON = "gopay_charge.json";
    public static final String INITIALIZE_PAYMENT_JSON = "initialise_payment_manual_bin.json";
    public static final String APPLY_MANUAL_COUPON = "/v2/orders/{order_id}/promotions";
    public static final String COUPON_VALID = "ICARD";
    public static final String COUPON_LOWER_CASE = "order";
    public static final String COUPON_NORMAL = "PDNORMAL";
    public static final String COUPON_EXPIRED = "ORDER_EXP";
    public static final String COUPON_INVALID = "@@@#$";
    public static final String COUPON_AUTO = "PDHIGH";
    public static final String COUPON_PROD_ID_NO_INT = "NINT_PID";
    public static final String COUPON_PROD_ID_INT = "INT_PID";
    public static final String METHOD_CARD = "card";
    public static final String METHOD_GOPAY = "gopay";
    public static final String payment_provider="midtrans" ;
    public static final String SERVICE_PD = "pharmacy_delivery";
    public static final String entity_type = "user";
    public static final String BIN_VISA = "481111";
    public static final String BIN_MASTER = "541011";
    public static final String CANCELLED_ORDER = "8Y2O63-3994";
    public static final String CONFIRMED_ORDER = "E06R39-6453";
    public static final String SHIPPED_ORDER = "D0V9M3-1357";
    public static final String GOPAY_PAYMENT_SUCCESSFUL = "success";
    public static final String currency="IDR";
    public static final String GOPAY_SERVICE_TYPE_PHARMACY_DEVLIVERY = "pharmacy_delivery";

    //CMS
    public static final String GET_PRODUCT_DESCRIPTION = "/v1/products/external/multi_get/attribute";
    public static final String UPDATE_MERCHANT_MAPPING_URL = "/v1/distributors/{distributor_id}/branches/{branch_id}/locations/{mapping_id}";

    //Medisend
    public static final String BURU_CREATE_MEDISEND_ORDER_URI = "/v1/distributor-orders";
    public static final String BURU_GET_MEDISEND_ORDER_URI = "/v1/distributor-orders/{order_id}";
    public static final String BURU_UPDATE_MEDISEND_ORDER_URI = "/v1/distributor-orders/{order_id}/items/update";
    public static final String BURU_ABANDON_MEDISEND_ORDER_URI = "/v1/distributor-orders/{order_id}/abandon";
    public static final String BURU_CONFIRM_MEDISEND_ORDER_URI = "/v1/distributor-orders/{order_id}/confirm";
    public static final String BURU_UPLOAD_DOCS_MEDISEND_ORDER_URI = "/v1/distributor-orders/{order_id}/shipments/{shipment_id}/documents/upload";
    public static final String BURU_GET_DOCS_MEDISEND_ORDER_URI = "/v1/distributor-orders/{order_id}/shipments/{shipment_id}/documents";
    public static final String BURU_REQUEST_CANCEL_MEDISEND_ORDER_URI = "/v1/distributor-orders/{order_id}/shipments/{shipment_id}/users/cancel";
    public static final String BURU_DELETE_DOCS_SHIPMENT_URI = "/v1/distributor-orders/{order_id}/shipments/{shipment_id}/documents/{docs_id}";
    public static final String DERAWAN_GET_MEDISEND_ORDER_DETAIL_URI = "/v1/internal/orders/{order_id}";
    public static final String DERAWAN_DELIVER_MEDISEND_ORDER_URI = "/v1/internal/orders/{order_id}/shipments/{shipment_id}/deliver";
    public static final String DERAWAN_CANCEL_MEDISEND_ORDER_URI = "/v1/internal/orders/{order_id}/shipments/{shipment_id}/cancel";
    public static final String DERAWAN_GET_MEDISEND_ORDER_URI = "/v1/internal/orders/search";
    public static final String DERAWAN_CREATE_MEDISEND_ORDER_NOTES_URI = "/v1/internal/orders/{order_id}/notes";
    public static final String DERAWAN_CONFIRM_MEDISEND_SHIPMENT_URI = "/v1/internal/orders/{order_id}/shipments/{shipment_id}/confirm";
    public static final String DERAWAN_TRANSIT_MEDISEND_SHIPMENT_URI = "/v1/internal/orders/{order_id}/shipments/{shipment_id}/transit";
    public static final String DERAWAN_COMPLETE_MEDISEND_SHIPMENT_URI = "/v1/internal/orders/{order_id}/shipments/{shipment_id}/complete";
    public static final String DERAWAN_UPDATE_STOCK_MEDISEND_SHIPMENT_URI = "/v1/internal/orders/{order_id}/shipments/{shipment_id}/items/update";
    public static final String DERAWAN_CREATE_CHILD_MEDISEND_SHIPMENT_URI = "/v1/internal/orders/{order_id}/shipments/partial";
    public static final String DERAWAN_SHIPMENT_ITEM_NOTES_URI = "/v1/internal/orders/{customer_order_id}/shipments/{shipments_id}/items/{items_id}/notes";
    public static final String DERAWAN_ADD_BATCH_NO_URI = "/v1/internal/orders/{order_id}/shipments/{shipment_id}/items/{entity_product_id}";
    public static final String DERAWAN_UPLOAD_DOCS_SHIPMENT_URI = "/v1/internal/orders/{order_id}/shipments/{shipment_id}/documents/upload";
    public static final String DERAWAN_DELETE_DOCS_SHIPMENT_URI = "/v1/internal/orders/{order_id}/shipments/{shipment_id}/documents/{docs_id}";
    public static final String DERAWAN_APPROVE_PAYMENT_URI = "/v1/internal/orders/{order_id}/shipments/{shipment_id}/paid-payment";
    public static final String DERAWAN_MEDISEND_ORDER="/v1/internal/orders/search";
    public static final String DERAWAN_REJECT_PAYMENT_URI = "/v1/internal/orders/{order_id}/shipments/{shipment_id}/reject-payment";
    public static final String CMS_MEDISEND_UPDATE_PRODUCT_URI = "/v1/distributors/{distributor_id}/branches/{branch_id}/products/{product_id}";
    public static final String CMS_MEDISEND_ATTRIBUTE_URL = "/v1/merchants/{merchant_id}/locations/{merchant_location_id}/attributes";
    public static final String JSON_LOCATION_MEDISEND = "fixtures/medisend";
    public static final String PRODUCT_JSON = "product.json";
    public static final String PROCUREMENT_JSON = "procurement.json";
    public static final String CANCEL_JSON = "cancel.json";
    public static final String CONFIRM_JSON = "confirm.json";
    public static final String NOTES_JSON = "notes.json";
    public static final String ENTITY_TYPE_PATH = "entity_type";
    public static final String TOTAL_PATH = "total";
    public static final String ITEMS_PATH = "items";
    public static final String STATUS_PATH = "status";
    public static final String MERCHANT_ID_PATH = "merchant_id";
    public static final String MERCHANT_LOCATION_ID_PATH = "merchant_location_id";
    public static final String PHONE_NUMBER_PATH = "phone_number";
    public static final String MAPPED_ENTITIES_PATH = "mapped_entities";
    public static final String MAPPED_PATH = "mapped";
    public static final String OWNER_NAME_PATH = "owner_name";
    public static final String FIRST_ARRAY_ELEMENT = "[0].";
    public static final String SECOND_ARRAY_ELEMENT = "[1].";
    public static final String THIRD_ARRAY_ELEMENT = "[2].";
    public static final String TITLE_PATH = "title";
    public static final String SHIPMENT_NOTES_PATH = "notes";
    public static final String SHIPMENTS_PATH = "shipments";
    public static final String COMMENTS_PATH = "comments";
    public static final String SELLING_PRICE_PATH = "selling_price";
    public static final String SHIPMENT_ITEMS_PATH = "shipment_items";
    public static final String QUANTITY_PATH = "quantity";
    public static final String REQUESTED_QUANTITY_PATH = "requested_quantity";
    public static final String LISTING_ID_PATH = "listing_id";
    public static final String BRANCH_ID_PATH = "distributor_entity_id";
    public static final String CUSTOMER_ORDER_ID_PATH = "customer_order_id";
    public static final String NAME_PATH = "name";
    public static final String DOCUMENT_ID_PATH = "document_id";
    public static final String RESULT_PATH = "result";
    public static final String ATTRIBUTES_PATH = "attributes";
    public static final String FULFILLMENT_TYPE_PATH = "fulfilment_type";
    public static final String EXTERNAL_ID_PATH = "external_id";
    public static final String CANCELLATION_REQUEST_TYPE_PATH = "cancellation_request_type";
    public static final String CANCELLATION_REQUEST_REASON_PATH = "cancellation_request_reason";
    public static final String CANCELLATION_REASON_PATH = "cancellation_reason";
    public static final String CANCELLATION_TYPE_PATH = "cancellation_type";
    public static final String PRICE_PATH = "price";
    public static final String RATE_STRATEGY_PATH = "rate_card_strategy";
    public static final String RATE_VALUE_PATH = "rate_card_value";
    public static final String ADJUSTMENT_PATH = "adjustments";
    public static final String TYPE_PATH = "type";
    public static final String VALUE_PATH = "value";
    public static final String BASE_PRICE_PATH = "base_price";
    public static final String BRANCH_MAPPING_PATH = "branch_mappings";
    public static final String RATE_CARDS_PATH = "rate_cards";
    public static final String ITEMS_REQUESTED_PRICE_PATH = "requested_price";
    public static final String ITEMS_APPLICABLE_RATE_PATH = "applicable_rate_cards";
    public static final String RATE_CARD_ID_PATH = "rate_card_id";
    public static final String PATH = ".";
    public static final String SHIPMENT_DISTRIBUTOR_ENTITY_NAME_PATH = "distributor_entity_name";
    public static final String BATCH_NO_PATH = "batch_no";
    public static final String EXPIRY_DATE_PATH = "expiry_date";
    public static final String SEGMENTATION_PATH = "segmentation";
    public static final String SELLING_UNIT_PATH = "selling_unit";
    public static final String SKU_ID_PATH = "sku_id";
    public static final String DISTRIBUTOR_CODE_PATH = "distributor_code";
    public static final String PHARMACY_REFERENCE_ID_PATH = "pharmacy_reference_id";
    public static final String PAYMENTS_PATH = "payments";
    public static final String DOCUMENTS_PATH = "documents";
    public static final String DOCUMENT_TYPE_PATH = "document_type";
    public static final String ERROR = "errors";
    public static final String MODE_EQUALS = "equals";
    public static final String MODE_NOT_EQUALS = "not_equals";
    public static final String MODE_CONTAINS = "contains";
    public static final String TYPE_CANCEL_USER = "cancelled_pharmacy";
    public static final String TYPE_CANCEL_CS = "cancelled_cs";
    public static final String TYPE_CANCEL_DISTRIBUTOR = "cancelled_distributor";
    public static final String CANCEL_REASON = "automation test reason cancelled";
    public static final String CANCEL_REASON_CUSTOM = "automation test reason cancelled custom";
    public static final String CANCEL_REASON_MORE_THAN_200CHARS = "automation test reason cancelled automation test reason cancelled automation test reason cancelled automation test reason cancelled automation test reason cancelled automation test reason cancelledauto";
    public static final String UPDATE_STOCK_REASON = "automation test reason update stock";
    public static final String TYPE_ORDER_NOTE_CS = "cs_note";
    public static final String ORDER_NOTE_COMMENT = "automation test comment";
    public static final String PHONE_NUMBER_MEDISEND = "+6281284555069";
    public static final String PHONE_NUMBER_MEDISEND_DISCOUNT_SETUP = "+628121127388";
    public static final String PHONE_NUMBER_MEDISEND_SAME_PHARMACY = "+628979019409";
    public static final String PHONE_NUMBER_MEDISEND_ACTIVE_PHARMACY = "+628244950394";
    public static final String PHONE_NUMBER_MEDISEND_INACTIVE_PHARMACY = "+6281224503151";
    public static final String PHONE_NUMBER_PENDING_USER = "+628283999840";
    public static final String CURRENCY_IDR = "IDR";
    public static final String MEDISEND_MERCHANT_INACTIVE_ID = "4381";
    public static final String MEDISEND_MERCHANT_LOCATION_INACTIVE_ID = "8757";
    public static final String DISTRIBUTOR_ID = "b8a47c98-4467-46f9-bb75-84d84a188f51";
    public static final String DISTRIBUTOR_ID_LOGIN_USER = "97076aa8-ad78-496c-adf2-6821c1deeb85";
    public static final String DISTRIBUTOR_BRANCH_LOGIN_USER = "c8da1f51-8389-4d64-a2f8-369dc31dc235";
    public static final String DISTRIBUTOR_ENTITY_MAPPING_EXTERNAL_LOGIN = "fe44c87d-f371-4ac2-b37a-69f7d4c32e2b";
    public static final String DISTRIBUTOR_ID_DISCOUNT = "83463fc4-1604-48a1-a335-a3d547a0a73c";
    public static final String DISTRIBUTOR_ID_MAI = "1acb5b61-09b1-495e-af3b-30f452b208e0";
    public static final String DISTRIBUTOR_ID_MBS = "93c2814a-9cc2-4dd7-b225-cc4a502e7bec";
    public static final String BRANCH_ACTIVE_NO_MINIMUM_ID_1 = "57d90803-6812-4e5d-bb1d-1059b7cc42ea";
    public static final String BRANCH_ACTIVE_NO_MINIMUM_ID_2 = "199c625a-8706-4b37-bb2a-0d3983762683";
    public static final String BRANCH_ACTIVE_WITH_MINIMUM_ID_1 = "6533c734-78d8-4c97-93b1-3b824ad518c3";
    public static final String BRANCH_INACTIVE_ID = "239324eb-3b58-4426-ab18-6d4c9e25197d";
    public static final String BRANCH_PARTIAL_FULFILLMENT_WITH_DISCOUNT = "46fffab3-1bfd-4321-a2e8-21e76337178a";
    public static final String BRANCH_WITH_DISCOUNT = "d00d7dcc-bbe3-4473-bd08-2213cb46a51d";
    public static final String BRANCH_MAI_ID = "484ccdd2-3310-4cb5-9045-757d432d3a8c";
    public static final String BRANCH_MBS_ID = "a8421cdd-64d5-4f64-a721-5b58c1a7941f";
    public static final String MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1 = "4ecfb0d9-2a1f-47ef-b9ac-06cf106683e9";
    public static final String MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2 = "df669afe-dfca-458c-acd4-f1181278f6ed";
    public static final String MEDISEND_PRODUCT_ACTIVE_UNAVAILABLE_ID_1 = "6f98fdb0-e3ee-4559-885a-d91222d6f7e7";
    public static final String MEDISEND_PRODUCT_ACTIVE_ROUNDUP_ID_1 = "d27448e9-6252-479f-9cb2-db04423554e2";
    public static final String MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_ID_1 = "8c3f8ffd-f8a6-49a3-927f-0a992b354a79";
    public static final String MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_ID_2 = "c6a2fa18-8d2a-4920-9d05-f492399e3df9";
    public static final String MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_ID_3 = "2a69a772-23ca-4b39-97f0-f472e25807e1";
    public static final String MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_MAPPING_ID_1 = "1ceb3c65-b763-473c-a8a9-54aa623b6bee";
    public static final String MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_MAPPING_ID_2 = "581737f8-8794-4922-a578-ec2140b0db2f";
    public static final String MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_MAPPING_ID_3 = "518161c4-f3f7-42aa-b7ae-bb703de7678c";
    public static final String MEDISEND_PRODUCT_INACTIVE_ID = "56a76d1f-5362-4ba5-adfe-d9e5726eb932";
    public static final String MEDISEND_PRODUCT_STATUS_ACTIVE_DISPLAY_FALSE_ID = "14d0ce29-ae5b-408b-bd78-a05b4c92bfb9";
    public static final String MEDISEND_PRODUCT_STATUS_INACTIVE_DISPLAY_TRUE_ID = "29a91ad1-b263-4243-8e40-2f2eef6796c4";
    public static final String MEDISEND_PRODUCT_ACTIVE_DISCOUNT = "cb232f15-9c71-4c11-ba6e-883b5e106a3a";
    public static final String MEDISEND_PRODUCT_PPO_ID = "5a48e8c4-8efc-4cd2-878e-abe5ca70e423";
    public static final String MEDISEND_PRODUCT_NON_PPO_ID = "08c4ef07-76bc-4b2a-a065-c1d026dca56f";
    public static final String MEDISEND_PRODUCT_MBS_ID = "4a2cb592-3a7b-4df9-8bd1-32220757672a";
    public static final String MEDISEND_INVALID_PRODUCT_MBS_ID = "aeeca122-f314-4a8b-97ba-df3450a3bb8c";
    public static final String MEDISEND_PRODUCT_UNAVAILABLE_MBS_ID = "0d8cd113-808b-4b31-80ee-9b9f1d83e000";
    public static final int REQUESTED_QUANTITY = 2;
    public static final int UPDATED_REQUESTED_QUANTITY = 1;
    public static final int MAX_REQUESTED_QUANTITY = 20;
    public static final int MAX_QUANTITY = 10;
    public static final int OUT_OF_STOCK_QUANTITY = 0;
    public static final int ABOVE_BASKET_SIZE_QUANTITY = 6;
    public static final int ZERO_QUANTITY = 0;
    public static final int RATE_VALUE_ZERO = 0;
    public static final int RATE_VALUE_SINGLE = 10;
    public static final int RATE_VALUE_MULTI = 20;
    public static final double BASE_PRICE = 10000.0;
    public static final int BASE_PRICE_CREATE_ORDER = 10000;
    public static final String ENTITY_TYPE_PHARMACY_USER = "pharmacy_user";
    public static final String MEDISEND_ORDER_ID = "WR6ZR1-6182";
    public static final String MEDISEND_ORDER_ID_WITH_DOCS = "WR6ZR1-1029";
    public static final String MEDISEND_SHIPMENT_ID_WITH_DOCS = "OY9WYD-3540";
    public static final String MEDISEND_SHIPMENT_DOCS_NAME = "8e174f2c-eaca-4546-980d-e200c34ef60d.png";
    public static final String MEDISEND_SHIPMENT_DOCS_ID = "5447048d-05cb-4b47-b18d-0622e1624d67";
    public static final String X_APP_TOKEN_DERAWAN = "e295e451-a2bf-42b0-8852-087ede2f7956";
    public static final String SEARCH_START_AT = "1607396400000";
    public static final String SEARCH_END_AT = "1607450400000";
    public static final String SEARCH_ENTITY_ID = "f9aef32f-3da7-46b1-9913-7d5ee7239fb2";
    public static final String SEARCH_ENTITY_TYPE = "pharmacy_user";
    public static final String SEARCH_ORDER_ID = "P3N4YM-4506";
    public static final String RATE_STRATEGY_PERCENTAGE = "percentage_discount";
    public static final String TYPE_TAX = "tax";
    public static final String TYPE_CONVENIENCE_FEE = "convenience_fee";
    public static final String DISCOUNT_SETUP_PRODUCT_ID = "cb232f15-9c71-4c11-ba6e-883b5e106a3a";
    public static final String DISCOUNT_SETUP_FIRST_BRANCH = "Automation Branch Backend - 1";
    public static final String DISCOUNT_SETUP_SECOND_BRANCH = "Automation Branch Backend - 2";
    public static final String DISCOUNT_SETUP_THIRD_BRANCH = "Automation Branch Backend - 3";
    public static final int NUMBER_OF_BRANCHES = 3;
    public static final double SELLING_PRICE_FOR_BRANCH = 8000.0;
    public static final int SELLING_PRICE_FOR_CREATE_ORDER = 8000;
    public static final String SEARCH_PRODUCT_DISCOUNT = "Gas";
    public static final int EXPECTED_TAX_VALUE = 1600;
    public static final String USER_NOTE = "user_note";
    public static final String SHIPMENT_NOTES_1 = "Automation Testing Notes";
    public static final String SHIPMENT_NOTES_2 = "Automation Testing Notes_2";

    public static final String SKU_ID_PRODUCT_1 = "21be1260-2af4-4db0-95fd-18eafd1e4eb1";
    public static final String SKU_ID_PRODUCT_2 = "3756608e-d4cf-467f-9ea4-d061269bc12e";
    public static final String SKU_ID_PRODUCT_3 = "a5c3018d-205c-4bc7-930b-b025bf679b0f";

    public static final String ACTION_TYPE_CANCEL_REQUEST = "cancel_requested";
    public static final String ACTION_TYPE_PARTIAL_FULFIL = "partial_fulfil";
    public static final String ACTION_TYPE_FAILED_ORDER = "failed_order";
    public static final String CREATED_AT_PATH = "created_at";
    public static final String ORDER_CREATION_FAILURE_PATH = "order_creation_failure";
    public static final String SORT_ORDER_ASC = "asc";
    public static final String CREATE_BATCH_NO = "CREATEBATCHNO";
    public static final String SEGMENTATION_RED = "red";
    public static final String SELLING_UNIT_BOX = "Box";
    public static final String SKU_ID_PRODUCT_NON_PPO = "SKUQA100013";
    public static final String SEGMENTATION_PPO = "ppo";
    public static final String SKU_ID_PRODUCT_PPO = "SKUQA100032";
    public static final String SEGMENTATION_CONSUMER_GOODS = "consumer goods";
    public static final String SKU_ID_MBS = "P71FCMAL";
    public static final String DISTRIBUTOR_CODE_MBS = "mbs";
    public static final String PHARMACY_REFERENCE_MBS_ID = "TGR1-1121";
    public static final String SKU_ID_INVALID_MBS = "SKUQA100014";
    public static final String SKU_ID_UNAVAILABLE_MBS = "SKUQA100100";

    public static final String FILE_TYPE_PNG = "image/png";
    public static final String FILE_TYPE_PDF = "application/pdf";
    public static final String FILE_TYPE_JPG = "image/jpg";
    public static final String FILE_TYPE_JPEG = "image/jpeg";
    public static final String FILE_TYPE_CSV = "text/csv";
    public static final String DOCUMENT_TYPE_INVOICE = "invoice_letter";
    public static final String DOCUMENT_TYPE_PROOFOFPAYMENT = "proof_payment";
    public static final String DOCUMENT_TYPE_PURCHASELETTER = "medisend_order_letter";
    public static final String DOCUMENT_TYPE_TAX = "taxation_letter";
    public static final String FILE_NAME_PNG = "sample.png";
    public static final String FILE_NAME_PDF = "sample.pdf";
    public static final String FILE_NAME_JPG = "sample.jpg";
    public static final String FILE_NAME_JPEG = "sample.jpg";
    public static final String FILE_NAME_CSV = "sample.csv";
    public static final String FILE_NAME_5MB = "sample5mb.jpg";
    public static final String FILE_NAME_MORETHAN5MB = "sampleGreaterThan5mb.jpg";
    public static final String STATUS_PAYMENT_UNPAID = "unpaid";
    public static final String STATUS_PAYMENT_PAID = "paid";
    public static final String STATUS_PAYMENT_WAITING_FOR_VERIFICATION = "waiting_verification";

    public static final int CUSTOM_DISCOUNT = 20;
    public static final int CUSTOM_PRICE = 2000;

    //Assert Error Message
    public static final String ORDER_CREATION_FAILURE = "FAILED TO CREATE ORDER";
    public static final String ORDER_CONFIRM_FAILURE = "FAILED TO CONFIRM ORDER";
    public static final String RESULT_MATCH_FAILURE = "FAILED TO MATCH ORDER DETAILS ";
    public static final String ORDER_CANCEL_FAILURE = "FAILED TO CANCEL ORDER";
    public static final String ORDER_GET_FAILURE = "FAILED TO GET ORDER";
    public static final String ORDER_DETAILS_FAILED_MESSAGE = "Following order details match failed: ";
    public static final String UPDATE_FAILURE = "FAILED TO UPDATE";
    public static final String INVALID_RESPONSE = "INVALID RESPONSE";

    //slack and S3 bucket url
    public static final String SLACK_HOOK = "https://hooks.slack.com/services/";
    public static final String S3_BUCKET_URL = "https://stage-test-automation-reports.s3-ap-southeast-1.amazonaws.com/oms-backend/";

    @Getter
    public enum MedisendOrderItem {
        ProductAvailable1BranchNoMinimum1(MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1),
        ProductAvailable2BranchNoMinimum1(MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1),
        ProductUnavailable1BranchNoMinimum1(MEDISEND_PRODUCT_ACTIVE_UNAVAILABLE_ID_1, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1),
        ProductMoreThanAvailable1BranchNoMinimum1(MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_ID_1, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1),
        ProductMoreThanAvailable2BranchNoMinimum1(MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_ID_2, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1),
        ProductMoreThanAvailable3BranchNoMinimum1(MEDISEND_PRODUCT_ACTIVE_REQUESTED_MORE_THAN_AVAILABLE_ID_3, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1),
        ProductInactiveBranchNoMinimum1(MEDISEND_PRODUCT_INACTIVE_ID, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1),
        ProductStatusActiveDisplayFalseBranchNoMinimum1(MEDISEND_PRODUCT_STATUS_ACTIVE_DISPLAY_FALSE_ID, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1),
        ProductStatusInactiveDisplayTrueBranchNoMinimum1(MEDISEND_PRODUCT_STATUS_INACTIVE_DISPLAY_TRUE_ID, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_1),
        ProductAvailable1BranchNoMinimum2(MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_2),
        ProductUnavailable1BranchNoMinimum2(MEDISEND_PRODUCT_ACTIVE_UNAVAILABLE_ID_1, DISTRIBUTOR_ID, BRANCH_ACTIVE_NO_MINIMUM_ID_2),
        ProductAvailable1BranchWithMinimum1(MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, DISTRIBUTOR_ID, BRANCH_ACTIVE_WITH_MINIMUM_ID_1),
        ProductUnavailable1BranchWithMinimum1(MEDISEND_PRODUCT_ACTIVE_UNAVAILABLE_ID_1, DISTRIBUTOR_ID, BRANCH_ACTIVE_WITH_MINIMUM_ID_1),
        ProductAvailable1BranchInactive(MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, DISTRIBUTOR_ID, BRANCH_INACTIVE_ID),
        ProductPartialFulfillmentDiscountSingleTier(MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_1, DISTRIBUTOR_ID, BRANCH_PARTIAL_FULFILLMENT_WITH_DISCOUNT),
        ProductPartialFulfillmentDiscountMultiTier(MEDISEND_PRODUCT_ACTIVE_AVAILABLE_ID_2, DISTRIBUTOR_ID, BRANCH_PARTIAL_FULFILLMENT_WITH_DISCOUNT),
        ProductPartialFulfillmentDiscountRoundUp(MEDISEND_PRODUCT_ACTIVE_ROUNDUP_ID_1, DISTRIBUTOR_ID, BRANCH_PARTIAL_FULFILLMENT_WITH_DISCOUNT),
        ProductAvailable1DiscountSingle(MEDISEND_PRODUCT_ACTIVE_DISCOUNT, DISTRIBUTOR_ID_DISCOUNT, BRANCH_WITH_DISCOUNT),
        ProductPPOBranchMAI(MEDISEND_PRODUCT_PPO_ID, DISTRIBUTOR_ID_MAI, BRANCH_MAI_ID),
        ProductNonPPOBranchMAI(MEDISEND_PRODUCT_NON_PPO_ID, DISTRIBUTOR_ID_MAI, BRANCH_MAI_ID),
        ProductAvailableBranchMBS(MEDISEND_PRODUCT_MBS_ID, DISTRIBUTOR_ID_MBS, BRANCH_MBS_ID),
        ProductInvalidBranchMBS(MEDISEND_INVALID_PRODUCT_MBS_ID, DISTRIBUTOR_ID_MBS, BRANCH_MBS_ID),
        ProductUnavailableBranchMBS(MEDISEND_PRODUCT_UNAVAILABLE_MBS_ID, DISTRIBUTOR_ID_MBS, BRANCH_MBS_ID);

        private final String listingId;
        private final String distributorId;
        private final String branchId;
        private int requestedQuantity;
        private int price;
        private int discount;

        MedisendOrderItem(String listingId, String distributorId, String branchId) {
            this.listingId = listingId;
            this.distributorId = distributorId;
            this.branchId = branchId;
        }

        public MedisendOrderItem getInstance(int requestedQuantity) {
            this.requestedQuantity = requestedQuantity;

            return this;
        }

        public MedisendOrderItem getInstance(int requestedQuantity, int price, int discount) {
            this.requestedQuantity = requestedQuantity;
            this.price = price;
            this.discount = discount;

            return this;
        }
    }
}
package com.halodoc.omstests;

public class Constants {

    public static final String user_agent = "HD customer app/8.701/android 10";

    public static final String FILE_LOCATION = "fixtures";
    public static final String X_APP_TOKEN = "6c44fcf2-fcd6-4630-81a1-676f47d41a36";
    public static final String OMS_X_APP_TOKEN = "37707323-0bf7-4c21-892c-df7da803c0f8";
    public static final String CMS_X_APP_TOKEN = "9057f23b-b6ca-4342-ad00-67048b77ed6a";
    public static final String X_APP_TOKEN_KARIMATA = "e8ea7213-58af-4c5c-acac-45e75a6b494a";
    public static final String GET_TOKEN_DETAILS="http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9150/v1/authentication/token/details";


    //status codes
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int OK_WITH_NO_CONTENT = 204;
    public static final int NOT_FOUND = 404;
    public static final int UNPROCESSABLE_ENTITY = 422;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHOURISED = 401;




    //garuda - to get otp code
    public static final String BASE_GARUDA_URL = "http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:4000";
    public static final String COMMUNICATION_REQUEST = "/communication_requests";
    public static final String GARUDA_X_APP_TOKEN = "1c0de9a5-cf8b-417f-a9af-90ca37c7cee2";


    public static final int TEST_GEO_ZONE_ID_01 = 1;
    public static final int TEST_GEO_ZONE_ID_02 = 2;
    public static final String INVALID_STRING = "12345";
    public static final String USER_AGENT_PATIENT = "HD customer app/99/android 9";
    public static final String USER_AGENT_PHARMACY = "HD customer app/99/android 9";
    public static final String CONTENT_TYPE = "application/json";

    public static final String BASE_FLORES_URL = "http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9150/v1";
    public static final String BASE_PHARMACY_URL = "http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9150/v1";
    public static final String REQUEST_OTP = "/authentication/otp/requests";
    public static final String VALIDATE_OTP = "/authentication/otp/validations";
    public static final String LOGOUT_USER = "/authentication/logout";

    public static final String ORDER_BASE_URL = "http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9150/v1";
    public static final String ORDERS = "/orders";
    public static final String ORDER_CONFIRM = "/orders/$order_id/users/confirm";
    public static final String ORDER_CANCEL = "/orders/$order_id/cancel";
    public static final String ORDER_ABANDON = "/orders/{order_id}/abandon";

    public static final String PHARMACY_ORDER_ASSIGN="http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9130/v1/orders/{order_id}/groups/{group_id}/assign";
    public static final String PHARMACY_ORDER_READY="http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9130/v1/orders/{order_id}/groups/{group_id}/ready";
    public static final String PHARMACY_ORDER_DISPATCH="http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9130/v1/orders/{order_id}/groups/{group_id}/dispatch";
    public static final String PHARMACY_ORDER_REJECT="http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9130/v1/orders/{order_id}/groups/{group_id}/reject";

    public static final String ORDERS_INTERNAL = "/orders/internal";
    public static final String ORDER_CONFIRM_INTERNAL = "/orders/internal/$order_id/users/confirm";


    public static final String UPLOAD_LEADS_DOC = "/leads/documents/upload";
    public static final String LEADS = "/leads";
    public static final String LEADS_INTERNAL = "/leads/internal";
    public static final String SEARCH_LEADS = "/leads/internal/search?start_lead_date=0";
    public static final String REJECT_LEAD = "/leads/internal/{lead_id}/reject";
    public static final String LEAD_NOTES = "/leads/internal/{lead_id}/notes";
    public static final String LEAD_DOCUMENTS = "/leads/internal/{lead_id}/documents";

    public static final String base_geo_url = "http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9150";
    public static final String geo_path = "/v1/geo/";
    public static final String geo_cache_path = "/v1/geo/cache";
    public static final String geo_cities_path = "/v1/geo/cities";

    public static final String order_base_url = "http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9130/";
    public static final String komodo_base_url = "http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9150/";
    public static final String karimata_base_url = "http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9210/";

    public static final String update_cart_url = "v1/orders/$orderId/items/update";

    public static final String promotions = "/orders/$orderId/promotions";

    public static final String campaign = "v1/campaigns";
    public static final String condition = "v1/conditions";
    public static final String camp_condition = "v1/campaigns/$campaign/conditions";
    public static final String coupon = "v1/campaigns/$campaign/coupons";
    public static final String coupon_condition = "v1/campaigns/$campaign/coupons/$coupon/conditions";

    //payment
    public static final String PAYMENT_URL="http://halodoc-api-staging.linkdokter.com";
    public static final String TOP_UP_PAYMENT="/payments/checkout.html?auth_token=qso1_zsn9voUQJd2Wn6hf";
    public static final String NOTIFY="/payments/notify";


    public static final String merchant_attributes="http://stage-elb-1767630364.ap-southeast-1.elb.amazonaws.com:9111/v1/merchants/310/locations/423/attributes";

    //----------------------------------------Multiple Images Support------------------------------------------------------
    public static final String IMAGES_TYPE_THUMB_NAIL_URL = "thumbnail_url" ;
    public static final String IMAGES_TYPE_IMAGE_URL = "image_url" ;
    public static final String PRODUCT_SEARCH_TEXT = "kkpravee_automation_product" ;
    public static final String INVALID_SEARCH_TEXT = "$%^&*#@$!" ;

}

package com.halodoc.oms.properties;

public class HalodocProperties {
    public static PropertyReader propertyReader = new PropertyReader();

    public static final String DB_USER = propertyReader.getDbUserName();
    public static final String DB_PASSWORD = propertyReader.getDbPassword();
    public static final String WAKATOBI_CATALOG_DB_URL = propertyReader.getWakatobiCatalogServiceDb();

}

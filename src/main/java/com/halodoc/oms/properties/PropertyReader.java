package com.halodoc.oms.properties;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.testng.Reporter;

public class PropertyReader {

    private Properties properties;

    PropertyReader() {
        properties = new Properties();
        try {
            FileReader reader=new FileReader(System.getProperty("user.dir")+"/src/main/resources/config/db.properties");
            properties.load(reader);
        } catch (IOException e) {
            Reporter.log(e.getMessage(),true);
        }
    }

    public String getDbUserName() { return properties.getProperty("db_userName");}

    public String getDbPassword() { return properties.getProperty("db_password");}

    public String getWakatobiCatalogServiceDb() { return properties.getProperty("wakatobiCatalogServiceDb");}

}

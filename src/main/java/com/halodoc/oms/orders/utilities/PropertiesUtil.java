package com.halodoc.oms.orders.utilities;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    @SneakyThrows
    public void setProperties(){
       try {
           System.out.println("STUFF  "+System.getProperty("user.dir")+"/src/main/resources/oms.properties");
    InputStream input = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/oms.properties");
        Properties prop = new Properties();
        prop.load(input);
        System.setProperties(prop);
        String noOfOrders=prop.getProperty("no_ofOrders");
        System.setProperty("no_ofOrders",noOfOrders);
       } catch (Exception ex) {
        ex.printStackTrace();
    }
    }
}

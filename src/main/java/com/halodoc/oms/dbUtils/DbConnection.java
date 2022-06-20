package com.halodoc.oms.dbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.halodoc.oms.properties.HalodocProperties.*;

public class DbConnection {

    private static Connection wakaTobiCatalogDbCon ;
    private static DbConnection dbIsntance;



    private DbConnection() {
    }

    public static DbConnection getInstance(){
        if(dbIsntance==null){
            dbIsntance= new DbConnection();
        }
        return dbIsntance;
    }


    public Connection getWakaTobiCatalogDbConnection() {
        if (wakaTobiCatalogDbCon == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                wakaTobiCatalogDbCon = DriverManager.getConnection(WAKATOBI_CATALOG_DB_URL,DB_USER,DB_PASSWORD);
            } catch (Exception ex) {
                Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return wakaTobiCatalogDbCon;
    }

    public void closeConnection(){
        try {
            if(wakaTobiCatalogDbCon!=null)
                wakaTobiCatalogDbCon.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

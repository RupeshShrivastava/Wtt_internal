package com.halodoc.oms.orders.utilities;

import com.halodoc.utils.jdbc.JDBCUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.halodoc.oms.orders.utilities.constants.Constants.DB_PROPERTIES_FILE_PATH;

/**
 * @author praveenkumar
 * This class contains the required sql query execution methods.
 */
@Slf4j
public class DbUtils extends JDBCUtil {
    public static DbUtils dbUtilsInstance = null;
    protected Connection conn;
    private static String DB_URL, DB_SCHEMA_PAYMENTS, DB_USERNAME_PAYMENTS, DB_PASSWORD_PAYMENTS ;

    /*private static void loadDbProperties(){
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try  {
            InputStream resourceStream = loader.getResourceAsStream(DB_PROPERTIES_FILE_PATH);
            prop.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DB_URL = prop.getProperty("db.url") ;
        DB_SCHEMA_PAYMENTS = prop.getProperty("db.schema.scrooge.payment") ;
        DB_USERNAME_PAYMENTS = prop.getProperty("db.username.scrooge.payment") ;
        DB_PASSWORD_PAYMENTS = prop.getProperty("db.password.scrooge.payment") ;
    }*/

    private DbUtils(String url, String username, String password) {
        conn = JDBCUtil(url, username, password);
    }

    public static DbUtils getDbUtilsInstance() {
        //loadDbProperties() ;
        if (dbUtilsInstance == null ) {
            dbUtilsInstance = new DbUtils(DB_URL+DB_SCHEMA_PAYMENTS, DB_USERNAME_PAYMENTS, DB_PASSWORD_PAYMENTS) ;
            log.info("jdbc connection establishment is success!");
            return dbUtilsInstance ;
        } else
            return dbUtilsInstance;
    }

    public ArrayList<Map<String, Object>> getDbDataByQuery(String getQuery) {
        ArrayList<Map<String, Object>> queryResult = new ArrayList<>();
        try {
            ResultSet rs = executeGetQuery(getQuery, conn);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columncount = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columncount; i++) {
                    row.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                queryResult.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Exception caught while executing Query to fetching db data: " + e);
            e.printStackTrace();
        }
        return queryResult;
    }

    public void closeDbConnection() {
        closeDbConnection(conn);
    }
}

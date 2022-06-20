package com.halodoc.oms.dbUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.testng.Reporter;

public class DbQueryExecution {


    public void updateStageDbData(String query){
        Connection con = DbConnection.getInstance().getWakaTobiCatalogDbConnection();
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            Reporter.log(e.getMessage(),true);
        }
    }


}

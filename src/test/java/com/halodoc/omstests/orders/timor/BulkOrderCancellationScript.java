package com.halodoc.omstests.orders.timor;

import static com.halodoc.oms.orders.utilities.constants.Constants.BULK_ORDER_CANCELLATION_XML_FILE_PATH;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import com.halodoc.oms.orders.library.BaseUtil;
import com.halodoc.oms.orders.utilities.timor.TimorUtil;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BulkOrderCancellationScript extends BaseUtil {
    public TimorUtil timorHelper;

    public BulkOrderCancellationScript() {
        timorHelper = new TimorUtil();
    }
    @Test
    public void bulkOrderCancellation(){
        String path =System.getProperty("user.dir")+BULK_ORDER_CANCELLATION_XML_FILE_PATH;
        System.out.println("path->"+path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = workbook.getSheet("Sheet1");
        int lastRow = sheet.getLastRowNum();
        for(int i=1; i <= lastRow; i++)
        {
            String order_id=sheet.getRow(i).getCell(0)+"";
            Response response = timorHelper.systemCancelOrder(order_id,true);
            Row row1 = sheet.getRow(i);
            Cell cell = row1.createCell(1);
            if(response.getStatusCode()==204){
                cell.setCellValue("cancelled");
                log.info("----"+i+"-----"+order_id+"----------cancelled");
            }
            if(response.getStatusCode()==422) {
                {
                    cell.setCellValue("Multiple shipment,shipment wise cancelled");
                    log.info("----" + i + "----" + order_id + "----------Multiple shipment,shipment wise cancelled");
                }
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
    }
    }


}

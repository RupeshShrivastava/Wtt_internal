package com.halodoc.omstests.orders.timor;

import com.halodoc.oms.orders.library.BaseUtil;
import com.halodoc.oms.orders.utilities.timor.TimorUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;

@Slf4j
public class GetAuthCode {
    public TimorUtil timorHelper;
    public BaseUtil baseUtil = new BaseUtil();

    public GetAuthCode() throws FileNotFoundException {
    }

    public BaseUtil getBaseUtil() {
        return baseUtil;
    }

    @Test
    public void getAuthCode() throws IOException {
        BaseUtil baseUtil = getBaseUtil();
        List<String> usersInput = readFromCSV();
        List<String> outPut = new ArrayList<>();
        for (String input : usersInput) {
            String temp = baseUtil.getAccessTokenFromGaruda(input, CUSTOMER_PORT_AUTH, CUSTOMER_USER_AGENT, OLD_BASE_URL);
            if (temp != null) {
                outPut.add(temp);
            }

        }
        createCsv(outPut);
    }

    public void createCsv(List<String> sample) throws IOException {
        FileWriter writer = new FileWriter(System.getProperty("user.dir") + "/src/main/resources/fixtures/timor/" + "outPut.csv");
        String collect = sample.stream().collect(Collectors.joining("\n"));
        writer.write(collect);
        writer.close();

    }

    public List<String> readFromCSV() throws IOException {
        List<String> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/main/resources/fixtures/timor/" + "input.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\n");
                records.add(values[0]);
            }
        }
        return records;
    }

}

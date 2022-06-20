package com.halodoc.oms.orders.utilities.payments;

import com.halodoc.utils.http.RestClient;
import io.restassured.response.Response;

import java.util.HashMap;

import static com.halodoc.oms.orders.utilities.constants.Constants.*;

public class Instrument {

    private static String url;

        public static Response getInstrument(HashMap headers) {
            url = PAYMENTS_URL + GET_INSTRUMENT;
            RestClient client = new RestClient(url, headers);
            return client.executeGet();
        }
}

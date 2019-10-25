package com.guce.util;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpParams;

/**
 * Created by chengen.gu on 2017/7/15.
 */
@SuppressWarnings("all")
public class CustomerHttpClient {
    private static DefaultHttpClient customerHttpClient;

    private CustomerHttpClient() {
    }

    public static HttpClient getHttpClient(PoolingClientConnectionManager cm, HttpParams httpParams) {
        if (null == customerHttpClient) {
            customerHttpClient = new DefaultHttpClient(cm, httpParams);
        }
        return customerHttpClient;
    }
}

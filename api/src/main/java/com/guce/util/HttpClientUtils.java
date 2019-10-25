package com.guce.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.pool.PoolStats;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chengen.gu on 2017/7/15.
 */
public class HttpClientUtils {
    private static final long WARN_TIME = 800;

    private static final String _127_0_0_1 = "127.0.0.1";

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    public final static String DEFAULT_CHARSET = "UTF-8";

    public static final int DEFAULT_TIMEOUT = 5;

    /**
     * 请求超时时间
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = DEFAULT_TIMEOUT * 1000;

    /**
     * 响应超时时间
     */
    public static final int DEFAULT_SOCKET_TIMEOUT = DEFAULT_TIMEOUT * 1000;

    public static final ThreadLocal<String> ip_session = new ThreadLocal<String>();

    private static PoolingClientConnectionManager cm = null;

    static {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        // TODO should we support https?
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        cm = new PoolingClientConnectionManager(schemeRegistry);
        // Increase max total connection to 200
        //设置连接池的最大数量
        //cm.setMaxTotal(3000);
        cm.setMaxTotal(5000);
        // Increase default max connection per route to 20
        //设置
        //cm.setDefaultMaxPerRoute(450);
        cm.setDefaultMaxPerRoute(1000);
        // Increase max connections for localhost:80 to 50
        // cm.setMaxPerRoute(miRoute, 200);
        // cm.setMaxPerRoute(msgRoute, 200);
    }

    static {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        X509TrustManager manager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };

        SSLContext context = null;
        try {
            context = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            logger.error("getInstance erorr", e);
        }

        try {
            context.init(null, new TrustManager[] { manager }, new SecureRandom());
        } catch (KeyManagementException e) {
            logger.error("init ssl context error", e);
        }

        SSLSocketFactory socketFactory = new SSLSocketFactory(context, SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

        schemeRegistry.register(new Scheme("https", 443, socketFactory));// SSLSocketFactory.getSocketFactory()));
        cm = new PoolingClientConnectionManager(schemeRegistry);
        // Increase max total connection to 200
        //cm.setMaxTotal(3000);
        cm.setMaxTotal(5000);
        // Increase default max connection per route to 20
        //cm.setDefaultMaxPerRoute(450);
        cm.setDefaultMaxPerRoute(1000);
        // Increase max connections for localhost:80 to 50
        // cm.setMaxPerRoute(miRoute, 200);
        // cm.setMaxPerRoute(msgRoute, 200);

    }

    /**
     * 得到HttpClient方法
     *
     * @return
     */
    public static HttpClient getHttpClient(int timeOut) {
        HttpParams httpParams = new BasicHttpParams();

        // 连接建立时的毫秒级超时时间
        HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_CONNECTION_TIMEOUT);

        // 套接字的毫秒级超时时间 请求超时
        HttpConnectionParams.setSoTimeout(httpParams, timeOut > 0 ? timeOut * 1000 : DEFAULT_SOCKET_TIMEOUT);

        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

        HttpClientParams.setRedirecting(httpParams, true);

        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";

        HttpProtocolParams.setUserAgent(httpParams, userAgent);

        // return new DefaultHttpClient(httpParams);
        // 通过连接池返回
        //DefaultHttpClient httpClient = new DefaultHttpClient(cm, httpParams);

        DefaultHttpClient httpClient = (DefaultHttpClient) CustomerHttpClient.getHttpClient(cm, httpParams);

        // logConnectionStats();
        return httpClient;
    }

    /**
     * 执行doGet方法
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        // 使用工具默认编码
        return doGet(url, DEFAULT_CHARSET);
    }

    public static String doGet(String url, String charset) throws Exception {
        return doGet(url, charset, DEFAULT_TIMEOUT);
    }

    public static String doGet(String url, int timeout) throws Exception {
        return doGet(url, DEFAULT_CHARSET, timeout);
    }

    public static String doGet(String url, String charset, int timeout) throws Exception {
        return doGet(url, charset, timeout, true);
    }

    public static String doGet(String url, String charset, int timeout, boolean isLog) throws Exception {
        String responseString = "";
        HttpResponse httpResponse = null;
        HttpClient httpClient = null;
        HttpGet httpRequest = null;
        /* 发送请求并等待响应 */
        try {
            long start = System.currentTimeMillis();
            httpClient = getHttpClient(timeout);
            httpRequest = new HttpGet(url);
            // specify some common header, like IP, reference, accept
            updateCommonHeader(httpRequest);

            httpResponse = httpClient.execute(httpRequest);

            // 查看响应状态 如果不是正常状态 则主动abort请求并返回
            if (isSuccess(httpResponse.getStatusLine().getStatusCode()) == false) {
                httpRequest.abort();
                if (isLog) {
                    logger.warn("target return error status, the url is :" + url + " status code: "
                            + httpResponse.getStatusLine().getStatusCode() + "response: "
                            + new String(getResponseEntityAsByteArray(httpResponse)));
                }
                return null;
            }

            if (httpResponse.getEntity() != null) {
                byte[] bytes = getResponseEntityAsByteArray(httpResponse);
                if (isLog) {
                    if (logger.isInfoEnabled()) {
                        String responseMinStr = logHttpResponse(bytes, charset);
                        logger.debug("HTTP GET : " + url + ",response is [" + responseMinStr + "],length is ["
                                + (bytes != null ? bytes.length : 0) + "]");
                    }
                }
                responseString = new String(bytes, charset);
                responseString = filterHtml(responseString);

                if (isLog) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("get to " + url + ", response: " + responseString);
                    }
                }
            }
            long offset = System.currentTimeMillis() - start;
            if (offset > 800L)
                logConnectionStats(offset, url);
        } catch (Exception e) {
            if (e instanceof IOException || e instanceof ClientProtocolException) {
                if (isLog) {
                    logger.error("get to " + url + ", encountering " + e.getMessage());
                }
            } else {
                if (isLog) {
                    logger.error("get to " + url, e);
                }
            }

            if (httpRequest != null) {
                httpRequest.abort();
            }
            throw e;
        }finally{
            if(httpRequest != null) {
                httpRequest.releaseConnection();
            }
        }
        return responseString;
    }

    /**
     *
     * @param url
     * @param headerMap
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> headerMap) throws Exception {
        return doGet(url, headerMap, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
    }

    /**
     *
     * @param url
     * @param headerMap
     * @param charset
     * @param timeout
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> headerMap, String charset, int timeout)
            throws Exception {
        String strResult = "";
        HttpResponse httpResponse = null;
        HttpClient httpClient = null;
        HttpGet httpRequest = null;
        try {

            long start = System.currentTimeMillis();

            httpClient = getHttpClient(timeout);

            httpRequest = new HttpGet(url);

            updateCommonHeader(httpRequest);

            addHeaderForHttpRequestBase(httpRequest, headerMap);

            httpResponse = httpClient.execute(httpRequest);

            if (!isSuccess(httpResponse.getStatusLine().getStatusCode())) {
                httpRequest.abort();
                logger.warn("target return error status, the url is :" + url);
                return null;
            }
            if (httpResponse.getEntity() != null) {
                byte[] bytes = getResponseEntityAsByteArray(httpResponse);

                if (logger.isInfoEnabled()) {
                    String responseMinStr = logHttpResponse(bytes, charset);
                    logger.debug("HTTP GET : " + url + ",response is [" + responseMinStr + "],length is ["
                            + (bytes != null ? bytes.length : 0) + "]");
                }

                strResult = new String(bytes, charset);

                strResult = filterHtml(strResult);
            }
            long offset = System.currentTimeMillis() - start;
            if (offset > 800L)
                logConnectionStats(offset, url);
        } catch (Exception e) {
            logger.error("http get error with url " + url, e);
            if (httpRequest != null) {
                httpRequest.abort();
            }
            throw e;
        }
        return strResult;
    }

    /**
     *
     *
     * @param imageApiUrl
     * @param param
     *@param url
     * @param i  @return
     */
    /*public static String doPost(String imageApiUrl, Map<String, String> param, String url, int i) {
        return doPost(url, null, DEFAULT_CHARSET);
    }*/

    /**
     *
     * @param url
     * @param content
     * @return
     */
    public static String doPost(String url, String content) {
        return doPost(url, content, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
    }

    /**
     *
     * @param url
     * @param content
     * @param charset
     * @param timeout
     * @return
     */
    public static String doPost(String url, String content, String charset, int timeout) {
        return doPost(url, content, null, charset, timeout);
    }

    /**
     * post request by specified url & parameters
     *
     * @param url
     * @param parameters
     * @return
     */
    public static String doPost(String url, Map<String, Object> parameters) {
        return doPost(url, parameters, DEFAULT_CHARSET);
    }

    /**
     * post request by specified url, charset & parameters
     *
     * @param url
     * @param charset
     * @param parameters
     * @return
     */
    public static String doPost(String url, Map<String, Object> parameters, String charset) {
        return doPost(url, parameters, charset, DEFAULT_TIMEOUT);
    }

    /**
     *
     * @param url
     * @param parameters
     * @param charset
     * @param timeout
     * @return
     */
    public static String doPost(String url, Map<String, Object> parameters, String charset, int timeout) {
        return doPost(url, parameters, null, charset, timeout);
    }

    /**
     * post request by specified url, parameters, and headers
     *
     * @param url
     * @param parameters
     * @param headerMap
     * @return
     */
    public static String doPost(String url, Map<String, Object> parameters, Map<String, String> headerMap) {
        return doPost(url, parameters, headerMap, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
    }

    /**
     *
     * @param url
     * @param content
     * @param headerMap
     * @param charset
     * @param timeout
     * @return
     */
    public static String doPost(String url, String content, Map<String, String> headerMap, String charset,
                                int timeout) {
        if (logger.isInfoEnabled())
            logger.debug("HTTP_post-{}| content: {}, header: {}" + "->" + url + "->" + content + "->" + JSON.toJSONString(headerMap));
        HttpClient httpClient = getHttpClient(timeout);
        HttpPost httpRequest = new HttpPost(url);

        try {
            HttpEntity repEntity = new StringEntity(content, charset);
            httpRequest.setEntity(repEntity);

            // set request headers
            addHeaderForHttpRequestBase(httpRequest, headerMap);

            return doPost(httpClient, httpRequest);
        } catch (Exception e) {
            logger.error("HTTP_post-{} - {}| content: {}, header: {}"
                    + "->" + url + "->"
                    + e.getMessage() + "->"
                    + content + "->"
                    + JSON.toJSONString(headerMap) + "->"
                    + (e instanceof UnsupportedEncodingException ? null : e));

        }

        return null;
    }

    /**
     *
     * @param url
     * @param parameters
     * @param headerMap
     * @param charset
     * @param timeout
     * @return
     */
    public static String doPost(String url, Map<String, Object> parameters, Map<String, String> headerMap,
                                String charset, int timeout) {
        if (logger.isInfoEnabled()) {
            logger.debug("post url: " + url + " param:"
                    + JSON.toJSONString(parameters) + " header: "
                    + JSON.toJSONString(headerMap));
        }
        HttpClient httpClient = getHttpClient(timeout);
        HttpPost httpRequest = new HttpPost(url);
        // set request parameters
        if (parameters != null && !parameters.isEmpty()) {
            List<NameValuePair> params = new ArrayList<NameValuePair>(parameters.size());
            for (String key : parameters.keySet()) {
                params.add(new BasicNameValuePair(key, String.valueOf(parameters.get(key))));
            }
            try {
                httpRequest.setEntity(new UrlEncodedFormEntity(params, DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException e) {
                logger.warn("encode parameter error, " + " post to " + url
                        + " param:" + JSON.toJSONString(parameters)
                        + " header: " + JSON.toJSONString(parameters));
            }
        }

        // set request headers
        addHeaderForHttpRequestBase(httpRequest, headerMap);

        try {
            String responseString = doPost(httpClient, httpRequest);
            if (logger.isDebugEnabled())
                logger.debug("post to " + url + " param:"
                        + JSON.toJSONString(parameters) + " header: "
                        + JSON.toJSONString(parameters)
                        + ", response: " + responseString);
            return responseString;
        } catch (Exception e) {
            if (e instanceof IOException || e instanceof ClientProtocolException)
                logger.warn("post to " + url + " param:"
                        + JSON.toJSONString(parameters) + " header: "
                        + JSON.toJSONString(headerMap)
                        + ", encountering " + e.getMessage());
            else
                logger.warn("post to " + url + " param:"
                        + JSON.toJSONString(parameters) + " header: "
                        + JSON.toJSONString(headerMap), e);
        }
        return null;
    }

    /**
     *
     * @param url
     * @param parameters
     * @param headerMap
     * @param charset
     * @param timeout
     * @return
     */
    public static String doMultipartPost(String url, Map<String, Object> parameters, Map<String, String> headerMap,
                                         String charset, int timeout) {
        if (logger.isInfoEnabled())
            logger.debug("HTTP_post-{}| content: {}, header: {}"
                    + url + "->"
                    + parameters + "->"
                    + JSON.toJSONString(headerMap));
        HttpClient httpClient = getHttpClient(timeout);
        HttpPost httpRequest = new HttpPost(url);

        try {
            MultipartEntity repEntity = new MultipartEntity();

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof String) {
                    repEntity.addPart(key, new StringBody((String) value));
                } else if (value instanceof File) {
                    repEntity.addPart(key, new FileBody((File) value));
                } else if (value instanceof InputStream) {
                    repEntity.addPart(key, new InputStreamBody((InputStream) value, key));
                } else if (value instanceof byte[]) {
                    repEntity.addPart(key, new ByteArrayBody((byte[]) value, key));
                }
            }

            httpRequest.setEntity(repEntity);

            // set request headers
            addHeaderForHttpRequestBase(httpRequest, headerMap);

            return doPost(httpClient, httpRequest);
        } catch (Exception e) {
            logger.error("HTTP_post-{} - {}| content: {}, header: {}"
                    + url + "->"
                    + e.getMessage() + "->"
                    + parameters + "->"
                    + JSON.toJSONString(headerMap) + "->"
                    + (e instanceof UnsupportedEncodingException ? null : e));
        }

        return null;
    }

    public static String doPost(HttpClient httpClient, HttpEntityEnclosingRequestBase httpRequest) throws Exception {
        HttpEntity rspEntity = null;
        try {
            long start = System.currentTimeMillis();

            // specify some common header, like IP, reference, accept
            updateCommonHeader(httpRequest);

            HttpResponse httpResponse = httpClient.execute(httpRequest);
            if (isSuccess(httpResponse.getStatusLine().getStatusCode())) {
                rspEntity = httpResponse.getEntity();
                if (rspEntity != null) {
                    byte[] bytes = getResponseEntityAsByteArray(httpResponse);

                    if (logger.isInfoEnabled() && (bytes.length < 15 || bytes.length > 1024)) {
                        String responseMinStr = getMinHttpResponeStringForLog(bytes, DEFAULT_CHARSET);
                        logger.debug("HTTP_post| url: {},length is {}, response: {}" + "->"
                                + httpRequest.getURI() + "->"
                                + (bytes != null ? bytes.length : 0) + "->"
                                + responseMinStr);
                    }
                    return new String(bytes, DEFAULT_CHARSET);
                }
                logIfSlowURLWithConnectionStats(httpRequest.getURI().toString(), start);
            } else {
                // 查看响应状态 如果不是正常状态 则主动abort请求并返回{
                logger.warn("HTTP_post error status code-{}| the url is: {}, response: {}" + "->" +
                        httpResponse.getStatusLine().getStatusCode() + "->"
                        + httpRequest.getURI() + "->"
                        + new String(getResponseEntityAsByteArray(httpResponse)), DEFAULT_CHARSET);
                httpRequest.abort();
                return null;
            }
        } catch (Exception e) {
            httpRequest.abort();
            throw e;
        } finally {
            try {
                if (rspEntity != null) {
                    EntityUtils.consume(rspEntity);
                }
            } catch (IOException e) {
                logger.error("HTTP_post fail consume the response with Exception {}|", e.getMessage());
            }
        }

        return null;
    }

    /**
     * add headers for HttpRequestBase
     *
     * @param httpRequest
     * @param headerMap
     */
    private static void addHeaderForHttpRequestBase(HttpRequestBase httpRequest, Map<String, String> headerMap) {
        if (headerMap != null && !headerMap.isEmpty()) {
            for (String key : headerMap.keySet()) {
                httpRequest.addHeader(key, headerMap.get(key));
            }
        }
    }

    private static void updateCommonHeader(HttpRequestBase httpRequest) {
       // httpRequest.addHeader("Referer", "http://rank-admin.vip.vip.com/");
        httpRequest.addHeader("Accept-Encoding", "gzip, deflate");

        // 请求参数增加客户端ip
        populateRequestIp(httpRequest);
    }

    private static void populateRequestIp(HttpRequestBase httpRequest) {
        // 增加IP转发功能
        String client_id = ip_session.get();
        if (StringUtils.isEmpty(client_id)) {
            client_id = _127_0_0_1;
        }
        httpRequest.setHeader("X-Forwarded-For", client_id);
        if (logger.isDebugEnabled()) {
            logger.debug("HTTP_request ip-{}|", client_id);
        }
    }

    private static boolean isSuccess(int respStatusCode) {
        if (respStatusCode == HttpStatus.SC_OK || respStatusCode == HttpStatus.SC_CREATED) {
            return true;
        } else {
            return false;
        }
    }

    private static void logIfSlowURLWithConnectionStats(String uri, long start) {
        long offset = System.currentTimeMillis() - start;
        if (offset > WARN_TIME) {
            logSlowURLWithConnectionStats(offset, uri);
        }
    }

    protected static void logSlowURLWithConnectionStats(long offset, String url) {
        PoolStats totalStats = cm.getTotalStats();
        try {
            URL u = new URL(url);
            logger.warn(
                    "HTTP_take much time-{}| {} cost {} millis. ### http connection pool, max: {}, acitve: {}, leased: {}, pending: {}"
                            + u.getHost() + "->" + url + "->" + offset + totalStats.getMax()
                            + totalStats.getAvailable(), totalStats.getLeased(),
                    totalStats.getPending());
        } catch (MalformedURLException e) {
        }
    }

    private static byte[] getResponseEntityAsByteArray(HttpResponse httpResponse) throws IOException {
        Header contentEncodingHeader = httpResponse.getFirstHeader("Content-Encoding");
        byte[] bytes = null;
        if (contentEncodingHeader != null && contentEncodingHeader.getValue().toLowerCase().indexOf("gzip") > -1) {
            bytes = EntityUtils.toByteArray(new GzipDecompressingEntity(httpResponse.getEntity()));
        } else {
            bytes = EntityUtils.toByteArray(httpResponse.getEntity());
        }
        return bytes;
    }

    /**
     * 打印HTTP的响应
     *
     * @author jame.xu
     * @date 2014-4-3
     * @description
     * @mail jame.xu@vipshop.com
     */
    private static String getMinHttpResponeStringForLog(byte[] data, String charset) {
        if (data != null && data.length > 0) {
            int length = data.length >= 16 ? 16 : data.length;
            byte[] dest = new byte[length];
            System.arraycopy(data, 0, dest, 0, length);
            try {
                String result = new String(dest, charset);
                if (null != result) {
                    result = result.replace("\r\n", " ");
                    return result;
                }
            } catch (UnsupportedEncodingException e) {
            }
        }
        return null;
    }

    private static String filterHtml(String result) {
        if (null != result) {
            result = result.replace("&#039;", "'");
            result = result.replace("&amp;", "&");
            result = result.replace("&nbsp;", " ");
        }
        return result;
    }

    private static String logHttpResponse(byte[] data, String charset) {
        if ((data != null) && (data.length > 0)) {
            int length = data.length >= 16 ? 16 : data.length;
            byte[] dest = new byte[length];
            System.arraycopy(data, 0, dest, 0, length);
            try {
                String result = new String(dest, charset);
                if (null != result) {
                    return result.replace("\r\n", " ");
                }
            } catch (UnsupportedEncodingException e) {
            }
        }

        return null;
    }

    protected static void logConnectionStats(long offset, String url) {
        PoolStats totalStats = cm.getTotalStats();
        logger.warn("http request[" + url + "] take much time " + offset + " millis. ### http connection pool, max: "
                + totalStats.getMax() + ", acitve: " + totalStats.getAvailable() + ", leased: " + totalStats.getLeased()
                + ", pending:" + totalStats.getPending());
    }

    public static void main(String[] args) {

    }
}

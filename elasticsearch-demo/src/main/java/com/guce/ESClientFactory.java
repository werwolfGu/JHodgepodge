package com.guce;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/8/21 2:06 下午
 *
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/index.html
 */
public class ESClientFactory implements InitializingBean , FactoryBean {

    private  RestClient restClient;

    private RestHighLevelClient highLevelClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    //@PostConstruct
    public void init() {

        HttpHost[] httpHosts = makeHttpHost("");
        highLevelClient = new RestHighLevelClient( RestClient.builder(httpHosts));
    }

    private HttpHost[] makeHttpHost(String s) {

        if (StringUtils.isBlank(s)){
            return new HttpHost[0];
        }

        String[] addrs = s.split(";");
        if (addrs.length == 0) {
            return new HttpHost[0];
        }

        List<HttpHost> hostList = new ArrayList<>();
        for (String address : addrs) {
            String[] addrArr = address.split(":");
            String ip = addrArr[0];
            int port = Integer.parseInt(addrArr[1]);
            HttpHost httpHost = new HttpHost(ip, port, "http");
            hostList.add(httpHost);
        }

        return hostList.toArray(new HttpHost[0]);
    }

    @Override
    public Object getObject() throws Exception {
        return highLevelClient;
    }

    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }

}

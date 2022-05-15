package com.guce.es;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * elasticsearch RestHighLevelClient factory
 * @Author chengen.gce
 * @DATE 2021/8/21 2:06 下午
 *
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/index.html
 */
@Slf4j
public class ESClientFactory {

    //private  RestClient restClient;

    //private RestHighLevelClient highLevelClient;

    @Setter
    private String hostsStr ;

    public void afterPropertiesSet() {
        init();
    }

    /**
     * 初始化Elasticsearch client
     */
    //@PostConstruct
    public synchronized void init() {
        try{
            log.warn("初始化 ESClient start : {}" ,hostsStr);
            HttpHost[] httpHosts = makeHttpHost(hostsStr);
            if (Objects.isNull(httpHosts)) {
                log.warn("ESClient init error ,服务端配置为空");
                return ;
            }
            //highLevelClient = new RestHighLevelClient( RestClient.builder(httpHosts));
            log.warn("初始化 ESClient sucessed...");
        }catch (Exception e){
            log.error("init ESClient error " , e);
        }

    }

    private HttpHost[] makeHttpHost(String s) {

        if (StringUtils.isBlank(s)){
            return null;
        }

        String[] addrs = s.split(";");
        if (addrs.length == 0) {
            return null;
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

    /*@Override
    public Object getObject() throws Exception {
        return highLevelClient;
    }

    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }
*/
}
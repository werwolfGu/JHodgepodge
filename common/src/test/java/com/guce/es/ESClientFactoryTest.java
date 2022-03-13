package com.guce.es;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author chengen.gce
 * @DATE 2021/8/21 2:06 下午
 *
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/index.html
 */
@Slf4j
public class ESClientFactoryTest {


    @Autowired
    private RestHighLevelClient highLevelClient;


    public void indexTest(){
        //highLevelClient.index();
    }

}
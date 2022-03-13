package com.guce.dao.impl;

import com.alibaba.fastjson.JSON;
import com.guce.dao.DemoDao;
import com.guce.domain.UserInfo;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository("demoDao")
public class DemoDaoImpl implements DemoDao {

    private static Logger logger = LoggerFactory.getLogger(DemoDaoImpl.class);

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Resource(name = "secondJdbcTemplate")
    private JdbcTemplate jdbcTemplate1;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public String getInfo(Map<String,Object> paramMap) {
        String sqlStr = "select * from user_info ";
        List list = jdbcTemplate.queryForList(sqlStr);
        if(logger.isInfoEnabled()){
            logger.info("jdbcTemplate dao list:" + list);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try{

            paramMap.put("id",1);
            DemoDao dao = sqlSession.getMapper(DemoDao.class);
            dao.getInfo(paramMap);
            List<UserInfo> userInfos = sqlSession.selectList("sample.mybatis.mapper.UserInfo.selectUserInfoById",paramMap);
            if(logger.isInfoEnabled()){
                logger.info("mybatis result:{}" ,JSON.toJSONString(userInfos));
            }
        }catch (Exception e){
            logger.error("从数据库中获取数据Exception:{}" ,e.getMessage(),e);
        }finally {
            sqlSession.close();
        }
        return null;
    }
}

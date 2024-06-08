# Mybatis学习



## Executor执行图

**Executor执行器**

![](/Users/chengen.gu/workspace/github/springboot-demo/MD/picture/Mybatis.png)

**Executor接口类继承关系**

<img src="/Users/chengen.gu/workspace/github/springboot-demo/MD/picture/Executor.png" style="zoom:50%;" />

| 方式           | 优点                           | 缺点                                                         |
| -------------- | :----------------------------- | ------------------------------------------------------------ |
| SimpleExecutor | 默认执行器， 节约服务器资源    | 每次都要开关Statement                                        |
| ReuseExecutor  | 提升后端接口处理效率           | 每次一个新sql都缓存，增大JVM内存压力                         |
| BatchExecutor  | 专门用于更新插入操作，效率最快 | 对select 不适用，另外特殊要求，比如限制一次execteBatch的数量时需要写过滤器定制 |



- batchExecutor : 使用数据库批量 insert、update 需要手动提交；

  ```java
  public void saveOrder(Order t) {
          SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
          OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
          
          try{
              orderMapper.save(t);
              sqlSession.commit();
          }catch(Exception e){
              logger.error("批量导入数据异常，事务回滚", e);
              sqlSession.rollback();
          }finally {
              if (sqlSession != null) {
                  sqlSession.close();
              }
      }
  ```

  

## Mybatis执行流程

执行一个SQL时执行执行流程

```javascript
DefaultSqlSession.selectOne(String, Object)  (org.apache.ibatis.session.defaults)
    DefaultSqlSession.selectList(String, Object)  (org.apache.ibatis.session.defaults)
        DefaultSqlSession.selectList(String, Object, RowBounds)  (org.apache.ibatis.session.defaults)
            DefaultSqlSession.selectList(String, Object, RowBounds, ResultHandler)  (org.apache.ibatis.session.defaults)
                Executor.query(MappedStatement, Object, RowBounds, ResultHandler)  (org.apache.ibatis.executor)
                    CachingExecutor.query(MappedStatement, Object, RowBounds, ResultHandler)  (org.apache.ibatis.executor)
                        MappedStatement.getBoundSql(Object)  (org.apache.ibatis.mapping)
                        CachingExecutor.createCacheKey(MappedStatement, Object, RowBounds, BoundSql)  (org.apache.ibatis.executor)
                        CachingExecutor.query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql)  (org.apache.ibatis.executor)
                            Executor.query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql)(2 usages)  (org.apache.ibatis.executor)
                                BaseExecutor.query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql)  (org.apache.ibatis.executor)
                                    BaseExecutor.queryFromDatabase(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql)  (org.apache.ibatis.executor)
                                        PerpetualCache.putObject(Object, Object)(3 usages)  (org.apache.ibatis.cache.impl)
                                            Map.put(K, V)  (java.util)
                                        BaseExecutor.doQuery(MappedStatement, Object, RowBounds, ResultHandler, BoundSql)  (org.apache.ibatis.executor)
                                            BatchExecutor.doQuery(MappedStatement, Object, RowBounds, ResultHandler, BoundSql)  (org.apache.ibatis.executor)
                                            SimpleExecutor.doQuery(MappedStatement, Object, RowBounds, ResultHandler, BoundSql)  (org.apache.ibatis.executor)
                                                Configuration.newStatementHandler(Executor, MappedStatement, Object, RowBounds, ResultHandler, BoundSql)  (org.apache.ibatis.session)
                                                    RoutingStatementHandler.RoutingStatementHandler(Executor, MappedStatement, Object, RowBounds, ResultHandler, BoundSql)  (org.apache.ibatis.executor.statement)
                                                    InterceptorChain.pluginAll(Object)  (org.apache.ibatis.plugin)
                                                StatementHandler.query(Statement, ResultHandler)  (org.apache.ibatis.executor.statement)
                                                    SimpleStatementHandler.query(Statement, ResultHandler)  (org.apache.ibatis.executor.statement)
                                                        BoundSql.getSql()  (org.apache.ibatis.mapping)
                                                        Statement.execute(String)  (java.sql)
                                                            PgStatement.execute(String)  (org.postgresql.jdbc)
                                                                PgPreparedStatement.execute(String)  (org.postgresql.jdbc)
                                                            SQLServerStatement.execute(String)  (com.microsoft.sqlserver.jdbc)
                                                                SQLServerPreparedStatement.execute(String)  (com.microsoft.sqlserver.jdbc)
                                                            JdbcPreparedStatement.execute(String)  (org.h2.jdbc)
                                                            JDBCStatement.execute(String)  (org.hsqldb.jdbc)
                                                            PgPreparedStatement.execute(String)  (org.postgresql.jdbc)
                                                            EmbedStatement.execute(String)  (org.apache.derby.impl.jdbc)
                                                                EmbedPreparedStatement.execute(String)  (org.apache.derby.impl.jdbc)
                                                            BrokeredStatement.execute(String)  (org.apache.derby.iapi.jdbc)
                                                            StatementWrapper.execute(String)  (com.mysql.cj.jdbc)
                                                            JdbcStatement.execute(String)  (org.h2.jdbc)
                                                                JdbcPreparedStatement.execute(String)  (org.h2.jdbc)
                                                            EmbedPreparedStatement.execute(String)  (org.apache.derby.impl.jdbc)
                                                            UpdatableVTITemplate.execute(String)  (org.apache.derby.vti)
                                                            SQLServerPreparedStatement.execute(String)  (com.microsoft.sqlserver.jdbc)
                                                            JDBCPreparedStatement.execute(String)  (org.hsqldb.jdbc)
                                                            StatementImpl.execute(String)  (com.mysql.cj.jdbc)
                                                        ResultSetHandler.handleResultSets(Statement)  (org.apache.ibatis.executor.resultset)
                                                            DefaultResultSetHandler.handleResultSets(Statement)  (org.apache.ibatis.executor.resultset)
                                                    RoutingStatementHandler.query(Statement, ResultHandler)  (org.apache.ibatis.executor.statement)
                                                BaseExecutor.closeStatement(Statement)  (org.apache.ibatis.executor)
                                                    Statement.close()  (java.sql)
                                        PerpetualCache.removeObject(Object)  (org.apache.ibatis.cache.impl)
                                            Map.remove(Object)  (java.util)
                                        MappedStatement.getStatementType()  (org.apache.ibatis.mapping)
                                    load() in DeferredLoad in BaseExecutor  (org.apache.ibatis.executor)
                                        PerpetualCache.getObject(Object)  (org.apache.ibatis.cache.impl)
                                        ResultExtractor.extractObjectFromList(List<Object>, Class<?>)  (org.apache.ibatis.executor)
                                        MetaObject.setValue(String, Object)  (org.apache.ibatis.reflection)
                                    ConcurrentLinkedQueue.clear()  (java.util.concurrent)
                                    Configuration.getLocalCacheScope()  (org.apache.ibatis.session)
```


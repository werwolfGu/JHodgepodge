# spring事务管理

## spring事务隔离级别

> 未提交读，已提交读，重复读，串行化 同数据库的隔离级别



## spring 事务传播机制

> spring事务传播机制包含7种；

如果代码使用了 `@Transactional`  如何判断是否在事务中

```java
boolean startTransaction = org.springframework.transaction.support.TransactionSynchronizationManager#isActualTransactionActive();
```






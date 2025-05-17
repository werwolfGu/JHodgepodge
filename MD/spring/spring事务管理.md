# spring事务管理

## spring事务隔离级别

> 未提交读，已提交读，重复读，串行化 同数据库的隔离级别



## spring 事务传播机制

> spring事务传播机制包含7种；

如果代码使用了 `@Transactional`  如何判断是否在事务中

```java
boolean startTransaction = org.springframework.transaction.support.TransactionSynchronizationManager#isActualTransactionActive();
```



## 通过spring管理事务能力

```java
public void updateUser(User user) {
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    def.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
    def.setTimeout(30);
    def.setReadOnly(false);

    TransactionStatus status = transactionManager.getTransaction(def);

    try {
        // 反射调用 userRepository.save(user)
        Method saveMethod = UserRepository.class.getMethod("save", User.class);
        saveMethod.invoke(userRepository, user);
        transactionManager.commit(status);
    } catch (Exception e) {
        transactionManager.rollback(status);
        throw e;
    }
}

```






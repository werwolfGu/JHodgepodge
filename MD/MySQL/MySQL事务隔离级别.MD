# MySQL事务隔离级别

MySQL 支持四种事务隔离级别，分别是：`读未提交`、`读已提交`、`可重复读`、`串行化`。默认使用 `可重复读` 隔离级别。

## 🔍 新增内容：四种事务隔离级别详解

| 隔离级别             | 英文名称                  | 是否解决脏读 | 是否解决不可重复读 | 是否解决幻读 | 性能开销 | 使用场景 |
|----------------------|---------------------------|-------------|-------------------|--------------|-----------|----------|
| 读未提交             | `READ UNCOMMITTED`        | ❌           | ❌                 | ❌            | 最低      | 很少使用，性能高但数据不一致风险大 |
| 读已提交             | `READ COMMITTED`          | ✅           | ❌                 | ❌            | 一般      | Oracle、PostgreSQL 默认级别 |
| 可重复读（默认）     | `REPEATABLE READ`         | ✅           | ✅                 | ⚠️部分缓解     | 较高      | MySQL 默认级别，适用于大多数业务场景 |
| 串行化               | `SERIALIZABLE`            | ✅           | ✅                 | ✅            | 最高      | 对数据一致性要求极高，牺牲并发性能 |


> 提示：MySQL 使用 MVCC + Gap Lock 在 `REPEATABLE READ` 级别下缓解幻读问题。

---

## 📌 每种隔离级别解决的问题详解

### 1. **脏读（Dirty Read）**
> 一个事务读取了另一个事务尚未提交的数据。

- 危害：如果对方事务回滚，你读到的就是“无效数据”。

✅ 解决方法：使用 `READ COMMITTED` 或更高隔离级别。

---

### 2. **不可重复读（Non-Repeatable Read）**
> 同一个事务中多次读取同一行记录，结果不一致。

- 原因：其他事务修改并提交了该行数据。

✅ 解决方法：使用 `REPEATABLE READ` 或 `SERIALIZABLE`。

---

### 3. **幻读（Phantom Read）**
> 同一个事务中多次执行相同查询，返回的行数不同（因为其他事务插入或删除了数据）。

⚠️ 注意：
- `REPEATABLE READ` 在 MySQL 中通过 **间隙锁（Gap Lock）** 缓解幻读。
- 标准 SQL 中只有 `SERIALIZABLE` 才能完全避免幻读。

✅ 解决方法：使用 `SERIALIZABLE`。

---

## 🔐 设置事务隔离级别

你可以查看当前会话或全局的隔离级别：

```sql
-- 查看当前会话隔离级别
SELECT @@tx_isolation;

-- 查看全局隔离级别
SELECT @@global.tx_isolation;
```

设置隔离级别示例：

```sql
-- 设置为读已提交
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;

-- 设置为可重复读
SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;

-- 设置为串行化
SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE;
```

---

## 🧩 小结

| 隔离级别             | 是否解决脏读 | 是否解决不可重复读 | 是否解决幻读 | 使用建议 |
|----------------------|--------------|---------------------|---------------|-----------|
| `READ UNCOMMITTED`   | ❌            | ❌                   | ❌              | 不推荐使用 |
| `READ COMMITTED`     | ✅            | ❌                   | ❌              | Oracle 默认 |
| `REPEATABLE READ`    | ✅            | ✅                   | ⚠️部分缓解        | ✅MySQL 默认 |
| `SERIALIZABLE`       | ✅            | ✅                   | ✅               | 强一致性场景使用 |

[MySQL-MVCC.MD](MySQL-MVCC.MD)

[MySQL-如何防止幻读.MD](MySQL-%E5%A6%82%E4%BD%95%E9%98%B2%E6%AD%A2%E5%B9%BB%E8%AF%BB.MD)
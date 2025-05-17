# MySQL 中 InnoDB 如何防止幻读

## 🧾 什么是幻读？

> **幻读**是指在一个事务中多次执行相同的查询语句，返回的行数不同。新增的“幻影记录”来自于其他事务的插入或删除操作。

例如：

```sql
-- 事务 A
START TRANSACTION;
SELECT * FROM users WHERE age BETWEEN 20 AND 30; -- 返回 5 行

-- 事务 B
INSERT INTO users (name, age) VALUES ('New User', 25);
COMMIT;

-- 回到事务 A
SELECT * FROM users WHERE age BETWEEN 20 AND 30; -- 如果看到新增的一行，则发生了幻读
```

MySQL 在 `REPEATABLE READ` 隔离级别下通过 **间隙锁（Gap Lock）** 和 **临键锁（Next-Key Lock）** 来防止幻读。

---

## ✅ 可重复读如何解决幻读？

虽然 `REPEATABLE READ` 隔离级别下的 **MVCC 快照读** 能防止不可重复读，但它**不能完全防止幻读**。MySQL 在 InnoDB 引擎中引入了以下机制来缓解幻读问题：

### 🔐 1. **间隙锁（Gap Lock）**

#### 定义：
间隙锁锁定的是索引记录之间的“间隙”，而不是记录本身。它防止其他事务在这个间隙中插入新数据。

#### 示例：

```sql
-- 假设表 users 有主键 id，当前数据为：1, 3, 5
-- 事务 A 执行：
SELECT * FROM users WHERE id BETWEEN 2 AND 4 FOR UPDATE;

-- 此时会加一个间隙锁：(1,3), (3,5)
-- 事务 B 尝试插入：
INSERT INTO users (id, name) VALUES (4, 'Bob'); -- 被阻塞
```

> 间隙锁阻止了事务 B 在 `(3,5)` 区间插入数据，从而避免了幻读。

---

### 🔒 2. **临键锁（Next-Key Lock）**

#### 定义：
临键锁 = 行锁 + 间隙锁。它锁定一个索引记录及其前面的间隙。

#### 特点：
- 是 InnoDB 默认使用的锁机制。
- 可以同时防止 **不可重复读** 和 **幻读**。

#### 示例：

```sql
-- 事务 A
START TRANSACTION;
SELECT * FROM users WHERE age = 25 FOR UPDATE;
```

InnoDB 使用 Next-Key Lock 锁定 `age=25` 的记录以及其前后的索引区间，防止其他事务插入数据。

---

### 🧩 3. **唯一索引 vs 普通索引 加锁差异**

| 查询值 | 是否存在 | 索引类型 | 加锁机制         | 锁定范围           | 是否防止幻读 |
|--------|----------|-----------|------------------|--------------------|---------------|
| age=25 | 不存在   | 普通索引 | Next-Key Lock    | `(20,30]`         | ✅             |
| age=25 | 存在     | 普通索引 | Next-Key Lock    | `(20,25]`         | ✅             |
| email='bob@example.com' | 存在 | 唯一索引 | Record Lock      | 当前行              | ❌             |
| email='new@example.com' | 不存在 | 唯一索引 | Gap Lock         | `(charlie@example.com, ∞)` | ✅             |

---

## 🔄 结合 SQL 示例说明加锁行为

### 示例 1：使用当前读防止幻读

```sql
-- 事务 A
START TRANSACTION;
SELECT * FROM users WHERE age BETWEEN 20 AND 30 FOR UPDATE;

-- 事务 B
INSERT INTO users (name, age) VALUES ('New User', 25); -- 被阻塞

-- 回到事务 A
COMMIT;

-- 回到事务 B
-- 插入成功，因为事务 A 已提交
```

> ✅ 因为事务 A 使用了 `FOR UPDATE` 当前读，InnoDB 自动对相关索引范围加锁，防止事务 B 插入数据，从而防止幻读。

---

### 示例 2：不加锁时可能出现幻读

```sql
-- 事务 A
START TRANSACTION;
SELECT * FROM users WHERE age BETWEEN 20 AND 30;

-- 事务 B
INSERT INTO users (name, age) VALUES ('New User', 25);
COMMIT;

-- 回到事务 A
SELECT * FROM users WHERE age BETWEEN 20 AND 30; -- 可能看不到新增记录（快照读）
SELECT * FROM users WHERE age BETWEEN 20 AND 30 FOR UPDATE; -- 看到并锁定新记录
```

> ⚠️ 第一次快照读看不到幻读记录；但第二次当前读可以看到并锁定新记录，说明幻读确实发生过。

---

## 📌 总结一句话：

> **MySQL 的 `REPEATABLE READ` 隔离级别通过间隙锁（Gap Lock）和临键锁（Next-Key Lock）防止幻读，而不是依靠 MVCC。普通索引上使用 Next-Key Lock 实现防幻读，而唯一索引在命中记录时不加间隙锁，因此可能无法防止幻读。**
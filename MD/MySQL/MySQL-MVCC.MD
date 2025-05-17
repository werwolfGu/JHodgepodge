# MySQL MVCC

## MySQL 存储文件
[MySQL日志文件](./MySQL日志文件.MD)

## MVCC 在不同隔离级别的行为差异

MySQL 中的 `REPEATABLE READ` 和 `READ COMMITTED` 是两个支持 MVCC 的隔离级别，它们的核心区别在于 **Read View 的创建时机和复用方式**。

| 特性 | `READ COMMITTED` | `REPEATABLE READ` |
|------|------------------|-------------------|
| **Read View 创建时机** | 每次执行 SELECT 时都生成一个新的 Read View | 事务第一次执行 SELECT 时创建一个 Read View，之后复用 |
| **是否解决不可重复读** | ❌                | ✅                 |
| **是否缓解幻读**       | ❌                | ⚠️部分缓解         |
| **是否每次查询都新建 Read View** | ✅              | ❌                 |

---

## Read View 的生成机制与事务可见性

Read View 是 InnoDB 实现一致性视图的关键结构。它由以下字段组成：

| 字段             | 含义 |
|------------------|------|
| `m_ids`          | 当前活跃的事务 ID 列表 |
| `min_trx_id`     | `m_ids` 中最小的事务 ID |
| `max_trx_id`     | 下一个将要分配的事务 ID |
| `creator_trx_id` | 创建该 Read View 的事务 ID |

### 可见性判断规则（简化版）

1. 如果 `DB_TRX_ID` <= `min_trx_id`：数据版本对当前事务可见。
2. 如果 `DB_TRX_ID` >= `max_trx_id`：数据版本未提交，不可见。
3. 如果 `DB_TRX_ID` 在 `m_ids` 中：是未提交事务修改的数据，不可见。
4. 如果 `DB_TRX_ID` 不在 `m_ids` 中：是已提交事务修改的数据，可见。

---


## 快照读与当前读的区别

| 特性 | 快照读（Consistent Read） | 当前读（Current Read） |
|------|---------------------------|-------------------------|
| 是否使用 MVCC | ✅                        | ❌                      |
| 是否加锁 | ❌                        | ✅（共享锁或排他锁）    |
| 数据来源 | 基于 Read View 的一致性快照 | 最新提交的数据版本      |
| 是否能看到其他事务修改 | ❌（只看初始快照）       | ✅                      |
| 典型语句 | `SELECT ...`             | `SELECT ... FOR UPDATE`, `UPDATE`, `DELETE` |

---


## MVCC 如何支持一致性非锁定读

在 `REPEATABLE READ` 隔离级别下，MVCC 通过以下机制实现一致性非锁定读：

1. **Undo Log**：记录数据的历史版本。
2. **Read View**：事务第一次查询时生成，后续查询复用。
3. **隐藏字段**：
   - `DB_TRX_ID`：最后一次修改该行记录的事务 ID
   - `DB_ROLL_PTR`：指向 undo log 中的旧版本记录

> ✅ 这样保证了事务在整个生命周期中看到的数据视图保持一致。

---


## 结合 SQL 示例说明 MVCC 工作流程

### 示例 1：REPEATABLE READ 下的快照读

```
-- 事务 A
START TRANSACTION;
SELECT * FROM users WHERE id = 1; -- 第一次读，生成 Read View，看到 'Alice'

-- 事务 B
START TRANSACTION;
UPDATE users SET name = 'Bob' WHERE id = 1;
COMMIT;

-- 回到事务 A
SELECT * FROM users WHERE id = 1; -- 第二次读，仍看到 'Alice'
COMMIT;
```

> ✅ 事务 A 使用的是第一次生成的 Read View，因此看不到事务 B 的修改。

---

### 示例 2：READ COMMITTED 下的快照读

```
-- 事务 A
START TRANSACTION;
SELECT * FROM users WHERE id = 1; -- 第一次读，生成 Read View，看到 'Alice'

-- 事务 B
START TRANSACTION;
UPDATE users SET name = 'Bob' WHERE id = 1;
COMMIT;

-- 回到事务 A
SELECT * FROM users WHERE id = 1; -- 第二次读，重新生成 Read View，看到 'Bob'
COMMIT;
```

> ✅ 在 `READ COMMITTED` 隔离级别下，事务 A 的第二次查询看到了事务 B 提交后的修改。

---

### 示例 3：当前读绕过 MVCC

```
-- 事务 A
START TRANSACTION;
SELECT * FROM users WHERE id = 1 FOR UPDATE; -- 当前读，看到 'Alice'

-- 事务 B
START TRANSACTION;
UPDATE users SET name = 'Bob' WHERE id = 1; -- 被阻塞，因为事务 A 加了排他锁

-- 回到事务 A
COMMIT; -- 提交事务，释放锁

-- 回到事务 B
UPDATE users SET name = 'Bob' WHERE id = 1; -- 成功更新
COMMIT;

-- 事务 C
START TRANSACTION;
SELECT * FROM users WHERE id = 1; -- 看到 'Bob'
COMMIT;
```

> ⚠️ 当前读绕过了 MVCC，直接读取最新数据并加锁。


比如我们有如下表：
```shell
mysql> SELECT * FROM t;
+----+--------+
| id | name   |
+----+--------+
|  1 | 刘备   |
+----+--------+
1 row in set (0.01 sec)
```
假设该条记录的事务ID为10；

当发生如下操作时

| 发生时间编号 | trx50                               | trx80                               | 
|--------|-------------------------------------|-------------------------------------|
| 1      | begin                               |                                     |
| 2      | update t set name='关羽' where id=1;  |                                     |
| 3      | update t set name='张飞' where id=1;  |                                     |
| 4      | commit                              |                                     |
| 5      |   | update t set name='赵云' where id=1;  |
| 6      |  | update t set name='诸葛量' where id=1; |
| 7      |                                     | commit                              |

此时在`undo log`中的记录应该是这样的

![](./MVCC.png)


## 快照读：
简单的select操作，属于快照读，不加锁。(当然，也有例外，下面会分析)

select * from table where ?;
## 当前读：
特殊的读操作，插入/更新/删除操作，属于当前读，需要加锁。

select * from table where ? lock in share mode;
select * from table where ? for update;
insert into table values (…);
update table set ? where ?;
delete from table where ?;

## MVCC情况下会出现幻读

- session2
```sql

start transaction; 
select * from yang; 
---此时session3 commit
//新添加语句
update yang set name='Tian' where id=4;
select * from yang; 
commit;

```

- session3
```sql
start transaction;
insert into yang values(NULL,'tian');
commit;
```
update 使用了当前读 ；会读到最新数据


## 间隙锁解决幻读

MVCC 和 MySql 的间隙锁进行配合来保证`不出现幻读`

- 原则 1：加锁的基本单位是 next-key lock。希望你还记得，next-key lock 是前开后闭区间。
- 原则 2：查找过程中访问到的对象才会加锁。
- 优化 1：索引上的等值查询，给唯一索引加锁的时候，next-key lock 退化为行锁。
- 优化 2：索引上的等值查询，向右遍历时且最后一个值不满足等值条件的时候，next-key lock 退化为间隙锁。
- 一个 bug：唯一索引上的范围查询会访问到不满足条件的第一个值为止。


id(主键)	c（普通索引）	d（无索引）
5	    5	        5
10	    10	        10
15	    15	        15
20	    20	        20
25	    25	        25

>以上数据为了解决幻读问题，更新的时候不只是对上述的五条数据增加行锁，还对于中间的取值范围增加了6间隙锁，
>（-∞，5]（5，10]（10，15]（15，20]（20，25]（25，+supernum] 
>（其中supernum是数据库维护的最大的值。为了保证间隙锁都是左开右闭原则。）







参考  
[MVCC无法解决幻读](https://www.jianshu.com/p/cef49aeff36b)  
[MVCC无法解决幻读1](https://www.zhihu.com/question/372905832)  
[临界锁](https://juejin.cn/post/6844903666420285454)  
[间隙锁如何锁](https://www.jianshu.com/p/32904ee07e56)  

[MVCC原理详解](https://www.cnblogs.com/qdhxhz/p/15750866.html)


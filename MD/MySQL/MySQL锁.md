# MySQL锁

> MySQL 提供了多种锁机制来确保数据的一致性和完整性，同时提高并发性能。

## 行级锁

> 行级锁是对单行记录进行的锁定，MySQL主要通过两种方式实现行级锁：共享锁和排他锁。
>
> 如果查询条件中没有索引，可能会锁住表中的所有的记录

- 共享锁

  - 使用 `select ... lock in share mode ` 语句。
  - 允许多个事务同时读取行，但不允许修改被锁定的行。

  ```SQL
  select * from user where age > 30 lock in share mode ;
  ```

- 排他锁

  - 使用` select ... for update` 语句。
  - 不允许其他事务读取和修改被锁定的行。

  ```SQL
  select * from user where age > 30 for update;
  ```

## 表锁

表级锁是对整个表进行的锁定。MySQL的MyISAM存储引擎主要使用这种锁。

- **读锁（Read Lock）**：

  - 通过`LOCK TABLES ... READ`语句。
  - 允许多个事务同时读取表，但不允许写操作。

  ```SQL
  
  LOCK TABLES users READ;
  ```

- **写锁（Write Lock）**：

  - 通过`LOCK TABLES ... WRITE`语句。
  - 不允许其他事务读或写被锁定的表。

  ```SQL
  LOCK TABLES users WRITE;
  ```

## 意向锁

意向锁是InnoDB存储引擎的一种内部锁机制，用于实现多粒度锁定。意向锁包括意向共享锁（IS）和意向排他锁（IX）。

- 意向共享锁（IS, Intention Shared Lock）：
  - 表示事务准备在某些行上加共享锁。
- 意向排他锁（IX, Intention Exclusive Lock）：
  - 表示事务准备在某些行上加排他锁。

意向锁不会阻塞其他事务获取表级锁，而是用来协调行级锁和表级锁之间的冲突。

## **间隙锁（Gap Locks）**

间隙锁是在InnoDB存储引擎中用来防止幻读的一种锁。它锁定行之间的间隙，以防止其他事务在这些间隙中插入或删除记录。

- 间隙锁（Gap Lock）：

  - 通过范围查询如`SELECT ... FOR UPDATE`实现。
  - 锁定某一范围内的所有间隙，防止其他事务在这些间隙中插入新记录。

  ```SQL
  SELECT * FROM users WHERE age > 30 FOR UPDATE;
  ```




# MySQL MVCC

## MySQL 存储文件
[MySQL日志文件](./MySQL日志文件.MD)

## MVCC

> `已提交读` `可重复读` 都可以通过MVCC来实现事务隔离

MySQL使用InnoDB存储引擎时，它的聚簇索引中包含了2个必要的隐藏列
- trx_id ： 事务ID隐藏列
- roll_pointer：对于某条聚簇索引记录每次改动时，都会把旧版本的记录写到undo log中，然后这个隐藏列就是执行该旧版本的指针；

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

### readview
readview是一种数据结构，包含如下字段：

- m_ids，当前有哪些事务正在执行，且还没有提交，这些事务的 id 就会存在这里；
- min_trx_id，是指 m_ids 里最小的值；
- max_trx_id，是指下一个要生成的事务 id。下一个要生成的事务 id 肯定比现在所有事务的 id 都大；
- creator_trx_id，每开启一个事务都会生成一个 ReadView，而 creator_trx_id 就是这个开启的事务的 id。

判断逻辑
从最新的版本开始判断，逻辑如下：
- 当前版本如果比记录的所有事务id都大，即在当前事务开始的时候，该事务并未启动，所以一定不可见的。
- 当前版本如果比记录的所有事务id都小，即在当前事务开始的时候，该事务已提交，所以可见
- 当前版本在readview中，即在当前事务开始的时候，该事务未提交，不可见
- 当前版本不在readview中，即在当前事务考试的时候，该事务已提交，可见
- 更新逻辑，因为更新不可能在快照上做更新，所以更新的时候是读取最新的数据上再做修改，且因为修改之后的undo log会记录自己的事务id，所以自己再次查询也是可见


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
select * from yang; 
（事物3已结commit了）
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


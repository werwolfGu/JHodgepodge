# MySQL MVCC

## MySQL 存储文件
> 文件有三个 `redo log` `bin log` `undo log` 



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


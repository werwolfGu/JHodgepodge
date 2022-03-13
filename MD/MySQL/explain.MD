#执行计划 EXPLAIN


## EXPLAIN Output Columns


expain出来的信息有10列，
分别是`id`、`select_type`、`table`、`type`、`possible_keys`、`key`、`key_len`、`ref`、`rows`、`Extra`，下面对这些字段进行解释：

- id  
  执行编号，标识select所属的行。如果在语句中没子查询或关联查询，
  只有唯一的select，每行都将显示1。
  否则，内层的select语句一般会顺序编号，对应于其在原始语句中的位置
  
- select_type  
  显示本行是简单或复杂select。
  如果查询有任何复杂的子查询，则最外层标记为PRIMARY（DERIVED、UNION、UNION RESUlT）;
  
- table   
  访问引用哪个表（引用某个查询，如“derived3”）

- type  
  数据访问/读取操作类型（ALL、index、range、ref、eq_ref、const/system、NULL）
  
- possible_keys  
  揭示哪一些索引可能有利于高效的查找

- key  
  显示mysql决定采用哪个索引来优化查询

- key_len  
  显示mysql在索引里使用的字节数

- ref  
  显示了之前的表在key列记录的索引中查找值所用的列或常量
  
- rows  
  为了找到所需的行而需要读取的行数，估算值，不精确。
  通过把所有rows列值相乘，可粗略估算整个查询会检查的行数

- Extra  
  额外信息，如using index、filesort等
  

### id

>1. id相同时，执行顺序由上至下，内存会认为三个表，乘积小的先执行
    比如三个表：t3-t4-t5
    ：t3-t3-t4
>2. 如果是子查询，id的序号会递增，`id值越大优先级越高，越先被执行`  
>3. id如果相同，可以认为是一组，从上往下顺序执行；在所有组中，id值越大，优先级越高，越先执行

mysql> explain select * from (select * from ( select * from t3 where id=123) a) b;  
+----+-------------+------------+--------+-------------------+---------+---------+------+------+-------+  
| id | select_type | table      | type   | possible_keys     | key     | key_len | ref  | rows | Extra |  
+----+-------------+------------+--------+-------------------+---------+---------+------+------+-------+  
|  1 | PRIMARY     | <derived2> | system | NULL              | NULL    | NULL    | NULL |    1 |       |  
|  2 | DERIVED     | <derived3> | system | NULL              | NULL    | NULL    | NULL |    1 |       |  
|  3 | DERIVED     | t3         | const  | PRIMARY,idx_t3_id | PRIMARY | 4       |      |    1 |       |  
+----+-------------+------------+--------+-------------------+---------+---------+------+------+-------+  


### type

>system、const、eq_reg、ref、range、index、ALL（从左到右，性能从好到差）

- ALL  
  全表扫描
  
- index
  索引全表扫描  
  
- range  
  索引范围查询
  
>当使用=、<>、>、>=、<、<=、IS NULL、<=>、BETWEEN或者IN操作符，用常量比较关键字列时，可以使用range
  
- ref  
  触发条件：
  - 触发联合索引最左原则 ; 
  - 或者这个索引不是主键，也不是唯一索引（换句话说，如果这个在这个索引基础之上查询的结果多于一行）。
    
>在对已经建立索引列进行 = 或者 <=> 操作的时候，ref会被使用到
  
  
- eq_ref  
  和ref类似 不过  `eq_ref` 是 `主键索引`或`唯一索引`；
  
- const
  当MySQL对查询某部分进行优化，并转换为一个常量时，使用这些类型访问。
  
- system  
  当查询的表只有一行数据；
  

参考：  
http://www.cnitblog.com/aliyiyi08/archive/2008/09/09/48878.html
  

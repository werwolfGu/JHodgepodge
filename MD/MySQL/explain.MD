# 执行计划(EXPLAIN)


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

>system、const、eq_ref、ref、range、index、ALL（从左到右，性能从好到差）

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

### Extra

该列包含MySQL解决查询的详细信息,下面详细.

(1).Distinct
一旦MYSQL找到了与行相联合匹配的行，就不再搜索了

(2).Not exists
MYSQL优化了LEFT JOIN，一旦它找到了匹配LEFT JOIN标准的行，就不再搜索了

(3).Range checked for each

Record（index map:#）
没有找到理想的索引，因此对于从前面表中来的每一个行组合，MYSQL检查使用哪个索引，并用它来从表中返回行。这是使用索引的最慢的连接之一

(4).Using filesort
`看到这个的时候，查询就需要优化了。`MYSQL需要进行额外的步骤来发现如何对返回的行排序。它根据连接类型以及存储排序键值和匹配条件的全部行的行指针来排序全部行

(5).Using index
      using index代表使用覆盖索引，不用回表

(6).Using temporary
`看到这个的时候，查询需要优化了。`这里，MYSQL需要创建一个临时表来存储结果，这通常发生在对不同的列集进行ORDER BY上，而不是GROUP BY上

(7).Using where

​       不管有没有通过索引查找，只要加载了数据到内存进行where条件筛选，都是

​		使用了WHERE从句来限制哪些行将与下一张表匹配或者是返回给用户。如果不想返回表中的全部行，并且连接类型ALL或index，这就会发生，或者是查询有问题

参考：  
http://www.cnitblog.com/aliyiyi08/archive/2008/09/09/48878.html

[MySQL优化之EXPLAIN命令解析](https://juejin.cn/post/7073761727850119199)

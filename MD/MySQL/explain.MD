# 执行计划(EXPLAIN)


## EXPLAIN Output Columns


expain出来的信息有10列，
分别是`id`、`select_type`、`table`、`type`、`possible_keys`、`key`、`key_len`、`ref`、`rows`、`Extra`，下面对这些字段进行解释：

**id**：查询的序列号。用于标识查询中各个部分的顺序，复杂查询（如子查询、联合查询）会有多个id。

**select_type**：查询的类型，表示查询的复杂程度，如 `SIMPLE`（简单查询）、`PRIMARY`（最外层查询）、`SUBQUERY`（子查询）、`DERIVED`（派生表，子查询的结果）。

**table**：被访问的表名或派生表的名称。

**partitions**：使用的分区信息。如果表使用了分区，这里显示使用了哪些分区。

**type**：访问类型，表示MySQL如何找到数据行。常见类型有：

- `ALL`：全表扫描，性能最差。

- `index`：索引全扫描，比全表扫描快。

  ```SQL 
  CREATE INDEX idx_last_name ON users (last_name);
  -- 查询只访问索引列；
  EXPLAIN SELECT last_name FROM users;
  EXPLAIN SELECT last_name FROM users ORDER BY last_name;
  
  CREATE INDEX idx_name_age ON users (last_name, age);
  -- 联合索引，索引覆盖
  EXPLAIN SELECT last_name, age FROM users WHERE last_name = 'Doe';
  ```

- `range`：索引范围扫描，只扫描满足条件的索引范围。

  - 一般出现在`between` `in` `>` `>=` `<` `<=` `like`等情况会出现

  ```SQL
  -- age 是索引
  EXPLAIN SELECT * FROM users WHERE age > 25;
  EXPLAIN SELECT * FROM users WHERE age IN (20, 25, 30);
  EXPLAIN SELECT * FROM users WHERE age BETWEEN 20 AND 30;
  -- last_name是索引
  EXPLAIN SELECT * FROM users WHERE last_name LIKE 'D%';
  -- 联合索引：last_name first_name 
  EXPLAIN SELECT * FROM users WHERE last_name = 'Doe' AND first_name BETWEEN 'A' AND 'M';
  ```

  

- `ref`：使用非唯一索引扫描，返回匹配某个单独值的所有行。

  - 使用等值查询 `=` 

  ```SQL 
  -- 索引：age
  EXPLAIN SELECT * FROM users WHERE age = 30;
  -- 联合索引：last_name, age
  EXPLAIN SELECT * FROM users WHERE last_name = 'Doe' AND age = 30;
  ```

  

- `eq_ref`：当查询中的某个表通过唯一索引或主键与另一个表（也是主键或唯一键）进行连接时，MySQL 可能会选择 `eq_ref` 访问类型。

  ```SQL 
  -- id 为主键 ； user_id为外键
  EXPLAIN SELECT * FROM users u JOIN orders o ON u.id = o.user_id;
  ```

  

- `const`、`system`：常量表，最多只返回一条记录，性能最好。

```SQL 
-- const 
-- id 是主键
EXPLAIN SELECT * FROM users WHERE id = 1;
EXPLAIN SELECT * FROM users u JOIN orders o ON u.id = o.user_id WHERE u.id = 1;

-- system 通常表中只有一条数据
```



**possible_keys**：查询可能使用的索引。

**key**：实际使用的索引。如果为空，表示没有使用索引。

**key_len**：使用的索引键的长度，表示MySQL在索引中使用的字节数。

**ref**：显示索引的哪一列被用来查找索引记录，通常是常数。

**rows**：MySQL 估计为找到所需数据需要读取的行数，越少越好。

**filtered**：返回结果行的百分比，表示查询条件过滤掉的数据行比例。

**Extra**：额外的信息，描述MySQL在查询过程中采取的操作。常见的有：

- `Using index`：表示查询只使用索引，不需要访问表数据。
- `Using where`：表示使用了WHERE子句进行过滤。
- `Using temporary`：表示使用了临时表，通常用于排序或分组。
- `Using filesort`：表示MySQL使用了文件排序，而不是索引排序。

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

# MySQL索引

## 索引类型

- **B-Tree索引**

  - **用途** ：用于大多数据类型的查询，尤其是范围查询和排序操作；
  - **特性**： B-Tree索引结构平衡，适合于等值查询和范围查询；

- **哈希索引**

  - **用途**：用于等值查询；
  - **特性**：基于hash表，查询速度快，但不适用于范围查询；

- **聚簇索引**

  > 聚簇索引是通过B+ 树结构实现的；

  - **用途**：InnoDB使用聚簇索引来组织数据表；
  - **聚簇索引特点**
    - 在聚簇索引中，表的行数据实际存储在索引的叶子节点中；
    - 数据表按照聚簇索引的键值顺序进行物理存储；
  - **唯一性**
    - 每个表只有一个聚簇索引，因为数据行是直接存储在叶子节点上；
    - 一般情况下，表主键是聚簇索引，如果表没有主键，InnoDB会选一个非空唯一索引作为聚簇索引，如果也没有，会生成一个隐藏的聚簇索引；

## 索引优化

- 索引选择和优化

  - 选择合适的索引

    - 优先在经常出现在 `where` `join` `order by` `group by ` 中的列创建索引；
    - 不建议在选择性较低的列创建索引；比如：`性别` `状态` 等；

  - 索引覆盖

    - 通过在索引包含所需的所有列，可避免回表操作，提高查询性能；

    ```SQL
    -- 创建联合索引；
    CREATE INDEX idx_covering ON table_name(column1, column2, column3);
    -- 下面这个语句实现了索引覆盖
    select column1 , column2 , column3 from table_name where column1='xxx' and column2='xxx' and column3='xxx'
    
    ```

  - 前缀索引

    - 对于长文本字段，可以常见前缀索引以节约空间；

    ```SQL 
    CREATE INDEX idx_prefix ON table_name(column_name(10));
    
    ```

  - 联合索引

    - 创建多列索引；通过联合索引查询数据时，需要满足`最左匹配原则`;

  - 索引下推

    ```sql
    select * from user where name like 'gce%' and age='18';
    ```

    如上SQL创建了联合索引(name , age) 根据索引最左匹配原则会走索引，MySQL 5.6 后name查询到后会再根据age查询是否满足18 然后再回表；

  

## 索引维护

- 定期使用`ANALYZE TABLE`命令分析表并更新索引统计信息。
- 使用`OPTIMIZE TABLE`命令重建表和索引，优化存储空间。

```SQL 
ANALYZE TABLE table_name;
OPTIMIZE TABLE table_name;
```






#MySQL数据库分库分表
    当存储在数据库中的数据越来越多时，DB访问已经成为了系统的瓶颈，这时我们就需要考虑对数据库表进行拆分(水平拆分、垂直拆分)；随着数据的不断增加
    表拆分已经不能满足我们的需求，这时我们就需要考虑分库；
    
## 数据表拆分
    数据表拆分包括垂直拆分和水平拆分；
    
- 垂直拆分

当一张表的字段过多时，这时我们可以考虑对数据表进行垂直拆分成多张表；通常将使用频繁的字段放在一张表，其余字段放在另一张表中；`查询不建议使用join原因后面说`

- 水平拆分

当表中的数据非常多时，通过`SQL优化/索引优化`都已经不能满足我们的要求时，此时可以考虑将表拆分；通常的拆分策略：`ID范围拆分` `按时间拆分` `hash拆分`；
比如用户表、我们可以通过user_id的取值返回来拆分；或者用户创建时间来拆分；

## 系统分库
    当表拆分已经无法满足我们的需求时我们可以通过分库实现数据的访问压力；
    
    
拆分策略
- 查询拆分
        
将ID和库的mapping关系存在一个单独的库中；
        
![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/db_1.png)
    
- 范围拆分
    
比如按时间范围或按ID范围拆分；
        
![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/db_2.png)
        
- hash拆分       
     
一般采用mod来拆分;

![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/db_3.png)
        
## 下面重点介绍下mod策略

如图所示

![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/db_4.png)    

对比数据库拆分我们希望是一劳永逸，可以很方便的进行水平扩展的，这里我们使用`mod 2^n` 
数据库达到瓶颈原有的规则不变 将数据库数量增加一倍如图

![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/db_5.png) 

>在之前有4个数据库，现在扩展成8个数据库`db5`存有`db1` 的冗余`5 == hash(key)%8`的数据`db6`有`db2`的冗余`5 == hash(key)%8`的数据...按之前hash(key) mod 4 ；现在mod 8 ；
比如现在 key为5 ；之前是落到db1 ；但现在落到db5不过是有db1的数据，这样就可以平滑实现数据库扩张；以此类推；水平分表同理

- 如果使用[一致性hash算法](https://github.com/werwolfGu/JHodgepodge/blob/master/MD/consistent_hash.md)实现分表分库可以随便增加几个数据库节点；亦无需按倍数来增加数据库

参考 [大众点评订单系统分库分表实践](https://tech.meituan.com/dianping_order_db_sharding.html)

# MySQL事务隔离级别

## MySQL四种事务隔离级别

1. 未提交读
   
   可能出现脏读
   
2. 已提交读

    会出现重复读
 
3. 可重复读

    可能会出现幻读
 
4. 串行化

MySQL默认使用3 可重复读事务隔离级别


## MySQL怎么实现可重复读

### MySQL3个日志文件

> `redo log` `bin log` `undo log`

#### Redo log 的用途
为了保证数据能正确的持久化，在系统出现异常的时候通常会对redo log进行回放，把已经commit的事务进行数据重做。  


      Redo Log记录的是新数据的备份。在事务提交前，只要将Redo Log持久化即可，不需要将数据持久化。当系统崩溃时，虽然数据没有持久化，但是Redo Log已经持久化。系统可以根据Redo Log的内容，将所有数据恢复到最新的状态。

#### Undo log 的用途

1. 保证事务进行rollback时的原子性，当事务进行回滚或者系统异常需要对数据进行回滚的的时候可以用undo log的日志进行数据重做。

2.  用于`MVCC快照读`的数据，在MVCC多版本控制中，通过读取undo log的历史版本数据可以实现不同事务版本号都拥有自己独立的快照数据版本。


      Undo Log的原理很简单，为了满足事务的原子性，在操作任何数据之前，首先将数据备份到一个地方（这个存储数据备份的地方称为Undo Log）。然后进行数据的修改。如果出现了错误或者用户执行了ROLLBACK语句，系统可以利用Undo Log中的备份将数据恢复到事务开始之前的状态。
      除了可以保证事务的原子性，Undo Log也可以用来辅助完成事务的持久化。

#### redo log 和undo log 的生成过程  
        
        事务操作：将user表进行 update user set name = '顾彻' where id=1 
        
整体流程如下：
1. 在修改数据前，把需要修改的数据load到内存中；
2. 将原数据写入到undo log中；
3. 将修改后的数据写入到redo log中；
4. 当commit / rollback 时，将redo log  / undo log 中的数据修改到表中；

事务持久性的保障：
持久化性是在系统出现异常、崩溃时依然能保证我们的数据能正常持久化到数据库中，在系统出现异常或崩溃时，
我们会对redo log进行回放，对于已经commit的事务进行数据重做，对于事务没有commit的事务，我们可以通过
undo log来对事务涉及到的数据回滚从而保证事务数据的正确性；

### MVCC(多版本控制实现) 可重复读

对于每一行数据都会增加 trx_id  和 roll_pointer


ReadView 说白了就是一种数据结构，它主要包含这样几部分：

m_ids，当前有哪些事务正在执行，且还没有提交，这些事务的 id 就会存在这里；
min_trx_id，是指 m_ids 里最小的值；
max_trx_id，是指下一个要生成的事务 id。下一个要生成的事务 id 肯定比现在所有事务的 id 都大；
creator_trx_id，每开启一个事务都会生成一个 ReadView，而 creator_trx_id 就是这个开启的事务的 id。




参考：
https://juejin.cn/post/6844903808376504327  

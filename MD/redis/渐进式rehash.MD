# redis-rehash

## redis渐进式rehash

### 背景

> Redis 使用哈希表来存储键值对，当哈希表中的元素数量增加到一定程度时，为了避免过多的哈希冲突，Redis 会进行 rehash 操作，即重新计算哈希值并调整元素在哈希表中的位置。

### 渐进式rehash

> Redis实现了渐进式rehash，这种机制下，rehash操作分多次逐步完成；

- 为何要进行渐进式rehash

  性能影响：如果哈希表中的所有元素同时进行rehash，可能会导致Redis在短时间内无法处理其他请求，从而影响性能；

- 实现原理

  Redis 会维护两个哈希表，一个是旧的哈希表，另一个是新的哈希表。当新元素添加到哈希表时，它们会被放入新的哈希表中。同时，Redis 会逐渐将旧哈希表中的元素迁移到新的哈希表。

- 触发条件

  当哈希表的负载因子（即哈希表中的`元素数量`除以`哈希表的大小`）超过某个阈值时，Redis 会决定进行 rehash 操作

  >  一般情况下负载因子 `大于等于` 1 就会触发rehash ； 如果在RDB过程中或AOF重写过程中 一般 负载因子 `大于等于` 5 才会触发

- 执行步骤

  > <span style="color:red;">**(步骤2 和步骤3 是`串行执行`的 ；即`rehash复制元素`和`客户端请求`)**</span>

  1. 创建新的hash表：新hash表大小一般是旧hash表的2倍，以减少后面rehash的频率；

  2. 复制元素：将旧hash表中的元素复制到新的hash表中，这个过程不是一次完成，而是在处理客户端请求过程中逐步完成 。

  3. 处理请求：在渐进式rehash期间，Redis会同时维护2张hash表，当客户端请求访问hash的元素时，先访问`新hash表`，如果元素不存在，再检查`旧hash表`；<span style="color:red;">**（写操作只会写新hash表）**</span>
  4. 重复执行 `步骤2` 和 `步骤 3`；
  5. 完成迁移：旧hash表所有元素都迁移到新hash表后，丢弃旧hash表，使用新hash表来处理所有请求；



参考：
http://redisbook.com/
http://redisbook.com/preview/dict/incremental_rehashing.html
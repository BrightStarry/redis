####数据库学习
    
union 取两个select的合集，重复记录只显示一次  
union all 求合集，显示所有记录
intersect 求交集
minus 集合相减，前面的-后面的

锁：
共享锁：排斥其他排它锁，不排斥共享锁.    
排他锁（独占）:排斥其他排他锁和共享锁。

锁类型:
DML锁（data locks）:保护数据的完整性.
TX(行级锁).TM(表级锁)。
DDL（数据字典锁），保护对象的结构，如表，索引等.
SYSTEM锁，保护数据的内部结构。

只有事务会产生锁，保证数据的完整性和正确性

---
---
数据库表的设计
1. 业务要学会切分
2. 逻辑分层（数据库分层），基础信息，业务信息
3. 表结构设计与拆分
    (1)水平拆分mysql。
    (2)分区oracle
    (3)物化视图，真实存储起来的视图，oracle
    (4)中间表
    
结构优化的设计
1. 建立索引
2. 可以直接在增加的时候就用id区分类型，比如都以1000开头，然后男性用10001001,女性用10002001
，这样查询的时候只要查询id在10001001xxx-10001001yyyy间的就是男性
3. 合理冗余

---
---
垂直拆分
根据业务，进行分表，也就是把一个A表的字段进行切分，可以分成A表和B表。
优点：逻辑清晰，扩展性强，维护简单

水平拆分
假设一个A表有若干条数据，可以根据type，放到A1、A2、A3表中。也可以根据Id，比如id%3 ,
结果可能是0,1,2，这样，也可以拆分成三张表。
---
---
分布式事务
1. 如果业务逻辑复杂，数据量不大，可以用soa，在service上做多个切面（aop），配置多个事务
2. mycat
3. 存入缓存，定时更新

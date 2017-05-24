####Redis 学习

####redis策略
1. 主从模式：主节点可写，其下有两个从节点可写。
2. 哨兵模式：一台主节点，若干个从节点。还有一个哨兵。如果主节点挂了，就从若干从节点中选取一个成为新的主机点。等那个主节点修复
好了，那个主节点就成为了从节点。（无法分布式）
3. 集群模式：支持多主多从。水平扩展：也就是如果有一主二从，那就设置多个一主二从。

RDB 每个几秒把文件写到硬盘，保存成rdb文件，可以在redis.conf文件中的dir属性指定保存位置
AOF 持久化记录服务器执行的所有写操作命令，并在服务器启动时，通过重新执行这些命令来还原数据集。（可能会影响redis写入性能）

---
####安装
1.
下载地址http://redis.io/download
安装步骤：
1 首先需要安装gcc，把下载好的redis-3.0.0-rc2.tar.gz 放到linux /usr/local文件夹下
2 进行解压 tar -zxvf redis-3.0.0-rc2.tar.gz

3 进入到redis-3.0.0目录下，进行编译 make
4 进入到src下进行安装 make install  验证(ll查看src下的目录，有redis-server 、redis-cil即可)

5 建立俩个文件夹存放redis命令和配置文件

mkdir -p /usr/local/redis/etc
mkdir -p /usr/local/redis/bin

6 把redis-3.0.0下的redis.conf 移动到/usr/local/redis/etc下，
 
   cp redis.conf /usr/local/redis/etc/

7 把redis-3.0.0/src里的mkreleasehdr.sh、redis-benchmark、redis-check-aof、redis-check-dump、redis-cli、redis-server 
文件移动到bin下，命令：

mv mkreleasehdr.sh redis-benchmark redis-check-aof redis-check-dump redis-cli redis-server /usr/local/redis/bin
mv mkreleasehdr.sh redis-benchmark redis-check-aof redis-check-dump redis-cli redis-server /zx/redis/bin

8 启动时并指定配置文件：./redis-server /usr/local/redis/etc/redis.conf（注意要使用后台启动，所以修改redis.conf里的 daemonize 改为yes)

9 验证启动是否成功：
ps -ef | grep redis 查看是否有redis服务 或者 查看端口：netstat -tunpl | grep 6379

进入redis客户端 ./redis-cli 退出客户端quit
退出redis服务： 
（1）pkill redis-server 、
（2）kill 进程号、
                            
（3）/usr/local/redis/bin/redis-cli shutdown 

---
2.
http://blog.csdn.net/csujiangyu/article/details/49278801

http://www.imooc.com/article/16526 

http://blog.csdn.net/han_cui/article/details/54098061

$ wget http://download.redis.io/releases/redis-3.2.8.tar.gz

$ tar xzf redis-3.2.8.tar.gz

$ cd redis-3.2.8

$ make

make PREFIX=/usr/local/redis install 这个可以设置安装路径

cp redis.conf /usr/local/redis/bin redis.conf是redis的配置文件，安装的时候不会在安装目录自动生成，所以要手动从redis的解压目录里拷贝过去(还是在解压目录中操作)
好像还有一种是进入 src 执行 make install 进行安装

2、run(在redis-3.2.8目录中)

$ src/redis-server

或者

$ src/redis-server redis.conf

或者

修改redis.conf配置文件，修改daemonize的值为yes——后端模式启动

redis停止命令：pikll redis-server 或者  src/redis-cli shutdown 或者 kill 进程号

！！！如果需要连接虚拟机的redis，需要修改 redis.conf,且每次启动都需要使用这个redis.conf启动
    1.将 bing 127.0.0.1 注释
    2.增加密码，或者  protected-mode no
---
redis 增加密码

设置redis.conf 中的 requirepass xxxx

然后在redis-cli中使用 auth [密码] 登录。 也可以直接 redis-cli -a [密码]登录。

---
查看端口信息
ps -ef|grep redis

在redis-cli中 使用 info查看这个redis信息，有角色等

---
使用静态IP  http://blog.csdn.net/johnnycode/article/details/40624403

1. 使用ifconfig 命令查看目前使用的ifcfg-xx是多少。
2. 使用vim /etc/sysconfig/network-scripts/ifcfg-xxx 命令修改该配置
3. 修改如下内容：
    BOOTPROTO="static" #dhcp改为static   
    ONBOOT="yes" #开机启用本配置  
    IPADDR=192.168.7.106 #静态IP  
    GATEWAY=192.168.7.1 #默认网关  
    NETMASK=255.255.255.0 #子网掩码  
    DNS1=192.168.7.1 #DNS 配置 
4. 保存，并重启网络。service network restart
---
---
####Redis数据结构
flushdb：清空
1. String
    
    set x y 存入； get x 取值；setnx  不存在则存入，存在则不设置； setex x 10 y 设置x的有效期为10s；setrange x 10 xx 表示从x的第十位开始替换成xx
    
    mset mget 一次性设置和取多个值；incr decr 对某个值递增递减；incrby decrby 指定大小的递增递减；append 字符串追加；strlen 获取字符长度
2. Hash(用的最多) 类似map，适合存储对象
    
    也就是一个key为user的hash结构中，可以存储多个key，对应对应多个value。如果每次存取不是一整条记录的存取，而是存取user中的某个key，可以设置
    成和数据库表中对应的结构；如果存取都是一整条的，可以在key为user的hash中，设置一个id和一个content，然后在content里面直接存储json格式的整条记录。
    
    hset name field1 value field2 value 存；hget name field1 取；hmset hmget 批量存取，这个批量是指一次性把hash的某个key的所有字段和值添加；hexists 是否存在key；
    hdel 删除指定hash的field；hkeys 返回hash所有value；hgetall 返回hash里所有key和value；
3. List,类似queue.是一个双向链表结构。也就是既可以做栈（LIFO），也可以做队列（FIFO）。  
    
    lpush:往头部加入元素（栈，LIFO）；rpush：往尾部加入元素（队列，FIFO）；lrange name 0 -1 取出所有元素；
    linsert 在某个值前后插入元素；lset 替换指定下标的元素；lrem 删除元素，返回删除个数；
     lpop 从头部删除元素，返回被删除的元素；rpop 从尾部删除元素，返回被删除的元素；
     rpoplpush 先从尾部删除元素，然后从头部加入元素；lindex 返回元素索引；llen返回元素个数;
     
     blpop key timeout:lpop的阻塞版，timeout设置为0，表示永远阻塞。
     brpop key timeout :rpop的阻塞版，timeout设置为0.表示永远阻塞。
     
4. Set：是string类型的无序集合，通过hashtable实现，可以取此集合的交集、并集、差集。

    sadd:添加；srem删除；spop：随机返回删除的key；sdiff返回两个集合中不同的元素；sdiffstore 返回两个集合不同元素并存储；
    sinter：返回集合的交集；sinterstore：返回交集结果，并存储；sunion：取并集；sunionstore取并集，并存储。
    smove：移动set到另一个set；scard查看元素个数；sismember判断元素是否存在集合中；srandmember随机返回一个元素
5. Zset 有序set，可以做rank（排行榜，权重，看哪个数据和搜索关键词更匹配）
    
    zadd添加数据，如果存在，则更新顺序；zrem删除；zincrby 指定大小的增加或减少；
    zrangebyscore 找到指定范围（权重）的数据并返回；zremrangebyrank删除权重x-y间的；
    zremrangebyscore 删除指定序号；zrank 返回排序索引，从小到大。zrevrank 返回排序索引，从大到小;
    zcard 元素个数；zcount 指定权重的元素个数；

---
---
####Redis高级命令
 keys * 返回匹配的键，可以模糊匹配; exists 是否存在这个key;

expire 设置key过期时间； ttl查看key过期时间； persist 取消过期时间；

select 选择数据库，分为0-15，共16个，默认进入0；（早期没有集群，redis内部被分为16份，逻辑上的划分）

move [key] [数据库下标] 将当前数据库中的key移到其他数据库

randomkey 随机返回数据库中一个key； rename 重命名key；

echo 打印命令； dbsize查看key数量； info 获取数据库信息；

flushdb/flushall：清空当前/所有数据库; config get * 返回配置项

---
---
####Redis主从复制
主服务器主要负责数据写入，从服务器主要负责读取；

Master(主)可以有多个slave(从)；
多个slave可以连接同一个Master外，还可以连接其他的slave；
主从复制不会阻塞master，在同步数据时，master可以继续处理client请求。

---
主从复制过程
1. slave与master建立连接，发送sync同步命令。
2. master会开启一个后台进程，将数据库快照保存到文件中，同时master主进程开始收集新的写命令并缓存
3. 后台保存完成后，将文件发送给slave
4. slave将文件保存到硬盘上

---
主从复制配置
    clone服务器(linux,改下mac)。也就是弄出3台固定ip的安装了redis的linux。
    然后只要修改slave就可以了。修改redis.conf：1. slaveof <masterIp> <masterPort> 2.masterauth <master-password>（如果有密码）
    
配置完成后，slave将只能够读，无法写入。

---
---
####Redis哨兵 (高可用) 一台服务器可以同时启动redis主或从，和哨兵
监控主从服务器，如果出现故障，可以替换掉主服务器，让从服务器升级为主服务器。故障服务器修复后，将成为从服务器。
因为选举主服务器，最好总的节点数是奇数个

实现步骤:
1. 修改redis目录下sentinel.conf文件
    
    sentinel| monitor [名称] [ip] [端口] [x个sentinel断定其宕机才进行故障转移] #设置监控的主节点(Master)
    
    sentinel| down-after-milliseconds [名称] 5000  #多久检测一次
    
    sentinel| failover-timeout [名称] 900000  #故障转移超时时间，就是主节点挂了，进行切换，切换超时时间？
    
    sentinel| parallel-syncs [名称] 2  #在执行故障转移时，最多可以有多少个Slave同时对新的Master进行同步。这个数字设置为1，虽然完成故障转移所需的时间会变长，但是可以保证每次只有1个Slave处于不能处理命令请求的状态
    
    daemonize yes #后台启动
    
2. 启动哨兵
 
redis-server sentinel.conf --sentinel
或者
redis-sentinel sentinel.conf （推荐）

3. 查看哨兵相关信息命令(哨兵默认端口26379)

redis-cli -h 192.168.0.188 -p 26379 info Sentinel

4. 关闭主服务器查看集群信息

redis-cli -h 192.168.0.188 -p 6379 shutdown

查看哨兵集群状态
redis-cli -p 26379
sentinel master mymaster//查看master的状态 
SENTINEL slaves mymaster //查看salves的状态
SENTINEL sentinels mymaster //查看哨兵的状态
SENTINEL get-master-addr-by-name mymaster//获取当前master的地址
info sentinel//查看哨兵信息
---
---
####Redis事务
使用multi打开事务，然后进行设置，此时设置的数据都会被放入队列（同一事务）进行保存，最后使用exec执行。
使用discard取消事务。
---
---
####Redis持久化机制
1. RDB(snapshotting,快照)，默认方式。将内存中的数据以快照的方式写入到文件中，默认为dump.rdb,
可以配置文件目录，以及x秒内如果超过m个key被修改就自动做快照。
    save 900 1 #900s内超过1个key被修改，则保存快照
    不希望将数据同步到快照文件中，可以设置为save “”
2. AOF(append-only file，附加唯一文件)，类似日志。由于RDB有时间间隔可能发生宕机，丢失数据。
AOF会讲每一个收到的写命令都通过write方法追加到命令中，当redis重启时会重新执行文件中保存的写命令
在内存中重建数据。（开启aof后，rdb失效）
    
    文件保存到redis.conf配置中的dir设置中。appendonly.aof,它不是立即写到硬盘上，可以通过修改配置，强制写到硬盘。
    
    appendonly yes #启用aof
    AOF有三种写入方式：
        1. appendfsync always #收到命令就马上写入硬盘，效率最慢，但是能保证持久化
        2. appendfsync everysec #美妙写入一次磁盘，折中了性能和持久化的保证。
        3. appendfsync no #完全依赖os（计算机开心了，再写），性能最好，持久化没保证
---
---
####Redis发布与订阅消息
使用subscribe [频道]进行订阅监听（可以监听多个频道）；使用publish [频道] [发布内容]进行消息广播;

---
---
####Jedis
单点的可以直接使用Jedis类；主从或哨兵使用ShardJedis类（分片）。
分片（redis2.0，就是默认16的那个分区），类似disruptor中的ringBuffer。使用hash区分每个片。

使用redis进行类似select * from user where age > 20的操作,假设有千万条数据，
肯定是不能全部取出来，然后遍历去判断的（麻痹的，貌似我之前的黄网爬虫就是这么干的）。

那么可以将user这个表要存入的key分类。假设使用set类型。
可以将key设置为 user_25、user_30、user_m、user_w，也就是年龄25的存入25岁的user_25这个key中，
男性存入user_m这个key中，然后所有数据都分类存。
并且，如果多个条件，比如想要大于20且为男性，就可以取两个key的交集。（我草，厉害了）

然后上面set只存id，获取到对应的id后，就可以去hash类型的key中获取数据具体的值了。
（将user转成json字符串，以id为key放入map，然后使用jedis.hmset()方法map存入)

---
---
####Redis集群
至少需要3个Master。和3个slave，总共6个节点。
每个节点都需要知道其他所有节点的存在，所以需要配置noodesxxx.conf
1. 创建一个文件夹redis-cluster，然后在下面分别创建6个文件夹，把这个6个文件夹模拟成6个redis节点
    mkdir redis-cluster;
    mkdir 001; mkdir 002;...
2. 给每个目录拷贝redis.conf，然后修改成不同的port。并且配置文件中，
    port [不同端口]
    bind [本机ip] #必须要绑定，不然是深坑——听说（可能会在取值或设置的时候无限重定向redis节点）
    dir [文件路径] #必须是不同路径，不然会丢失数据
    cluster-enabled yes #开启集群模式
    cluster-config-file nodes7000.conf  #设置节点配置文件，7000就是对应的端口号，最好。
    cluster-node-timeout 5000
    appendonly yes #开启aof
    
    哨兵模式所有主从节点数据都是一样的，集群模式每个节点的数据是不同的（槽）.
3. 安装ruby
    yum install ruby
    yum install rubygems 这个好像已经有了
    gem install redis 安装redis和ruy的接口
    
    然后使用同一个redis-server，但使用每个文件夹下不同的redis.conf启动6个redis。
    使用netstat -tunpl | grep redis命令查看是否启动完成
4. 然后到redis/src下，执行redis-trib.rb命令

    ./redis-trib.rb create --replicas  1 
    192.168.0.104:7001 192.168.0.104:7002 192.168.0.104:7003 192.168.0.104:7004 
    192.168.0.104:7005 192.168.0.104:7006 192.168.0.104:7007
    
    这个redis-trib.rb就是专门用来操作集群的。里面的 1表示集群中主从节点的比例，也就是主节点/从节点。
    且，下面的所有redis实例，前几个就是主，后面的就是从，具体的按照比例。
    
    集群中，每个节点存的数据不同，每个主节点都有自己对应的槽(slots分片)。从节点没有槽（不支持写操作）。
5. 至此搭建完成，进行验证。
    连接任何一个客户端， ./redis-cli -c -h -p (-c表示集群模式，-h表示主机，-p表示端口)
    例如 ./redis-cli -c -h 192.168.0.104 -p 7001
    
    进入redis-cli后，cluster info 查看集群信息.cluster nodes 查看节点信息，cluster nodes查看节点列表
    
    然后如果在x端口的redis-cli中存入数据，数据并不一定会存入当前redis实例，而可能会存入其他主节点。
    这样的话，当前端口的redis中是没有这条存入的数据的，因为集群的redis中，数据在主节点中并不是同步的。
    而是分为若干个槽，每个节点只存储对应槽的数据（相当于水平分表）。从节点也是只存储主节点对应槽的数据，
    但是，在任何一个redis节点中，可以取出任何一个节点的数据。如果数据不在该节点下，就会自动从对应节点获取。
6. 关闭集群需要逐个关闭，使用命令 redis-cli -c -h xxx.xx.xx.xx -p xxxx shutdown
    
    如果出现集群无法启动时，删除临时的数据文件，然后重启每个redis服务，重新构造集群环境。
    
还可以个集群作增加节点，分配槽等操作。

---
####Java操作redis实例
Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
jedisClusterNode.add(new HostAndPort("192.168.0.104",7001));
jedisClusterNode.add(new HostAndPort("192.168.0.104",7001));

JedisPoolConfig cfg = new JedisPoolConfig();
cfg.setMaxTotal(100);

JedisCluster jc =new JedisCluster(jedisClusterNode,6000,100,cfg);

然后直接操作jc就可以了
具体看test中的RedisTest.

如果要整合spring ，除了直接使用spring redis，还可以直接只是用spring配置对应的bean，最后注入一个JedisCluster就可以了。


很详细的spring-data-redis
http://blog.csdn.net/zhu_tianwei/article/details/44923001

---
---
####Redis实现session共享
1. 使用github上的TomcatRedisClusterEnabledSessionManager

2. 这篇文章介绍的是使用spring-session和spring-data-redis实现的session共享。且无需依赖web容器（tomcat）
http://blog.csdn.net/xiao__gui/article/details/52706243
---
---
### Redis与Lua  http://www.runoob.com/lua/lua-tutorial.html
lua:简单的脚本语言
编写以.lua为后缀的文件，使用lua命令执行即可。

Lua的安装
1. 安装依赖
yum install -y readline
yum install -y readline-devel
2. 解压
tar -zxvf /zx/lua-5.3.4.tar.gz
3. 安装
cd lua-5.3.4
make linux
make install
4. 测试
命令行直接输入lua进入lua Client
输入print("hello world")
---
redis与lua结合
只要在lua脚本中写好对应的业务逻辑，然后使用redis.call函数去操作即可，

使用redis执行lua脚本
/zx/redis/bin/redis-cli --eval /zx/test.lua


http://blog.csdn.net/hengyunabc/article/details/19433779/

    




    



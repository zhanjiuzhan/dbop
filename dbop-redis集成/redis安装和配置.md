# Redis 安装和配置
## 1. Redis 安装
1. [官网](https://redis.io/download)查看或者下载最新版本  , redis 不支持window版本, 不过可以通过 [github](https://github.com/microsoftarchive/redis/releases) 下载到windows用的版本。
```shell
# 版本你自己选择最新的就行
wget https://download.redis.io/releases/redis-5.0.9.tar.gz
tar xzf redis-5.0.9.tar.gz
cd redis-5.0.9
make
```
2. make 若出错的话, 需要[更新 gcc 版本到最新](https://www.cnblogs.com/zuxing/articles/8708011.html), 或者安装老版本的 redis。make 后我们回发现多了 src和deps文件夹。
3. 解压的文件目录下执行命令
```shell
make install PREFIX=/usr/local/redis
cp redis.conf /usr/local/redis/
```
4. 到安装目录启动redis
```shell
cd /usr/local/redis/bin/
./redis-server

# 默认的启动为前端启动 所以需要修改为后端启动
vim ../redis.conf 
# 修改 daemonize no -> yes
# 重新启动
./redis-server ../redis.conf

# 查看启动结果
ps -ef | grep redis

# 停止启动
./redis-cli shutdown

# 开启启动
vim /etc/rc.local 
# 添加  /usr/local/redis/bin/redis-server ../redis.conf

# 添加path
vim /etc/profile
# 追加  export PATH=$PATH:/usr/local/redis/bin/ 
source /etc/profile  
```

> + 至此 redis 安装完成, 我们可以通过 redis-cli -h localhost -p 6379 启动命令行界面, 进行 redis 操作。  
> + redis-server 用于启动或者停止redis  
> + redis.conf 用于配置redis的服务信息  

## 2. Redis 配置
配置文件 redis.conf(Windows 名为 redis.windows.conf)。  
可通过修改配置文件, 然后启动, 也可以用config set|get 来查看或者修改配置属性。

| 配置参数 | 值 | 说明 |
| :---- | :---- | :---- |
| daemonize | no, yes | 默认是no, yes是以守护进程启动的意思, windows不支持 |
| pidfile | 路径 | 默认 /var/run/redis_6379.pid. 当redis以守护进程启动时, 指定pid保存文件的位置 |
| port | 数字 | 默认 6379 |
| bind | ip | 默认 127.0.0.1 绑定主机号 |
| timeout | 秒 | 默认 0 , 当客户端闲置指定秒会自动关闭连接, 0 关闭此功能 |
| loglevel | debug、verbose、notice、warning | 默认 notice |
| logfile | 路径 | 默认 "" 标准输出, 若为守护进程, 且 "", 则指向 /dwv/null |
| databases | 数字 | 默认 16, 设置数据库數量 |
| save <seconds> <changes> | 数字 | 指定在多长时间(秒)内，有多少次更新操作，就将数据同步到数据文件，可以多个条件配合 |
| rdbcompression | no, yes | 指定存储至本地数据库时是否压缩数据，默认为 yes，Redis 采用 LZF 压缩，如果为了节省 CPU 时间，可以关闭该选项，但会导致数据库文件变的巨大 |
| dbfilename  | 字符串 | 默认 dump.rdb |
| dir  | 目录 | 默认 ./ , 本地数据库存放目录 |
| slaveof <masterip> <masterport> |  | 默认被注解, 设置当本机为 slave 服务时，设置 master 服务的 IP 地址及端口，在 Redis 启动时，它会自动从 master 进行数据同步 |
| masterauth <master-password> |  | 默认被注解, 当 master 服务设置了密码保护时，slav 服务连接 master 的密码 |
| requirepass | 密码 | 默认被注解(无密码), 设置密码 |
| maxclients | 数字 | 默认被注解(无限), 同一时间最大客户端连接数, 0 为不限制, 当达到最大会新连接并返回max number of clients reached 错误信息 |
| maxmemory <bytes> | 数字 | 默认被注解, Redis 在启动时会把数据加载到内存中, 达到最大内存后, Redis 会先尝试清除已到期或即将到期的 Key, 当此方法处理 后，仍然到达最大内存设置, 将无法再进行写入操作, 但仍然可以进行读取操作。Redis 新的 vm 机制，会把 Key 存放内存，Value 会存放在 swap 区 |
| appendonly | no, yes | 默认为 no, 指定是否在每次更新操作后进行日志记录，Redis 在默认情况下是异步的把数据写入磁盘，如果不开启，可能会在断电时导致一段时间内的数据丢失。因为 redis 本身同步数据文件是按上面 save 条件来同步的，所以有的数据会在一段时间内只存在于内存中 |
| appendfilename | 字符串 | 默认为 appendonly.aof, 指定更新日志文件名 |
| appendfsync | everysec, always, no | 指定更新日志条件, no：表示等操作系统进行数据缓存同步到磁盘（快）, always：表示每次更新操作后手动调用 fsync() 将数据写到磁盘（慢，安全）, everysec：表示每秒同步一次（折中，默认值） |
| activerehashing  | no, yes | 默认yes, 指定是否激活重置哈希 |
| include | 路径 | 默认被注解, 指定包含其它的配置文件 |

[详细配置1](https://www.cnblogs.com/metu/p/9609604.html) [详细配置2](https://www.cnblogs.com/ysocean/p/9074787.html)

## 3. 主从模式配置
### 特点
1) 读写分离, 缓解主节点压力。
2) 数据安全, 数据备份在多个从节点, 只要有一个可用, 那么都可以恢复数据。
3) 从节点也可以写, 但是不会同步到主节点和其它节点。 主节点宕机, 其它节点不会竞争成为主节点, redis 丧失写的能力。
4) 从节点也可以有从节点。
### 原理
同步时复制初始化(乐观复制)
1) 从节点启动后会向主节点发送sync命令。
2) 主节点收到命令后开始后台保存快照(RDB持久化, 主从赋值无条件触发RDB), 并将期间收到的命令缓存起来, 快照完成后，redis会将快照文件和所有缓存命令发送给数据库
3) 从数据库接收到快照文件和缓存命令后，会载入快照文件和执行命令

复制初始化结束后
1) 主数据库每当受到写命令时，就会将命令同步给从数据库，保证主从数据一致性

* 在Redis2.6之前，每次主从数据库断开连接后，Redis需要重新执行复制初始化，在数据量大的情况下，非常低效。而在Redis2.8之后，在断线重连后，主数据库只需要将断线期间执行的命令传送给从数据库。  
* 全量复制   增量复制  

### 配置
1) 可以配置文件设置也可以命令执行
```shell
# 指定主节点服务ip port
slaveof <masterip> <masterport>
# 取消主从
slaveof no one
# 查看主从节点信息的命令
info replication
```
2) 先启动主, 在启动从。


## 4. 哨兵模式配置
监控主数据库和从数据库是否能够正常运行, 主数据库出现故障时自动将从数据库转换为主数据库。
### 特点
1) 依赖主从配置, 相当于主从模式的拓展, 提供了高可用性, 避免了因为宕机造成的redis丧失写的能力。
2) 竞选机制, 依赖系统中启动的sentinel进程, 具有
    1. 监控: 监听主服务器和从服务器之间是否在正常工作
    2. 通知: 够通过API告诉系统管理员或者程序，集群中某个实例出了问题
    3. 故障转移：它在主节点出了问题的情况下，会在所有的从节点中竞选出一个节点，并将其作为新的主节点。
    4. 提供主服务器地址：它还能够向使用者提供当前主节点的地址。这在故障转移后，使用者不用做任何修改就可以知道当前主节点地址。
3) sentinel也可以是集群, 部署多个哨兵。

### 配置
1) 书写配置文件 sentinel-26379.conf
```conf
port 26379
daemonize yes
logfile "26379.log"
dir "./"
# 告诉sentinel去监听地址为ip:port的一个master，这里的master-name可以自定义，quorum是一个数字，指明当有多少个sentinel认为一个master失效时，master才算真正失效
sentinel monitor mymaster 192.168.88.128 6379 2
# 需要多少失效时间，一个master才会被这个sentinel主观地认为是不可用的。 单位是毫秒，默认为30秒
sentinel down-after-milliseconds mymaster 30000
# 指定了在发生failover主备切换时最多可以有多少个slave同时对新的master进行同步，这个数字越小，完成failover所需的 时间就越长，但是如果这个数字越大，就意味着越多的slave因为replication而不可用。可以通过将这个值设为1来保证每次只 有一个slave处于不能处理命令请求的状态。
sentinel parallel-syncs mymaster 1
# 1. 同一个sentinel对同一个master两次failover之间的间隔时间
# 2. 当一个slave从一个错误的master那里同步数据开始计算时间。直到slave被纠正为向正确的master那里同步数据时。
# 3. 当想要取消一个正在进行的failover所需要的时间
# 4. 当进行failover时，配置所有slaves指向新的master所需的最大时间。不过，即使过了这个超时，slaves依然会被正确配 置为指向master，但是就不按parallel-syncs所配置的规则来了
sentinel failover-timeout mymaster 15000
# 授权密码输入
#sentinel auth-pass mymaster 123
bind 192.168.88.128 127.0.0.1
```
2) 命令执行 
```shell
# 启动哨兵
redis-server sentinel-26379.conf --sentinel
# 查看其信息
redis-cli -h 127.0.0.1 -p 26379 info Sentinel
# 关闭此功能
redis-cli -h 127.0.0.1 -p 26380 shutdown
```
3) 当关闭主节点后, 会自动发生failover, 相关日志信息可以在sentinel日志中查看。
4) failover(故障转移)后, 对应节点的配置文件参数是会被修改的(slaveof).

## 5. 集群模式配置
redis 集群式提供多个redis节点共享数据的程序集.
1) 不支持处理keys这样的命令, 因为需要在多个节点上移动数据, 高负载的情况下可能导致不可预料的错误.
2) 自定分隔数据到不同的节点上, 高可用。整个集群部分节点不可用或不可达的情况下可以继续处理命令。
3) 集群有16384个哈希槽, 每个key通过CRC16校验后对16384取模来决定放置到那个hash槽。添加或者移除节点不会造成集群的不可用。

### 配置
1) 进行节点配置
```shell
bind 192.168.88.127
port 6381
daemonize yes
pidfile /var/run/redis_6381.pid
dbfilename dump6381.rdb
cluster-enabled yes
cluster-config-file nodes-6381.conf
# 节点失效检测
cluster-node-timeout 15000
```
2) 启动所有的节点
> root      13574      1  0 14:24 ?        00:00:00 ../bin/redis-server 192.168.88.127:6385 [cluster]  
> root      13586      1  0 14:31 ?        00:00:00 ../bin/redis-server 192.168.88.127:6386 [cluster]  
> root      13596      1  0 14:31 ?        00:00:00 ../bin/redis-server 192.168.88.127:6384 [cluster]  
> root      13601      1  0 14:31 ?        00:00:00 ../bin/redis-server 192.168.88.127:6383 [cluster]  
> root      13608      1  0 14:32 ?        00:00:00 ../bin/redis-server 192.168.88.127:6381 [cluster]  
> root      13613      1  1 14:32 ?        00:00:00 ../bin/redis-server 192.168.88.127:6382 [cluster]  

3) 下面命令进行创建集群 命令执行后有创建日志打印  

```shell
# 该命令为redis 5以上才有 5以下换要安装ruby才行
# replicas 1 表示为每个主节点创建一个从节点
redis-cli --cluster create 192.168.88.127:6381 192.168.88.127:6382 192.168.88.127:6383 192.168.88.127:6384 192.168.88.127:6385 192.168.88.127:6386 --cluster-replicas 1 
#
# 输入yes 看到控制台有绿色的下面信息  说明集群ok
# [OK] All nodes agree about slots configuration.
# >>> Check for open slots...
# >>> Check slots coverage...
# [OK] All 16384 slots covered.
#

# 添加节点信息
redis-cli --cluster add-node 127.0.0.1:6385 127.0.0.1:6379

#查看集群节点信息
cluster nodes

# 注意启动redis-cli客户端时需要设置集群模式  不然 会报错 (error) MOVED 
redis-cli -c -hxxx -p 6379
```
[集群操作命令](https://www.cnblogs.com/zhoujinyi/p/11606935.html)

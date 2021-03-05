# Redis 持久化

redis 持久化是将内存中数据保存到磁盘中，防止数据丢失，redis提供俩种持久化方式

* RDB 持久化，默认保存文件是 dump.rdb

* AOF 持久化，

## RDB
RDB 是一种快照保存持久化方式，会在某一时刻将内存中的数据保存到默认文件dump.rdb，当服务启动时候会加载文件中数据到内存中 
### 开启RDB持久化
1. 指令 save/bgsave 可以可以让服务器生成rdb 文件，生成过程中 先创建临时文件保存完成之后替换原先文件，save 指令是同步进行 在操作保存命令时候 redisServer 不能处理其他请求，bgsave 会fork一个子线程去处理保存操作。
2. 配置文件配置自动触发（bgsave），在redis.conf 配置文件中配置rbd 持久化信息,但最好不要通过这种方式出发rdb ，时间太短会频繁触发fork子线程保存，时间太长会丢失数据
```
# 900s内至少达到一条写命令
save 900 1
# 300s内至少达至10条写命令
save 300 10
# 60s内至少达到10000条写命令
save 60 10000

# 是否压缩rdb文件
rdbcompression yes

# rdb文件的名称
dbfilename redis-6379.rdb

# rdb文件保存目录
dir ~/redis/
```
## AOF
AOF 持久化会记录每一条操作指令，并将操作指令追加到后缀为 .aof 文件中， redis 重新启动时候会自动加载指令并执行, 保存到aof 文件中指令都是fork子线程去完成
### 开启aof （默认不开启）
```
# 开启aof机制
appendonly yes

# aof文件名
appendfilename "appendonly.aof"

# 写入策略,always表示每个写操作都保存到aof文件中,也可以是everysec或no
appendfsync always

# 默认不重写aof文件
no-appendfsync-on-rewrite no

# 保存目录
dir ~/redis/
```
### 写入策略
* always 每条指令都写入
* everysec 每秒写入一次（默认）
* no 不负责写入
### aof 文件重写
aof 文件中保存到指令可能是会一个key 的重复操作，所以支持aof文件重写，重写会影响服务器性能 <br />
* 开启重写配置
```
#默认不重写aof文件
no-appendfsync-on-rewrite no
```
* 指令aof 文件重写
bgrewriteaof
## RDB && AOF
* rdb 文件小恢复数据快，会丢失数据
* aof 文件大 恢复数据慢，但不会丢失数据

# 实现分布式锁的三种方法
```text
1. 基于数据库实现分布式锁
2. 基于缓存(redis等)实现分布式锁
3. 基于Zookeeper实现分布式锁
```
## 1. 基于数据库实现分布式锁
### 1. 悲观锁
```text
    即每当修改数据的时候，会有其他线程也会同时修改该数据，所以针对这种情况悲观锁的做法是：读取数据之后就加锁(eg：select...for update),
    这样别的线程读取该数据的时候就需要等着，等到当前线程完工释放锁，获得锁的线程才能获得该数据的读写权限，从而保证了并发修改数据错误的问题；
    但是由于阻塞原因，会导致吞吐量不高，所以悲观锁比较适合于多写少读的情况
    
    利用select...for update 排他锁：
    注： 这里需要注意的是“where name=lock ”，name字段必须要走索引，否则会锁表。有些情况下，比如表不大，mysql优化器会不走这个索引，导致锁表问题
```
举个栗子：
```text
场景: 同学A和同学B都要给你转500块钱(开心坏了吧，这样最终你能得到1000块钱)。

使用悲观锁的流程:
1.同学A获取到你的账户余额balance = 0并对该条记录加锁。
2.同学B获取你的账户余额。由于同学A已经对这条记录加锁了，所以同学B需要等同学A转帐完成(释放锁)才能获得余额。
3.同学A转账完成并释放锁，此时你的账户余额balance=balance + 500 = 500
4.同学B获取到你的账户余额balance = 500，并对该条记录加锁(如果你人缘好，此时同学C给你转账也是需要等待同学B转账完成才可以转账哦)
5.同学B转账完成并释放锁(如果有同学C想给你转账，此时同学C就可以获得锁并转账了)。此时你的账户余额为balance = balance + 500 = 1000
6.最终你开开心心的得到了1000块钱。
假设转账过程没有锁，我们看看会发生什么:

1.同学A获取到你的账户余额balance_a = 0(没有加锁，此时同学B也可以获取到账户余额)
2.同学B获取到你的账户余额balance_b = 0
3.同学A转账完成，此时你的账户余额为balance = balance_a + 500 = 500
4.同学B转账完成，此时你的账户余额为balance = balance_b + 500 = 500
最终同学A和同学B都转了500，但是你最终只获得了500, 

所以加锁是非常必须的。
    但是加了悲观锁，同学都要排队给我转账，对于没有耐心的同学就直接不转帐了，
    我岂不是错失了发财的好机会。那有什么好办法呢？答案就是乐观锁
```
### 2. 乐观锁
```text
    乐观锁：
    即只在更新数据的时候才会检查这条数据是否被其他线程更新了(这条和悲观锁一样，悲观锁在读取数据的时候就加锁了)，
    如果在更新数据时，发现这条数据被其他线程更新了，则此次更新会失败。如果数据未被其他线程更新，则此次更新会成功；
    由于乐观锁没有了 锁 的等待，提高了吞吐量，所以乐观锁适合多读少写的情况
    
    注：
      常见的乐观锁实现方式是：版本号version和CAS(compare and swap)。此处只介绍版本号方式
```
继续上面的栗子：
注：要采用版本号，首先需要在数据库表中新增一个字段version，表示此条记录的更新版本，记录每变动一次，版本号加1
```text
1.同学A获取到你的账户余额balance = 0和版本号version_a = 0
2.同学B获取到你的账户余额balance = 0和版本号version_b = 0
3.同学A转账完成update table set balance = ${balance}, version = version + 1 and version = 0。(此时版本号为0，所以更新成功)
4.同学B转账完成update table set balance = ${balance}, version = version + 1 and version = 0。(此时版本号为1，所以更新失败，更新失败之后同学B再转一次即可)
同学B重新转帐之后，你还是美滋滋的获得了1000
```

举个栗子：假设开始时   账户初始account == 100
进程A：
```sql
SELECT version,account FROM personal_bank WHERE INTO DUMPFILE = "xxx";
newAccount = 100 + 100
UPDATE personal_bank SET account=200, version = oldVersion + 1
  WHERE connection_id() = "xxx" AND version = oldVersion
```
更新失败了   version != oldVersion

进程B： 
```sql
SELECT version, account FROM personal_bank WHERE id = "xxx";
newAccount = 100-100

update personal_bank set account = 0, version=oldVersion + 1  where id="xxx" AND version=oldVersion;
```
更新成功了

所以乐观锁与前边最大区别在于基于CAS思想，是不具有互斥性，不会产生锁等待而消耗资源，
操作过程中认为不存在并发冲突，只有update version失败后才能觉察到
我们的抢购、秒杀就是用了这种实现以防止超卖

 
## 基于缓存(redis等)实现分布式锁 (jedisPool / Redisson)
1. 使用命令介绍：
```text
（1）SETNX
SETNX key val：当且仅当key不存在时，set一个key为val的字符串，返回1；若key存在，则什么都不做，返回0。
（2）expire
expire key timeout：为key设置一个超时时间，单位为second，超过这个时间锁会自动释放，避免死锁。
（3）delete
delete key：删除key
```
在使用Redis实现分布式锁的时候，主要就会使用到这三个命令。

2. 实现思想：
```text
（1）获取锁的时候，使用setnx加锁，并使用expire命令为锁添加一个超时时间，
    超过该时间则自动释放锁，锁的value值为一个随机生成的UUID，通过此在释放锁的时候进行判断。
（2）获取锁的时候还设置一个获取的超时时间，若超过这个时间则放弃获取锁。
（3）释放锁的时候，通过UUID判断是不是该锁，若是该锁，则执行delete进行锁释放
```

## 基于Zookeeper实现分布式锁
ZooKeeper是一个为分布式应用提供一致性服务的开源组件，它内部是一个分层的文件系统目录树结构，
    规定同一个目录下只能有一个唯一文件名。基于ZooKeeper实现分布式锁的步骤如下：
```text
（1）创建一个目录mylock；
（2）线程 A 想获取锁就在mylock目录下创建 临时顺序节点；
（3）获取mylock目录下所有的子节点，然后获取比自己小的兄弟节点，如果不存在，则说明当前线程顺序号最小，获得锁；
（4）线程B获取所有节点，判断自己不是最小节点，设置监听比自己次小的节点；
（5）线程A处理完，删除自己的节点，线程B监听到变更事件，判断自己是不是最小的节点，如果是则获得锁。
```
这里推荐一个Apache的开源库Curator，它是一个ZooKeeper客户端，Curator提供的InterProcessMutex是分布式锁的实现，acquire方法用于获取锁，release方法用于释放锁

优点：具备高可用、可重入、阻塞锁特性，可解决失效死锁问题。
缺点：因为需要频繁的创建和删除节点，性能上不如Redis方式


## 对比
```text
数据库分布式锁实现
缺点：
1.db操作性能较差，并且有锁表的风险
2.非阻塞操作失败后，需要轮询，占用cpu资源;
3.长时间不commit或者长时间轮询，可能会占用较多连接资源

Redis(缓存)分布式锁实现
缺点：
1.锁删除失败 过期时间不好控制
2.非阻塞，操作失败后，需要轮询，占用cpu资源;

ZK分布式锁实现
缺点：性能不如redis实现，主要原因是写操作（获取锁释放锁）都需要在Leader上执行，然后同步到follower。

总之：ZooKeeper有较好的性能和可靠性。

从理解的难易程度角度（从低到高）数据库 > 缓存 > Zookeeper
从实现的复杂性角度（从低到高）Zookeeper >= 缓存 > 数据库
从性能角度（从高到低）缓存 > Zookeeper >= 数据库
从可靠性角度（从高到低）Zookeeper > 缓存 > 数据库
```

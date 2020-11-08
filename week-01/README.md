-Denv=PRO
环境设置，pro一般为生产安环境，用于区分测试和开发环境


-server
设置jdk为服务器版本，现在的jdk一般都是服务器版本，这个参数可以不设置


-Xms4g 
jvm的初始内存


-Xmx4g 
jvm的最大分配内存，和初始内存一般设置一样，防止在使用过程中扩容带来不必要的


-Xmn2g
设置年轻代的内存为2g


-XX:MaxDirectMemorySize=512m
堆外内存512M


-XX:MetaspaceSize=128m
jvm 元空间最小为 128m


-XX:MaxMetaspaceSize=512m 
jvm 元空间最大为 512m


-XX:-UseBiasedLocking
禁止使用偏向锁


-XX:-UseCounterDecay 
配置关闭热度衰减，让计数器统计绝对值


-XX:AutoBoxCacheMax=10240
修改Integer对象的缓存，默认是-128~127，用来减少封箱和拆箱的开销
-XX:+UseConcMarkSweepGC
设置使用cms回收器
-XX:CMSInitiatingOccupancyFraction=75
设置使用空间占到75%的时候开始回收，单独配置不生效，配合下面参数使用，之前调优经验遇到过，配置的是80，然后当空间达到70多的时候，程序的问题突然一个大对象导致占用直接到了90%多，差点凉凉的情况
-XX:+UseCMSInitiatingOccupancyOnly
配合上面参数使用
 -XX:MaxTenuringThreshold=6 
新生代的对象被复制的次数，超过这个次数则到达老年代，默认15次
-XX:+ExplicitGCInvokesConcurrent
如果程序中需要手动去执行gc，此参数则设置触发的gc为cmsgc，而不是full gc
-XX:+ParallelRefProcEnabled 
默认为false,并行的处理Reference对象,如WeakReference,除非在GC log里出现Reference处理时间较长的日志，否则效果不会很明显。
-XX:+PerfDisableSharedMem 
以禁止JVM写在文件中写统计数据，无法使用jps、jstat命令，可以通过jmx获取数据
-XX:+AlwaysPreTouch 
jvm启动的时候就分配物理内存给jvm，而不是在使用的时候分配，虽然降低启动速度，但是不影响系统的稳定性和在运行过程中的速度
-XX:-OmitStackTraceInFastThrow
强制要求JVM始终抛出含堆栈的异常
  -XX:+ExplicitGCInvokesConcurrent
重复
 -XX:+ParallelRefProcEnabled 
重复
-XX:+HeapDumpOnOutOfMemoryError
当oom的时候自动生成heap文件
 -XX:HeapDumpPath=/home/devjava/logs/ 
heap文件的地址
-Xloggc:/home/devjava/logs/lifecircle-tradecore-gc.log
gc文件的地址
 -XX:+PrintGCApplicationStoppedTime
日志打印gc时候stw的时间
 -XX:+PrintGCDateStamps 
打印时间戳
-XX:+PrintGCDetails
打印详细的gc日志
 -javaagent:/home/devjava/ArmsAgent/arms-bootstrap-1.7.0-SNAPSHOT.jar
阿里云arms日志的设置
2020-10-29T21:19:19.488+0800: 114.015: [GC (CMS Initial Mark) [1 CMS-initial-mark: 106000K(2097152K)] 1084619K(3984640K), 0.2824583 secs] [Times: user=0.86 sys=0.00, real=0.28 secs]
2020-10-29T21:19:19.488+0800: 
这里有两个时间,[2020-10-29T21:19:19.488+0800:]这个是当前的时间,也就是GC发生时候的时间;
114.015
[114.015]为jvm从启动到gc发生的秒数,这个由[-XX:+PrintGCDateStamps]配置后打印
GC
这个gc是相对full gc而言的,其实这里就是一次较小的gc,表示为Minor Gc
CMS Initial Mark
为CMS垃圾回收器的初始化标记，
[1 CMS-initial-mark: 106000K(2097152K)]
目前老年代已使用的内存106000K = 106.000M; 2097152K老年代的可用内存; 
1084619K(3984640K)
1084619K是当前堆的已用大小，3984640K为整个堆的大小
0.2824583 secs
整个标记过程耗时约为0.28秒
[Times: user=0.86 sys=0.00, real=0.28 secs]
整个标记过程耗时约为0.28秒，其中[user=0.86]为垃圾收集器耗费的时间；[sys=0.00]为操作系统消耗的时间；[real=0.28 secs]
2020-10-29T21:19:19.771+0800: 114.298: [CMS-concurrent-mark-start]
2020-10-29T21:19:19.931+0800: 114.458: [CMS-concurrent-mark: 0.160/0.160 secs] [Times: user=0.32 sys=0.03, real=0.16 secs]
-- 并发标记，从Roots节点开始扫描全局，收集所有的可及对象，0.16s表示所耗时的时间，此处会降低系统的吞吐量，用标记清除算法
2020-10-29T21:19:19.931+0800: 114.459: [CMS-concurrent-preclean-start]2020-10-29T21:19:19.998+0800: 114.525: [CMS-concurrent-preclean: 0.065/0.066 secs] [Times: user=0.05 sys=0.01, real=0.06 secs]
-- 开始预清理，以及预清理的时间为0.065s
2020-10-29T21:19:19.998+0800: 114.525: [CMS-concurrent-abortable-preclean-start]CMS: abort preclean due to time 
2020-10-29T21:19:25.072+0800: 119.599: [CMS-concurrent-abortable-preclean: 5.038/5.073 secs] [Times: user=7.72 sys=0.50, real=5.08 secs]
这里是会等到5秒，这个5秒是默认的等待时间，preclean是为了加速下一级的remark过程，因为remark过程是STW的。preclean过程中为了更好的使用parallel，它会等待一次小gc（默认等待5s），如果5s内小gc没来，就会强制开始STW remark过程，并打印信息abort preclean due to time。
2020-10-29T21:19:25.076+0800: 119.603: [GC (CMS Final Remark) [YG occupancy: 1279357 K (1887488 K)]
重新标记，由于用户线程在执行的是会产生新的对象，所以在正式清理之前，会再次修正标记，会独占CPU，系统会停顿，引发STW；并且打印出了年轻代的已用空间和总空间 [1279357 K (1887488 K)]]
2020-10-29T21:19:25.076+0800: 119.603: [Rescan (parallel) , 0.3120602 secs]
-- 在系统暂停的情况下重新扫描了，耗时约为0.3秒
2020-10-29T21:19:25.388+0800: 119.915: [weak refs processing, 0.0015920 secs]
2020-10-29T21:19:25.390+0800: 119.917: [class unloading, 0.0517863 secs]2020-10-29T21:19:25.441+0800: 119.969: [scrub symbol table, 0.0212825 secs]
此时分别处理了，弱引用、无用的类和清理包含类级元数据和内部化字符串的符号表和字符串表；以及打印了他们的耗时
2020-10-29T21:19:25.463+0800: 119.990: [scrub string table, 0.0022435 secs][1 CMS-remark: 106000K(2097152K)] 1385358K(3984640K), 0.3959182 secs] [Times: user=1.33 sys=0.00, real=0.40 secs]
这里打印出了，重新标记后老年代的已用空间为106000K，以及堆的已用空间和总空间
2020-10-29T21:19:25.473+0800: 120.000: [CMS-concurrent-sweep-start]2020-10-29T21:19:25.540+0800: 120.067: [CMS-concurrent-sweep: 0.067/0.067 secs] [Times: user=0.18 sys=0.02, real=0.06 secs]
这里是标记清除阶段，这阶段是和用户线程一起执行的，不会影响用户使用系统
2020-10-29T21:19:25.540+0800: 120.068: [CMS-concurrent-reset-start]2020-10-29T21:19:25.544+0800: 120.071: [CMS-concurrent-reset: 0.003/0.003 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
重置阶段，为下一个收集阶段做准备




java -Denv=PRO -server -Xms4g -Xmx4g -Xmn2g -XX:MaxDirectMemorySize=512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:-UseBiasedLocking -XX:-UseCounterDecay -XX:AutoBoxCacheMax=10240 -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:MaxTenuringThreshold=6 -XX:+ExplicitGCInvokesConcurrent -XX:+ParallelRefProcEnabled 
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
# Netty

一开始有一个执行器EventExecutorGroup 继承 ScheduledExecutorService 那么他就拥有了一些调度执行的功能
且通过里面的next方法获取这个组里面的一个EventExecutor,EventExecutor是这个组里面一个专门执行
任务的一个执行体,又是同时EventExecutor一个特殊的EventExecutorGroup(一个人也是一个组),为什么
引入EventLoopGroup(继承EventExecutorGroup),有时候我们要注册通道(从select中选择一个channel注册到event loop中),并且我们需要将这些线程粘起来
通过next循环这个线程数组


AbstractEventExecutorGroup(里面的方法都是next.  表示异步)  实现  EventExecutorGroup

Multi(多线程) thread EventExecutorGroup  继承 AbstractEventExecutorGroup
# 实现了shutdownGracefully()方法
包含 EventExecutor[] children 
     Set<EventExecutor> readonlyChildren; (只读 底层禁用增删改方法)
     AtomicInteger terminatedChildren (监听器)  AtomicInteger terminatedChildren、
     AtomicInteger terminatedChildren
     EventExecutorChooserFactory.EventExecutorChooser chooser (选择器)  :2的倍数的时候  index&length-1
                                                                        :不是2个倍数    Math.abs(index&length) 防止符号位溢出
     ps: 创建线程使用的工厂 默认是直接开启一个线程
     构造方法流程：
                1.判断线程工厂是否为空  (防止子类直接调佣构造方法)    new ThreadPerTaskExecutor(newDefaultThreadFactory());
                2.初始childer 线程  children[i] = newChild(executor, args); (具体由子类实现)
                3.创建选择器
                4.创建监听器(有最后一个设置成false) 添加到children中
                5.创建只可读的children   Set<EventExecutor> childrenSet
               
MultithreadEventLoopGroup extends MultithreadEventExecutorGroup implements EventLoopGroup                
前置知识：
  CPU          CPU里面有一个RunQueue,存放任务的。    切换任务流程(大致 顺序不一定): 1.选取进程2.保存待切换的进程存储器
   |                                                            3.还原被切换的进程存储器 4.执行io
RunQueue                                            切换任务的时候 不能没有另外一个任务(RunQueue>1)


DEFAULT_EVENT_LOOP_THREADS  默认线程数为2  实现EventLoopGroup的方法 都是next().  代表异步 

完整的实现类:   NioEventLoopGroup extends MultithreadEventLoopGroup  (NIO模型的)  后面还有EpollEventLoopGroup的

核心是实现了EventLoop newChild(Executor executor, Object... args)   方法
                        EventLoop 执行器
                         ||       ||
return new NioEventLoop(this, executor, (SelectorProvider) args[0],
            ((SelectStrategyFactory) args[1]).newSelectStrategy(), (RejectedExecutionHandler) args[2]);
                    ||
                  选择策略        

↓NioEventLoop extends SingleThreadEventLoop
-----------------
↓SingleThreadEventLoop extends SingleThreadEventExecutor implements EventLoop
除了队列里面任务的优先级 队列之间也有优先级
Queue<Runnable> tailTasks;一： 尾部队列  new LinkedBlockingQueue<Runnable>(maxPendingTasks)→最小数量是16  性能 ：this.maxPendingTasks = Math.max(16, maxPendingTasks);
-----------------
↓SingleThreadEventExecutor extends AbstractScheduledEventExecutor implements OrderedEventExecutor
            ↓                                ↓   
          单线程                        所以可以执行周期性任务

private final Queue<Runnable> taskQueue;  二：普通队列 
private final Executor executor;
-----------------
↓AbstractScheduledEventExecutor extends AbstractEventExecutor  
(实现了Scheduled 可执行周期性任务)↑ 
PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue;    三： 周期任务对队列





ServerBootstrap(netty启动类) 启动流程:
前置  初始化 boos和worker  channel  handler childHandler
1.绑定端口启动  doBind()：流程
                1.channel = channelFactory.newChannel();
                            channelFactory由初始化channel的时候创建的→→channelFactory(new ReflectiveChannelFactory<C>(channelClass))
                            而newChannel 是通过反射创建的channel  目的是为了解耦 否则就要引入channel包 
                2.init(channel);
                             1.setChannelOptions(channel, options, logger);
                                    传入logger的ServerBootstrap的loger  目的是告诉开发者是哪里开始进行设置的
                                    channel.config().setOption((ChannelOption<Object>) option, value)
                                    config是初始化ServerSocketChannel的时候创建的→→new NioServerSocketChannelConfig(this, javaChannel().socket());
                                    然后在DefaultServerSocketChannelConfig 里面进行setOptions   (javaChannel().socket()→→serverSocket) 
                             2. channel.attr(key).set(e.getValue());(我的理解是启动serverSoker的时候就绑定soket) 一般没有
                             3.ChannelPipeline p = channel.pipeline();  //相当于过滤器
                                    前置知识：线性表分为顺序表(数组)和链表(地址) 
                                             数组运用缓存行、空间局部性来进行加速，查找的时候会非常快，再加上有索引，所以访问时间复杂度是O(1)
                                             链表增删改时间复杂度是O(1) ,访问效率较慢  channel主要是添加操作
                                    DefaultChannelPipeline implements ChannelPipeline{
                                    final AbstractChannelHandlerContext head;
                                    final AbstractChannelHandlerContext tail;
                                    private final Channel channel;    
                                    }
                             4.currentChildOptions = childOptions.entrySet().toArray(newOptionArray(childOptions.size()));
                             5.currentChildAttrs = childAttrs.entrySet().toArray(newAttrArray(childAttrs.size()));
                             6.(p是serverbootstrap的ChannelPipeline)p.addLast() 目的为了在serverSocketChannel中添加ServerBootstrapAcceptor
                                    将初始化的handel扔到serverSocketChannel中 再给boss的事件循环组去实现(先执行logger→LoggerHandel,再接收客户端连接)
                                    ps：为什么要放到boss的事件循环组去执行，主线程是没办法操作这些数据的 只能放到netty的线程中去执行
                                    ps：我要放到netty的线程池中去执行，但是我又不得不让他在外面先执行,所以就添加一个initChannel去执行
                3.ChannelFuture regFuture = config().group().register(channel);
                                                    (boos的group)  
                                                     (MultithreadEventLoopGroup的register 虽然它是一个线程,但是它是一个组,所以是MultithreadEventLoopGroup不是ingleThreadEventLoop )
                                 1.MultithreadEventLoopGroup.register()→→SingleThreadEventLoop.register()
                                 -->this.register((ChannelPromise)(new DefaultChannelPromise(channel, this)))
                                 -->new DefaultChannelPromise this就是注册的线程(boss 线程)
                                 -->promise.channel().unsafe().register(this, promise);
                                 -->register(EventLoop eventLoop, final ChannelPromise promise) {
                                    if (eventLoop.inEventLoop()) //当前线程是否是事件循环组
                                        this.register0(promise);
                                    } else {//不是的话 就放在boss的线程里面执行
                                       try {
                                             eventLoop.execute(new Runnable() {
                                                public void run() {
                                                AbstractUnsafe.this.register0(promise);}
                                             });
                                           } 
                                    }
                                 }
                                 -->register0(promise)↓
                                    void register0(ChannelPromise promise) {
                                    doRegister(){//注册selector
                                    ps:selector中的selectionKey 和channel双向绑定  selector向内核中放内核中感兴趣的事件集有(四种类型in out erroe hup 没有0) 处理
                                    //此时selector永远工作不了 需要等待开启  修改事件集的类型
                                    this.selectionKey = this.javaChannel().register(this.eventLoop().unwrappedSelector(), 0, this);
                                    }
                                    this.safeSetSuccess(promise);-->promise.trySuccess() //回调 Channel channel = sbs.bind().sync().channel();
                                    //观察者模式 当被观察者在xx之前或之后触发了xxx  去通知观察者回调观察者的方法
                                    pipeline.invokeHandlerAddedIfNeeded();初始化ChannelInitializer-->可以执行LoggingHandler和接收连接的时候执行ServerBootstrapAcceptor
                                    safeSetSuccess(promise);回调 唤醒sbs.bind().sync()
                                    pipeline.fireChannelRegistered();
                                    }
                4.doBind0(regFuture, channel, localAddress, promise);

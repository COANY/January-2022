资源名： 唯一名称，默认是请求路径

针对来源： Sentinel可以针对调用者进行限流，填写微服务的名称，默认是default（不区分来源）

阈值类型/单机阈值：可随时更改，无需重启系统

QPS：每秒的请求数量，当调用该api的QPS达到阈值的时候进行限流。---挡在外面直接失败
线程数： 当调用该api的线程数达到阈值的时候进行限流。---可以放进来，但是不处理直接失败
流控模式：

直接：api达到限流条件时，直接限流
关联：当关联的资源达到限流条件时，限流自己
链路：只记录指定链路上的流量（指定资源从入口资源进来的流量，如果达到阈值，就进行限流）【api级别的针对来源】
流控效果：

快速失败：直接失败，抛出异常
Warm Up: 根据codeFactor（冷加载因子，默认3）的值，从阈值/codeFactor，经过预热时长，才达到设置的QPS阈值
排队等待：匀速排队，让请求以匀速通过，阈值类型必须设置为QPS

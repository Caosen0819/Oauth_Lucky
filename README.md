## 20230924
临时升级带宽，使用10Mbps大小的带宽 2核8G。

优化前：TPS：33；RTT：12408
优化后：TPS：46；RTT：9160
优化措施：redis预热，中间的一些数据放到redis操作，kafka+定时任务同步到Mysql

## 20230901
先试用最低带宽1Mbps进行压测
3.3 -> 3.4 + redis-> 3.9 + kafka -> 4.3

## 20230829
成功打通微信公众号，域名服务器，备案等

## 20230825
成功整合之前自己做的权限系统

## 20230824：
1、为了压测，将ActivityPartakeImpl.java其中的一部分代码注释掉了,容易找到
2、ActivityDrawProcessImpl.java
buildDrawOrderVO函数里面的drawOrderVO.setTakeId(takeId)改成了drawOrderVO.setTakeId(takeId + orderId);
3、router镜像换了一个
4、顺带关闭发送mq试试看\ActivityDrawProcessImpl.java
5、顺便关闭logger试试看

## 20230911：
1、和0824的第一点一样，发生冲突是因为一个用户的总数使用减去使用，所以必须注释
2 新开了几个函数，用于压测
## 20230814：
对依赖进行了去留测试，若是后面启动有问题，可以恢复依赖
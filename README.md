## 20230824：
1、为了压测，将ActivityPartakeImpl.java其中的一部分代码注释掉了,容易找到
2、ActivityDrawProcessImpl.java
buildDrawOrderVO函数里面的drawOrderVO.setTakeId(takeId)改成了drawOrderVO.setTakeId(takeId + orderId);
3、router镜像换了一个
4、顺带关闭发送mq试试看\ActivityDrawProcessImpl.java
5、顺便关闭logger试试看
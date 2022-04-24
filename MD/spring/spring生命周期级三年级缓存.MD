# spring初始化过程
## 如图

![](../.spring_images/spring生命周期.png)

为何使用三级缓存：如果使用二级缓存 :如果bean对象是AOP对象那么步骤如下：  
1. 实例化bean成AOP代理对象；  
2. 初始化bean对象，但是此时是AOP代理对象，无法直接获取原始Bean的属性信息，无疑增加了属性bean的初始化的复杂度;
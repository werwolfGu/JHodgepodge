# spring常见问题

## spring生命周期

spring创建一个对象时执行的的流程：

- getBean

- doGetBean

- getSingleton 

  > 从缓存中获取，如果一级，二级缓存中没有，三级缓存有，那么加入到二级缓存中；此时如果是代理对象，就会生成代理对象写入到二级缓存中 ; 如果3个缓存中都没有，需要createBean

  - createBean
  - doCreateBean
    - createBeanInstance     实例化bean对象
    - addSingletonFactory   加入到第三级缓存
    - populateBean    填充属性
      - applyPropertyValues  ----属性赋值
        - resolveValueIfNecessary
        - resolveReference   
        - getBean() 
    - initializeBean 
      - BeanPostProcessor#postProcessBeforeInitialization()
      - @PostConstruct
      - afterPropertiesSet()
      - BeanPostProcessor#postProcessAfterInitialization()
  - addSingleton  --加入到一级缓存

- getBean  -----如果在三级缓存中，将对象放入到二级缓存，在这个时候如果是代理对象，会生成代理对象

![](/Users/chengen.gu/workspace/github/springboot-demo/MD/picture/spring生命周期.png)

## spring-AOP失效问题

springAOP失效原因  
1. spring容器Service类中的方法 通过 private 、static 、 final ；
2. Service方法 A方法调用B方法；B方法是比如使用springAOP注解 如： @Transactional 此时事务是失效的；如：
```java
    public void A(){
        doSomething();
        B();
    }
     @Transactional
    public void B(){
        
    }
```

失效原因：  
Spring AOP使用JDK动态代理和CGLib，由于没有接口的类，所以使用CGLib代理。当方法被代理时，
其实通过动态代理生成了代理对象，然后代理对象执行invoke方法，在调用被代理对象的方法时，执
行其他操作。问题就在于被代理对象的方法中调用被代理对象的其他方法时，`使用的是被代理对象本身
，而非代理对象`。这就导致了一个方法时代理对象调用的，一个是被代理对象调用的。他们的调用始终
不出于同一个对象。

解决方案

```java
    public void A(){
        doSomething();
        //获取代理对象
        (T)AopContext.currentProxy().B();
    }
     @Transactional
    public void B(){
        
    }
```


[spring学习文档](https://www.kancloud.cn/luoyoub/spring-note/2243936)
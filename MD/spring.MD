# spring常见问题

## spring AOP失效问题
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
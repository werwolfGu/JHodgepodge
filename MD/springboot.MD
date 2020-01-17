# springboot

spring-boot-autoconfigure包会自动注入各种插件jar，看 META-INF/spring.factories 文件；
比如需要引入redis jar 可以看
org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
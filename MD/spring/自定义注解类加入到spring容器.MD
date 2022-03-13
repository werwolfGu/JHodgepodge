# 实现类似@Component spring动态注入


导入
1. AutoConfiguredMapperScannerRegistrar implement ImportBeanDefinitionRegistrar
   初始化
2. MapperScannerConfigurer implement BeanDefinitionRegistryPostProcessor

将其注册到Spring Ioc里面
3. ClassPathMapperScanner implement ClassPathBeanDefinitionScanner

使用FactoryBean
4. MapperFactoryBean implement FactoryBean

简单的例子请看：
spring-ann-dynamic

对加了 @AnnDemo的类加载到springIOC容器中；
具体可以参考Mybatis的 @Mapper



参考：spring-Mybatis-starter

org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration


参考：https://segmentfault.com/a/1190000024523534


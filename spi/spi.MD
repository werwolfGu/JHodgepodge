## SPI是什么
SPI 英文全称（serice provider interface）,字面意思就是服务提供接口，他是jdk提供给"服务提供商"或者插件开发者"使用的接口；

## Java SPI的具体约定
- 当服务的提供者，提供了接口的一种具体实现后，在jar包的META-INF/services/目录中创建一个以“接口全限定名”为命名的文件，内容为实现类的全限定名。
- SPI所在的jar放在主程序的classpath中
- 外部程序通过java.util.ServiceLoader动态装载实现模块，它通过扫描META-INF/services目录下的配置文件找到实现类的全限定名，把类加载到jvm。
   - 注意：SPI的实现类必须带一个不带参数的构造方法
 
 ## API和SPI的区别
 - API：提供给调用方，完成某项功能的接口（类、或者方法），你可以使用它完成任务。
 - SPI：是一种callback的思想，在一些通用的标准中（即API），为实现厂商提供扩展点。当API被调用时，会动态加载SPI路由到特定的实现中。
 
 ## Java SPI 运用场景
 - java.sql.Driver的spi实现，比如MySQL，实现类是com.mysql.jdbc.Driver，在mysql-connector-java-5.1.6.jar下我们能看到META-INF/service
 有个java.sql.Driver的文件；内容就是com.mysql.jdbc.Driver;
 - common-logging的日志接口实心；
 
 ## 具体事例
 com.gce.SpiServiceMain
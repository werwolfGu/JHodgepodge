#GRVYF框架
##介绍
GRVYF框架核心使用groovy脚本让规则可动态添加并且执行；

##能力

1. 规则可动态添加，动态执行
2. 不同的规则可以实现对不同的规则结果处理；
3. 多条规则可实现对执行结果进行只取一条或所有结果；

##实现原理

- 规则字段
    - script        脚本
    - type          规则类型
    - resultHandle  结果处理类路劲
    - priority      优先级
    - resultSetType 结果集类型
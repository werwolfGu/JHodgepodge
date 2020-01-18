package com.guce.groovy

public static void  evalScriptAsMainMethod (){

    println("hello groovy ")
    StringBuffer buffer = new StringBuffer();
    //define API
    buffer.append("class User{")
            .append("String name;Integer age;")
    //.append("User(String name,Integer age){this.name = name;this.age = age};")
            .append("String sayHello(){return 'Hello,I am ' + name + ',age ' + age;}}\n");
    //Usage
    buffer.append("def user = new User(name:'zhangsan',age:1);")
            .append("user.sayHello();");
    Binding binding = new Binding();
    GroovyShell shell = new GroovyShell(binding);
    String message = (String)shell.evaluate(buffer.toString());
    System.out.println(message);
    //重写main方法,默认执行
    String mainMethod = "static void main(String[] args){def user = new User(name:'lisi',age:12);print(user.sayHello());}";
    shell.evaluate(mainMethod);
    shell = null;
}



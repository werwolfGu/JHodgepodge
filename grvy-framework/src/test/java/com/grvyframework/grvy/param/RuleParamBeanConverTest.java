package com.grvyframework.grvy.param;

import com.grvyframework.model.RuleParamFieldAlias;
import com.grvyframework.param.BeanToMapConver;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author chengen.gce
 * @DATE 2022/4/3 10:47 AM
 */
@Slf4j
public class RuleParamBeanConverTest {


    @Test
    public void test1(){
        B bObj = new B();
        bObj.setId("123");
        bObj.setDesc("welcome to java");
        A a = new A();
        a.setAge("18");
        a.setName("gce");
        a.setSex("male");
        a.setFlag(true);
        bObj.setaObj(a);

        CObjC cObj = new CObjC();
        cObj.setB(bObj);
        cObj.setId("aaa");
        List<RuleParamFieldAlias> pathList = new ArrayList<>();

        String path = "b.aObj.name";
        RuleParamFieldAlias fieldAlias = new RuleParamFieldAlias();
        fieldAlias.setBeanPath(path);
        fieldAlias.setAlias("aName");
        pathList.add(fieldAlias);
        path = "b.aObj.sex";
        fieldAlias = new RuleParamFieldAlias();
        fieldAlias.setBeanPath(path);
        fieldAlias.setAlias("aSex");
        pathList.add(fieldAlias);
        path = "b.aObj.age";
        fieldAlias = new RuleParamFieldAlias();
        fieldAlias.setBeanPath(path);
        pathList.add(fieldAlias);
        path = "b.aObj.flag";
        fieldAlias = new RuleParamFieldAlias();
        fieldAlias.setBeanPath(path);
        fieldAlias.setAlias("aflag");
        pathList.add(fieldAlias);
        path = "b.aObj.abc";
        fieldAlias = new RuleParamFieldAlias();
        fieldAlias.setBeanPath(path);
        fieldAlias.setAlias("notexist");
        pathList.add(fieldAlias);
        path = "b.id";
        fieldAlias = new RuleParamFieldAlias();
        fieldAlias.setBeanPath(path);
        fieldAlias.setAlias("bbbbbbId");
        pathList.add(fieldAlias);

        path = "b.desc";
        fieldAlias = new RuleParamFieldAlias();
        fieldAlias.setBeanPath(path);
        pathList.add(fieldAlias);
        path = "id";
        fieldAlias = new RuleParamFieldAlias();
        fieldAlias.setBeanPath(path);
        fieldAlias.setAlias("cccccid");
        pathList.add(fieldAlias);


        Map<String,Object> map = BeanToMapConver.conver(cObj,pathList);
        System.out.println(map);
    }



    static class CObjC {
        String id ;
        B b ;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }
    }
    static class A{
        String name;
        String age;
        boolean flag ;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        String sex;
    }

    static class B {
        A aObj;
        String id;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        String desc;

        public A getaObj() {
            return aObj;
        }

        public void setaObj(A aObj) {
            this.aObj = aObj;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}

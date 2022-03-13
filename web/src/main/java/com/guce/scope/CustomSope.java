package com.guce.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author chengen.gce
 * @DATE 2021/12/8 10:28 下午
 */
public class CustomSope implements Scope {

    Map<String,Object > instanceMap = new ConcurrentHashMap<>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {

        Object instance = instanceMap.get(name);
        if (Objects.isNull(instance)) {
            instance = objectFactory.getObject();
            instanceMap.put(name,instance);
        }
        return instance;
    }

    @Override
    public Object remove(String name) {
        return instanceMap.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}

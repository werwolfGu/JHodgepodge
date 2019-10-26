package com.guce;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.guce.module.ServerNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by chengen.gu on 2018/9/20.
 */
public class ConsistentHashWithVN {

    private HashFunction hash = Hashing.murmur3_32();

    private int virtual_num = 4;

    List<ServerNode> serverNodeList = new ArrayList<>();

    /**
     * 增加服务节点  同时增加虚拟节点
     * @param serverName
     */
    public void addServerNode(String serverName){

        if(serverName == null || "".equals(serverName)){
            return;
        }

        for(int i = 0 ; i < virtual_num ; i++ ){
            String virtualServerName = serverName + "##" + i;
            ServerNode node = new ServerNode(serverName,getHash(virtualServerName));
            node.setVirtualValue(String.valueOf(i));
            serverNodeList.add(node);
        }

        if(serverNodeList.size() <= 1){
            return;
        }

        Collections.sort(serverNodeList, new Comparator<ServerNode>() {
            @Override
            public int compare(ServerNode o1, ServerNode o2) {
                return o1.getServerNodeHash() - o2.getServerNodeHash();
            }
        });

    }

    /**
     * 删除服务节点
     * @param serverName
     */
    public void deleteServerNode(String serverName){
        if(serverName == null || "".equals(serverName)){
            return;
        }
        List<ServerNode> removeNodeList = new ArrayList<>();
        for(ServerNode node : serverNodeList){
            if(node.getServerNodeName().startsWith(serverName)){
                removeNodeList.add(node);
            }
        }
        if(removeNodeList.size() > 0){
            serverNodeList.removeAll(removeNodeList);
        }
    }

    /**
     *
     * 匹配hash节点
     * @param key
     * @return
     */
    public ServerNode matchServerNode(String key){

        int hashKey = getHash(key);
        for(ServerNode node : serverNodeList){
            if(node.getServerNodeHash() > hashKey){
                return node;
            }
        }

        return serverNodeList.get(0);
    }

    public int getHash(String key){
        HashCode hashCode = hash.hashBytes(key.getBytes());
        int hashValue = hashCode.asInt();
        if(hashValue < 0 ){
            hashValue = Math.abs(hashValue);
        }
        return hashValue;
    }

    public void printServerNodes(){
        for(ServerNode node : serverNodeList){
            System.out.println(node.toString());
        }
    }


    public static void main(String[] args) {

        ConsistentHashWithVN nv = new ConsistentHashWithVN();

        //增加服务器
        nv.addServerNode("127.0.0.1:1111");
        nv.addServerNode("192.168.1.2:1111");
        nv.addServerNode("192.168.1.5:1141");
        nv.addServerNode("192.168.1.23:1111");

        //打印节点信息
        nv.printServerNodes();

        //客户端匹配服务器
        ServerNode node = nv.matchServerNode("54123232");

        System.out.println("54123232 :" +nv.getHash("54123232") +" 匹配节点 ->" + node.toString());
        node = nv.matchServerNode("1123232");
        System.out.println("1123232 :" +nv.getHash("1123232") +" 匹配节点 ->" + node.toString());
        node = nv.matchServerNode("1988922322");
        System.out.println("1988922322 :"  +nv.getHash("1988922322") +" 匹配节点 ->"+node.toString());
        node = nv.matchServerNode("8766713232");
        System.out.println("8766713232 :" +nv.getHash("8766713232") +" 匹配节点 ->" +node.toString());

        node = nv.matchServerNode("87667132324321");
        System.out.println("87667132324321 :" +nv.getHash("87667132324321") +" 匹配节点 ->" +node.toString());



    }


}
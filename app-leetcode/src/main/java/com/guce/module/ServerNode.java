package com.guce.module;

/**
 * Created by chengen.gu on 2018/9/20.
 */
public class ServerNode {

    private String serverNodeName;
    private int serverNodeHash;

    private String virtualValue;

    public ServerNode(String serverNodeName,int serverNodeHash){
        this.serverNodeName = serverNodeName;
        this.serverNodeHash = serverNodeHash;
    }
    public String getServerNodeName() {
        return serverNodeName;
    }

    public void setServerNodeName(String serverNodeName) {
        this.serverNodeName = serverNodeName;
    }

    public int getServerNodeHash() {
        return serverNodeHash;
    }

    public void setServerNodeHash(int serverNodeHash) {
        this.serverNodeHash = serverNodeHash;
    }

    public String toString(){
        return serverNodeName + "->" + serverNodeHash;
    }

    public String getVirtualValue() {
        return virtualValue;
    }

    public void setVirtualValue(String virtualValue) {
        this.virtualValue = virtualValue;
    }
}

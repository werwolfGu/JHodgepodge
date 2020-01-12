package com.guce.javassist.demo;

import java.util.List;

/**
 * @Author chengen.gu
 * @DATE 2019/12/11 8:59 下午
 */
public abstract class AbstractPointRule {


    protected List<String>  list ;

    public boolean filter(PointsModel pointsModel){

        if (list != null && list.contains(pointsModel.getProductId())){
            return true;
        }
        return false;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}

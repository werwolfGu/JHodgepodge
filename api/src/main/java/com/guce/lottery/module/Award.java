package com.guce.lottery.module;

/**
 * Created by chengen.gu on 2018/10/18.
 * 奖品
 */
public class Award {

    private int awardId;
    private int probability;  //概率

    public int getAwardId() {
        return awardId;
    }

    public void setAwardId(int awardId) {
        this.awardId = awardId;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }
}

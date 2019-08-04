package com.guce.lottery;

import com.guce.lottery.module.Award;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by chengen.gu on 2018/10/18.
 * 奖池
 */
public class AwardPool {

    private String cityId;
    private String scores;
    private String userType;
    private List<Award> awardList;

    /**
     * 当前奖池是否与城市匹配
     * @param cityId
     * @return
     */
    public boolean matchCity(String cityId){
        return true;
    }

    /**
     * 当前奖池是否与用户类型匹配
     * @param score
     * @return
     */
    public boolean matchedScore(String score){
        return true;
    }

    public Award randomGenerateAward(){
        int sumOfProbablity = 0 ;
        for(Award award : awardList){
            sumOfProbablity += award.getProbability();
        }
        int randomNumber = ThreadLocalRandom.current().nextInt(sumOfProbablity);
        int range = 0 ;
        for(Award award : awardList){
            range += award.getProbability() ;
            if(randomNumber < range){
                return award;
            }
        }
        return null;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<Award> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<Award> awardList) {
        this.awardList = awardList;
    }
}

package com.guce.backtrace;

/**
 *
 * https://leetcode-cn.com/problems/additive-number/
 * 判断是否是累加数
 * @Author chengen.gce
 * @DATE 2021/4/18 2:57 下午
 */
public class IsAdditiveNumber {


    public boolean isAdditiveNumber(String s){

        char[] chArr = s.toCharArray() ;
        return dfs(chArr , 0,0,0,0);
    }

    /**
     *
     * @param chArr
     * @param idx
     * @param count
     * @param prevPre
     * @param pre
     * @return
     */
    public boolean dfs(char[] chArr , int idx ,int count, long prevPre ,long pre ){
        if (idx == chArr.length) {
            return count > 2 ;
        }

        long curr = 0 ;
        for (int i = idx ; i < chArr.length ; i++ ){

            /// 如果首字母是 0 ；比如是 01 这类的数 直接返回
            if (i > idx && chArr[idx] == '0'){
                return false;
            }
            curr = curr * 10 + chArr[i] - '0';
            /////前面已经有2个值了
            if (  count >= 2 ){
                long sum = pre + prevPre;
                /////2. 如果前2个数的和 < 当前的数 直接返回 false;
                if (curr > sum) {
                    return false;
                }
                ////3. 如果前2个数的和 > 当前数 ，说明当前数还要继续往后加数
                if (curr < sum){
                    continue;
                }
            }
            /////prevPre + pre = curr 或 count < 2
            if (dfs(chArr , i + 1, count + 1 ,pre , curr )){
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) {

        IsAdditiveNumber number = new IsAdditiveNumber();
        System.out.println(number.isAdditiveNumber("112358"));
    }
}

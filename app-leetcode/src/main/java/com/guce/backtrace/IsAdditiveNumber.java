package com.guce.backtrace;

/**
 * @Author chengen.gce
 * @DATE 2021/4/18 2:57 下午
 */
public class IsAdditiveNumber {


    public boolean isAdditiveNumber(String s){

        char[] chArr = s.toCharArray() ;
        return dfs(chArr , chArr.length , 0,0,0,0);
    }

    //////
    public boolean dfs(char[] chArr , int len ,int idx , int preVal ,int sum ,int count){
        if (idx == len) {
            return count >= 3 ;
        }

        for (int i = idx ; i < len ; i++ ){

            ///转换成数字
            int curr = toNumber(chArr,idx , i);

            if (curr < 0){
                continue;
            }
            if (  count >= 2 && sum != curr){
                continue;
            }

            if (dfs(chArr ,len, i + 1 ,curr , preVal + curr , count + 1)){
                return true;
            }
        }
        return false;
    }

    public int toNumber(char[] chArr , int start , int end){

        if (start != end && chArr[start] == '0' ){
            return -1 ;
        }
        int res = 0 ;
        for (int i = start ; i <= end ; i++ ){
            res = res * 10 + chArr[i] - '0';
        }
        return res;
    }

    public static void main(String[] args) {

        IsAdditiveNumber number = new IsAdditiveNumber();
        System.out.println(number.isAdditiveNumber("112358"));
    }
}

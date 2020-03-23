package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/3/20 10:23 下午
 */
public class IntegerReplacement {

    public static int solution(int n){
        int x = 0 ;

        while (n > 1){
            if ( (n & 1) == 0){
                n = n >>> 1;
                x++ ;
            }else {
                if ( (n & 2) == 0){
                    n -= 1;
                    x++;
                }else {
                    if (n == 3){
                        x += 2 ;
                        break;
                    }else {
                        n += 1;
                    }
                    x++;

                }

            }


        }
        return x;
    }

    public static void main(String[] args) {
        System.out.println(solution(1234));
    }

}

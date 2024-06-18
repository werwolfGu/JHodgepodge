package com.guce;

import java.util.Arrays;

/**
 * @Author chengen.gce
 * @DATE 2024/6/10 15:51
 */
public class NumRescueBoats {

    public int numRescueBoats(int[] people, int limit) {

        int ligth = 0 , heavy = people.length - 1, ans = 0 ;
        Arrays.sort(people);
        while (ligth <= heavy) {
            if (people[ligth] + people[heavy] <= limit) {
                ligth++;
            }
            heavy--;
            ans++;
        }

        return ans;
    }

    public static void main(String[] args) {
        int[] people = {1,1,1,1,1};
        int limit = 5;
        System.out.println(new NumRescueBoats().numRescueBoats(people,limit));
    }
}

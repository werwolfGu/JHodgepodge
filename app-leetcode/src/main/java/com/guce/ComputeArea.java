package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/3/20 10:16 下午
 * https://leetcode-cn.com/problems/rectangle-area/submissions/
 */
public class ComputeArea {

    public static int solution(int A, int B, int C, int D, int E, int F, int G, int H){
        int x1 = Math.max(A,E);
        int y1 = Math.max(B,F);
        int x2 = Math.min(G,C);
        int y2 = Math.min(D,H);
        int area1 = (C -A) * (D - B);
        int area2 = (G - E) * (H -F);
        int area = area1 + area2;
        if ( x1 < x2 && y1 < y2){
            area -= (x2 - x1) * (y2 - y1);
        }
        return area;
    }

    public static void main(String[] args) {
        System.out.println(solution(-3, 0, 3, 4, 0, -1, 9, 2));
        System.out.println( (5 & 1) == 1 );
    }
}

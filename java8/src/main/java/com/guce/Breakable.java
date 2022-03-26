package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/3/29 12:56 上午
 */
public class Breakable {
    String id;
    private int failcount;
    public Breakable(String id, int failcount) {
        this.id = id;
        this.failcount = failcount;
    }
    @Override
    public String toString() {
        return "Breakable_" + id + " [" + failcount + "]";
    }
    public static Breakable work(Breakable b) {
        if(--b.failcount == 0) {
            System.out.println( "Throwing Exception for " + b.id + "");
            throw new RuntimeException( "Breakable_" + b.id + " failed");
        }
        System.out.println(b);
        return b;
    }

    public static void main(String[] args) {

        int COUNT_BITS = Integer.SIZE - 3;
        int SHUTDOWN   =  1 << COUNT_BITS;
        System.out.println(SHUTDOWN);
    }
}

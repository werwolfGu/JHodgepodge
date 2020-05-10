package com.guce.bigdata;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author chengen.gce
 * @DATE 2020/4/11 11:25 上午
 */
public class BigDataSort implements IBigData {

    private final static int DEFAULT_SEGMENT_ARR_LEN = 10000;
    private final static int DEFAULT_ARR_LEN = 10;

    private int ARR_LEN ;

    private int segmentLen;

    private volatile Segment[] segmentArr;

    public BigDataSort(){
        segmentArr = new Segment[DEFAULT_ARR_LEN];
        segmentLen = DEFAULT_ARR_LEN ;
        ARR_LEN = DEFAULT_SEGMENT_ARR_LEN;
    }

    @Override
    public int get(int idx) {

        int segmentIdx = idx / ARR_LEN;
        int arrLen = idx % ARR_LEN ;

        return segmentArr[segmentIdx].nodes[arrLen];
    }

    @Override
    public int set(int idx) {

        int segmentIdx = idx / ARR_LEN;
        int arrIdx = idx % ARR_LEN;
        Segment tmpSegment ;
        if (segmentIdx > segmentArr.length){
            resize(segmentIdx);
        }
        if (tabAt(segmentArr,segmentIdx) == null){
            tmpSegment = new Segment();
            casTabAt(segmentArr,segmentIdx,null,tmpSegment);
        }
        tmpSegment = segmentArr[segmentIdx];
        tmpSegment.nodes[arrIdx]++ ;
        tmpSegment.totailSum.getAndIncrement();
        return 0;
    }

    @Override
    public int remove(int idx) {
        int segmentIdx = idx / ARR_LEN;
        int arrIdx = idx % ARR_LEN;
        if (segmentIdx > segmentArr.length){
            return 0;
        }
        if (segmentArr[segmentIdx].nodes == null){
            return 0 ;
        }
        segmentArr[segmentIdx].nodes[arrIdx] -= 1 ;
        segmentArr[segmentIdx].totailSum.decrementAndGet();
        return 0;
    }

    public void resize(int size){
        int len = size << 1;
        Segment[] nt = new Segment[len];
        transfer(segmentArr,nt);
        segmentArr = nt;
        segmentLen = len;

    }
    public void transfer(Segment[] src ,Segment nt[]){

        for (int i = 0 ; i < src.length ; i++){
            if (src[i] != null){
                nt[i] = src[i];

            }
        }
    }
    static final BigDataSort.Segment tabAt(BigDataSort.Segment[] tab, int i) {
        return (BigDataSort.Segment)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);
    }

    static final boolean casTabAt(BigDataSort.Segment[] tab, int i,
                                        BigDataSort.Segment c, BigDataSort.Segment v) {
        return U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
    }

    public static class Segment{
        AtomicInteger totailSum;
        volatile int[] nodes;

        public Segment(){
            nodes = new int[DEFAULT_SEGMENT_ARR_LEN];
            totailSum = new AtomicInteger(0);
        }

        public synchronized int set (int idx){

            totailSum.getAndIncrement();
           return nodes[idx]++ ;
        }

        public int get (int idx){
            return nodes[idx] ;
        }
        
    }

    private static final sun.misc.Unsafe U;

    private static final long ABASE;
    private static final int ASHIFT;

    static {
        try {
            U = getUnsafeInstance();
            Class<?> ak = BigDataSort.Segment[].class;
            ABASE = U.arrayBaseOffset(ak);
            int scale = U.arrayIndexScale(ak);
            if ((scale & (scale - 1)) != 0){
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static Unsafe getUnsafeInstance() throws SecurityException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeInstance.setAccessible(true);
        return (Unsafe) theUnsafeInstance.get(Unsafe.class);
    }

    public static void main(String[] args) {

        BigDataSort bigDataSort = new BigDataSort();
        bigDataSort.set(2);
        bigDataSort.set(2);
        bigDataSort.set(2);
        bigDataSort.set(5);
        bigDataSort.set(5);
        bigDataSort.set(5);
        bigDataSort.set(1090000);
        bigDataSort.set(5);

        bigDataSort.set(1090000);
        bigDataSort.set(1090000);
        bigDataSort.set(1090002);
        bigDataSort.set(1090001);

        System.out.println(bigDataSort.get(2));
        System.out.println(bigDataSort.get(5));
        System.out.println(bigDataSort.get(1090000));
        System.out.println(bigDataSort.get(1090001));
        System.out.println(bigDataSort.get(1090002));
        bigDataSort.remove(5);
        bigDataSort.remove(5);
        System.out.println(bigDataSort.get(5));


    }
}

package com.guce;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author chengen.gce
 * @DATE 2020/3/29 12:42 上午
 */
public class CompletableOperations {
    static CompletableFuture<Integer> cfi(int i) {
        return CompletableFuture.completedFuture( Integer.valueOf(i));
    }
    public static void main(String[] args) {

        showr(cfi(1)); // Basic test
        voidr(cfi(2).runAsync(() ->
                System.out.println("runAsync")));
        voidr(cfi(3).thenRunAsync(() ->
                System.out.println("thenRunAsync")));
        voidr(CompletableFuture.runAsync(() ->
                System.out.println("runAsync is static")));
        showr(CompletableFuture.supplyAsync(() -> 99));
        voidr(cfi(4).thenAcceptAsync(i ->
                System.out.println("thenAcceptAsync: " + i)));
        showr(cfi(5).thenApplyAsync(i -> i + 42));
        showr(cfi(6).thenComposeAsync(i -> cfi(i + 99)));
        CompletableFuture<Integer> c = cfi(7);
        c.obtrudeValue(111);
        showr(c);
        showr(cfi(8).toCompletableFuture());
        c = new CompletableFuture<>();
        c.complete(9);
        showr(c);
        c = new CompletableFuture<>();
        c.cancel(true);
        System.out.println("cancelled: " + c.isCancelled());
        System.out.println("completed exceptionally: " +
                c.isCompletedExceptionally());
        System.out.println("done: " + c.isDone());
        System.out.println(c);
        c = new CompletableFuture<>();
        System.out.println(c.getNow(777));
        c = new CompletableFuture<>();
        c.thenApplyAsync(i -> i + 42)
                .thenApplyAsync(i -> i * 12);
        System.out.println("dependents: " + c.getNumberOfDependents());
        c.thenApplyAsync(i -> i / 2);
        System.out.println("dependents: " + c.getNumberOfDependents());
    }

    public static void showr(CompletableFuture<Integer> fun){
        try {
            System.out.println(fun.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void voidr(CompletableFuture<Void> fun){
        try {
            fun.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}

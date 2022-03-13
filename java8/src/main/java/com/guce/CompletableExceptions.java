package com.guce;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author chengen.gce
 * @DATE 2020/3/29 12:55 上午
 */
public class CompletableExceptions {
    static CompletableFuture<Breakable> test(String id, int failcount) {
        return
                CompletableFuture.completedFuture(
                        new Breakable(id, failcount))
                        .thenApply(Breakable::work)
                        .thenApply(Breakable::work)
                        .thenApply(Breakable::work)
                        .thenApply(Breakable::work);
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Exceptions don't appear ..

        CompletableFuture abcFuture = CompletableFuture.runAsync( () -> {
            try {
                System.out.println("abcFuture: " + System.currentTimeMillis());
                Thread.sleep(3_000L);

            } catch (InterruptedException e) {
                System.out.printf("异常");
            }
        }).thenCombine( CompletableFuture.runAsync( () -> {
            try {
                System.out.println("abcFuture : " + System.currentTimeMillis());
                Thread.sleep(3_000L);

            } catch (InterruptedException e) {
                System.out.printf("异常");
            }
        }),(a,b) -> {
            System.out.println("bath:" + System.currentTimeMillis());
            return null ;});
        abcFuture.get();

    }
}

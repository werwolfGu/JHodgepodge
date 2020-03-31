package com.guce;

import java.util.concurrent.CompletableFuture;

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
    public static void main(String[] args) {
        // Exceptions don't appear ...
        test("A", 1);
        test("B", 2);
        test("C", 3);
        test("D", 4);
        test("E", 5);
        // ... until you try to fetch the value:
        try {
            test("F", 2).get(); // or join()
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        // Test for exceptions:
        System.out.println(
                test("G", 2).isCompletedExceptionally());
        // Counts as "done":
        System.out.println(test("H", 2).isDone());
        // Force an exception:
        CompletableFuture<Integer> cfi =
                new CompletableFuture<>();
        System.out.println("done? " + cfi.isDone());
        cfi.completeExceptionally( new RuntimeException("forced"));
        try {
            cfi.get();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

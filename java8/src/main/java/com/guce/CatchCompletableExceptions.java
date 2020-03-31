package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/3/29 12:55 上午
 */
public class CatchCompletableExceptions {
    static void handleException(int failcount) {
        // Call the Function only if there's an
        // exception, must produce same type as came in:
        CompletableExceptions
                .test("exceptionally", failcount)
                .exceptionally((ex) -> { // Function
                    if(ex == null)
                        System.out.println("I don't get it yet");
                    return new Breakable(ex.getMessage(), 0);
                })
                .thenAccept(str ->
                        System.out.println("result: " + str));
        // Create a new result (recover):
        CompletableExceptions
                .test("handle", failcount)
                .handle((result, fail) -> { // BiFunction
                    if(fail != null)
                        return "Failure recovery object";
                    else
                        return result + " is good"; })
                .thenAccept(str ->
                        System.out.println("result: " + str));
        // Do something but pass the same result through:
        CompletableExceptions
                .test("whenComplete", failcount)
                .whenComplete((result, fail) -> {// BiConsumer
                    if(fail != null)
                        System.out.println("It failed");
                    else
                        System.out.println(result + " OK");
                })
                .thenAccept(r ->
                        System.out.println("result: " + r));
    }
    public static void main(String[] args) {
        System.out.println("**** Failure Mode ****");
        handleException(2);
        System.out.println("**** Success Mode ****");
        handleException(0);
    }
}

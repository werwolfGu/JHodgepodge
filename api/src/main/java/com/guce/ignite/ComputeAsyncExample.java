package com.guce.ignite;

public class ComputeAsyncExample {

    /*public static void main(String[] args) throws IgniteException {
        try (Ignite ignite = Ignition.start("config/example-ignite.xml")) {
            System.out.println();
            System.out.println("Compute asynchronous example started.");

            // Enable asynchronous mode.
            IgniteCompute compute = ignite.compute().withAsync();

            Collection<IgniteFuture<?>> futs = new ArrayList<>();

            // Iterate through all words in the sentence and create runnable jobs.
            for (final String word : "Print words using runnable".split(" ")) {
                // Execute runnable on some node.
                compute.run(() -> {
                    System.out.println();
                    System.out.println(">>> Printing '" + word + "' on this node from ignite job.");
                });

                futs.add(compute.future());
            }

            // Wait for completion of all futures.
            futs.forEach(IgniteFuture::get);

            System.out.println();
            System.out.println(">>> Finished printing words using runnable execution.");
            System.out.println(">>> Check all nodes for output (this node is also part of the cluster).");
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}

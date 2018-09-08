package com.guce.ignite;

public class IgniteDemo {

    /*public static void main(String[] args) {

        *//*ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
        Ignite ignite = Ignition.start(cfg);
        IgniteCache<Integer, Object> cache = ignite.getOrCreateCache("c1");
//        cache.clear();

        int keyCnt = 10;
        System.out.println("Synchronously put records ...");
        for (int i = 0; i < keyCnt; i++)
            cache.put(i, Integer.toString(i));

        for (int i = 0; i < keyCnt; i++)
            System.out.println("Got [key=" + i + ", val=" + cache.get(i) + "] ");*//*

        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");

        try (IgniteClient igniteClient = Ignition.startClient(cfg)) {
            System.out.println();
            System.out.println(">>> Thin client put-get example started.");

            final String CACHE_NAME = "put-get-example";

            ClientCache<Integer, Address> cache = igniteClient.getOrCreateCache(CACHE_NAME);

            System.out.format(">>> Created cache [%s].\n", CACHE_NAME);

            Integer key = 1;
            Address val = new Address("1545 Jackson Street", 94612);

            cache.put(key, val);

            System.out.format(">>> Saved [%s] in the cache.\n", val);

            Address cachedVal = cache.get(key);

            System.out.format(">>> Loaded [%s] from the cache.\n", cachedVal);
        } catch (ClientException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }catch (Exception e) {
            System.err.format("Unexpected failure: %s\n", e);
        }

    }*/
}

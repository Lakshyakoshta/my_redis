package com.myredis.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataStoreTest {

    @Test
    void shouldStoreAndRetrieveValue() {

        DataStore store = new DataStore();

        store.set("name", "Lakshya");

        assertEquals(
                "Lakshya",
                store.get("name")
        );
    }

    @Test
    void shouldReturnNullForMissingKey() {

        DataStore store = new DataStore();

        assertNull(
                store.get("missing")
        );
    }

    @Test
    void shouldKnowIfKeyExists() {

        DataStore store = new DataStore();

        store.set("name", "Lakshya");

        assertTrue(
                store.exists("name")
        );

        assertFalse(
                store.exists("missing")
        );
    }

    @Test
    void shouldDeleteValue() {

        DataStore store = new DataStore();

        store.set("name", "Lakshya");

        assertTrue(
                store.delete("name")
        );

        assertNull(
                store.get("name")
        );
    }

    @Test
    void shouldOverwriteExistingValue() {

        DataStore store = new DataStore();

        store.set("name", "Lakshya");
        store.set("name", "Rahul");

        assertEquals(
                "Rahul",
                store.get("name")
        );
    }
    @Test
    void shouldSupportConcurrentWrites()
            throws Exception {
    
        DataStore store =
                new DataStore();
    
        int threadCount = 10;
    
        Thread[] threads =
                new Thread[threadCount];
    
        for (int i = 0; i < threadCount; i++) {
    
            final int threadNumber = i;
    
            threads[i] =
                    new Thread(() -> {
    
                        for (int j = 0; j < 100; j++) {
    
                            store.set(
                                    "key-" + threadNumber + "-" + j,
                                    "value"
                            );
                        }
                    });
    
            threads[i].start();
        }
    
        for (Thread thread : threads) {
            thread.join();
        }
    
        for (int i = 0; i < threadCount; i++) {
    
            for (int j = 0; j < 100; j++) {
    
                assertEquals(
                        "value",
                        store.get(
                                "key-" + i + "-" + j
                        )
                );
            }
        }
    }
    @Test
    void shouldReturnFalseWhenDeletingMissingKey() {
    
        DataStore store =
                new DataStore();
    
        assertFalse(
                store.delete("unknown")
        );
    }
    @Test
    void shouldExpireValueAfterTtl() throws Exception {
    
        DataStore store =
                new DataStore();
    
        store.set(
                "name",
                "Lakshya",
                100
        );
    
        assertEquals(
                "Lakshya",
                store.get("name")
        );
    
        Thread.sleep(150);
    
        assertNull(
                store.get("name")
        );
    }
    @Test
    void shouldKeepValueWhenNoTtlIsSet() {
    
        DataStore store =
                new DataStore();
    
        store.set(
                "name",
                "Lakshya"
        );
    
        assertEquals(
                "Lakshya",
                store.get("name")
        );
    }
    @Test
    void shouldReturnFalseForExpiredKey() throws InterruptedException {
    
        DataStore store = new DataStore();
    
        store.set(
                "name",
                "Lakshya",
                100
        );
    
        assertTrue(store.exists("name"));
    
        Thread.sleep(150);
    
        assertFalse(store.exists("name"));
    }
    @Test
    void shouldReturnNegativeTwoForMissingKey() {
    
        DataStore store = new DataStore();
    
        assertEquals(
                -2,
                store.ttl("missing")
        );
    }
    @Test
    void shouldReturnNegativeOneForKeyWithoutExpiration() {
    
        DataStore store = new DataStore();
    
        store.set(
                "name",
                "Lakshya"
        );
    
        assertEquals(
                -1,
                store.ttl("name")
        );
    }
    @Test
    void shouldReturnRemainingTtl() throws InterruptedException {
    
        DataStore store = new DataStore();
    
        store.set(
                "name",
                "Lakshya",
                5000
        );
    
        long ttl =
                store.ttl("name");
    
        assertTrue(
                ttl >= 4 && ttl <= 5
        );
    
        Thread.sleep(5100);
    
        assertEquals(
                -2,
                store.ttl("name")
        );
    }
    @Test
    void shouldReturnRemainingTtlInMilliseconds() {
    
        DataStore store = new DataStore();
    
        store.set(
                "name",
                "Lakshya",
                5000
        );
    
        long pttl =
                store.pttl("name");
    
        assertTrue(
                pttl > 0 &&
                pttl <= 5000
        );
    }
    @Test
    void shouldReturnNegativeOneForPttlOfNonExpiringKey() {
    
        DataStore store = new DataStore();
    
        store.set(
                "name",
                "Lakshya"
        );
    
        assertEquals(
                -1,
                store.pttl("name")
        );
    }
    @Test
    void shouldReturnNegativeTwoForPttlOfMissingKey() {
    
        DataStore store = new DataStore();
    
        assertEquals(
                -2,
                store.pttl("missing")
        );
    }
}
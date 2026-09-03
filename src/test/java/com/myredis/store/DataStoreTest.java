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

        store.delete("name");

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
}
package com.myredis.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntryTest {

    @Test
    void shouldStoreValueAndExpiration() {

        Entry entry =
                new Entry(
                        "Lakshya",
                        12345L
                );

        assertEquals(
                "Lakshya",
                entry.getValue()
        );

        assertEquals(
                12345L,
                entry.getExpiresAt()
        );
    }
}
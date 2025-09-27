package com.example.hello_spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CacheService to verify caching functionality
 */
@SpringBootTest
class CacheServiceTest {

    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new CacheService();
    }

    @Test
    void testPutAndGet() {
        // Test basic put and get operations
        String key = "testKey";
        String value = "testValue";
        
        cacheService.put(key, value);
        Object retrievedValue = cacheService.get(key);
        
        assertEquals(value, retrievedValue);
    }

    @Test
    void testPutAndGetWithDifferentDataTypes() {
        // Test with string
        cacheService.put("stringKey", "stringValue");
        assertEquals("stringValue", cacheService.get("stringKey"));
        
        // Test with integer
        cacheService.put("intKey", 42);
        assertEquals(42, cacheService.get("intKey"));
        
        // Test with boolean
        cacheService.put("boolKey", true);
        assertEquals(true, cacheService.get("boolKey"));
        
        // Test with custom object
        TestObject testObj = new TestObject("test", 123);
        cacheService.put("objKey", testObj);
        TestObject retrievedObj = (TestObject) cacheService.get("objKey");
        assertEquals(testObj.getName(), retrievedObj.getName());
        assertEquals(testObj.getValue(), retrievedObj.getValue());
    }

    @Test
    void testGetNonExistentKey() {
        // Test getting a key that doesn't exist
        Object result = cacheService.get("nonExistentKey");
        assertNull(result);
    }

    @Test
    void testGetWithNullKey() {
        // Test getting with null key
        Object result = cacheService.get(null);
        assertNull(result);
    }

    @Test
    void testPutWithNullKey() {
        // Test putting with null key should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            cacheService.put(null, "value");
        });
    }

    @Test
    void testPutWithNullValue() {
        // Test putting with null value should throw exception (Caffeine doesn't support null values)
        String key = "nullValueKey";
        assertThrows(IllegalArgumentException.class, () -> {
            cacheService.put(key, null);
        });
    }

    @Test
    void testDelete() {
        // Test delete functionality
        String key = "deleteKey";
        String value = "deleteValue";
        
        cacheService.put(key, value);
        assertEquals(value, cacheService.get(key));
        
        cacheService.delete(key);
        assertNull(cacheService.get(key));
    }

    @Test
    void testDeleteNonExistentKey() {
        // Test deleting a key that doesn't exist (should not throw exception)
        assertDoesNotThrow(() -> {
            cacheService.delete("nonExistentKey");
        });
    }

    @Test
    void testDeleteWithNullKey() {
        // Test deleting with null key (should not throw exception)
        assertDoesNotThrow(() -> {
            cacheService.delete(null);
        });
    }

    @Test
    void testSize() {
        // Test size functionality
        assertEquals(0, cacheService.size());
        
        cacheService.put("key1", "value1");
        assertEquals(1, cacheService.size());
        
        cacheService.put("key2", "value2");
        assertEquals(2, cacheService.size());
        
        cacheService.delete("key1");
        assertEquals(1, cacheService.size());
    }

    @Test
    void testClear() {
        // Test clear functionality
        cacheService.put("key1", "value1");
        cacheService.put("key2", "value2");
        cacheService.put("key3", "value3");
        
        assertEquals(3, cacheService.size());
        
        cacheService.clear();
        assertEquals(0, cacheService.size());
        
        // Verify all keys are gone
        assertNull(cacheService.get("key1"));
        assertNull(cacheService.get("key2"));
        assertNull(cacheService.get("key3"));
    }

    @Test
    void testOverwriteExistingKey() {
        // Test overwriting an existing key
        String key = "overwriteKey";
        String originalValue = "originalValue";
        String newValue = "newValue";
        
        cacheService.put(key, originalValue);
        assertEquals(originalValue, cacheService.get(key));
        
        cacheService.put(key, newValue);
        assertEquals(newValue, cacheService.get(key));
        
        // Size should remain 1
        assertEquals(1, cacheService.size());
    }

    @Test
    void testCacheServiceIsSingleton() {
        // Verify that the service maintains state across calls
        cacheService.put("singletonTest", "testValue");
        
        // Create another instance and verify it doesn't share state
        // (This tests that individual instances work correctly)
        CacheService anotherInstance = new CacheService();
        assertNull(anotherInstance.get("singletonTest"));
    }

    /**
     * Helper class for testing custom objects in cache
     */
    private static class TestObject {
        private final String name;
        private final int value;

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        public int getValue() { return value; }
    }
}
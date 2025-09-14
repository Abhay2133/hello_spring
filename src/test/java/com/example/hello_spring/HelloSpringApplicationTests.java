package com.example.hello_spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.devtools.restart.enabled=false")
class HelloSpringApplicationTests {

@LocalServerPort
private int port;

private TestRestTemplate restTemplate = new TestRestTemplate();
private ObjectMapper objectMapper = new ObjectMapper();

@Test
void contextLoads() {
}

@Test
void memoryEndpointReturnsHumanReadableFormat() throws Exception {
String url = "http://localhost:" + port + "/memory";
ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

assertEquals(200, response.getStatusCode().value());

JsonNode json = objectMapper.readTree(response.getBody());

// Check that raw memory values exist
assertTrue(json.has("usedMemory"));
assertTrue(json.has("freeMemory"));
assertTrue(json.has("totalMemory"));
assertTrue(json.has("maxMemory"));

// Check that formatted memory values exist
assertTrue(json.has("usedMemoryFormatted"));
assertTrue(json.has("freeMemoryFormatted"));
assertTrue(json.has("totalMemoryFormatted"));
assertTrue(json.has("maxMemoryFormatted"));

// Check that usage percentage exists
assertTrue(json.has("usagePercentage"));

// Verify formatted values contain units (KB, MB, GB, B)
String usedFormatted = json.get("usedMemoryFormatted").asText();
String freeFormatted = json.get("freeMemoryFormatted").asText();
String totalFormatted = json.get("totalMemoryFormatted").asText();
String maxFormatted = json.get("maxMemoryFormatted").asText();

assertTrue(usedFormatted.matches(".*\\d+\\.\\d+\\s+(B|KB|MB|GB)"));
assertTrue(freeFormatted.matches(".*\\d+\\.\\d+\\s+(B|KB|MB|GB)"));
assertTrue(totalFormatted.matches(".*\\d+\\.\\d+\\s+(B|KB|MB|GB)"));
assertTrue(maxFormatted.matches(".*\\d+\\.\\d+\\s+(B|KB|MB|GB)"));

// Verify usage percentage is a valid number between 0 and 100
double usagePercentage = json.get("usagePercentage").asDouble();
assertTrue(usagePercentage >= 0 && usagePercentage <= 100);
}
}

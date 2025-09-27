package com.example.hello_spring.cron_jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service to ping a configured URL at regular intervals.
 * Useful for keeping applications alive on free hosting services.
 */
@Service
public class PingService {

    private static final Logger logger = LoggerFactory.getLogger(PingService.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${ping.url:}")
    private String pingUrl;

    @Value("${ping.interval:5}")
    private int pingInterval;

    @Value("${ping.enabled:false}")
    private boolean pingEnabled;

    private final RestTemplate restTemplate;
    private int pingCount = 0;
    private String lastPingResult = "Not started";
    private LocalDateTime lastPingTime;

    public PingService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Scheduled method that pings the configured URL.
     * The interval is configurable via the PING_INTERVAL environment variable.
     */
    @Scheduled(fixedRateString = "#{${ping.interval:5} * 60 * 1000}", initialDelayString = "10000")
    public void pingUrl() {
        if (!pingEnabled || pingUrl == null || pingUrl.trim().isEmpty()) {
            logger.debug("Ping service is disabled or URL not configured");
            return;
        }

        try {
            logger.info("Pinging URL: {} (Attempt #{}) at {}", 
                pingUrl, ++pingCount, LocalDateTime.now().format(formatter));
            
            long startTime = System.currentTimeMillis();
            String response = restTemplate.getForObject(pingUrl, String.class);
            long responseTime = System.currentTimeMillis() - startTime;
            
            lastPingTime = LocalDateTime.now();
            lastPingResult = String.format("SUCCESS - Response time: %dms", responseTime);
            
            logger.info("Ping successful! Response time: {}ms", responseTime);
            
            // Log first 100 characters of response for debugging
            if (response != null && !response.trim().isEmpty()) {
                String shortResponse = response.length() > 100 ? 
                    response.substring(0, 100) + "..." : response;
                logger.debug("Response preview: {}", shortResponse.trim());
            }
            
        } catch (RestClientException e) {
            lastPingTime = LocalDateTime.now();
            lastPingResult = "FAILED - " + e.getMessage();
            logger.error("Failed to ping URL: {} - Error: {}", pingUrl, e.getMessage());
        } catch (Exception e) {
            lastPingTime = LocalDateTime.now();
            lastPingResult = "ERROR - " + e.getMessage();
            logger.error("Unexpected error during ping: {}", e.getMessage(), e);
        }
    }

    /**
     * Get ping service status information
     */
    public PingStatus getStatus() {
        return new PingStatus(
            pingEnabled,
            pingUrl,
            pingInterval,
            pingCount,
            lastPingResult,
            lastPingTime != null ? lastPingTime.format(formatter) : "Never"
        );
    }

    /**
     * Data class for ping status information
     */
    public static class PingStatus {
        private final boolean enabled;
        private final String url;
        private final int intervalMinutes;
        private final int totalPings;
        private final String lastResult;
        private final String lastPingTime;

        public PingStatus(boolean enabled, String url, int intervalMinutes, 
                         int totalPings, String lastResult, String lastPingTime) {
            this.enabled = enabled;
            this.url = url;
            this.intervalMinutes = intervalMinutes;
            this.totalPings = totalPings;
            this.lastResult = lastResult;
            this.lastPingTime = lastPingTime;
        }

        // Getters
        public boolean isEnabled() { return enabled; }
        public String getUrl() { return url; }
        public int getIntervalMinutes() { return intervalMinutes; }
        public int getTotalPings() { return totalPings; }
        public String getLastResult() { return lastResult; }
        public String getLastPingTime() { return lastPingTime; }
    }
}
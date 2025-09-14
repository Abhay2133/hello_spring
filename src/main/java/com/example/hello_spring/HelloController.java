package com.example.hello_spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@Autowired
	private PingService pingService;

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@GetMapping("/memory")
	public MemoryResponse getMemoryUsage() {
		Runtime runtime = Runtime.getRuntime();
		long totalMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		long usedMemory = totalMemory - freeMemory;
		long maxMemory = runtime.maxMemory();
		
		return new MemoryResponse(usedMemory, freeMemory, totalMemory, maxMemory);
	}

	/**
	 * Memory response data class
	 */
	public static class MemoryResponse {
		private final long usedMemory;
		private final long freeMemory;
		private final long totalMemory;
		private final long maxMemory;
		private final String usedMemoryFormatted;
		private final String freeMemoryFormatted;
		private final String totalMemoryFormatted;
		private final String maxMemoryFormatted;
		private final double usagePercentage;
		
		public MemoryResponse(long usedMemory, long freeMemory, long totalMemory, long maxMemory) {
			this.usedMemory = usedMemory;
			this.freeMemory = freeMemory;
			this.totalMemory = totalMemory;
			this.maxMemory = maxMemory;
			this.usedMemoryFormatted = formatBytes(usedMemory);
			this.freeMemoryFormatted = formatBytes(freeMemory);
			this.totalMemoryFormatted = formatBytes(totalMemory);
			this.maxMemoryFormatted = formatBytes(maxMemory);
			this.usagePercentage = Math.round((double) usedMemory / totalMemory * 100.0 * 100.0) / 100.0;
		}
		
		/**
		 * Format bytes into human readable format
		 */
		private static String formatBytes(long bytes) {
			if (bytes < 1024) return bytes + " B";
			double kb = bytes / 1024.0;
			if (kb < 1024) return String.format("%.2f KB", kb);
			double mb = kb / 1024.0;
			if (mb < 1024) return String.format("%.2f MB", mb);
			double gb = mb / 1024.0;
			return String.format("%.2f GB", gb);
		}
		
		// Getters for raw values
		public long getUsedMemory() { return usedMemory; }
		public long getFreeMemory() { return freeMemory; }
		public long getTotalMemory() { return totalMemory; }
		public long getMaxMemory() { return maxMemory; }
		
		// Getters for formatted values
		public String getUsedMemoryFormatted() { return usedMemoryFormatted; }
		public String getFreeMemoryFormatted() { return freeMemoryFormatted; }
		public String getTotalMemoryFormatted() { return totalMemoryFormatted; }
		public String getMaxMemoryFormatted() { return maxMemoryFormatted; }
		public double getUsagePercentage() { return usagePercentage; }
	}

	@GetMapping("/ping-status")
	public PingService.PingStatus getPingStatus() {
		return pingService.getStatus();
	}

	@GetMapping("/health")
	public HealthResponse health() {
		return new HealthResponse("UP", "Hello Spring Boot is running", 
			System.currentTimeMillis(), pingService.getStatus());
	}

	/**
	 * Health response data class
	 */
	public static class HealthResponse {
		private final String status;
		private final String message;
		private final long timestamp;
		private final PingService.PingStatus pingStatus;

		public HealthResponse(String status, String message, long timestamp, 
							 PingService.PingStatus pingStatus) {
			this.status = status;
			this.message = message;
			this.timestamp = timestamp;
			this.pingStatus = pingStatus;
		}

		// Getters
		public String getStatus() { return status; }
		public String getMessage() { return message; }
		public long getTimestamp() { return timestamp; }
		public PingService.PingStatus getPingStatus() { return pingStatus; }
	}
}

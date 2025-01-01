package com.coffeecode;

import com.coffeecode.utils.CustomException;
import com.coffeecode.utils.LoggerUtil;

public class App {
    public static void main(String[] args) {
        LoggerUtil.setThreadContext("sessionId", "SESSION-001");

        // Start Logging
        LoggerUtil.logInfo("Application started");

        // Simulate performance-heavy operation
        long startTime = System.currentTimeMillis();
        simulateIOOperation();
        LoggerUtil.logPerformance("Data processing completed in {} ms", System.currentTimeMillis() - startTime);

        // Security logging
        LoggerUtil.logSecurity("Unauthorized access attempt detected from IP: 192.168.1.100");

        // Audit logging (User actions)
        LoggerUtil.logAudit("User uploaded a file: sample.csv");

        // Exception handling
        try {
            throw new CustomException("Sample exception");
        } catch (CustomException e) {
            LoggerUtil.logError("An error occurred", e);
        } finally {
            LoggerUtil.clearThreadContext();
        }
    }

    private static void simulateIOOperation() {
        try {
            LoggerUtil.logIO("Starting data read operation...");
            Thread.sleep(200); // Simulate file read
            LoggerUtil.logIO("Data read operation completed.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LoggerUtil.logError("IO operation interrupted", e);
        }
    }
}

package com.coffeecode.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.ThreadContext;

public class LoggerUtil {
    private static final Logger logger = LogManager.getLogger(LoggerUtil.class);

    private static final Marker PERFORMANCE_MARKER = MarkerManager.getMarker("PERFORMANCE");
    private static final Marker SECURITY_MARKER = MarkerManager.getMarker("SECURITY");
    private static final Marker IO_MARKER = MarkerManager.getMarker("IO");
    private static final Marker AUDIT_MARKER = MarkerManager.getMarker("AUDIT");

    private LoggerUtil() {
        // Utility class; no instantiation
    }

    // General Info Logging
    public static void logInfo(String message, Object... params) {
        logger.info(message, params);
    }

    // Performance Logging
    public static void logPerformance(String message, Object... params) {
        logger.info(PERFORMANCE_MARKER, message, params);
    }

    // Security Logging
    public static void logSecurity(String message, Object... params) {
        logger.warn(SECURITY_MARKER, message, params);
    }

    // IO Logging
    public static void logIO(String message, Object... params) {
        logger.debug(IO_MARKER, message, params);
    }

    // Audit Logging (e.g., user actions)
    public static void logAudit(String message, Object... params) {
        logger.info(AUDIT_MARKER, message, params);
    }

    // Error Logging
    public static void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    // Add Thread Context (e.g., request ID, user session)
    public static void setThreadContext(String key, String value) {
        ThreadContext.put(key, value);
    }

    // Clear Thread Context
    public static void clearThreadContext() {
        ThreadContext.clearAll();
    }
}

/*
 * sample Use Of Logging class
 * LoggerUtil.logInfo("Application started"); : Just Custom Logging
 * 
 * sample to use performance logging
 * long startTime = System.currentTimeMillis();
 * simulateOperation();
 * LoggerUtil.logPerformance("simulateOperation", startTime);
 * 
 * sample to use security logging
 * LoggerUtil.logSecurity("Invalid login attempt from IP: 192.168.1.1");
 * 
 * sample to catch error
 * try {
 * throw new RuntimeException("Sample exception");
 * } catch (Exception e) {
 * LoggerUtil.logError("An error occurred", e);
 * }
 * finally {
 * 
 * Sample to clear all thread context
 * LoggerUtil.clearThreadContext();
 * 
 * Sample for thread
 * LoggerUtil.putThreadContext("requestId", "REQ-456");
 * LoggerUtil.logInfo("Handling request");
 * LoggerUtil.clearThreadContext();
 * 
 */

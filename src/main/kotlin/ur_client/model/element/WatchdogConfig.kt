package com.wolfscowl.ur_client.model.element


/**
 * Configuration settings for the internal connection watchdog mechanism.
 *
 * The watchdog monitors communication with the robot and can trigger reconnects,
 * track latency, and optionally log its activity.
 *
 * @param silenceTimeoutMillis Time span of inactivity after which a reconnect
 * is triggered, in milliseconds.
 *
 * @param maxReconnectAttempts Maximum number of reconnect attempts
 * (≤ 0 means unlimited).
 *
 * @param reconnectWindowSeconds Time window in which reconnect attempts are counted,
 * in seconds (≤ 0 means unlimited).
 *
 * @param enableLogging Enables or disables watchdog activity logging.
 *
 * @param logPackageThresholdMillis Time after which package latency is logged,
 * in milliseconds (only if logging is enabled).
 */
data class WatchdogConfig(
    val silenceTimeoutMillis: Long = 1000,
    val maxReconnectAttempts: Int = 3,
    val reconnectWindowSeconds: Long = 10,
    val enableLogging: Boolean = false,
    val logPackageThresholdMillis: Long = 120
)
package com.wolfscowl.ur_client

import com.wolfscowl.ur_client.core.internal.URConfig
import com.wolfscowl.ur_client.core.URDashBoard
import com.wolfscowl.ur_client.core.URInterface
import com.wolfscowl.ur_client.core.internal.URApi
import com.wolfscowl.ur_client.model.element.WatchdogConfig

interface UR : URInterface, URDashBoard

/**
 * Creates and initializes a connection to a UR cobot using the provided network
 * configuration.
 *
 * This factory function builds a [URApi] instance based on the given host address
 * and optional configuration parameters. The returned instance provides full access
 * to the robot’s primary interface and dashboard server, enabling remote control,
 * state monitoring, and command execution.
 *
 * @param host The IP address of the UR cobot.
 * @param interfacePort The port of the primary interface
 * (**Default: 30001** )
 * @param dashBoardPort The port of the dashboard server
 * (**Default:** `29999`)
 * @param connectTimeout Connection timeout in milliseconds for primary interface & dashboard server
 * (**Default: 1000**)
 * @param soTimeout Read timeout in milliseconds only for dashboard server. To control the behavior for the primary interface, use [watchdogConfig].
 * (**Default: 5000**)
 * @param watchdogConfig Configuration for the internal primary interface watchdog
 * (**Default: see [WatchdogConfig]**`)
 * @param logRobotStates Whether robot state messages should be logged
 * (**Default: false**)
 * @param logRobotMessages Whether system and diagnostic messages should be logged
 * (**Default: false**)
 *
 * @return A configured [URApi] instance used to remotely control the robot.
 */
fun UR(
    host: String,
    interfacePort: Int = 30001,
    dashBoardPort: Int = 29999,
    connectTimeout: Int = 1000,
    soTimeout: Int = 5000,
    watchdogConfig: WatchdogConfig = WatchdogConfig(),
    logRobotStates: Boolean = false,
    logRobotMessages: Boolean = false
): UR {
    return URApi(
        URConfig(
            host = host,
            interfacePort = interfacePort,
            dashBoardPort = dashBoardPort,
            connectTimeout = connectTimeout,
            soTimeout = soTimeout,
            watchdogConfig = watchdogConfig,
            logRobotStates = logRobotStates,
            logRobotMessages = logRobotMessages
        )
    )
}
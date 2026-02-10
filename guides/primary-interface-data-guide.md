
# 📖 Guide 2: Primary Interface
### Focus: Real-Time Data & Monitoring


## Introduction

While [Guide 1](/guides/dashboard-server-guide.md) focused on the Dashboard Server for High-Level State Control 
(powering on, loading programs, or releasing brakes), 
Guide 2 is dedicated to the **Primary Interface (Port 30001)** and its capabilities for Real-Time Data & Monitoring.

## 📡 Connection Handling

Unlike the Dashboard Server, which uses short-lived connections, 
the Primary Interface requires a **persistent connection** to stream data. 
You must call `connect()` once at the start of your workflow.

```kotlin
// Setup the robot instance
val ur = UR(
    host = "192.168.2.1",
    // additional optional configurations...
)

try {
    ur.connect()
} catch (e: Exception) {
    println("Host is unavailable")
}
```

## 1. Real-Time Data & Messaging

The Primary Interface continuously broadcasts information at 10Hz (every 100ms). 
<br>This library categorizes that information into two distinct types:
1.  **Robot States**
2. **Robot Messages**

### 1.1 Robot States

Robot States represent the physical and logical condition of the robot (e.g., joint angles, TCP position, temperatures). 
These are updated at 10Hz (every 100ms).
<br>You can access them via **Snapshots** (current value) or **StateFlows** (reactive updates).

| Category                                                                                                                                               | Snapshot Property       | StateFlow (Reactive) |
|:-------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------|:---|
| Connection                                                                                                                                             | `isConnected`           | `isConnectedFlow` |
| [RobotModeData](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_state/-robot-mode-data/index.html)                 | `robotModeData`         | `robotModeDataFlow` |
| [JointData](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_state/-joint-data/index.html)                          | `jointData`             | `jointDataFlow` |
| [JointPosition](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.element/-joint-position/index.html)                      | `jointPosition`         | `jointPositionFlow` |
| [ToolData](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_state/-tool-data/index.html)                            | `toolData`              | `toolDataFlow` |
| [MasterBoardData](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_state/-master-board-data/index.html)             | `masterBoardData`       | `masterBoardDataFlow` |
| [CartesianInfo](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_state/-cartesian-info/index.html)                  | `cartesianInfo`         | `cartesianInfoFlow` |
| [TCP Pose](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.element/-pose/index.html)                                     | `tcpPose`               | `tcpPoseFlow` |
| [TCP Offset](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.element/-pose/index.html)                                   | `tcpOffset`             | `tcpOffsetFlow` |
| [KinematicsInfo](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_state/-kinematics-info/index.html)                | `kinematicsInfo`        | `kinematicsInfoFlow` |
| [ConfigurationData](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_state/-configuration-data/index.html)          | `configurationData`     | `configurationDataFlow` |
| [ForceModeData](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_state/-force-mode-data/index.html)                 | `forceModeData`         | `forceModeDataFlow` |
| [AdditionalInfo](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_state/-additional-info/index.html)                | `additionalInfo`        | `additionalInfoFlow` |
| [ToolCommunicationInfo](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_state/-tool-communication-info/index.html) | `toolCommunicationInfo` | `toolCommunicationInfoFlow` |
| [ToolModeInfo](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_state/-tool-mode-info/index.html)                   | `toolModeInfo`          | `toolModeInfoFlow` |


**Example: Snapshot vs. StateFlow**

```kotlin
// A snapshot is great for a quick check
val currentX = ur.tcpPose?.x ?: 0.0

// A Flow is perfect for continuous monitoring

runBlocking {
    ur.tcpPoseFlow.collect { pose ->
        println("Robot moved to: $pose")
    }
}
```

### 1.2 Robot Messages (Events)

Unlike states, Robot Messages are events. They include execution information, safety mode transitions, or runtime exceptions. They don't represent a continuous value but a point-in-time notification.

The library distinguishes between 9 different message types:
<table>
  <tr>
    <th colspan="3">Robot Message Types</th>
  </tr>
  <tr>
    <td><a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_message/-key-message/index.html">KeyMessage</a></td>
    <td><a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_message/-popup-message/index.html">PopupMessage</a></td>
    <td><a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_message/-program-threads-message/index.html">ProgramThreadsMessage</a></td>
  </tr>
  <tr>
    <td><a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_message/-request-value-message/index.html">RequestValueMessage</a></td>
    <td><a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_message/-robot-comm-message/index.html">RobotCommMessage</a></td>
    <td><a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_message/-runtime-exception-message/index.html">RuntimeExceptionMessage</a></td>
  </tr>
  <tr>
    <td><a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_message/-safety-mode-message/index.html">SafetyModeMessage</a></td>
    <td><a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_message/-text-message/index.html">TextMessage</a></td>
    <td><a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.model.robot_message/-version-message/index.html">VersionMessage</a></td>
  </tr>
</table>



1. **History (List)**: A snapshot list of all messages received since the connection was established.<br> Perfect for logging or post-analysis.


2. **Live Stream (SharedFlow)**: A reactive stream to catch events exactly when they happen.<br> Perfect for error handling and UI alerts.


**Example: Snapshot vs. SharedFlow**
```kotlin
// 1. Access the Message History (Snapshot)
val allMessages = ur.robotMessages
println("Total messages received: ${allMessages.size}")
val lastError = allMessages.filterIsInstance<RuntimeExceptionMessage>().lastOrNull()

// 2. Monitor for Real-Time Events (SharedFlow)
runBlocking {
    ur.robotMessagesFlow.collect { message ->
        when (message) {
            is SafetyModeMessage -> {
                println("Safety Event: ${message.safetyModeType.name}")
            }
            else -> {}
        }
    }
}
```


## ⏭️ Next Step
You now know how to monitor the robot's state. The next step is to make the robot move.

[Go to Guide 3: State-Driven Motion & Procedures](/guides/primary-interface-procedure-guide.md)
— Learn how to send movement commands and synchronize your code with the robot's physical actions using our state-driven procedure logic.




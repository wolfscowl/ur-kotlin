# 📖 Guide 4: Primary Interface
### Focus: OnRobot Gripper Control & Workflow Integration

## Introduction

While [Guide 3](guides/primary-interface-procedures-guide.md) focused on moving the robot arm, Guide 4 is dedicated to OnRobot Gripper Control and its seamless integration into your automation workflows.

Just like the arm movements, every tool command follows the two core principles of this library:
1. **Safety-First (Pre-Flight Checks):** Before any command is sent the library automatically validates:
    - **Connection:** Is the Primary Interface connected?
    - **Robot Mode:** Is the robot in `ROBOT_MODE_RUNNING`?
    - **Safety Status:** Is there an active Emergency or Protective Stop?


2. **State-Driven Design:** Commands are non-blocking. Instead of waiting for the robot to finish, every command returns a "Live State" object. This allows you to monitor progress, handle errors, or await completion asynchronously.


## 1. Supported OnRobot Gripper

The library provides dedicated controllers for the most common OnRobot hardware:

| Gripper                                                                                                                                          | Commands                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | Return |
|:-------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-------|
| RG2 / RG6                                                                                                                                        | [grip()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-r-g/grip.html)<br>[release()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-r-g/release.html)<br>                                                                                                                                                                                                                                                                                                 | [RGToolState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.state/-r-g-tool-state/index.html) |
| 2FG7 / 2FG14                                                                                                                                     | [gripExt()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-t-f-g/grip-ext.html)<br>[gripInt()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-t-f-g/grip-int.html)<br>[releaseExt()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-t-f-g/release-ext.html)<br>[releaseInt()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-t-f-g/release-int.html) | [TFGToolState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.state/-t-f-g-tool-state/index.html) |
| VGC10 / VG10                                                                                                                                     | [grip()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-v-g/grip.html)<br>[release()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-v-g/release.html)<br>[seekGrip()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-v-g/seek-grip.html)                                                                                                                                                                                                                                                                                                          | [VGToolGripState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.state/-v-g-tool-grip-state/index.html)<br>[VGToolReleaseState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.state/-v-g-tool-release-state/index.html)<br>[VGToolSeekState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.state/-v-g-tool-seek-state/index.html) |

## 2. Attaching a Gripper
To control a gripper, you "attach" it to your `UR` instance. This creates a specialized controller for the specific tool type. You must provide the IP of the **OnRobot ComputeBox** (or EyeBox) and the **tool index** (usually `0` for single-tool setups).

```kotlin
val ur = UR(host = "192.168.2.1")
ur.connect()

// Attach different OnRobot models
val gripperRG = ur.attachToolOnRobotRG(host = "192.168.2.2", toolIndex = 0)
val gripper2FG = ur.attachToolOnRobotTFG(host = "192.168.2.2", toolIndex = 0)
val vacuumVG = ur.attachToolOnRobotVG(host = "192.168.2.2", toolIndex = 0)
```


## 3. Usage & Feedback

Tool commands provide rich feedback. For example, an `RGToolState` tells you not just if the command finished, but also the current width and whether an object was actually grasped.

**Gripper Example with Output:**
<table>
<tr>
<td  valign="top">

```kotlin
gripperRG.grip(
    width = 2.0, 
    force = 40, 
    depthComp = false,
    onChange = {},
    onFinished = { state -> println(state) }
)


```
</td> 
<td width="50%" valign="top">

```text
-=RGToolState=-
  scriptName = rg_grip_46050402022026
  runningState = END
  safetyModeStop = false
  errorOccurred = false
  toolDetected = false
  width = 5.0
  depth = 0.0
  gripDetected = true
```
</td> </tr> </table>

## 4. Workflow Synchronization (Await Pattern)

Gripper commands work exactly like arm movements. 
They both return a state object and support the same logic (like `await()`), you can mix arm and tool actions in a single, fluid sequence.

```kotlin
runBlocking {
    // 1. Move to pickup
    ur.arm.movej(pickupPose).await()

    // 2. Grip object (Waiting for the tool to finish)
    val gripState = gripperRG.grip(width = 10.0, force = 40).await()
   
    if (gripState.gripDetected) {
        // 3. Move to drop-off
        ur.arm.movej(dropPose).await()
        // 4. Release
        gripperRG.release(width = 60.0).await()
    }
    else {
        println("Pick failed: No object detected.")
    }
}
```

## 🏁 Conclusion & Beyond

**Congratulations!** You have now mastered the full cycle of robot automation with this library:

1. **High-Level Control** via the Dashboard Server.

2. **Real-Time Monitoring** using the Digital Twin.

3. **State-Driven Motion** and synchronized Procedures.

4. **Tool Integration** for complex physical interactions.

With these building blocks, you can create robust, reactive, and safety-aware applications for Universal Robots.

**Where to go from here:**
- [Examples Script](https://github.com/wolfscowl/ur-kotlin/blob/main/src/main/kotlin/ur_client/examples/Examples.kt): Check out the comprehensive showcase script. It demonstrates everything from powering on the robot and managing `.urp` programs to executing complex `movej` sequences and testing OnRobot end effectors (RG, VG, and TFG).
- [API Reference](https://wolfscowl.github.io/ur-kotlin/): Explore the detailed documentation of all classes, such as `ArmState`, `RGToolState`, and the core `UR` instance.
- [Community & Support](https://github.com/wolfscowl/ur-kotlin/issues): Found a bug or have a feature request? Open an issue on GitHub and help improve the library!

**Happy Coding! 🤖🚀**
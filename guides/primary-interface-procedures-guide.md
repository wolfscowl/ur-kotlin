# 📖 Guide 3: Primary Interface
### Focus: State-Driven Motion, Awaiting Results, and Multi-Step Sequences

## Introduction

While [Guide 2](/guides/primary-interface-data-guide.md) focused on reading data, Guide 3 is dedicated to the Primary Interface's motion capabilities and its state-driven procedure logic.

In this library, all motion and script commands follow two core principles:
1. **Safety-First (Pre-Flight Checks):** Before any command is sent the library automatically validates:
   - **Connection:** Is the Primary Interface connected?
   - **Robot Mode:** Is the robot in `ROBOT_MODE_RUNNING`?
   - **Safety Status:** Is there an active Emergency or Protective Stop?
 

2. **State-Driven Design:** Commands are non-blocking. Instead of waiting for the robot to finish, every command returns a "Live State" object. This allows you to monitor progress, handle errors, or await completion asynchronously.




## 1. Robot Arm Commands

The library provides high-level wrappers for the most common URScript profiles via `ur.arm`. These commands handle the generation of the underlying URScript for you.

| command | Return                                                                                                                     |
|:--------|:---------------------------------------------------------------------------------------------------------------------------|
| [movej](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/movej.html)   | [ArmState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.state/-arm-state/index.html) |
| [movel](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/movel.html)  | [ArmState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.state/-arm-state/index.html)                                                                                                                    |
| [movec](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/movec.html)   | [ArmState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.state/-arm-state/index.html)                                                                                                                    |
| [setTcpOffset](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/set-tcp-offset.html)  | [ArmState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.state/-arm-state/index.html)                                                                                                                    |
| [enterFreeDriveMode](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/enter-free-drive-mode.html) | [ArmState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.state/-arm-state/index.html)                                                                                                                   |
| [exitFreeDriveMode](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/exit-free-drive-mode.html) | [ArmState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.state/-arm-state/index.html)                                                                                                                   |

**Movement Example with Output:**
<table>
<tr>
<td  valign="top">

```kotlin
val joints = ur.jointPosition

if (joints != null) {
   val state = ur.arm.movej(
      q = joints.copy(
         base = joints.base - Math.toRadians(5.0)
      ),
      a = 1.2,
      v = 0.9,
      t = 0.0,
      r = 0.0,
      onChange = {},
      onFinished = { state -> println(state) }
   )
}

```
</td> 
<td width="50%" valign="top">

```text
-=ArmState=-
  scriptName = movej_54440729012026
  runningState = END
  safetyModeStop = false
  errorOccurred = false
  jointPosition = 
    ...
  tcpPose = 
    ...
  tcpOffset = 
    ...
  Payload = 1.0
  PayloadCog = 
    ...
  PayloadInertia = 
    ...
```
</td> </tr> </table>




## 2. Custom Script Command

For specialized logic not covered by the standard controller, you can send raw URScript code directly via the UR instance.

| command                                                                                                                                          | Return                                                                                                                     |
|:-------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------|
| [runURScript](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-interface/run-u-r-script.html)                        | [URScriptState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.state/-u-r-script-state/index.html) |


**Important Rules for Custom Scripts:**

- **Body Only:** Do not wrap your code in a `def ... end` block. The API automatically generates a unique function context and wraps your script before transmission.
- **Error Signaling:** You can manually trigger errors within your script that will be reflected in the `URScriptState`. Use the built-in function: `send_event("error", "Your Title", "Your Message")`.


**URScript Example with Output:**
<table>
<tr>
<td  valign="top">

```kotlin
val urScript = """
   movej([-1.57 + delta, -1.57, 0.00, -3.14, -1.57, -1.57], a=1.4, v=1.05, t=0, r=0)
   send_event("error", "Test Error", "This is just a test :)")
""".trimIndent()

ur.runURScript(
   script = urScript,
   cmdTimeout = 20000,
   onChange = { },
   onFinished = { state -> println(state) }
)
```
</td> 
<td width="50%" valign="top">

```text
-=URScriptState=-
  scriptName = custom_script_58070829012026
  runningState = END
  safetyModeStop = false
  errorOccurred = true
  errors = 
    1. Test Error
      This is just a test :)
      
      
      
```
</td> </tr> </table>

## 3. Workflow Control (Awaiting Results)

Because all commands are non-blocking, the library provides extension functions to manage the timing of your commands. This allows you to build sequential workflows where one action starts only after the previous one has reached a specific state.

### Awaiting Completion

You can let your workflow wait until a command reaches a final state (`runningState` = `END`, `PAUSED`, `CANCELED`, or `TIMEOUT`) before proceeding to the next execution step.
- **Coroutines:** Use `state.await()` or `state.awaitUntil { ... }`.
- **Threads:** Use `state.awaitBlocking()` or `state.awaitBlockingUntil { ... }`.

### When is a command CANCELED?

A command will transition to the `CANCELED` state if:

- **Safety/Pre-flight checks fail:** The robot is not connected, in the correct mode or a safety stop is active.
- **URScript Runtime Exception:** A syntax or logic error occurs during execution on the robot controller.
- **Safety Stop:** An emergency or security stop occurs during execution.
- **Command Overwriting:** A new command is sent via the Primary Interface before the current one has finished. The UR controller immediately aborts the previous script to start the new one.


**Workflow Example:**
```kotlin
runBlocking {
   // 1. Move to a safe position
   val moveState = ur.arm.movej(safePose).await()

   // Check if it was canceled or timed out before proceeding
   if (moveState.runningState == RunningState.END) {
      // 2. Perform the next step only if the first was successful
      ur.arm.movel(pickPose).await()
   } else if (moveState.errorOccurred) {
      println("Move failed: ${moveState.errors}")
   }
}
```

## ⏭️ Next Step
With the ability to move the robot arm, you are ready to interact with objects using tools.

[Go to Guide 4: OnRobot Gripper Control](/guides/primary-interface-gripper-guide.md)
— Learn how to integrate OnRobot grippers tools into your automation workflows.
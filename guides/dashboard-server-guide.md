# 📖 Guide 1: Dashboard Server 

### Focus: Controlling Polyscope, Power States, and Program Execution.


## Introduction
This guide covers how to manage the robot's operational state and program execution.

The **Dashboard Server (Port 29999)** is your remote control for PolyScope. 
Its primary purpose is to change the robot's operational state — powering the arm on, 
releasing brakes, and managing the execution of .urp programs.
While it can return information, its main focus in this library is High-Level State Control.


> **💡 Core Concept: Safety First**
> Every Dashboard command in this library returns a Kotlin `Result<String>`. This prevents your app from crashing on network errors and forces you to handle potential hardware failures. [Learn how to handle results below.](#4-handling-results--errors)






## 📡 Setup & Connection Handling (Automatic)

You **do not** need to call `connect()` for Dashboard commands. The library handles everything for you:
- It opens a short-lived connection to Port 29999.
- It sends your command (e.g., play).
- It closes the connection immediately after the robot responds.

Creating a UR Instance:
```kotlin
// Setup the robot instance
val ur = UR(
    host = "192.168.2.1",
    // additional optional configurations...
)
```





## 1. Power & Safety Management

You can transition the robot between different operational modes programmatically. 
This is essential for automated startups or remote recovery.

| Task | Method                                                                                                                                             | Typical Response                               |
| :--- |:---------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------|
| **Full Startup** | [powerOn()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/power-on.html)                            | `"..." "Powering on" "Brake releasing`         |
| **Power Off** | [powerOff()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/power-off.html)                          | `"Powering off"`                               |
| **Unlock Stop** | [unlockProtectiveStop()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/unlock-protective-stop.html) | `"Protective stop releasing"`                  |
| **Shutdown** | [shutdown()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/shutdown.html)                           | `"Shutting down"`                              |


One of the most powerful features of the Dashboard Server is bringing the robot from a "Cold Start"
to a fully operational state without touching the Teach Pendant.

The `powerOn()` function in this library is a high-level macro.
It automatically handles multiple steps for you:
- closing popups
- closing safety popups
- unlocking protective stops
- powering on the motors
- releasing the brakes

```kotlin
runBlocking {
    println("Initializing robot...")
    
    ur.powerOn()
        .onSuccess { println("Robot is now: $it") }
        .onFailure { error -> println("Failed to power on: ${error.message}") }
}
```






## 2. Quick Status Check

While the Primary Interface is better for continuous monitoring, the Dashboard Server is useful for one-time checks before starting a procedure.

| Task | Method                                                                                                                                               | Typical Response                                     |
| :--- |:-----------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------| 
| **RobotModel** | [fetchRobotModel()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-robot-model.html)             | `"UR3"` `"UR5"` etc.                                 |
| **SerialNumber** | [fetchSerialNumber()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-serial-number.html)         | `20194499999`                                        |
| **PolyscopeVersion** | [fetchPolyscopeVersion()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-polyscope-version.html) | `"URSoftware 5.12.6.1102099 (Sep 21 2023)"`                                               |
| **RobotMode** | [fetchRobotMode()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-robot-mode.html)               | `"POWER_ON"` `"RUNNING"` etc.                        |
| **LoadedProgram** | [fetchLoadedProgram()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-loaded-program.html)       | `"Loaded program: ../task.urp"`                      |
| **IsRunning** | [fetchIsRunning()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-is-running.html)              | `"Program running: true"` `"Program running: false"` |
| **ProgramState** | [fetchProgramState()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-program-state.html)         | `"STOPPED"` `"PLAYING"` `"PAUSED"`                   |
| **SafetyStatus** | [fetchSafetyStatus()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-safety-status.html)         | `"NORMAL"` `"PROTECTIVE_STOP"`  etc.                 |
```kotlin
runBlocking {
    val result = ur.fetchRobotMode().getOrElse { it.message }
    println(result)
    if (result == "RUNNING") {
        // do something
    }
}
```





## 3. Controlling PolyScope Programs (.urp)

You can manage and execute industrial programs stored on the robot controller. 
This allows your Kotlin application to act as a master PLC.

| Task                  | Method                                                                                                                                    | Typical Response                                 |
|:----------------------|:------------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------|
| **Load Program**      | [load()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/load.html)                          | `"Loading program: ../task.urp"`                 |
| **Load Installation** | [loadInstallation()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/load-installation.html) | `"Loading installation: ../default.installation` |
| **Start Execution**   | [play()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/play.html)                          | `"Starting program"`                             |
| **Pause**             | [pause()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/pause.html)                        | `"Pausing program"`                              |
| **Stop**              | [stop()](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/stop.html)                          | `"Stopped"`                                      |

```kotlin
runBlocking {
    // 1. Load: If null or fails, print and exit
    val loadRes = ur.load("test.urp").getOrNull()
    if (loadRes?.startsWith("Loading program") != true) {
        println("Load failed: $loadRes")
        return@runBlocking 
    }
    println("Success: Program loaded")

    // 2. Play: If null or fails, print
    val playRes = ur.play().getOrNull()
    if (playRes?.contains("Starting program") != true) {
        println("Play failed: $playRes")
        return@runBlocking 
    }
    println("Success: Robot is moving!")
}
```




## 4. Handling Results & Errors

As mentioned, every command returns a `Result<String>`. Here is how you can work with it:

### Chaining Success & Failure
The cleanest way for logging or updating a UI.
```kotlin
runBlocking {
    val result = ur.load("programs/test.urp")
    
    ur.play()
        .onSuccess { msg -> println("Robot started: $msg") }
        .onFailure { err -> println("Network error: ${err.message}") }
}
```

### Providing a Fallback
Use `getOrElse` or `getOrNull` to provide a default string if the command fails, preventing crashes.
```kotlin
runBlocking {
    // If the robot is unreachable, 'status' will be "Unknown"
    val status = ur.fetchRobotMode().getOrElse { "Unknown" }
    println("Current mode: $status")
}
```

### Forcing a Result
Use `getOrThrow()` if your logic cannot continue without this command succeeding. If an error occurs (e.g., Timeout), it will throw a standard Exception.

```kotlin
runBlocking {
    // This will throw an exception if the robot is unreachable
    val status = ur.fetchRobotMode().getOrThrow()
    println("Current mode: $status")
}
```


## ⏭️ Next Step
Now that you can control the robot's power and programs, it's time to look under the hood.

[Go to Guide 2: Real-Time Data & Monitoring](guides/primary-interface-data-guide.md)
 — Learn how to read live data and monitor the robot's "Digital Twin" through the Primary Interface.
# IN PROGRESS: 
# 📖 Guide 1: Remote Control & Program Management

Focus: Controlling Polyscope, Power States, and Program Execution.

## Introduction
The Dashboard Server is your remote control for PolyScope. 
Its primary purpose is to change the robot's operational state — powering the arm on, releasing brakes, and managing the execution of .urp programs. While it can return information, its main focus in this library is taking action.

## 📡 Connection Handling (Automatic)

You **do not** need to call `connect()` for Dashboard commands. The library handles everything for you:
- It opens a short-lived connection to Port 29999.
- It sends your command (e.g., play).
- It closes the connection immediately after the robot responds.

## 1. Quick Status Check

While the Primary Interface is better for continuous monitoring, the Dashboard Server is useful for one-time checks before starting a procedure.
Zurzeit laufen die DashBoard Server Befehle noch in einer eigenen Coroutine, in der Zukunft könnte sich das aber ändern. 
```kotlin
val ur = UR(
    host = "192.168.2.1",
    // additional configurations...
)
runBlocking {
    ur.fetchLoadedMode(
        onResponse = { resultDeferred.complete(it) },
        onFailure = { }
    )


}

```
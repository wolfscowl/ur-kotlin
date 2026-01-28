# Universal Robots API client for Kotlin

[![Release](https://jitpack.io/v/wolfscowl/ur-kotlin.svg)](https://jitpack.io/#wolfscowl/ur-kotlin)
[![Docs](https://img.shields.io/badge/docs-api-blueviolet?logo=kotlin&logoColor=white)](https://wolfscowl.github.io/ur-test/)

A lightweight Kotlin API client for Universal Robots (Primary Interface and Dashboard Server) with coroutines support.

## 📦 Setup

### Gradle (Kotlin DSL)

Install the Universal Robots API client by adding the JitPack repository and the dependency to your `build.gradle` file:

```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.wolfscowl:ur-kotlin:1.0.0")
}
```

<details> <summary><b>Gradle (Groovy)</b></summary>
If you are using Groovy, add this to your build.gradle:

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.wolfscowl:ur-kotlin:1.0.0'
}
```
</details>

### Maven

Install the Universal Robots API client by adding the JitPack repository and the dependency to your `pom.xml` file:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.wolfscowl</groupId>
    <artifactId>ur-kotlin</artifactId>
    <version>1.0.0</version>
</dependency>
```

## ⚡️ Getting Started

The API distinguishes between Dashboard Server commands and commands sent via the Primary Interface.
- Primary Interface: Requires a persistent connection to receive Real-Time data or execute movement commands.
- Dashboard Commands: A short-lived connection is automatically established for each command.


Create an instance of the Universal Robots client by providing the robot's IP address:

```kotlin
val ur = UR(
    host = "192.168.2.1",
    // additional configurations...
)
```

Connect to the Primary Interface to receive real-time data and execute movement commands. 

```kotlin
try {
    ur.connect()
} catch (e: Exception) {
    println("Host is unavailable")
}
```

## 📚 Guides
Explore our detailed tutorials to master robot automation with Kotlin:

📖 [Remote Control & Program Management:](guides/remote-control-guide.md)
Learn how to interact with Polyscope, load installations, and automate program execution via the Dashboard Server.

🛠️ **Creating Motion Procedures & Workflows:** (In Progress) A deep dive into the Primary Interface. Learn how to script complex movement sequences and handle real-time data feedback.

🛠️ **Operating OnRobot End-Effectors:** (In Progress) Everything you need to know about integrating and controlling TFG, RG, and VG grippers within your procedures.




## 📡 Primary Interface

The Primary Interface allows you to send URScript commands to the controller and receive Real-Time Data (at 10Hz). 
The commands are divided into standard Universal Robots base commands and specialized implementations for OnRobot end-effectors.

### Base Commands

Standard motion and configuration commands for the robot arm:

- [movej](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/movej.html)
- [movel](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/movel.html)
- [movec](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/movec.html)
- [runURScript](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-interface/run-u-r-script.html)
- [setTcpOffset](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/set-tcp-offset.html)
- [setTargetPayload](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/set-target-payload.html)
- [enterFreeDriveMode](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/enter-free-drive-mode.html)
- [exitFreeDriveMode](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.arm/-u-r-arm/exit-free-drive-mode.html)

### End-Effector Commands

The following grippers are currently supported:

<table>
  <thead>
    <tr>
      <th align="left">OnRobot TFG</th>
      <th align="left">OnRobot RG</th>
      <th align="left">OnRobot VG</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td valign="top">
        <a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-t-f-g/grip-ext.html">gripExt</a><br>
        <a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-t-f-g/grip-int.html">gripInt</a><br>
        <a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-t-f-g/release-ext.html">releaseExt</a><br>
        <a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-t-f-g/release-int.html">releaseInt</a>
      </td>
      <td valign="top">
        <a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-r-g/grip.html">grip</a><br>
        <a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-r-g/release.html">release</a>
      </td>
      <td valign="top">
        <a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-v-g/grip.html">grip</a><br>
        <a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-v-g/release.html">release</a><br>
        <a href="https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.interfaces.tool/-on-robot-v-g/seek-grip.html">seekGrip</a>
      </td>
    </tr>
  </tbody>
</table>

### Real-Time Data

The client continuously updates robot state variables and provides them as both standard properties and Kotlin Coroutine Flows.
For a comprehensive list of available data fields (e.g., Joint Positions, TCP Poses, Currents), please refer to the [Universal Robots Primary Interface Documentation](https://github.com/wolfscowl/ur-kotlin/blob/main/guides/pdf/ur/ClientInterfaces_Primary_Secondary.pdf).


## 🖥️ Dashboard Server

The following commands are currently supported via the Dashboard Server. 
For a detailed description of each function, please refer to the [API Documentation](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/index.html) 
and [Dashboard Server Documentation](https://github.com/wolfscowl/ur-kotlin/blob/main/guides/pdf/ur/DashboardServer_e-Series_2022.pdf) from Universal Robots.

- [powerOn](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/power-on.html)
- [powerOff](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/power-off.html)
- [shutdown](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/shutdown.html)
- [loadInstallation](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/load-installation.html)
- [load](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/load.html)
- [play](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/play.html)
- [stop](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/stop.html)
- [pause](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/pause.html)
- [unlockProtectiveStop](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/unlock-protective-stop.html)
- [fetchRobotModel](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-robot-model.html)
- [fetchRobotMode](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-robot-mode.html)
- [fetchSafetyStatus](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-safety-status.html)
- [fetchSerialNumber](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-serial-number.html)
- [fetchPolyscopeVersion](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-polyscope-version.html)
- [fetchIsRunning](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-is-running.html)
- [fetchProgramState](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-program-state.html)
- [fetchLoadedProgram](https://wolfscowl.github.io/ur-kotlin/ur-kotlin/com.wolfscowl.ur_client.core/-u-r-dash-board/fetch-loaded-program.html)
    


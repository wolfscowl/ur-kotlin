# IN PROGRESS:
📖 Guide 2: Creating Motion Procedures & Workflows

Focus: Primary Interface, Real-Time Data, and Custom Scripting.
Introduction

While the Dashboard Server handles the robot's "management," the Primary Interface (Port 30001) is where the actual automation happens. It allows you to send precise movement commands and receive Real-Time Data at 10Hz. In this library, we use these capabilities to create complex Procedures—sequences of actions that the robot executes autonomously.
📡 Connection Handling (Persistent)

Unlike the Dashboard, the Primary Interface requires a persistent connection. You must call connect() once at the start of your workflow.
Kotlin

val ur = UR(host = "192.168.1.100")

runBlocking {
ur.connect()
ur.isConnectedFlow.first { it } // Ensure we are online

    // Your procedure starts here...
}

1. Basic Movement Commands

The library provides high-level wrappers for standard URScript movement commands. Each command is suspendable, meaning you can wait for the robot to finish its move before starting the next one.

    movej (Joint Motion): Best for moving between points quickly without worrying about the tool's path.

    movel (Linear Motion): Moves the Tool Center Point (TCP) in a straight line.

    movec (Circular Motion): Moves in a circular arc.

Kotlin

// Example: A simple Pick & Place movement
ur.arm.movej(q = targetPosition, a = 1.2, v = 0.9).await()
println("Reached target position")

2. Monitoring Real-Time Data

The Primary Interface streams data constantly. You can access this via standard variables or Kotlin Flows for reactive programming. This is perfect for "Wait-until" logic.
Kotlin

// Wait until the TCP reaches a certain height
ur.robotStateFlow.first { state ->
state?.cartesianInfo?.z ?: 0.0 > 0.5
}
println("Robot is now high enough!")

3. Running Custom URScript Blocks

Sometimes you need to execute a complex logic block directly on the robot controller. You can send raw URScript strings using runURScript.
Kotlin

ur.runURScript("""
def custom_procedure():
while (i < 3):
movej([-1.57, -1.57, 0.00, -3.14, -1.57, -1.57])
i = i + 1
end
end
custom_procedure()
""".trimIndent()).await()

4. FreeDrive Mode

For manual teaching or maintenance, you can programmatically enter and exit FreeDrive mode:
Kotlin

ur.arm.enterFreeDriveMode()
delay(5000) // Robot is "weightless" for 5 seconds
ur.arm.exitFreeDriveMode().await()

Summary

The Primary Interface is for execution. Use it to:

    Move the robot arm with precision.

    React to real-time sensor data and poses.

    Extend functionality with custom URScript.

Next Step: Ready to add a gripper to the mix? Check out Guide 3: Operating OnRobot End-Effectors.
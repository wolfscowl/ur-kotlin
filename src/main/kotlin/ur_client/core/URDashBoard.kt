package com.wolfscowl.ur_client.core

interface URDashBoard {



    /**
     * Loads a specified installation file in Polyscope via the dashboard server.
     * This command suspends until the load has completed or failed.
     * The command fails if the associated installation requires confirmation of safety.
     *
     * **IMPORTANT:**
     * After the installation has been loaded successfully, the robot transitions to the
     * operational state POWER OFF.
     *
     * @param installation The name or path of the installation file.
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages may include:
     * - `"Loading installation: <default.installation>"`
     * - `"File not found: <default.installation>"`
     * - `"Failed to load installation: <default.installation>"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpInstallation
     */
    suspend fun loadInstallation(installation: String): Result<String>


    /**
     * Loads a program (*.urp) in Polyscope via the dashboard server.
     *
     * @param program The name or path of the program file.
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages may include:
     * - `"Loading program: <program.urp>"`
     * - `"File not found: <program.urp>"`
     * - `"Error while loading program: <program.urp>"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpLoad
     */
    suspend fun load(program: String): Result<String>


    /**
     * Runs the current program (*.urp) in Polyscope via the dashboard server.
     *
     * **IMPORTANT:**
     * Does not work with stopped or paused URScripts.
     *
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages may include:
     * - `"Starting program"`
     * - `"Failed to execute: play"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpPlay
     */
    suspend fun play(): Result<String>


    /**
     * Stops the current running program via the dashboard server (URScript or Polyscope *.urp file).
     *
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages may include:
     * - `"Stopped"`
     * - `"Failed to execute: stop"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpStop
     */
    suspend fun stop(): Result<String>


    /**
     * Pauses the current running program via the dashboard server.
     *
     * **IMPORTANT:**
     * Not recommend for URScripts.
     * If a URscript is in pause mode, it should be stopped with [stop] before executing the next URscript.
     * If this is not done, the next URScript will be terminated immediately.
     *
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages may include:
     * - `"Pausing program"`
     * - `"Failed to execute: pause"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpPause
     */
    suspend fun pause(): Result<String>


    /**
     * Queries the execution state of the loaded program (*.urp) or URScript via the dashboard server.
     *
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages may include:
     * - `"Program running: true"`
     * - `"Program running: false"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpIsRunning
     */
    suspend fun fetchIsRunning(): Result<String>


    /**
     * Queries the state of the loaded program (*.urp) in polyscope via the dashboard server.
     *
     * **IMPORTANT:**
     * Does not consider running URScripts!
     *
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages may include:
     * - `"STOPPED"`
     * - `"PLAYING"`
     * - `"PAUSED"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpProgramState
     */
    suspend fun fetchProgramState(): Result<String>


    /**
     * Queries the path to loaded program file via the Dashboard Server.
     *
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages may include:
     * - `"Loaded program: <path to loaded program file>"`
     * - `"No program loaded"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpLoaded
     */
    suspend fun fetchLoadedProgram(): Result<String>


    /**
     * Powers on the robot arm and releases the brakes to reach "normal mode" via the Dashboard Server.
     *
     * This function sends a series of commands to the UR robot via the dashboard server to fully
     * power it on and make it ready for operation. The following steps are executed in order:
     *
     * 1. Close any open popup dialogs.
     * 2. Close any active safety popups.
     * 3. Unlock a protective stop if one is active.
     * 4. Power on the robot.
     * 5. Release the brakes.
     *
     * @return [Result] containing combined response strings on success, or an [Exception] on failure.
     * Response messages include:
     * - `"Powering on"`
     * - `"closing safety popup"`
     * - `"Protective stop releasing"`
     * - `"Powering on"`
     * - `"Brake releasing"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.powering
     */
    suspend fun powerOn(): Result<String>


    /**
     * Powers off the robot arm via the dashboard server.
     *
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages include:
     * - `"Powering off"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.powering
     */
    suspend fun powerOff(): Result<String>


    /**
     * Closes the current popup (Polyscope) and unlocks protective stop via the dashboard server.
     * The unlock protective stop command fails if less than 5 seconds has passed since
     * the protective stop occurred.
     *
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages may include:
     * - `"Protective stop releasing"`
     * - `"Cannot unlock protective stop ..."`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.unlockProtectiveStop
     */
    suspend fun unlockProtectiveStop(): Result<String>


    /**
     * Queries the model of the robot via the dashboard server.
     *
     * @return [Result] containing the robot model (e.g. `"UR3"`, `"UR5"`) on success, or an [Exception] on failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.robotModel
     */
    suspend fun fetchRobotModel(): Result<String>


    /**
     * Queries the safety status via the dashboard server.
     *
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages may include:
     * `"Safetystatus: <status>"`, where status is
     *  - `"NORMAL"`
     *  - `"REDUCED"`
     *  - `"PROTECTIVE_STOP"`
     *  - `"RECOVERY"`
     *  - `"SAFEGUARD_STOP"`
     *  - `"SYSTEM_EMERGENCY_STOP"`
     *  - `"ROBOT_EMERGENCY_STOP"`
     *  - `"VIOLATION"`
     *  - `"FAULT"`
     *  - `"AUTOMATIC_MODE_SAFEGUARD_STOP"`
     *  - `"SYSTEM_THREE_POSITION_ENABLING_STOP"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.safetyStatus
     */
    suspend fun fetchSafetyStatus(): Result<String>


    /**
     * Queries the serial number of the robot.
     *
     * @return [Result] containing the serial number on success, or an [Exception] on failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.serialNumber
     */
    suspend fun fetchSerialNumber(): Result<String>


    /**
     * Queries the Polyscope version via the dashboard server.
     *
     * @return [Result] containing the version string (e.g.`"URSoftware 5.12.6.1102099 (Sep 21 2023)") on success, or an [Exception] on failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.polyscopeVersion
     */
    suspend fun fetchPolyscopeVersion(): Result<String>


    /**
     * Queries the robot mode via the dashboard server.
     *
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages may include:
     * `"Robotmode: <mode>"`, where mode is
     * - `"NO_CONTROLLER"`
     * - `"DISCONNECTED,"`
     * - `"CONFIRM_SAFETY"`
     * - `"BOOTING"`
     * - `"POWER_OFF"`
     * - `"POWER_ON"`
     * - `"IDLE"`
     * - `"BACKDRIVE"`
     * - `"RUNNING"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.robotMode
     */
    suspend fun fetchRobotMode(): Result<String>


    /**
     * Shuts down and turns off robot and controller via the dashboard server.
     *
     * @return [Result] containing the response string on success, or an [Exception] on failure.
     * Response messages include:
     * - `"Shutting down"`
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.shutdown
     */
    suspend fun shutdown(): Result<String>
}
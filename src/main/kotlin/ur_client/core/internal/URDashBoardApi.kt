package com.wolfscowl.ur_client.core.internal

import com.wolfscowl.ur_client.core.URDashBoard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket


internal class URDashBoardApi(
    private val host: String,
    private val dashBoardPort: Int,
    private val connectTimeout: Int,
    private val soTimeout: Int
) : URDashBoard {

    // --- Program Control Commands ---

    override suspend fun loadInstallation(installation: String): Result<String> =
        runCatching { sendDashBoardCmd("load installation $installation") }

    override suspend fun load(program: String): Result<String> =
        runCatching { sendDashBoardCmd("load $program") }

    override suspend fun play(): Result<String> =
        runCatching { sendDashBoardCmd("play") }

    override suspend fun stop(): Result<String> =
        runCatching { sendDashBoardCmd("stop") }

    override suspend fun pause(): Result<String> =
        runCatching { sendDashBoardCmd("pause") }

    // --- State Fetching Commands ---

    override suspend fun fetchIsRunning(): Result<String> =
        runCatching { sendDashBoardCmd("running") }

    override suspend fun fetchProgramState(): Result<String> =
        runCatching { sendDashBoardCmd("programState") }

    override suspend fun fetchLoadedProgram(): Result<String> =
        runCatching { sendDashBoardCmd("get loaded program") }

    // --- Safety & Power Commands ---

    override suspend fun powerOn(): Result<String> = runCatching {
        sendDashBoardCmd(listOf(
            "close popup",
            "close safety popup",
            "unlock protective stop",
            "power on",
            "brake release"
        ))
    }

    override suspend fun powerOff(): Result<String> =
        runCatching { sendDashBoardCmd("power off") }

    override suspend fun unlockProtectiveStop(): Result<String> = runCatching {
        sendDashBoardCmd(listOf("unlock protective stop", "stop"))
    }

    // --- Info Commands ---

    override suspend fun fetchRobotModel(): Result<String> =
        runCatching { sendDashBoardCmd("get robot model") }

    override suspend fun fetchSerialNumber(): Result<String> =
        runCatching { sendDashBoardCmd("get serial number") }

    override suspend fun fetchSafetyStatus(): Result<String> =
        runCatching { sendDashBoardCmd("safetystatus") }

    override suspend fun fetchPolyscopeVersion(): Result<String> =
        runCatching { sendDashBoardCmd("PolyscopeVersion") }

    override suspend fun fetchRobotMode(): Result<String> =
        runCatching { sendDashBoardCmd("robotmode") }

    override suspend fun shutdown(): Result<String> =
        runCatching { sendDashBoardCmd("shutdown") }

    // --- Core Network Logic (Private) ---

    private suspend fun sendDashBoardCmd(cmd: String): String = withContext(Dispatchers.IO) {
        var socket: Socket? = null
        try {
            socket = Socket().apply {
                soTimeout = this@URDashBoardApi.soTimeout
                connect(InetSocketAddress(host, dashBoardPort), connectTimeout)
            }
            val input = BufferedReader(InputStreamReader(socket.inputStream))
            val output = PrintWriter(socket.outputStream, true)

            input.readLine() // Capture "Connected: Universal Robots Dashboard Server"
            output.println(cmd)

            return@withContext input.readLine() ?: throw Exception("Empty response from robot")
        } finally {
            socket?.close()
        }
    }

    private suspend fun sendDashBoardCmd(cmds: List<String>): String = withContext(Dispatchers.IO) {
        var socket: Socket? = null
        val responseBuilder = StringBuilder()
        try {
            socket = Socket().apply {
                soTimeout = this@URDashBoardApi.soTimeout
                connect(InetSocketAddress(host, dashBoardPort), connectTimeout)
            }
            val input = BufferedReader(InputStreamReader(socket.inputStream))
            val output = PrintWriter(socket.outputStream, true)

            input.readLine() // Welcome message
            cmds.forEach { cmd ->
                output.println(cmd)
                val resp = input.readLine() ?: "No response for command: $cmd"
                responseBuilder.append(resp).append("\n")
            }
            return@withContext responseBuilder.toString().trimEnd()
        } finally {
            socket?.close()
        }
    }
}

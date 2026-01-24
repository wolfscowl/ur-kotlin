package com.wolfscowl.ur_client.interfaces.state

interface ToolState: URScriptState {
    val toolDetected: Boolean

    override fun copy() : ToolState
}

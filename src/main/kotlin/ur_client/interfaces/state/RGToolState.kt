package com.wolfscowl.ur_client.interfaces.state

interface RGToolState: ToolState {
    val width: Float
    val depth: Float
    val gripDetected: Boolean

    override fun copy() : RGToolState
}
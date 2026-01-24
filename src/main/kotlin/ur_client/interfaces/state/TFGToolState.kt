package com.wolfscowl.ur_client.interfaces.state

interface TFGToolState: ToolState {
    val extWidth: Float
    val intWidth: Float
    val gripDetected: Boolean

    override fun copy() : TFGToolState
}
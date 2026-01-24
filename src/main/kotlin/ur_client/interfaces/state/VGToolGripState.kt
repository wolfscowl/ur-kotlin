package com.wolfscowl.ur_client.interfaces.state

interface VGToolGripState: VGToolState {
    val vacuumReached: Boolean

    override fun copy() : VGToolGripState
}
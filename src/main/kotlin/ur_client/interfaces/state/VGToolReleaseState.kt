package com.wolfscowl.ur_client.interfaces.state

interface VGToolReleaseState: VGToolState {
    val vacuumReleased: Boolean

    override fun copy() : VGToolReleaseState
}
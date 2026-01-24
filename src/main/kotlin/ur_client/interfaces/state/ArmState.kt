package com.wolfscowl.ur_client.interfaces.state

import com.wolfscowl.ur_client.model.element.Inertia
import com.wolfscowl.ur_client.model.element.JointPosition
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.model.element.Vec3

interface ArmState: URScriptState {
    val jointPosition: JointPosition
    val tcpPose: Pose
    val tcpOffset: Pose
    val payload: Float
    val payloadCog: Vec3
    val payloadInertia: Inertia

    override fun copy() : ArmState

    /**
     * Returns a string representation of the arm state using the requested units.
     *
     * This includes joint positions, TCP (tool center point) pose, TCP offset, payload,
     * payload center of gravity, and payload inertia. Various units and formatting options
     * can be controlled using the parameters.
     *
     * @param jointsInDegree If true, joint positions are displayed in degrees instead of radiant (default: false).
     * @param tcpPoseInDegree If true, TCP pose rotations are displayed in degrees instead of radiant (default: false).
     * @param tcpPoseInMillimeter If true, TCP pose translations are displayed in millimeters instead of meters (default: false).
     * @param tcpOffsetInDegree If true, TCP offset rotations are displayed in degrees instead of radiant (default: false).
     * @param tcpOffsetInMillimeter If true, TCP offset translations are displayed in millimeters instead of meters (default: false).
     * @param roundDecimals If true, numeric values are rounded to a fixed number of decimal places (default: false).
     *
     * @return A formatted string representing the current state of the arm with the requested units and rounding.
     */
    fun toFormattedString(
        jointsInDegree: Boolean = false,
        tcpPoseInDegree: Boolean = false,
        tcpPoseInMillimeter: Boolean = false,
        tcpOffsetInDegree: Boolean = false,
        tcpOffsetInMillimeter: Boolean = false,
        roundDecimals: Boolean = false,
    ): String
}
package com.wolfscowl.ur_client.model.robot_state


import com.wolfscowl.ur_client.core.internal.util.Util.formatString

// ROBOT_STATE_PACKAGE_TYPE_CONFIGURATION_DATA = 6
data class ConfigurationData (
    // ToDo make extra values (base,shoulder,elbow, wrist1, wrist2, wrist3) for ConfigurationData
    val jointConfigurationData: List<JointConfigurationData>,
    val vJointDefault: Double,      // double
    val aJointDefault: Double,      // double
    val vToolDefault: Double,       // double
    val aToolDefault: Double,       // double
    val eqRadius: Double,           // double
    val masterboardVersion: Int,    // int32_t
    val controllerBoxType: Int,     // int32_t
    val robotType: Int,             // int32_t
    val robotSubType: Int,          // int32_t
): RobotState {
    override fun toString(): String {
        return buildString {
            append("-=ConfigurationData=-\n")
            append("  vJointDefault = $vJointDefault\n")
            append("  aJointDefault = $aJointDefault\n")
            append("  vToolDefault = $vToolDefault\n")
            append("  aToolDefault = $aToolDefault\n")
            append("  eqRadius = $eqRadius\n")
            append("  jointConfigurationData = ")
            jointConfigurationData.forEach { jointConfig ->
                append("\n")
                append(jointConfig.toString().formatString(false, 4))
            }
        }
    }
    data class JointConfigurationData(
        val jointMinLimit: Double,          // double
        val jointMaxLimit: Double,         // double
        val jointMaxSpeed: Double,          // double
        val jointMaxAcceleration: Double,   // double
        val dhA: Double,                    // double
        val dhD: Double,                    // double
        val dhAlpha: Double,                // double
        val dhTheta: Double                 // double
    ) {
        override fun toString(): String {
            return buildString {
                append("-=JointConfigurationData=-\n")
                append("  jointMinLimit = $jointMinLimit\n")
                append("  jointMaxLimit = $jointMaxLimit\n")
                append("  jointMaxSpeed = $jointMaxSpeed\n")
                append("  jointMaxAcceleration = $jointMaxAcceleration\n")
                append("  dhA = $dhA\n")
                append("  dhD = $dhD\n")
                append("  dhAlpha = $dhAlpha\n")
                append("  dhTheta = $dhTheta")
            }
        }
    }

}


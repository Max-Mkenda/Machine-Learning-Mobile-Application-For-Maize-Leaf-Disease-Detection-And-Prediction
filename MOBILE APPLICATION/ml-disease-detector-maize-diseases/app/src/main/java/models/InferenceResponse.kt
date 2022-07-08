package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InferenceResponse(
    @SerialName("confidence")
    val confidence: Float,

    @SerialName("results")
    val results: Int
)

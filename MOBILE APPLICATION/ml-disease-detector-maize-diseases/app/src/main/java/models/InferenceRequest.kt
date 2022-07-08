package models

import kotlinx.serialization.Serializable

@Serializable
data class InferenceRequest(
    val application: String,
    val image: String
)

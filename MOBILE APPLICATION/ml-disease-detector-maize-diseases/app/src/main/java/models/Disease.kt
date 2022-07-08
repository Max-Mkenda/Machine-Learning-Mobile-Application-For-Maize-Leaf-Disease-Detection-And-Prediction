package models

import java.io.Serializable

data class Disease(
    val disease: String,
    val chemicalControl: String,
    val pathogen: String,
    val trigger: String,
    val symptoms: String,
    val image: String,
    val organicControl: String,
    val hosts: String,
    val measures: String
): Serializable

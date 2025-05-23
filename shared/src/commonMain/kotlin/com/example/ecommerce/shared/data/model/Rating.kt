package com.example.ecommerce.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    val rate: Double,
    val count: Int
)

package com.example.test.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val name: String
)

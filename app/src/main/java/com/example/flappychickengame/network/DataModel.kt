package com.example.flappychickengame.network

import com.google.gson.annotations.SerializedName

data class ScoreResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("score") val score: Int,
    @SerializedName("timestamp") val timestamp: String
)

data class LeaderboardEntry(
    val id: Int,
    val username: String,
    val score: Int,
    val timestamp: String // Or you can use a Date object or another appropriate data type
)

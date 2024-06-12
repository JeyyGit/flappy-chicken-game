package com.example.flappychickengame.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("/leaderboard")
    suspend fun addScore(
        @Query("username") username: String,
        @Query("score") score: Int
    ): Response<ScoreResponse>

    @GET("/leaderboard/top")
    suspend fun getTopLeaderboard(
        @Query("limit") limit: Int = 10
    ): List<ScoreResponse>
}

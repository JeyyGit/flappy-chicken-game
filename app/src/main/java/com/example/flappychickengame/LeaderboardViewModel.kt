package com.example.flappychickengame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flappychickengame.network.LeaderboardEntry
import com.example.flappychickengame.network.NetworkModule
import retrofit2.HttpException

class LeaderboardRepository {
    private val apiService = NetworkModule.apiService

    private val _leaderboardEntries = MutableLiveData<List<LeaderboardEntry>>()
    val leaderboardEntries: LiveData<List<LeaderboardEntry>> = _leaderboardEntries

    suspend fun fetchLeaderboardData() {
        try {
            val leaderboardResponse = apiService.getTopLeaderboard()
            val leaderboardEntries = leaderboardResponse.map { scoreResponse ->
                LeaderboardEntry(
                    id = scoreResponse.id,
                    username = scoreResponse.username,
                    score = scoreResponse.score,
                    timestamp = scoreResponse.timestamp
                )
            }
            _leaderboardEntries.postValue(leaderboardEntries)
        } catch (e: HttpException) {
            // error
        } catch (e: Throwable) {
            // error
        }
    }
}


class LeaderboardViewModel : ViewModel() {

    private val leaderboardRepository = LeaderboardRepository()

    val leaderboardEntries = leaderboardRepository.leaderboardEntries

    init {
        fetchLeaderboardData()
    }

    fun fetchLeaderboardData() {
        viewModelScope.launch {
            leaderboardRepository.fetchLeaderboardData()
        }
    }
}

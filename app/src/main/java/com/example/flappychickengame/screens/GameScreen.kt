package com.example.flappychickengame.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.flappychickengame.FlappyChickenView
import com.example.flappychickengame.network.NetworkModule
import com.example.flappychickengame.ui.theme.FlappyChickenGameTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GameScreen(navController: NavHostController) {
    var gameOver by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var playerName by remember { mutableStateOf("") }

    val context = LocalContext.current
    val chickenView = remember { FlappyChickenView(context, null) }

    if (gameOver) {
        AlertDialog(
            onDismissRequest = { /* Dismiss the dialog */ },
            title = { Text(text = "Game Over",
                style = TextStyle(shadow = Shadow(blurRadius = 5f)),
                fontFamily = undertaleFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.White,) },
            text = {
                Column {
                    Text(
                        text = "Your score: $score",
                        style = TextStyle(shadow = Shadow(blurRadius = 5f)),
                        fontFamily = undertaleFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = playerName,
                        onValueChange = { playerName = it },
                        textStyle = TextStyle(
                            fontFamily = undertaleFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                        ),
                        label = {
                            Text(
                                "Enter your name",
                                style = TextStyle(shadow = Shadow(blurRadius = 5f)),
                                fontFamily = undertaleFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = Color.White,
                            )
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (playerName.isNotBlank()) {
                            postScoreToLeaderboard(playerName, score)
                            gameOver = false
                            chickenView.resetGame()
                        }
                    },
                    enabled = playerName.isNotBlank()
                ) {
                    Text(
                        "Play Again",
                        style = TextStyle(shadow = Shadow(blurRadius = 5f)),
                        fontFamily = undertaleFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        if (playerName.isNotBlank()) {
                            postScoreToLeaderboard(playerName, score)
                            navController.navigate("menu")
                        }
                    },
                    enabled = playerName.isNotBlank()
                ) {
                    Text(
                        "Back to Menu",
                        style = TextStyle(shadow = Shadow(blurRadius = 5f)),
                        fontFamily = undertaleFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                    )
                }
            }
        )
    }

    FlappyChickenGameTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            it
            AndroidView(
                factory = {
                    FlappyChickenView(context, null).apply {
                        setGameOverCallback(object : FlappyChickenView.GameOverCallback {
                            override fun onGameOver(finalScore: Int) {
                                gameOver = true
                                score = finalScore
                            }
                        })
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun postScoreToLeaderboard(username: String, score: Int) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            NetworkModule.apiService.addScore(username, score)
        } catch (e: Exception) {
            // network error
            Log.e("GAME SCREEN", "Network Error")
        }
    }
}
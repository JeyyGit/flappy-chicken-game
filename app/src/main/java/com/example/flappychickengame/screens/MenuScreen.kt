package com.example.flappychickengame.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.flappychickengame.BuildConfig
import com.example.flappychickengame.LeaderboardViewModel
import com.example.flappychickengame.R

@Composable
fun MenuScreen(navController: NavHostController) {
    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.background_sprite_2),
            contentDescription = "background_image",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate("game") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(16.dp)
            ) {
                Text(
                    "Play Game",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 24.sp, shadow = Shadow(blurRadius = 5f)),
                    fontFamily = undertaleFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                )
            }
            Button(
                onClick = { navController.navigate("leaderboard") },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(16.dp)
            ) {
                Text(
                    "Leaderboard",
                    textAlign = TextAlign.Center,
                    style = TextStyle(shadow = Shadow(blurRadius = 5f)),
                    fontFamily = undertaleFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                )
            }
        }
    }
}


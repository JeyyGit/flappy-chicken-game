package com.example.flappychickengame.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flappychickengame.LeaderboardViewModel
import com.example.flappychickengame.R
import com.example.flappychickengame.network.LeaderboardEntry
import com.example.flappychickengame.ui.theme.Typography

val undertaleFontFamily = FontFamily(
    Font(R.font.undertale, FontWeight.Normal)
)

@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel,
    navController: NavController
) {
    val leaderboardEntries by viewModel.leaderboardEntries.observeAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchLeaderboardData()
    }

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
                .background(Color.Transparent)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Global Leaderboard",
                modifier = Modifier.padding(bottom = 16.dp),
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, shadow = Shadow(blurRadius = 5f)),
                fontFamily = undertaleFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.White,
            )

            if (leaderboardEntries != null) {
                LazyColumn {
                    items(leaderboardEntries!!) { entry ->
                        LeaderboardEntryItem(entry = entry)
                        Divider(color = Color.Gray, thickness = 1.dp)
                    }
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        //                    .(48.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Back to Menu",
                    style = TextStyle(fontSize = 18.sp, shadow = Shadow(blurRadius = 5f)),
                    fontFamily = undertaleFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun LeaderboardEntryItem(entry: LeaderboardEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = entry.username,
            modifier = Modifier.weight(1f),
            style = TextStyle(fontSize = 18.sp, shadow = Shadow(blurRadius = 5f)),
            fontFamily = undertaleFontFamily,
            fontWeight = FontWeight.Normal,
            color = Color.White,
        )
        Text(
            text = entry.score.toString(),
            style = TextStyle(fontSize = 18.sp, shadow = Shadow(blurRadius = 5f)),
            fontFamily = undertaleFontFamily,
            fontWeight = FontWeight.Normal,
            color = Color.White,
        )
    }
}
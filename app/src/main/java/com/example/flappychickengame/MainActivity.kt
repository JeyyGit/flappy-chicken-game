package com.example.flappychickengame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.flappychickengame.ui.theme.FlappyChickenGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlappyChickenGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameContent(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun FlappyChickenGame(modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            FlappyChickenView(context, null)
        },
        modifier = modifier
    )
}

@Composable
fun GameContent(modifier: Modifier = Modifier) {
    FlappyChickenGame(modifier = modifier.fillMaxSize())
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlappyChickenGameTheme {
        GameContent()
    }
}
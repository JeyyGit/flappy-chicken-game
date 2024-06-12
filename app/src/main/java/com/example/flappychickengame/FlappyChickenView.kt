package com.example.flappychickengame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.Random

class FlappyChickenView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var chickenY = 300f
    private var velocity = 0f
    private var gravity = 0.5f
    private val chickenSize = 50f
    private val chickenPaint = Paint().apply {
        color = Color.RED
    }

    private val obstacles = mutableListOf<Obstacle>()
    private val obstacleWidth = 100f
    private val obstacleGap = 700f
    private val obstaclePaint = Paint().apply {
        color = Color.GREEN
    }
    private val obstacleSpeed = 5f
    private val obstacleSpawnFrequency = 2000L
    private var lastSpawnTime = 0L

    private var score = 0
    private val scorePaint = Paint().apply {
        color = Color.WHITE
        textSize = 80f
    }

    init {
        startGameLoop()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawChicken(canvas)
        drawObstacles(canvas)
        drawScore(canvas)
        updateChicken()
        updateObstacles()
        invalidate()
    }

    private fun drawScore(canvas: Canvas) {
        canvas.drawText("Score: $score", 50f, 100f, scorePaint)
    }

    private fun drawChicken(canvas: Canvas) {
        canvas.drawCircle(100f, chickenY, chickenSize, chickenPaint)
    }

    private fun drawObstacles(canvas: Canvas) {
        for (obstacle in obstacles) {
            canvas.drawRect(obstacle.left, 0f, obstacle.right, obstacle.top, obstaclePaint)
            canvas.drawRect(obstacle.left, obstacle.bottom, obstacle.right, height.toFloat(), obstaclePaint)
        }
    }

    private fun updateChicken() {
        velocity += gravity
        chickenY += velocity

        if (chickenY > height) {
            resetGame()
        }

        if (chickenY < 0f) {
            chickenY = 0f
        }

        for (obstacle in obstacles) {
            if (isCollision(chickenY, 100f, obstacle)) {
                resetGame()
                return
            }
        }
    }

    private fun updateObstacles() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastSpawnTime >= obstacleSpawnFrequency) {
            spawnObstacle()
            lastSpawnTime = currentTime
        }

        val iterator = obstacles.iterator()
        while (iterator.hasNext()) {
            val obstacle = iterator.next()
            obstacle.move(obstacleSpeed)
            if (obstacle.right < 0) {
                iterator.remove()
            }
        }

        for (obstacle in obstacles) {
            if (isCollision(chickenY, 100f, obstacle)) {
                resetGame()
                return
            } else if (!obstacle.passed && obstacle.right < 100f && obstacle.right > 0) {
                obstacle.passed = true
                score++
            }
        }
    }

    private fun spawnObstacle() {
        val random = Random()
        val gapTop = random.nextInt(height - obstacleGap.toInt())
        obstacles.add(Obstacle(width.toFloat(), gapTop.toFloat(), obstacleWidth, obstacleGap))
    }

    private fun startGameLoop() {
        postDelayed({
            invalidate()
        }, 16)
    }

    private fun flap() {
        velocity = -20f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            flap()
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun isCollision(chickenY: Float, chickenX: Float, obstacle: Obstacle): Boolean {
        return (chickenY - chickenSize / 2 < obstacle.top || chickenY + chickenSize / 2 > obstacle.bottom) &&
                (chickenX + chickenSize / 2 > obstacle.left && chickenX - chickenSize / 2 < obstacle.right)
    }

    private fun resetGame() {
        chickenY = height / 2f
        velocity = 0f
        obstacles.clear()
        lastSpawnTime = System.currentTimeMillis()
        score = 0
    }
}



data class Obstacle(
    var left: Float,
    var top: Float,
    val width: Float,
    val gap: Float
) {
    val right: Float
        get() = left + width
    val bottom: Float
        get() = top + gap

    fun move(speed: Float) {
        left -= speed
    }

    var passed = false
}
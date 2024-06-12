package com.example.flappychickengame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.Random

class FlappyChickenView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    interface GameOverCallback {
        fun onGameOver(score: Int)
    }

    private var gameOverCallback: GameOverCallback? = null

    fun setGameOverCallback(callback: GameOverCallback) {
        gameOverCallback = callback
    }

    private var backgroundBitmap: Bitmap =
        BitmapFactory.decodeResource(resources, R.drawable.background_sprite_2)
    private var scaledBackgroundBitmap = Bitmap.createScaledBitmap(
        backgroundBitmap,
        backgroundBitmap.width, backgroundBitmap.height, false
    )

    private var backgroundX1 = 0f
    private var backgroundX2: Float = scaledBackgroundBitmap.width.toFloat()
    private val backgroundSpeed = 5f

    private var chickenY = 300f
    private var velocity = 0f
    private var gravity = 1f
    private val chickenSize = 50f
    private val chickenBitmap: Bitmap =
        BitmapFactory.decodeResource(resources, R.drawable.chicken_sprite)
    private var scaledChickenBitmap = Bitmap.createScaledBitmap(
        chickenBitmap,
        300, 300, false
    )
    private val chickenPaint = Paint().apply {
        color = Color.RED
    }

    private val obstacles = mutableListOf<Obstacle>()
    private val obstacleWidth = 200f
    private val obstacleGap = 700f
    private val obstaclePaint = Paint().apply {
        color = Color.GREEN
    }
    private val obstacleSpeed = 10f
    private val obstacleSpawnFrequency = 2000L
    private var lastSpawnTime = 0L

    private val obstacleBitmap: Bitmap =
        BitmapFactory.decodeResource(resources, R.drawable.obstacle_sprite)

    private var score = 0
    private val scorePaint = Paint().apply {
        color = Color.WHITE
        textSize = 170f
        typeface = Typeface.createFromAsset(context.assets, "HachicroUndertaleBattleFontRegular-L3zlg.ttf")
        setShadowLayer(1f, 0f, 0f, Color.BLACK)
    }

    private var isGameOver = false

    init {
        startGameLoop()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBackground(canvas)
        updateBackground()
        if (!isGameOver) {
            drawChicken(canvas)
            drawObstacles(canvas)
            drawScore(canvas)
            updateChicken()
            updateObstacles()
            invalidate()
        }
    }

    private fun drawScore(canvas: Canvas) {
        val textWidth = scorePaint.measureText(score.toString())

        canvas.drawText(score.toString(), (canvas.width - textWidth) / 2, 350f, scorePaint)
    }

    private fun drawChicken(canvas: Canvas) {
        val centerX = 100f - scaledChickenBitmap.width / 2
        val centerY = chickenY - scaledChickenBitmap.height / 2
        canvas.drawBitmap(scaledChickenBitmap, centerX, centerY, chickenPaint)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        backgroundX2 = height.toFloat()
        scaledBackgroundBitmap = Bitmap.createScaledBitmap(
            backgroundBitmap,
            height, height, false
        )
    }

    private fun updateBackground() {
        backgroundX1 -= backgroundSpeed
        backgroundX2 -= backgroundSpeed

        if (backgroundX1 <= -scaledBackgroundBitmap.width + 5) {
            backgroundX1 = scaledBackgroundBitmap.width.toFloat()
        }
        if (backgroundX2 <= -scaledBackgroundBitmap.width + 5) {
            backgroundX2 = scaledBackgroundBitmap.width.toFloat()
        }
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawBitmap(scaledBackgroundBitmap, backgroundX1, 0f, null)
        canvas.drawBitmap(scaledBackgroundBitmap, backgroundX2, 0f, null)
    }

    private fun drawObstacles(canvas: Canvas) {
        for (obstacle in obstacles) {

            val scaledBitmapTop = Bitmap.createScaledBitmap(
                obstacleBitmap,
                obstacleWidth.toInt(),
                obstacle.top.toInt(),
                false
            )
            val scaledBitmapBottom = Bitmap.createScaledBitmap(
                obstacleBitmap,
                obstacleWidth.toInt(),
                (height.toFloat() - obstacle.bottom).toInt(),
                false
            )

            canvas.drawBitmap(
                scaledBitmapTop,
                obstacle.left,
                0f,
                obstaclePaint
            )

            canvas.drawBitmap(
                scaledBitmapBottom,
                obstacle.left,
                obstacle.bottom,
                obstaclePaint
            )


        }
    }

    private fun updateChicken() {
        velocity += gravity
        chickenY += velocity

        if (chickenY > height) {
            isGameOver = true
            gameOverCallback?.onGameOver(score)
        }

        if (chickenY < 0f) {
            chickenY = 0f
        }

        for (obstacle in obstacles) {
            if (isCollision(chickenY, 100f, obstacle)) {
//                resetGame()
                isGameOver = true
                gameOverCallback?.onGameOver(score)
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
//                resetGame()

                isGameOver = true
                gameOverCallback?.onGameOver(score)
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
        velocity = -25f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (!isGameOver) {
                flap()
            } else {
                resetGame()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun isCollision(chickenY: Float, chickenX: Float, obstacle: Obstacle): Boolean {
        return (chickenY - chickenSize / 2 < obstacle.top || chickenY + chickenSize / 2 > obstacle.bottom) &&
                (chickenX + chickenSize / 2 > obstacle.left && chickenX - chickenSize / 2 < obstacle.right)
    }

    fun resetGame() {
        Log.d("View", "RESET GAME")
        chickenY = height / 2f
        velocity = 0f
        obstacles.clear()
        lastSpawnTime = System.currentTimeMillis()
        score = 0
        isGameOver = false
        invalidate()
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


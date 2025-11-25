package com.cs407.lab09

class Ball(
    private val backgroundWidth: Float,
    private val backgroundHeight: Float,
    private val ballSize: Float
) {
    var posX = 0f
    var posY = 0f
    var velocityX = 0f
    var velocityY = 0f
    private var accX = 0f
    private var accY = 0f

    private var isFirstUpdate = true

    init {
        reset()
    }

    /**
     * Updates the ball's position and velocity based on the given acceleration and time step.
     * (See lab handout for physics equations)
     */
    fun updatePositionAndVelocity(xAcc: Float, yAcc: Float, dT: Float) {
        if (isFirstUpdate) {
            isFirstUpdate = false
            accX = xAcc
            accY = yAcc
            return
        }

        val ax0 = accX
        val ay0 = accY
        val ax1 = xAcc
        val ay1 = yAcc

        val newVx = velocityX + 0.5f * (ax0 + ax1) * dT
        val newVy = velocityY + 0.5f * (ay0 + ay1) * dT

        val dx = velocityX * dT + (dT * dT / 6f) * (3f * ax0 + ax1)
        val dy = velocityY * dT + (dT * dT / 6f) * (3f * ay0 + ay1)

        posX += dx
        posY += dy

        velocityX = newVx
        velocityY = newVy

        accX = ax1
        accY = ay1

        checkBoundaries()
    }

    /**
     * Ensures the ball does not move outside the boundaries.
     * When it collides, velocity and acceleration perpendicular to the
     * boundary should be set to 0.
     */
    fun checkBoundaries() {
        val radius = ballSize

        if (posX < 0f) {
            posX = 0f
            velocityX = 0f
            accX = 0f
        }
        if (posX > backgroundWidth - radius) {
            posX = backgroundWidth - radius
            velocityX = 0f
            accX = 0f
        }

        if (posY < 0f) {
            posY = 0f
            velocityY = 0f
            accY = 0f
        }
        if (posY > backgroundHeight - radius) {
            posY = backgroundHeight - radius
            velocityY = 0f
            accY = 0f
        }
    }
    /**
     * Resets the ball to the center of the screen with zero
     * velocity and acceleration.
     */
    fun reset() {
        posX = (backgroundWidth - ballSize) / 2f
        posY = (backgroundHeight - ballSize) / 2f
        velocityX = 0f
        velocityY = 0f
        accX = 0f
        accY = 0f
        isFirstUpdate = true
    }
}

package com.cs407.lab09

import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BallViewModel : ViewModel() {

    private var ball: Ball? = null
    private var lastTimestamp: Long = 0L

    private val _ballPosition = MutableStateFlow(Offset.Zero)
    val ballPosition: StateFlow<Offset> = _ballPosition.asStateFlow()
    /**
     * Called by the UI when the game field's size is known.
     */
    fun initBall(fieldWidth: Float, fieldHeight: Float, ballSizePx: Float) {
        if (ball == null) {
            ball = Ball(fieldWidth, fieldHeight, ballSizePx)
            _ballPosition.value = Offset(ball!!.posX, ball!!.posY)
        }
    }
    /**
     * Called by the SensorEventListener in the UI.
     */
    fun onSensorDataChanged(event: SensorEvent) {
        val currentBall = ball ?: return

        val type = event.sensor.type
        if (type != Sensor.TYPE_GRAVITY && type != Sensor.TYPE_ACCELEROMETER) return

        if (lastTimestamp != 0L) {
            val NS2S = 1f / 1_000_000_000f
            val dT = (event.timestamp - lastTimestamp) * NS2S
            if (dT <= 0f) {
                lastTimestamp = event.timestamp
                return
            }

            // Raw values point opposite gravity; also convert to screen coords.
            // Screen coords: +x right, +y down.
            val scale = 400f  // make motion visible; tune 200–600 if desired
            val ax = -event.values[0] * scale
            val ay = event.values[1] * scale

            currentBall.updatePositionAndVelocity(ax, ay, dT)

            _ballPosition.update { Offset(currentBall.posX, currentBall.posY) }
        }

        lastTimestamp = event.timestamp
    }

    fun reset() {
        ball?.reset()
        ball?.let { _ballPosition.value = Offset(it.posX, it.posY) }
        lastTimestamp = 0L
    }
}

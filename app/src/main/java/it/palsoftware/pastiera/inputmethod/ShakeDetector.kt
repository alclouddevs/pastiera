package it.palsoftware.pastiera.inputmethod

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(
    private val context: Context,
    private val onShake: () -> Unit
) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var lastShakeTime = 0L

    companion object {
        private const val SHAKE_G_FORCE_THRESHOLD = 2.1f
        private const val SHAKE_COOLDOWN_MS = 1200L
    }

    fun register() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometer?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun unregister() {
        sensorManager?.unregisterListener(this)
        sensorManager = null
        accelerometer = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val e = event ?: return

        val x = e.values[0]
        val y = e.values[1]
        val z = e.values[2]

        val gForce = sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH

        if (gForce > SHAKE_G_FORCE_THRESHOLD) {
            val now = System.currentTimeMillis()
            if (now - lastShakeTime > SHAKE_COOLDOWN_MS) {
                lastShakeTime = now
                onShake()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

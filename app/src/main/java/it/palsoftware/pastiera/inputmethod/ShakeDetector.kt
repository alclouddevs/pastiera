package it.palsoftware.pastiera.inputmethod

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class ShakeDetector(
    private val context: Context,
    private val onShake: () -> Unit
) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    private var lastShakeTime = 0L
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private var initialized = false

    companion object {
        private const val SHAKE_THRESHOLD = 800
        private const val SHAKE_COOLDOWN_MS = 1000L
    }

    fun register() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null) {
            sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }
        initialized = false
    }

    fun unregister() {
        sensorManager?.unregisterListener(this)
        sensorManager = null
        accelerometer = null
        initialized = false
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val e = event ?: return
        val x = e.values[0]
        val y = e.values[1]
        val z = e.values[2]

        if (!initialized) {
            lastX = x
            lastY = y
            lastZ = z
            initialized = true
            return
        }

        val dx = x - lastX
        val dy = y - lastY
        val dz = z - lastZ
        lastX = x
        lastY = y
        lastZ = z

        val force = dx * dx + dy * dy + dz * dz

        if (force > SHAKE_THRESHOLD) {
            val now = System.currentTimeMillis()
            if (now - lastShakeTime > SHAKE_COOLDOWN_MS) {
                lastShakeTime = now
                onShake()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

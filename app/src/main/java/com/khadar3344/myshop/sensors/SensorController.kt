package com.khadar3344.myshop.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SensorController @Inject constructor(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    
    private val _accelerometerData = MutableStateFlow(FloatArray(3))
    val accelerometerData: StateFlow<FloatArray> = _accelerometerData

    private val _gyroscopeData = MutableStateFlow(FloatArray(3))
    val gyroscopeData: StateFlow<FloatArray> = _gyroscopeData

    private val _lightData = MutableStateFlow(0f)
    val lightData: StateFlow<Float> = _lightData

    fun startSensorMonitoring() {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.also { gyroscope ->
            sensorManager.registerListener(
                this,
                gyroscope,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)?.also { light ->
            sensorManager.registerListener(
                this,
                light,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stopSensorMonitoring() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                _accelerometerData.value = event.values.clone()
                Log.d("SensorController", "Accelerometer: x=${event.values[0]}, y=${event.values[1]}, z=${event.values[2]}")
            }
            Sensor.TYPE_GYROSCOPE -> {
                _gyroscopeData.value = event.values.clone()
                Log.d("SensorController", "Gyroscope: x=${event.values[0]}, y=${event.values[1]}, z=${event.values[2]}")
            }
            Sensor.TYPE_LIGHT -> {
                _lightData.value = event.values[0]
                Log.d("SensorController", "Light: ${event.values[0]}")
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("SensorController", "Accuracy changed for sensor ${sensor?.name}: $accuracy")
    }

    fun isAccelerometerAvailable(): Boolean {
        return sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
    }

    fun isGyroscopeAvailable(): Boolean {
        return sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null
    }

    fun isLightSensorAvailable(): Boolean {
        return sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null
    }
}

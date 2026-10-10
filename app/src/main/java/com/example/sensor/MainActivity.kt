package com.example.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.max

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    private var accelerometer: Sensor? = null
    private var lightSensor: Sensor? = null

    // Nilai sensor yang akan langsung mengubah UI
    var ballX by mutableFloatStateOf(0f)
    var ballY by mutableFloatStateOf(0f)
    var lightLevel by mutableFloatStateOf(0f)

    var containerWidth = 0f
    var containerHeight = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager =
            getSystemService(SENSOR_SERVICE) as SensorManager

        accelerometer =
            sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
            )

        lightSensor =
            sensorManager.getDefaultSensor(
                Sensor.TYPE_LIGHT
            )

        setContent {
            MaterialTheme {
                BolaSensorScreen(
                    activity = this
                )
            }
        }
    }

    // WAJIB: register sensor saat aplikasi aktif
    override fun onResume() {
        super.onResume()

        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        lightSensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    // WAJIB: unregister sensor saat aplikasi tidak aktif
    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    // Data sensor masuk ke sini
    override fun onSensorChanged(event: SensorEvent) {

        when (event.sensor.type) {

            // =========================
            // ACCELEROMETER
            // =========================
            Sensor.TYPE_ACCELEROMETER -> {

                val x = event.values[0]
                val y = event.values[1]

                val sensitivity = 2.8f

                ballX -= x * sensitivity
                ballY += y * sensitivity

                val maxX =
                    max(0f, containerWidth - 60.dp.toPx())

                val maxY =
                    max(0f, containerHeight - 60.dp.toPx())

                ballX = ballX.coerceIn(0f, maxX)
                ballY = ballY.coerceIn(0f, maxY)
            }

            // =========================
            // LIGHT SENSOR
            // =========================
            Sensor.TYPE_LIGHT -> {

                lightLevel = event.values[0]
            }
        }
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ) {}

    // Membantu mengubah dp menjadi pixel
    private fun Dp.toPx(): Float {
        return this.value * resources.displayMetrics.density
    }
}


@Composable
fun BolaSensorScreen(
    activity: MainActivity
) {

    val ballX = activity.ballX
    val ballY = activity.ballY
    val lightLevel = activity.lightLevel

    val density = LocalDensity.current

    val ballSize = 60.dp

    // Warna bola berdasarkan cahaya
    val ballColor = when {

        lightLevel < 50 ->
            Color(0xFF1A237E)       // Gelap → biru tua

        lightLevel < 300 ->
            Color(0xFF2196F3)       // Sedang → biru

        else ->
            Color(0xFFFFC107)       // Terang → kuning
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FC))
            .padding(20.dp)
    ) {

        // =========================
        // HEADER
        // =========================

        Text(
            text = "Balance Ball",
            fontSize = 28.sp,
            color = Color(0xFF222222)
        )

        Text(
            text = "Control the ball with your phone",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(50.dp),
            color = Color(0xFFE8F5E9)
        ) {
            Text(
                text = "Sensor ready",
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 7.dp
                ),
                color = Color(0xFF2E7D32),
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))


        // =========================
        // PLAY AREA
        // =========================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .onGloballyPositioned {

                    activity.containerWidth =
                        it.size.width.toFloat()

                    activity.containerHeight =
                        it.size.height.toFloat()
                }
        ) {

            Box(
                modifier = Modifier
                    .offset(
                        x = with(density) {
                            ballX.toDp()
                        },
                        y = with(density) {
                            ballY.toDp()
                        }
                    )
                    .size(ballSize)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                ballColor,
                                ballColor.copy(alpha = 0.6f)
                            )
                        )
                    )
            )
        }


        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Tilt your phone",
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            ),
            fontSize = 15.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(14.dp))


        // =========================
        // INFO CARD
        // =========================

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = Color.White
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "Cara menggunakan",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Miringkan HP untuk menggerakkan bola.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "Cahaya sekitar mengubah warna bola.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Cahaya: ${lightLevel.toInt()} lux",
                    fontSize = 13.sp,
                    color = ballColor
                )
            }
        }
    }
}
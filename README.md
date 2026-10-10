Balance Ball Sensor

Deskripsi
Balance Ball Sensor adalah aplikasi Android berbasis Kotlin dan Jetpack Compose yang memanfaatkan sensor smartphone untuk menggerakkan bola dan mengubah warnanya berdasarkan kondisi cahaya sekitar.

Sensor yang Digunakan

1. Accelerometer
- Kategori: Motion
- Fungsi: Mendeteksi percepatan dan kemiringan smartphone.
- Efek pada UI: Bola bergerak mengikuti kemiringan smartphone.

2. Light Sensor
- Kategori: Environment
- Fungsi: Mengukur intensitas cahaya sekitar dalam satuan lux.
- Efek pada UI: Warna bola berubah berdasarkan intensitas cahaya yang terdeteksi.

Teknologi
- Kotlin
- Android Studio
- Jetpack Compose
- Android SensorManager

Lifecycle Sensor
Sensor didaftarkan pada `onResume()` dan dihentikan pada `onPause()` agar pengelolaan sensor mengikuti lifecycle aplikasi.

Cara Menjalankan
1. Clone atau download repository ini.
2. Buka project menggunakan Android Studio.
3. Tunggu Gradle Sync selesai.
4. Jalankan aplikasi pada smartphone Android yang mendukung Accelerometer dan Light Sensor.

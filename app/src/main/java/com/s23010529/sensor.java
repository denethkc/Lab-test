package com.s23010529;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class sensor extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private TextView temperatureText;
    private Button pauseButton;
    private MediaPlayer mediaPlayer;

    private static final float THRESHOLD_TEMP = 29; // Replace with your SID last 2 digits
    private boolean audioPlayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Link UI elements
        temperatureText = findViewById(R.id.Temptext);
        pauseButton = findViewById(R.id.pause);

        // Set up sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (temperatureSensor == null) {
            Toast.makeText(this, "Temperature sensor not available", Toast.LENGTH_LONG).show();
            finish();
        }

        // Load local audio file
        mediaPlayer = MediaPlayer.create(this, R.raw.alert);

        // Pause button functionality
        pauseButton.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                audioPlayed = false; // Reset flag so it can play again
                Toast.makeText(this, "Sound Paused", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float currentTemp = event.values[0];
        temperatureText.setText("Temperature: " + currentTemp + " °C");

        if (currentTemp >= THRESHOLD_TEMP && !audioPlayed) {
            mediaPlayer.start();
            audioPlayed = true;
            Toast.makeText(this, "Threshold Exceeded!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed
    }
}

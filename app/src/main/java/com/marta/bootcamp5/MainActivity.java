package com.marta.bootcamp5;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main";
    private SensorManager sensorManager;
    private Sensor rotationVector;
    private Sensor accelerometer;

    private ImageView imageViewBall;
    private TextView tv;

    final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            imageViewBall.setRotation(RotationHelper.rotationInDegrees(sensorEvent.values));
            //tv.setText();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    public static Intent getStartingIntent(final Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        imageViewBall = (ImageView) findViewById(R.id.ImageView_ball);
        tv = (TextView) findViewById(R.id.TV);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null ||
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            Toast.makeText(getApplicationContext(), R.string.no_rotation_sensor, Toast.LENGTH_LONG).show();
            finish();
        }

        rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        //accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, rotationVector, SensorManager.SENSOR_DELAY_GAME);
        //sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

}

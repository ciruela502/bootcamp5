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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main";
    private SensorManager sensorManager;
    private Sensor rotationVector;
    private Sensor proximity;
    private Sensor gravitation;

    private ImageView imageViewBall;
    private TextView textView;
    //private RelativeLayout relativeMain;

    final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            if(sensorEvent.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR) {
                imageViewBall.setRotation(RotationHelper.rotationInDegrees(sensorEvent.values));

                if (ThrowElement.isElementDisappear(sensorEvent.values)) {
                    textView.setVisibility(View.VISIBLE);
                    imageViewBall.setVisibility(View.INVISIBLE);

                }
            }
            else if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY){
                if(ThrowElement.isElementAppear(sensorEvent.values)){
                    imageViewBall.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                }
            }
            //nie zdazylam dokonczyc
           /* else if(sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY){
               int colorValue =  ColorBackground.getColor(sensorEvent.values);
                relativeMain.setBackgroundColor(colorValue);

            }*/
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
        textView = (TextView) findViewById(R.id.TV);
        //relativeMain = (RelativeLayout) findViewById(R.id.RelativeLayout_view);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null ||
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null) {
            Toast.makeText(getApplicationContext(), R.string.no_rotation_sensor, Toast.LENGTH_LONG).show();
            finish();
        }

        rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        gravitation = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, rotationVector, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorEventListener, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, gravitation,SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

}

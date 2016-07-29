package com.marta.bootcamp5;

import android.util.Log;

import static android.hardware.SensorManager.AXIS_X;
import static android.hardware.SensorManager.AXIS_Z;
import static android.hardware.SensorManager.getOrientation;
import static android.hardware.SensorManager.getRotationMatrixFromVector;
import static android.hardware.SensorManager.remapCoordinateSystem;

public class RotationHelper {
    private static final int NUMBER_OF_SAMPLES = 7;
    private static final float THRESHOLD = 10f;
    private static final String TAG = "rotation";

    private static float[] sRotationValuesIn = new float[16];
    private static float[] sRotationValuesOut = new float[16];
    private static float[] sAnglesInRadians = new float[3];
    private static float[] sSamples = new float[NUMBER_OF_SAMPLES];
    private static int sCurrentSampleIndex = 0;

    static {
        sRotationValuesIn[0] = 1;
        sRotationValuesIn[4] = 1;
        sRotationValuesIn[8] = 1;
        sRotationValuesIn[12] = 1;
    }

    public static int rotationInDegrees(final float[] rotationVectorValues) {
        Log.d(TAG, "rotationLength " + rotationVectorValues.length);
        getRotationMatrixFromVector(sRotationValuesIn, rotationVectorValues);
        remapCoordinateSystem(sRotationValuesIn, AXIS_X, AXIS_Z, sRotationValuesOut);
        getOrientation(sRotationValuesOut, sAnglesInRadians);

        sSamples[sCurrentSampleIndex++] = (float) Math.toDegrees(sAnglesInRadians[2]);
        if (sCurrentSampleIndex >= NUMBER_OF_SAMPLES) {
            sCurrentSampleIndex = 0;
        }

        //Log.d(TAG, "rotationInDegrees: "+ sRotationValuesIn[0]+"," +sRotationValuesIn[1]+"," +sRotationValuesIn[2]+"," +
       //         sRotationValuesIn[3] + "," +sRotationValuesIn[4]);
        return calculateAverage();
    }

    private static int calculateAverage() {
        float maxValue = 0.0f;
        float minValue = 0.0f;
        for (int i = 0; i < NUMBER_OF_SAMPLES; i++) {
            maxValue = Math.max(maxValue, sSamples[i]);
            minValue = Math.min(minValue, sSamples[i]);
        }
        final boolean nearFlipArea = maxValue + minValue < THRESHOLD
                && maxValue > (180 - THRESHOLD)
                && (minValue < -180 + THRESHOLD);
        float average = 0.0f;
        for (int i = 0; i < NUMBER_OF_SAMPLES; i++) {
            float currentSample = sSamples[i];
            if (nearFlipArea && currentSample < 0) {
                average += 360f + currentSample;
            } else {
                average += currentSample;
            }
        }
        average /= NUMBER_OF_SAMPLES;
        return -(Math.round(average) % 360);
    }
}

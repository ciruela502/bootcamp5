package com.marta.bootcamp5;

import android.graphics.Color;

/**
 * Created by marta on 29.07.2016.
 */
public class ColorBackground {

    public static int getColor(float[] values) {

        int opacity = Math.round(values[2] * 100 / 255);

        return Color.argb(opacity, 0, 0, 255);

    }
}

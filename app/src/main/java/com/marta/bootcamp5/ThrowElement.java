package com.marta.bootcamp5;

/**
 * Created by marta on 29.07.2016.
 */
public class ThrowElement {



    public static Boolean isElementDisappear(float[] values){

        float orientationValue = values[2];
        return Math.abs(orientationValue) > -0.09 && Math.abs(orientationValue) < 0.9;

    }

    public static Boolean isElementAppear(float[] values){
        return values[0] < 4.8;

    }
}

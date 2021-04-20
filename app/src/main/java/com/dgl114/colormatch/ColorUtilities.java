package com.dgl114.colormatch;

import android.graphics.Color;
//****************************************************************************************
//ColorUtilities.java       Author: Unknown
//
//I modified this class to return an int instead of a color object. This class evaluates
//the alpha, but it is not considered throughout the rest of the classes. Alpha = 100%
//****************************************************************************************
public class ColorUtilities {
    public static int blend(Color c0, Color c1) {
        float totalAlpha = c0.alpha() + c1.alpha();
        float weight0 = c0.alpha() / totalAlpha;
        float weight1 = c1.alpha() / totalAlpha;

        float r = weight0 * c0.red() + weight1 * c1.red();
        float g = weight0 * c0.green() + weight1 * c1.green();
        float b = weight0 * c0.blue() + weight1 * c1.blue();
        float a = Math.max(c0.alpha(), c1.alpha());

        return Color.argb(a, r, g, b);
    }
}

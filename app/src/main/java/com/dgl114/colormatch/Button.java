package com.dgl114.colormatch;

//****************************************************************************************
//Button.java           Author: zakacat
//
//This class is for a Button object. A button object has two attributes -
// - mSelected (is it selected?)
// - mColor (the color of the button which is represented by an int)
//
//This class also includes a default constructor and getters and setters for the
//attributes.
//****************************************************************************************

public class Button {

    private boolean mSelected = false;
    private int mColor;

    public Button() {

    }

    //****************************************************************************************
    //Setters
    //****************************************************************************************
    public void setSelected(boolean maybe) {
        mSelected = maybe;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    //****************************************************************************************
    //Getters
    //****************************************************************************************
    public boolean isSelected() {
        return mSelected;
    }

    public int getColor() {
        return mColor;
    }


}

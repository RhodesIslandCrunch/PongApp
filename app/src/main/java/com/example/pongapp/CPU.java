package com.example.pongapp;

import android.graphics.RectF;

public class CPU<mLength> {

    private float mLength;
    private float mHeight;
    private RectF mRect;
    /**
     * Constructor for the CPU's Bat
     * @param mScreenX  horizontal size of screen
     * @param mScreenY  Vertical size of screen
     */
    public CPU(int mScreenX,int mScreenY) {
        // One eighth the screen width
        mLength = mScreenX / 8;
        //the bats are the same lenghth
        mHeight = mScreenY / 40;
        //the bats are the same height

        mRect = new RectF();
        mRect.top = 0;
        mRect.bottom = mHeight;

    }

    /**
     * returns the bat's loctation on screen
     * @return rectf the positon of the ball on the x part of the screen
     */
    RectF getRect() {
        return mRect;
    }
    /**
    * Updates the CPU's Bat rectF
    * @param ballLoc - the position of the ball
    */
    void update(float ballLoc){
        // move the bat
        mRect.left = mRect.left + (ballLoc - mLength/2);
        mRect.right = mRect.left + mLength;
    }
    float retColl(){
        return mHeight;
    }
}
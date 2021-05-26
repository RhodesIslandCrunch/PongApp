package com.example.pongapp;

import android.graphics.RectF;

public class Bat {
    //member variables
    //m prefix for member

    private RectF mRect;
    private float mLength;
    private float mXCoord;
    private float mBatSpeed;
    private int mScreenX;

    final int STOPPED = 0;
    final int LEFT = 1;
    final int RIGHT = 2;

    private int mBatMoving = STOPPED;

    public Bat(int sx, int sy){

        // horizontal resolution
        mScreenX = sx;
        // One eighth the screen width
        mLength = mScreenX / 8;
        // One fortieth the screen height
        float height = sy / 40;
        // Configure the starting location of the bat
        // Roughly the middle horizontally
        mXCoord = mScreenX / 2;
        // The height of the bat compared to the bottom of the screen
        float mYCoord = sy - height;
        // Initialize mRect based on the size and position
        mRect = new RectF(mXCoord, mYCoord,
                mXCoord + mLength,
                mYCoord + height);
        // Configure the speed of the bat
        // This code means the bat can cover the
        // width of the screen in 1 second
        mBatSpeed = mScreenX;
    }
    RectF getRect(){
        return mRect;
    }
    void setMovementState(int state){
        mBatMoving = state;
    }
    // Update the bat- Called each frame/loop
    void update(long fps){
        // Move the bat based on the mBatMoving variable
        // and the speed of the previous frame
        if(mBatMoving == LEFT){
            mXCoord = mXCoord - mBatSpeed / fps;
        }
        if(mBatMoving == RIGHT){
            mXCoord = mXCoord + mBatSpeed / fps;
        }
        // Stop the bat going off the screen
        if(mXCoord < 0){
            mXCoord = 0;
        }
        else if(mXCoord + mLength > mScreenX){
            mXCoord = mScreenX - mLength;
        }
        // Update mRect based on the results
        mRect.left = mXCoord;
        mRect.right = mXCoord + mLength;
    }

}

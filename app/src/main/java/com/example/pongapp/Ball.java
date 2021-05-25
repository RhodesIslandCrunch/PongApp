package com.example.pongapp;

import android.graphics.RectF;

public class Ball {
    //member variables
    //m prefix for member

    private RectF mRect;
    private float mXVelocity;
    private float mYVelocity;
    private float mBallWidth;
    private float mBallHeight;

    //constructor called by mBall = new Ball (mScreenX);
//called in ponggame
    public Ball(int screenX) {

        // Make the ball square and 1% of screen width
        // of the screen width

        mBallWidth = screenX / 100;
        mBallHeight = screenX / 100;

        // Initialize the RectF with 0, 0, 0, 0
        // We do it here because we only want to
        // do it once.
        // We will initialize the detail
        // at the start of each game

        mRect = new RectF();
    }
    //returns a reference to mRect to Ponggame
    RectF getRect(){

        return mRect;

    }
}
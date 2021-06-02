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
    /**
     * Constructor
     *
     * @param  screenX  the width of the screen used to set ball size
     */
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

    // Update the ball position.
    // Called each frame/loop
    /**
     * Resets the ball back to the middle of the screen when the game starts
     *
     * @param  fps  The speed the game is running at
     */
    void update(long fps){
        // Move the ball based upon the
        // horizontal (mXVelocity) and
        // vertical(mYVelocity) speed
        // and the current frame rate(fps)
        // Move the top left corner
        mRect.left = mRect.left + (mXVelocity / fps);
        mRect.top = mRect.top + (mYVelocity / fps);

        // Match up the bottom right corner
        // based on the size of the ball
        mRect.right = mRect.left + mBallWidth;
        mRect.bottom = mRect.top + mBallHeight;
    }
    // Reverse vertical
    /**
     * Reverses the Vertical Velocity upon collision
     */
    void reverseYVelocity(){
        mYVelocity = -mYVelocity;
    }
    /**
     * Reverses the Horizontal Velocity upon collision
     */
    void reverseXVelocity(){
        mXVelocity = -mXVelocity;
    }

    /**
     * Resets the ball back to the middle of the screen when the game starts
     *
     * @param  y  the vertical length of screen
     * @param  x  the horizontal length of screen
     */
    void reset(int x, int y){
        // Initialise the four points of the rectangle which defines the ball
        mRect.left = x / 2;
        mRect.top = 0;
        mRect.right = x / 2 + mBallWidth;
        mRect.bottom = mBallHeight;

        // How fast will the ball travel
        mYVelocity = -(y / 3);
        mXVelocity = (x / 2);
    }
    /**
     * Increases ball velocity after hit
     */
    void increaseVelocity(){
        // increase the speed by 10%
        mXVelocity = mXVelocity * 1.1f;
        mYVelocity = mYVelocity * 1.1f;
    }
    // Bounce the ball back based on whether it hits the left or right-hand side
    /**
     * Detects a collision between bat and ball for player
     *
     * @param  batPosition the position of the bat
     */
    void batBounce(RectF batPosition){
        // Detect centre of bat
        float batCenter = batPosition.left +
                (batPosition.width() / 2);
        // detect the centre of the ball
        float ballCenter = mRect.left +
                (mBallWidth / 2);
        // Where on the bat did the ball hit?
        float relativeIntersect = (batCenter - ballCenter);
        // Pick a bounce direction
        if(relativeIntersect < 0){
            // Go right
            mXVelocity = Math.abs(mXVelocity);
            // Math.abs = Absolute Value
        }else{
            // Go left
            mXVelocity = -Math.abs(mXVelocity);
        }
        // Flip vertical since horizontal is finished
        reverseYVelocity();
    }
}

package com.example.pongapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PongGame extends SurfaceView {
    //are we debugging?
    private final boolean DEBUGGING = true;
    //Objects for drawing
    private SurfaceHolder mOurHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    // FPS
    private long mFPS;
    // milliseconds in a second
    private final int MILLIS_IN_SECOND = 1000;
    //Screen Resolution
    private int mScreenX;
    private int mScreenY;
    // Text Size
    private int mFontSize;
    private int mFontMargin;
    //other game objects
    private Bat mBat;
    private Ball mBall;
    // score + lives
    private int mScore;
    private int mLives;
    //pong game constructor
    // called from pong activity
    //mponggame = new PongGame(this,size.x,size.y)
    public PongGame(Context context, int x, int y) {
        //Super ... Calls the parent class
        // constructor of SurfaceView
        // provided by Android
        super(context);
        //screen size values
        mScreenX = x;
        mScreenY = y;
        //font = 5% screen width
        mFontSize = mScreenX / 20;
        //intialize the objects for drawing
        //getholder is a method of SurfaceView
        mOurHolder = getHolder();
        mPaint = new Paint();
        //intialize bat + ball

        //start game
        startNewGame();
    }

}
package com.example.pongapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PongGame extends SurfaceView implements  Runnable {
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
    //Thread Variables
    private Thread mGameThread = null;
    //volatile variable can be accessed from both inside and outside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = true;

    //pong game constructo
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
        mBall = new Ball(mScreenX);
        //start game
        startNewGame();
    }
    //player lost or is starting first game
    private void startNewGame(){
        //put ball to start
        mBall.reset(mScreenX,mScreenY);

        //reset score and player lives
        mScore = 0;
        mLives = 3;
    }
    private void draw(){
        if(mOurHolder.getSurface().isValid()){
            //lock canvas ready to draw
            mCanvas = mOurHolder.lockCanvas();
            //fill screen with solid color
            mCanvas.drawColor(Color.argb(255,26,128,182));
            //color paint
            mPaint.setColor(Color.argb(255,255,255,255));
            //draw the bat and ball
            mCanvas.drawRect(mBall.getRect(), mPaint);
            //choose the font size
            mPaint.setTextSize(mFontSize);
            //draw HUD
            mCanvas.drawText("Score: "+mScore + "Lives: "+ mLives, mFontMargin,mFontSize,mPaint);
            if(DEBUGGING) {
                printDebuggingText();
            }
            // Display the Drawing on screen
            //unlockCanvasAndPost is a metod of Surfaceview
            mOurHolder.unlockCanvasAndPost(mCanvas);
        }
    }
    private void printDebuggingText(){
        int debugSize = mFontSize / 2;
        int debugStart = 150;
        mPaint.setTextSize(debugSize);
        mCanvas.drawText("FPS: " + mFPS, 10, debugStart+debugSize,mPaint);
    }
    //when we start the thread with : mGameThread.start();
    //the run method is continuously called by Android
    //because we implemented the Runnable interface
    //calling mGameThread.join();
    // Will stop the thread
    @Override
    public void run(){
        //mplaying gives better control since its accessible variable vs called function
        //mplaying must be true AND thread running for main loop to execute
        while(mPlaying){
            //Loop Start Time
            long frameStartTime = System.currentTimeMillis();
            //game is not paused
            //call update
            if(!mPaused){
                update();
                //bat and ball in new positions
                //check collisions
                detectCollisions();
            }
            // movement and collisions area handled
            //draw scene
            draw();
            //loop time
                //stored in timeThisFrame
            long timeThisFrame = System.currentTimeMillis()-frameStartTime;
            //at least 1 millisecond otherwise divison by zero
            if(timeThisFrame > 0){
                //calculate frame rate and pass to update methods
                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }
        }
    }
    private void update(){
        //update bat and ball
        mBall.update(mFPS);
    }
    private void detectCollisions(){
        //has bat hit ball
        //has ball hit screen edge
        //bottom
        //top
        //left
        //right

    }
    //this method is called by PongActivity when the player quits the game
    public void pause(){
        // Set mPlaying to false
        // Stopping the thread isn't always instant
        mPlaying = false;
        try {
            // Stop the thread
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }
// This method is called by PongActivity
// when the player starts the game
    public void resume() {
        mPlaying = true;
        // Initialize the instance of Thread
        mGameThread = new Thread(this);
        // Start the thread
        mGameThread.start();
    }



}
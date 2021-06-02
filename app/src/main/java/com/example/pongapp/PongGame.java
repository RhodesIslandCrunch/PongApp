package com.example.pongapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.media.SoundPool;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Build;
import java.io.IOException;

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
    private CPU mCPU;
    // score + lives
    private int mScore;
    private int mLives;
    //Thread Variables
    private Thread mGameThread = null;
    //volatile variable can be accessed from both inside and outside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = true;
    // These are for playing sounds
    private SoundPool mSP;
    private int mBeepID = -1;
    private int mBoopID = -1;
    private int mBopID = -1;
    private int mMissID = -1;
    //pong game constructor
    // called from pong activity
    //mponggame = new PongGame(this,size.x,size.y)
    /**
     * Constructor that handles all Pong Logic
     *
     * @param  y  the vertical length of screen
     * @param  x  the horizontal length of screen
     */
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
        mBat = new Bat(mScreenX,mScreenY);
        mCPU = new CPU(mScreenX,mScreenY);
        // Prepare the SoundPool instance
        // Depending upon the version of Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            mSP = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        try{
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;
            // I had to change the .ogg files to wav due to my computers
            // inabilty to handle the filetype
            //explained in pdf
            descriptor = assetManager.openFd("beep.wav");
            mBeepID = mSP.load(descriptor, 0);
            descriptor = assetManager.openFd("boop.wav");
            mBoopID = mSP.load(descriptor, 0);
            descriptor = assetManager.openFd("bop.wav");
            mBopID = mSP.load(descriptor, 0);
            descriptor = assetManager.openFd("miss.wav");
            mMissID = mSP.load(descriptor, 0);
        }catch(IOException e){
            Log.d("error", "failed to load sound files");
        }
        //start game
        startNewGame();
    }
    //player lost or is starting first game
    /**
     * Resets the ball and player lives/score
     */
    private void startNewGame(){
        //put ball to start
        mBall.reset(mScreenX,mScreenY);

        //reset score and player lives
        mScore = 0;
        mLives = 3;
    }
    /**
     * Handles all drawing on the screen including objects
     */
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
            mCanvas.drawRect(mBat.getRect(), mPaint);

            // My code
            mCanvas.drawRect(mCPU.getRect(), mPaint);

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
    /**
     * Print Games fps
     */
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
    /**
     * Game Running handles all logic
     */
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
        mBat.update(mFPS);
        mCPU.update(mBall.getRect().centerX());
    }
    // Handle all the screen touches

    /**
     * handles the player input to move the bat
     *
     * @param  motionEvent the place the player touched
     */
    @Override

    public boolean onTouchEvent(MotionEvent motionEvent) {
        // This switch block replaces the
        // if statement from the Sub Hunter game
        switch (motionEvent.getAction() &
                MotionEvent.ACTION_MASK) {
            // The player has put their finger on the screen
            case MotionEvent.ACTION_DOWN:
                // If the game was paused unpause
                mPaused = false;
                // Where did the touch happen
                if(motionEvent.getX() > mScreenX / 2){
                    // On the right hand side
                    mBat.setMovementState(mBat.RIGHT);
                }
                else{
                    // On the left hand side
                    mBat.setMovementState(mBat.LEFT);
                }
                break;
            // The player lifted their finger
            // from anywhere on screen.
            // It is possible to create bugs by using
            // multiple fingers. We will use more
            // complicated and robust touch handling
            // in later projects
            case MotionEvent.ACTION_UP:
                // Stop the bat moving
                mBat.setMovementState(mBat.STOPPED);
                break;
        }
        return true;
    }
    private void detectCollisions(){
        //has bat hit ball
        if(RectF.intersects(mBat.getRect(), mBall.getRect())) {
            // Realistic-ish bounce
            mBall.batBounce(mBat.getRect());
            mBall.increaseVelocity();
            mScore++;
            mSP.play(mBeepID, 1, 1, 0, 0, 1);
        }
        //has ball hit screen edge
        //bottom
        if(mBall.getRect().bottom > mScreenY){
            mBall.reverseYVelocity();
            mLives--;
            mSP.play(mMissID, 1, 1, 0, 0, 1);
            if(mLives == 0){
                mPaused = true;
                startNewGame();
            }
        }
        //top
        if(mBall.getRect().top < mCPU.retColl()){
            mBall.reverseYVelocity();
            mSP.play(mBoopID, 1, 1, 0, 0, 1);
        }
        //left
        if(mBall.getRect().left < 0){
            mBall.reverseXVelocity();
            mSP.play(mBopID, 1, 1, 0, 0, 1);
        }
        //right
        if(mBall.getRect().right > mScreenX){
            mBall.reverseXVelocity();
            mSP.play(mBopID, 1, 1, 0, 0, 1);
        }

    }
    //this method is called by PongActivity when the player quits the game
    /**
     * Pauses the game thread
     * stops the current game thread
     */
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
    /**
     * Unpauses the game
     * starts a new gamethread to handle pong
     */
    public void resume() {
        mPlaying = true;
        // Initialize the instance of Thread
        mGameThread = new Thread(this);
        // Start the thread
        mGameThread.start();
    }



}
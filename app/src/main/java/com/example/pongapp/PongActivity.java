package com.example.pongapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.graphics.Point;
import android.view.Display;

public class PongActivity extends Activity {

    private PongGame mPongGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //variable creation
        //Android stuff, fixing screen etc.
        super.onCreate(savedInstanceState);
        //Chapter 8
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mPongGame = new PongGame(this, size.x, size.y);
        setContentView(mPongGame);
    }
    @Override
    protected void onResume(){
        super.onResume();
        //Chapter 9
        mPongGame.resume();
    }
    @Override
    protected void onPause(){
        super.onPause();

        mPongGame.pause();
    }
}
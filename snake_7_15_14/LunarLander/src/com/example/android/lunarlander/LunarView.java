/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.lunarlander;

import gui.TouchRect;
import gui.UserAction;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;


/**
 * View that draws, takes keystrokes, etc. for a simple LunarLander game.
 *
 * Has a mode which RUNNING, PAUSED, etc. Has a x, y, dx, dy, ... capturing the
 * current ship physics. All x/y etc. are measured with (0,0) at the lower left.
 * updatePhysics() advances the physics based on realtime. draw() renders the
 * ship, and does an invalidate() to prompt another draw() as soon as possible
 * by the system.
 */
class LunarView extends SurfaceView implements SurfaceHolder.Callback {
    



    /** Pointer to the text view to display "Paused.." etc. */
    private TextView mStatusText;

    /** The thread that actually draws the animation */
    private LunarThread thread;

    private TouchRect touchLRect;
    private TouchRect touchRRect;
    private Context context;
    
    public LunarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

//        // create thread only; it's started in surfaceCreated()
//        thread = new LunarThread(holder, context, new Handler() {
//            @Override
//            public void handleMessage(Message m) {
//                mStatusText.setVisibility(m.getData().getInt("viz"));
//                mStatusText.setText(m.getData().getString("text"));
//            }
//        });

        setFocusable(true); // make sure we get key events
    }

    @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
    	int x = (int) event.getRawX();
    	int y = (int) event.getRawY();
    	
    	int actionR = touchRRect.touched(x, y);
    	if ( actionR != UserAction.NO_ACTION ) {
    		try {
    		this.getThread().getSnake().turnRight();
    		Log.i("touch event", "turn right");
    		return true;
    		} catch (Exception e){
    			System.out.println(e);
    		}
    	}
    	int actionL = touchLRect.touched(x, y);		
    	if ( actionL != UserAction.NO_ACTION ) {
    		try {
    	    this.getThread().getSnake().turnLeft();		
    	    Log.i("touch event", "turn left");
    	    return true; 
    		} catch (Exception e) {
    			System.out.println(e);
    		}
    	}
		return false;
	}

	/**
     * Fetches the animation thread corresponding to this LunarView.
     *
     * @return the animation thread
     */
    public LunarThread getThread() {
        return thread;
    }

    /**
     * Standard override to get key-press events.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        return thread.doKeyDown(keyCode, msg);
    }

    /**
     * Standard override for key-up. We actually care about these, so we can
     * turn off the engine or stop rotating.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        return thread.doKeyUp(keyCode, msg);
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) thread.pause();
    }

    /**
     * Installs a pointer to the text view used for messages.
     */
    public void setTextView(TextView textView) {
        mStatusText = textView;
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    	Rect snakeRect = new Rect(0,0,width,height);
    	Rect rectL = new Rect(0,0,width/2,height);
        touchLRect = new TouchRect(rectL, UserAction.MOVE_LEFT, true, false, null);
    	Rect rectR = new Rect(width/2,0,width,height);
    	touchRRect = new TouchRect(rectR, UserAction.MOVE_RIGHT, true, false, null);
//    	thread.setSurfaceSize(width, height);
    	 // create thread only; it's started in surfaceCreated()
        thread = new LunarThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {
                mStatusText.setVisibility(m.getData().getInt("viz"));
                mStatusText.setText(m.getData().getString("text"));
            }
        }, snakeRect);
        thread.setSurfaceSize(width, height);
        thread.setState(LunarThread.STATE_READY);
        thread.setRunning(true);
        thread.start();
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
    	
      
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}

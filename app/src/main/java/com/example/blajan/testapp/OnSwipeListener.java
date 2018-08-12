package com.example.blajan.testapp;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

// Detects left swipes
public class OnSwipeListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private int numberOfPointers;
    private int maxFingers;


    public OnSwipeListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void onSwipeLeft() {
    }
    public void onSwipeDown() {
    }
    public void onMove(float x, float y){
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                maxFingers=1;
                onMove(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                maxFingers++;
                break;
            case MotionEvent.ACTION_UP:
                //maxFingers=0;
                break;
            case MotionEvent.ACTION_MOVE:
                if(maxFingers==1)
                    onMove(event.getX(),event.getY());
                break;

        }
        Log.d("onTouch", "maxF:"+maxFingers);
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY)
                    && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                    && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
                    && maxFingers==2) {
                if (distanceX < 0) {
                    Log.d("swipe:"+numberOfPointers, "back");

                    onSwipeLeft();
                    return true;
                }
            }
            else if(Math.abs(distanceX) < Math.abs(distanceY)
                    && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD
                    && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD
                    && maxFingers==3){
                if (distanceY > 0){
                    Log.d("swipe:","close");
                    onSwipeDown();
                }
            }
            return false;
        }
    }
}


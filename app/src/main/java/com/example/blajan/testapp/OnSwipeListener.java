package com.example.blajan.testapp;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * OnTouchListener that detects:
 * - left swipe with two fingers
 * - down swipe with three fingers
 * - dragging with one finger
 */
public class OnSwipeListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private int maxFingers;


    OnSwipeListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void onSwipeLeft() {
    }

    public void onSwipeDown() {
    }

    public void onMove(float x, float y) {
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: //initial touch of current gesture
                maxFingers = 1;
                onMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN: //additional fingers touch the screen
                maxFingers++;
                break;
            case MotionEvent.ACTION_MOVE:   //when moving fingers over the screen
                if (maxFingers == 1)
                    onMove(event.getX(), event.getY());
                break;

        }
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

            // Detects left swipe with two fingers
            if (Math.abs(distanceX) > Math.abs(distanceY)
                    && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                    && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
                    && maxFingers == 2 && distanceX < 0) {
                onSwipeLeft();
                return true;

            }

            // Detects down swipe with three fingers
            else if (Math.abs(distanceX) < Math.abs(distanceY)
                    && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD
                    && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD
                    && maxFingers == 3 && distanceY > 0) {
                onSwipeDown();
            }
            return false;
        }
    }
}


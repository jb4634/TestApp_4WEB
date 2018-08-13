package com.example.blajan.testapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class DrawActivity extends AppCompatActivity {
    private static final String LINE_COLOR = "#01A19B"; // Color of the shape's outline

    String shape; // Selected shape
    double ratio; // Selected size ratio of the shape
    int xCenter; // X coordinate of the center of the screen
    int yCenter; // Y coordinate of the center of the screen
    Vibrator v;

    // Triangle points
    Point p1;
    Point p2;
    Point p3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets the selected shape and size from extras in intent
        Intent intent = getIntent();
        shape = intent.getStringExtra("shape");
        ratio = intent.getIntExtra("size", 70) / 100.0;
        View view = new DrawView(this);
        setContentView(view);

        // Get instance of Vibrator from current Context
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        view.setOnTouchListener(new OnSwipeListener(this) {
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(DrawActivity.this, ShapeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onSwipeDown() {
                Intent intent = new Intent(DrawActivity.this, MainActivity.class);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }

            /**
             * Overrides onMove function for dragging gesture with one finger
             * @param x: x coordinate of current finger position
             * @param y: y coordinate of current finger position
             */
            @Override
            public void onMove(float x, float y) {
                final int VIBRATE_DURATION = 100;
                final int ACCEPTABLE_ERROR = 20;

                double radius = yCenter * ratio;

                // Calculates horizontal and vertical bounds of the shape
                boolean isInsideH = x < xCenter + radius + ACCEPTABLE_ERROR && x > xCenter - radius - ACCEPTABLE_ERROR;
                boolean isInsideV = y < yCenter + radius + ACCEPTABLE_ERROR && y > yCenter - radius - ACCEPTABLE_ERROR;

                // Calculates the coordinates for the outline of the current shape
                // and vibrates if current coordinates are on the outline
                switch (shape) {
                    case "circle":
                        double distance = Math.sqrt(Math.pow(Math.abs(x - xCenter), 2)
                                + Math.pow(Math.abs(y - yCenter), 2));
                        if (distance > radius - ACCEPTABLE_ERROR && distance < radius + ACCEPTABLE_ERROR) {
                            v.vibrate(VIBRATE_DURATION);
                        }
                        break;
                    case "triangle":
                        if (isInsideH && isInsideV) {
                            boolean b1 = isOnLine(x, y, p1.x, p1.y, p2.x, p2.y);
                            boolean b2 = isOnLine(x, y, p2.x, p2.y, p3.x, p3.y);
                            boolean b3 = isOnLine(x, y, p3.x, p3.y, p1.x, p1.y);
                            if (b1 || b2 || b3)
                                v.vibrate(VIBRATE_DURATION);
                        }
                        break;
                    case "square":
                        double errx1 = Math.abs(x - xCenter - radius);
                        double errx2 = Math.abs(x - xCenter + radius);
                        double erry1 = Math.abs(y - yCenter - radius);
                        double erry2 = Math.abs(y - yCenter + radius);
                        if ((errx1 < ACCEPTABLE_ERROR || errx2 < ACCEPTABLE_ERROR) && isInsideV)
                            v.vibrate(VIBRATE_DURATION);
                        else if ((erry1 < ACCEPTABLE_ERROR || erry2 < ACCEPTABLE_ERROR) && isInsideH)
                            v.vibrate(VIBRATE_DURATION);
                        break;
                }
            }
        });
    }

    /**
     * Checks if current coordinates of a finger are on the line segment P1-P2
     *
     * @param currX: current x coordinate
     * @param currY: current y coordinate
     * @param p1x:   x coordinate of Point P1
     * @param p1y:   y coordinate of Point P1
     * @param p2x:   x coordinate of Point P2
     * @param p2y:   y coordinate of Point P2
     * @return boolean: true if current coordinates are on the line segment
     */
    private boolean isOnLine(double currX, double currY, double p1x, double p1y, double p2x, double p2y) {

        final double ACCEPTABLE_ERROR = 0.2;

        double dx1 = currX - p1x;
        if (dx1 == 0) dx1 = 1; // Avoids zero values (division by zero)

        double dy1 = currY - p1y;
        if (dy1 == 0) dy1 = 1;

        double dx2 = p2x - p1x;
        if (dx2 == 0) dx2 = 1;

        double dy2 = p2y - p1y;
        if (dy2 == 0) dy2 = 1;


        double k1 = dy1 / dx1; // Coefficient of the line from P1 to curr
        double k2 = dy2 / dx2; // Coefficient of the line from P1 to P2
        return Math.abs(k1 - k2) < ACCEPTABLE_ERROR; // Checks if coefficient are similar enough to represent the same line
    }

    public class DrawView extends View {
        int radius;
        Rect rect;
        Paint paint;

        public DrawView(Context context) {
            super(context);
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.parseColor(LINE_COLOR));
            paint.setStrokeWidth(10);
        }

        @Override
        public boolean performClick() {
            return super.performClick();
        }

        //Calculates the boundaries of the selected shape and draws the outline on the screen
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            xCenter = getWidth() / 2;
            yCenter = getHeight() / 2;
            radius = (int) (yCenter * ratio);
            switch (shape) {
                case "circle":
                    canvas.drawCircle(xCenter, yCenter, radius, paint);
                    break;
                case "square":
                    rect = new Rect(xCenter - radius, yCenter - radius,
                            xCenter + radius, yCenter + radius);
                    canvas.drawRect(rect, paint);
                    break;
                case "triangle":
                    p1 = new Point(xCenter - radius, yCenter + radius);
                    p2 = new Point(xCenter + radius, yCenter + radius);
                    p3 = new Point(xCenter, yCenter - radius);
                    canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
                    canvas.drawLine(p2.x, p2.y, p3.x, p3.y, paint);
                    canvas.drawLine(p3.x, p3.y, p1.x, p1.y, paint);
                    break;
            }

        }

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}

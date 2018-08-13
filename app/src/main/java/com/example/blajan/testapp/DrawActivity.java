package com.example.blajan.testapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class DrawActivity extends AppCompatActivity {
    private static final String LINE_COLOR = "#01A19B";

    String shape;
    double ratio;
    int xCenter;
    int yCenter;
    Vibrator v;
    Point p1;
    Point p2;
    Point p3;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        shape = intent.getStringExtra("shape");
        ratio = intent.getIntExtra("size", 70)/100.0;
        Log.d("prva",""+shape);
        Log.d("druga",""+ratio);
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
            public void onSwipeDown(){
                Intent intent = new Intent(DrawActivity.this, MainActivity.class);
                intent.putExtra("EXIT",true);
                startActivity(intent);
                finish();
            }
            @Override
            public void onMove(float x, float y){
                double radius = yCenter*ratio;
                boolean isInsideH = x<xCenter+radius+20 && x>xCenter-radius-20;
                boolean isInsideV = y<yCenter+radius+20 && y>yCenter-radius-20;
                switch(shape){
                    case "circle":
                        double distance = Math.sqrt(Math.pow(Math.abs(x-xCenter),2)+ Math.pow(Math.abs(y-yCenter),2));
                        if(distance>radius-20 && distance<radius+20){
                            v.vibrate(100);
                        }
                        break;
                    case "triangle":
                        if(isInsideH && isInsideV){
                            boolean b1 = isOnLine(x,y,p1.x,p1.y,p2.x,p2.y);
                            boolean b2 = isOnLine(x,y,p2.x,p2.y,p3.x,p3.y);
                            boolean b3 = isOnLine(x,y,p3.x,p3.y,p1.x,p1.y);

                            if(b1 || b2 || b3)
                                v.vibrate(50);
                        }


                        break;
                    case "square":
                        double errx1 = Math.abs(x-xCenter-radius);
                        double errx2 = Math.abs(x-xCenter+radius);
                        double erry1 = Math.abs(y-yCenter-radius);
                        double erry2 = Math.abs(y-yCenter+radius);

                        if((errx1<20 || errx2<20) && isInsideV)
                            v.vibrate(100);
                        else if((erry1<20 || erry2<20) && isInsideH)
                            v.vibrate(100);
                        break;
                }
            }
        });


    }

    private boolean isOnLine(double currX,double currY, double p1x, double p1y, double p2x, double p2y){

        double dx1 = currX - p1x;
        if (dx1==0)dx1=1;

        double dy1 = currY - p1y;
        if (dy1==0)dy1=1;

        double dx2 = p2x - p1x;
        if (dx2==0) dx2 = 1;

        double dy2 = p2y - p1y;
        if(dy2==0) dy2=1;


        double k1 = dy1/dx1;
        double k2 = dy2/dx2;
        return Math.abs(k1-k2)<0.2;
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
        }

        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);
            xCenter = getWidth()/2;
            yCenter = getHeight()/2;
            radius = (int) (yCenter*ratio);
            switch(shape){
                case "circle":
                    canvas.drawCircle(xCenter,yCenter,radius,paint);
                    break;
                case "square":
                    rect = new Rect(xCenter-radius,yCenter-radius,
                            xCenter+radius,yCenter+radius);
                    canvas.drawRect(rect,paint);
                    break;
                case "triangle":
                    p1 = new Point(xCenter-radius,yCenter+radius);
                    p2 = new Point(xCenter+radius, yCenter+radius);
                    p3 = new Point(xCenter,yCenter-radius);
                    canvas.drawLine(p1.x,p1.y,p2.x,p2.y,paint);
                    canvas.drawLine(p2.x,p2.y,p3.x,p3.y,paint);
                    canvas.drawLine(p3.x,p3.y,p1.x,p1.y,paint);
                    break;
            }

        }

    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Log.d("Focus debug", "Focus changed !");

        if(!hasFocus) {
            Log.d("Focus debug", "Lost focus !");

            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }
    @Override
    public void onBackPressed() {
        //do nothing
    }
}

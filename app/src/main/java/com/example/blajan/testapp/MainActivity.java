package com.example.blajan.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    Button ttsButton;
    Button shapeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ttsButton = (Button) findViewById(R.id.main_button_speech);
        shapeButton = findViewById(R.id.main_button_shapes);

        ttsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTTS(v);
            }
        });
        shapeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShape(v);
            }
        });

        findViewById(R.id.main_layout).setOnTouchListener(new OnSwipeListener(this) {
            @Override
            public void onSwipeLeft() {
                finish();
            }
            @Override
            public void onSwipeDown(){
                System.exit(0);
            }
        });

        if (getIntent().getBooleanExtra("EXIT", false))
        {
            System.exit(0);
        }

    }

    public void openTTS(View view) {
        Intent intent = new Intent(this,SpeechActivity.class);
        startActivity(intent);
        finish();
    }
    public void openShape(View view){
        Intent intent = new Intent(this, ShapeActivity.class);
        startActivity(intent);
        finish();
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
        //Do nothing
    }

}


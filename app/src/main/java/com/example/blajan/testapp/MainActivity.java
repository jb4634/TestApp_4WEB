package com.example.blajan.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ttsButton = findViewById(R.id.main_button_speech);
        Button shapeButton = findViewById(R.id.main_button_shapes);

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

        //Sets listener to watch for back gesture (left) or exit gesture (down)
        findViewById(R.id.main_layout).setOnTouchListener(new OnSwipeListener(this) {
            @Override
            public void onSwipeLeft() {
                finish();
            }

            @Override
            public void onSwipeDown() {
                System.exit(0);
            }
        });

        //Exit application if some other activity ordered it.
        if (getIntent().getBooleanExtra("EXIT", false)) {
            System.exit(0);
        }

    }

    public void openTTS(View view) {
        Intent intent = new Intent(this, SpeechActivity.class);
        startActivity(intent);
        finish();
    }

    public void openShape(View view) {
        Intent intent = new Intent(this, ShapeActivity.class);
        startActivity(intent);
        finish();
    }

    // Closes system dialog (status bar) if app loses focus (status bar is opened)
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    //Do nothing if "back" button is pressed
    @Override
    public void onBackPressed() {
    }

}


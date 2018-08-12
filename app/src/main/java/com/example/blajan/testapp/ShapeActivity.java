package com.example.blajan.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ShapeActivity extends AppCompatActivity {
    SeekBar seekBar;
    Button drawButton;
    TextView sizeText;
    RadioGroup shapeGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        drawButton = (Button) findViewById(R.id.draw_button);
        sizeText = findViewById(R.id.size_text);
        shapeGroup = findViewById(R.id.group_shape);

        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton selectedRb = findViewById(shapeGroup.getCheckedRadioButtonId());
                Intent intent = new Intent(ShapeActivity.this, DrawActivity.class);
                intent.putExtra("shape",selectedRb.getText());
                intent.putExtra("size", seekBar.getProgress());
                startActivity(intent);
                finish();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sizeText.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.shape_view).setOnTouchListener(new OnSwipeListener(this) {
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(ShapeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onSwipeDown(){
                Intent intent = new Intent(ShapeActivity.this, MainActivity.class);
                intent.putExtra("EXIT",true);
                startActivity(intent);
                finish();
            }
        });
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

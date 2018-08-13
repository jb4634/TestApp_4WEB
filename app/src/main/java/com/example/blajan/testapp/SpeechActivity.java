package com.example.blajan.testapp;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Locale;

public class SpeechActivity extends AppCompatActivity{

    TextToSpeech tts;
    Button speakButton;
    EditText speakText;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        speakText = (EditText) findViewById(R.id.speech_edit_text);
        speakButton = (Button) findViewById(R.id.speech_button);

        //Spinner for dropdown menu:
        spinner = (Spinner) findViewById(R.id.speech_language_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) { tts.setLanguage(Locale.US);}
            }
        });

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(spinner.getSelectedItemPosition()){
                    case 0: tts.setLanguage(Locale.US);
                        break;
                    case 1: tts.setLanguage(Locale.CANADA_FRENCH);
                        break;
                    case 2: tts.setLanguage(Locale.GERMANY);
                        break;
                    case 3: tts.setLanguage(Locale.ITALY);
                        break;
                }
                String toSpeak = speakText.getText().toString();
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        findViewById(R.id.speech_view).setOnTouchListener(new OnSwipeListener(this) {
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(SpeechActivity.this, MainActivity.class);
                intent.putExtra("EXIT",false);
                startActivity(intent);
                finish();
            }
            @Override
            public void onSwipeDown(){
                Intent intent = new Intent(SpeechActivity.this, MainActivity.class);
                intent.putExtra("EXIT",true);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.speech_edit_text).setOnTouchListener(new OnSwipeListener(this) {
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(SpeechActivity.this, MainActivity.class);
                intent.putExtra("EXIT",false);
                startActivity(intent);
                finish();
            }
            @Override
            public void onSwipeDown(){
                Intent intent = new Intent(SpeechActivity.this, MainActivity.class);
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
        //Do nothing
    }
}

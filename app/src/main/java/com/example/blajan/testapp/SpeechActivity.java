package com.example.blajan.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Locale;

public class SpeechActivity extends AppCompatActivity {

    TextToSpeech tts;
    Button speakButton;
    EditText speakText;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        PreventStatusBar.preventStatusBarExpansion(this);

        speakText = findViewById(R.id.speech_edit_text);
        speakButton = findViewById(R.id.speech_button);

        //Spinner for dropdown menu:
        spinner = findViewById(R.id.speech_language_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Initializes TextToSpeech variable
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        speakButton.setOnClickListener(new View.OnClickListener() {
            //Reads the text from EditText field in selected language
            @Override
            public void onClick(View v) {
                switch (spinner.getSelectedItemPosition()) {
                    case 0:
                        tts.setLanguage(Locale.US);
                        break;
                    case 1:
                        tts.setLanguage(Locale.CANADA_FRENCH);
                        break;
                    case 2:
                        tts.setLanguage(Locale.GERMANY);
                        break;
                    case 3:
                        tts.setLanguage(Locale.ITALY);
                        break;
                }
                String toSpeak = speakText.getText().toString();
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        // Detects gestures on the View component
        findViewById(R.id.speech_view).setOnTouchListener(new OnSwipeListener(this) {
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(SpeechActivity.this, MainActivity.class);
                intent.putExtra("EXIT", false);
                startActivity(intent);
                finish();
            }

            @Override
            public void onSwipeDown() {
                Intent intent = new Intent(SpeechActivity.this, MainActivity.class);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }
        });

        // Detects gestures on the Edit Text component
        findViewById(R.id.speech_edit_text).setOnTouchListener(new OnSwipeListener(this) {
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(SpeechActivity.this, MainActivity.class);
                intent.putExtra("EXIT", false);
                startActivity(intent);
                finish();
            }

            @Override
            public void onSwipeDown() {
                Intent intent = new Intent(SpeechActivity.this, MainActivity.class);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Do nothing
    }
}

package com.example.koktoh.voicerecognizertest;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE = 5910;
    private SpeechRecognizer sr;

    private Button riButton;
    private Button srButton;

    private TextView result;

    private class listener implements RecognitionListener {
        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "Start Speech", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResults(Bundle results) {
            String resultStr = "";

            ArrayList<String> resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < resultList.size(); i++) {
                resultStr += resultList.get(i) + "\n";
            }
            result.setText(resultStr);
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());

        riButton = (Button) findViewById(R.id.riButton);
        srButton = (Button) findViewById(R.id.srButton);

        result = (TextView) findViewById(R.id.textView);

        riButton.setOnClickListener(this);
        srButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riButton:
                try {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "VoiceRecgnizerTest");
                    startActivityForResult(intent, REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "ActivityNotFoundException", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.srButton:
                sr.setRecognitionListener(new listener());

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                sr.startListening(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String resultStr = "";

            ArrayList<String> resultList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (int i = 0; i < resultList.size(); i++) {
                resultStr += resultList.get(i) + "\n";
            }
            result.setText(resultStr);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


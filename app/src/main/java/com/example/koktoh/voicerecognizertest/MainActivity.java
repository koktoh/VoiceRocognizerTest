package com.example.koktoh.voicerecognizertest;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    private final int REQUEST_CODE = 59;
    private SpeechRecognizer sr;
    private MyRecognitionListener listener;
    private Intent intent;

    private Button riButton;
    private Button srButton;

    private TextView result;

    private class MyRecognitionListener implements RecognitionListener {
        @Override
        public void onBeginningOfSpeech() {
            Log.d(TAG, "on beginning of speech");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "on buffer received");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "on end of speech");
        }

        @Override
        public void onError(int error) {
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    Log.e(TAG, "音声データ保存失敗");
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    Log.e(TAG, "Android端末内のエラー(その他)");
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    Log.e(TAG, "権限無し");
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    Log.e(TAG, "ネットワークエラー(その他)");
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    Log.e(TAG, "ネットワークタイムアウトエラー");
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    Log.e(TAG, "音声認識結果無し");
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    Log.e(TAG, "RecognitionServiceへ要求出せず");
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    Log.e(TAG, "Server側からエラー通知");
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    Log.e(TAG, "音声入力無し");
                    break;
                default:
            }
            Log.e(TAG, "Recognizerエラー　リスナ再登録");
            sr.setRecognitionListener(listener);
            sr.startListening(intent);
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "on event");
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "on partial results");
        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "on ready for speech");
        }

        @Override
        public void onResults(Bundle results) {
            Log.d(TAG, "on results");

            String resultStr = "";

            ArrayList<String> resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < resultList.size(); i++) {
                resultStr += resultList.get(i) + "\n";
            }
            result.setText(resultStr);

            sr.setRecognitionListener(listener);
            sr.startListening(intent);
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "VoiceRecognizerTest");

        listener = new MyRecognitionListener();

        if (PermissionChecker.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "パーミッション未許可");
            Log.d(TAG, "パーミッション許可要求");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
        }

        sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        sr.setRecognitionListener(listener);


        riButton = (Button) findViewById(R.id.riButton);
        srButton = (Button) findViewById(R.id.srButton);

        result = (TextView) findViewById(R.id.textView);

        riButton.setOnClickListener(this);
        srButton.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "on stop");
        super.onStop();
        if (sr != null) {
            sr.destroy();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                    sr.setRecognitionListener(listener);
                } else {
                    Toast.makeText(this, "パーミッションを許可してください\nアプリケーションを終了します", Toast.LENGTH_LONG).show();
                    this.finish();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riButton:
                try {
                    startActivityForResult(intent, REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "ActivityNotFoundException", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.srButton:
                try {
                    Log.d(TAG, "Recognizer start");
                    sr.startListening(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Recognizer null");
                }
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


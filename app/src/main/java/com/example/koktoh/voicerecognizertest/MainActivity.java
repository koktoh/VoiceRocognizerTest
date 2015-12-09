package com.example.koktoh.voicerecognizertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button riButton;
    private Button srButton;

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        riButton = (Button)findViewById(R.id.riButton);
        srButton = (Button)findViewById(R.id.srButton);

        result = (TextView)findViewById(R.id.textView);

        riButton.setOnClickListener(this);
        srButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.riButton:
                result.setText("hoge");
                break;
            case R.id.srButton:
                result.setText("hogehoge");
                break;
        }
    }
}


package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Debug;

import com.example.myfirstapp.Collector.AlarmCollector;
import com.example.myfirstapp.util.Logger;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAG = MainActivity.class.getSimpleName();


    // Collector
    private String mDir;
    private static String output_filename;

    private AlarmCollector alarmcollector;

    // Logger


    Button alarm_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.alarm_button = findViewById(R.id.button2);
        this.alarm_button.setOnClickListener(this);

        this.alarm_button = findViewById(R.id.button);
        this.alarm_button.setOnClickListener(this);

        this.mDir = String.valueOf(this.getFilesDir());

        this.alarmcollector = new AlarmCollector(this,this.mDir);

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.button2:
                this.alarmcollector.start();
                break;
            case R.id.button:
                this.alarmcollector.stop();
        }
    }
}
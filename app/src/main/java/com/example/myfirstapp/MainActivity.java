package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myfirstapp.Collector.CollectorManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAG = MainActivity.class.getSimpleName();


    // Collector
    private String mDir;
    private static String output_filename;

    private CollectorManager collectorManager;

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

        this.collectorManager = new CollectorManager(this,this.mDir);

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.button2:
                this.collectorManager.start();
                break;
            case R.id.button:
                this.collectorManager.stop();
        }
    }
}
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

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAG = "Main";


    // Collector
    private static String mDir;
    private static String output_filename;

    Button alarm_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.alarm_button = findViewById(R.id.button2);
        this.alarm_button.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.button2:
                this.mDir = String.valueOf(this.getFilesDir());
                this.output_filename = this.mDir+"/"+"test1.log";
                Log.d(TAG, "onClick: ");

                try {
                    FileOutputStream out1 = new FileOutputStream(output_filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                FileDescriptor fd = new FileDescriptor();
                android.os.Debug.dumpService("Alarm",fd,null);
        }
    }
}
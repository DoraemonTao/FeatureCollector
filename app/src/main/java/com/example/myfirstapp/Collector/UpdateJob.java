package com.example.myfirstapp.Collector;

import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UpdateJob {
    public static String TAG = UpdateJob.class.getSimpleName();


    String tmp_log_path;
    String result_log_path;

    public UpdateJob(String tmp_path,String result_path){
        this.tmp_log_path = tmp_path;
        this.result_log_path = result_path;
    }

    public void updateTmp(){

        Log.d(TAG, "Tmp Log is dump!");

        // dump alarm 需要参数
        String[] args = new String[1];
        args[0] = "--prot";

        // 导出临时log文件路径

        FileOutputStream out1 = null;

        try {
            out1 = new FileOutputStream(this.tmp_log_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileDescriptor fd = null;
        try {
            fd = out1.getFD();
        } catch (IOException e) {
            e.printStackTrace();
        }
        android.os.Debug.dumpService("jobscheduler",fd,null);
    }

    public void updateResult(){
        SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
        String dateStr = dateformat.format(System.currentTimeMillis());
    }
}

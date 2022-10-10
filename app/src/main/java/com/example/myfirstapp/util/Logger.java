package com.example.myfirstapp.util;

import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Logger {
    //最终写入文件
    private String log_path;

    //临时存放文件
    private String tmp_log_path ;

    // 默认文件名
    private String default_log_path = "result.log";
    private String default_tmp_log_path = "tmp.log";
    //当前路径
    private String mDir;

    public static String TAG = Logger.class.getSimpleName();

    public Logger(String dir) {
        this.mDir = dir;
    }

    // 导出log文件
    public void logTmp() throws IOException{
        Log.d(TAG, "Tmp Log is dump!");

        // dump alarm 需要参数
        String[] args = new String[1];
        args[0] = "--prot";

        // 导出临时log文件路径
        tmp_log_path = this.mDir+"/"+this.default_tmp_log_path;
        FileOutputStream out1 = null;

        try {
            out1 = new FileOutputStream(tmp_log_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileDescriptor fd = null;
        try {
            fd = out1.getFD();
        } catch (IOException e) {
            e.printStackTrace();
        }
        android.os.Debug.dumpService("alarm",fd,args);
    }

    // 更新最终文件
    public void updateResult() throws IOException{

    }
}

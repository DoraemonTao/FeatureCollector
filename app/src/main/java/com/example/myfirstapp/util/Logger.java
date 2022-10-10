package com.example.myfirstapp.util;

import android.util.Log;
import android.util.Patterns;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class Logger {
    //最终写入文件
    private String log_path;

    //临时存放文件
    private String tmp_log_path ;

    private String result_log_path;

    // 默认文件名
    private String default_result_log_path = "result.log";
    private String default_tmp_log_path = "tmp.log";
    //当前路径
    private String mDir;

    public static String TAG = Logger.class.getSimpleName();

    public Logger(String dir) {
        this.mDir = dir;
        this.tmp_log_path = this.mDir+"/"+this.default_tmp_log_path;
        this.result_log_path = this.mDir+ "/" + this.default_result_log_path;
    }

    // 导出log文件
    public void logTmp() throws IOException{
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
        android.os.Debug.dumpService("alarm",fd,args);
    }

    // 更新最终文件
    public void updateResult() throws IOException{
        BufferedReader in = new BufferedReader(new FileReader(this.tmp_log_path));
        String line;

        // 匹配pending alarm的正则表达式
        String pendingPattern = ".*pending alarms:.*";

        // 匹配非pending alarm的正则表达式
        String lazyPattern = ".*LazyAlarmStore stats:.*";

        // 匹配alarm信息的正则表达式
        String alarmPattern = ".*[(RTC_WAKEUP)(RTC)(ELAPSED_REALTIME_WAKEUP)(ELAPSED_REALTIME)].*";

        boolean pendingMatch = false;

        while((line = in.readLine())!= null){
            if(pendingMatch){
                if(line)

                // 当匹配完pending alarm的信息后，重置
                if(Pattern.matches(lazyPattern,line)){
                    pendingMatch = false;
                }
            }
            else{
                pendingMatch = Pattern.matches(pendingPattern,line);
            }

        }

    }
}

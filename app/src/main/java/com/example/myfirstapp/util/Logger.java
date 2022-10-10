package com.example.myfirstapp.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Logger {

    // Log下所有的alarm ID
    private ArrayList<String> alarmIdList = new ArrayList<>();

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
        BufferedWriter out = new BufferedWriter(new FileWriter(this.result_log_path));

        String line;

        // 匹配pending alarm的正则表达式
        String pendingPattern = ".*pending alarms:.*";

        // 匹配非pending alarm的正则表达式
        String lazyPattern = ".*LazyAlarmStore stats:.*";

        // 匹配alarm信息的正则表达式
        String alarmPattern = ".*[(RTC_WAKEUP)(RTC)(ELAPSED_REALTIME_WAKEUP)(ELAPSED_REALTIME)].*";

        // 匹配alarm唯一标识符的正则表达式
        String alarmIdRegex = "Alarm\\{(\\w+\\s)";

        boolean pendingMatch = false;
        boolean AlarmFlag = false;
        boolean writeFlag = false;

        Pattern alarmIdPattern = Pattern.compile(alarmIdRegex);
        Matcher alarmIdMatch;

        // alarm唯一标识符
        String alarmId;

        while((line = in.readLine())!= null){
            // 匹配pending段
            pendingMatch = Pattern.matches(pendingPattern,line);
            if(pendingMatch){
                AlarmFlag = Pattern.matches(alarmPattern,line);
                // 匹配到新alarm时
                if(AlarmFlag){
                    alarmIdMatch = alarmIdPattern.matcher(line);
                    alarmId = alarmIdMatch.group(0);
                    // 当发现是从未发现的alarmId时
                    if(!alarmIdList.contains(alarmId)){
                        writeFlag = true;
                        alarmIdList.add(alarmId);
                    }
                    else {
                        writeFlag = false;
                    }
                }
                if (writeFlag){
                    out.write(line);
                }
            }

            // 当匹配完pending alarm的信息后，跳出
            if(Pattern.matches(lazyPattern,line)){
                break;
            }
        }

    }
}

package com.example.myfirstapp.Collector;

import android.util.Log;

import com.example.myfirstapp.util.TimeConvert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.PasswordAuthentication;
import java.sql.Time;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateAlarm {

    public static String TAG = UpdateAlarm.class.getSimpleName();

    // Log下所有的alarm ID
    private ArrayList<String> alarmIdList = new ArrayList<>();

    String tmp_log_path;
    String result_log_path;


    public UpdateAlarm(String tmp_path, String result_path){
        this.tmp_log_path = tmp_path;
        this.result_log_path = result_path;
    }

    public void updateTmp() throws IOException {
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

    public void updateResult() throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(this.tmp_log_path));
        BufferedWriter out = new BufferedWriter(new FileWriter(this.result_log_path,true));

        String line;

        // TimeConv
        TimeConvert timeConvert = new TimeConvert();

        // 匹配pending alarm的正则表达式
        String pendingPattern = ".*pending alarms:.*";

        // 匹配非pending alarm的正则表达式
        String lazyPattern = ".*LazyAlarmStore stats:.*";

        // 匹配alarm信息的正则表达式
        String alarmPattern = ".*[(RTC_WAKEUP)(RTC)(ELAPSED_REALTIME_WAKEUP)(ELAPSED_REALTIME)].*";

        // 匹配alarm唯一标识符的正则表达式
        String alarmIdRegex = "Alarm\\{(\\w+)\\s";

        Pattern alarmIdPattern = Pattern.compile(alarmIdRegex);
        Matcher alarmIdMatch;

        // alarm唯一标识符
        String alarmId;

        // elapsedTime有关的时间
        long nowRTC = 0;
        long nowElpased = 0;
        long elpased = 0;

        Pattern nowRTCPattern = Pattern.compile("nowRTC=(\\d+)");
        Pattern nowElapsedPattern = Pattern.compile("nowELAPSED=(\\d+)");
        Pattern elapsedPattern = Pattern.compile(" (\\d+) ");

        Matcher nowRTCMatch;
        Matcher nowElapsedMatch;
        Matcher elapsedMatch;

        boolean pendingFlag = false;
        boolean AlarmFlag = false;
        boolean writeFlag = false;
        boolean nowRTCFlag = false;
        boolean nowElapsedFlag = false;

        String dateTime;



        while((line = in.readLine())!= null){
            // 匹配nowRTC
            if (!nowRTCFlag){
                nowRTCMatch = nowRTCPattern.matcher(line);
                if (nowRTCMatch.find()){
                    nowRTC = Long.valueOf(nowRTCMatch.group(1)).longValue();
                    nowRTCFlag = true;
                }
            }

            // 匹配nowElapsed
            if (!nowElapsedFlag){
                nowElapsedMatch = nowElapsedPattern.matcher(line);
                if(nowElapsedMatch.find()){
                    nowElpased = Long.valueOf(nowElapsedMatch.group(1)).longValue();
                    nowElapsedFlag = true;
                }
            }


            // 匹配pending段
            if(!pendingFlag)
                pendingFlag = Pattern.matches(pendingPattern,line);

            // 当匹配完pending alarm的信息后，跳出
            if(Pattern.matches(lazyPattern,line)){
                break;
            }




            if(pendingFlag){
                AlarmFlag = Pattern.matches(alarmPattern,line);
                // 匹配到新alarm时
                if(AlarmFlag){
                    alarmIdMatch = alarmIdPattern.matcher(line);
                    if(alarmIdMatch.find())
                    {
                        alarmId = alarmIdMatch.group(1);
                        // 当发现是从未发现的alarmId时
                        if(!alarmIdList.contains(alarmId)) {
                            writeFlag = true;
                            alarmIdList.add(alarmId);
                        }
                        else {
                            writeFlag = false;
                        }
                    }
                }
                if (writeFlag){
                    // 匹配elapsed

                    elapsedMatch = elapsedPattern.matcher(line);
                    while(elapsedMatch.find()){
                        elpased = Long.valueOf(elapsedMatch.group(1)).longValue();
                        dateTime = timeConvert.elapsedToDate(nowRTC,nowElpased,elpased);
                        line = line.replace(elapsedMatch.group(1),dateTime);
                    }


                    out.write(line);
                    out.newLine();
                    out.flush();
                }
            }
        }
        out.close();
    }
}

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    

    public void updateResult(long currentTime) throws IOException{

        // TimeConvert util
        TimeConvert timeConvert = new TimeConvert();

        // 读取文件
        BufferedReader in = new BufferedReader(new FileReader(this.tmp_log_path));
        BufferedWriter out = new BufferedWriter(new FileWriter(this.result_log_path,true));

        // date
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = dateformat.format(System.currentTimeMillis());


        String line;

        // Current elapsed time:的正则匹配项
        Pattern elapsedTimePattern = Pattern.compile("Current elapsed time: (\\d+)");
        Matcher elapsedTimeMatch;
        String elapsedTime = null;

        // Recently completed jobs:的正则匹配项
        String completedJobRegex = "Recently completed jobs:";
        Boolean completedJobFlag = false;

        // RelaTime:的正则匹配项
        Pattern jobTimePattern = Pattern.compile("((\\+|-)\\d*d?\\d*h?\\d*m?\\d*s?\\d+ms)");
        Matcher jobTimeMatch;

        // Time的定义
        String jobRelaTime;
        String jobAbsoTime = null;
        String relaTime;

        long time;
        String sign;

        // JobInfo
        Boolean jobInfoFlag = false;




        while((line = in.readLine())!= null){
            // 是否到达Recently completed jobs段
            if(!completedJobFlag)
                completedJobFlag = Pattern.matches(completedJobRegex,line);

            // 获得相对时间
            if(completedJobFlag){

                // TODO: Skip the JobInfo
                if(!jobInfoFlag)
                    jobInfoFlag = Pattern.matches("\\s+JobInfo:",line);
                else
                    jobInfoFlag = !Pattern.matches("\\s+Required constraints.*",line);


                // 匹配到新的job
                jobTimeMatch = jobTimePattern.matcher(line);
                // 使用while来获得所有相对时间，因为一段中含有多个
                while(jobTimeMatch.find()){

                    // 当line为jobInfo段时，跳过
                    if(jobInfoFlag)
                        break;

                    jobRelaTime = jobTimeMatch.group(1);

                    // 将相对时间转换为ElapsedTime类型
                    sign = jobRelaTime.substring(0,1);
                    relaTime = jobRelaTime.substring(1);
                    time = timeConvert.relaToAbso(relaTime);

                    // 相减后转换成日期格式
                    time = sign.equals("+") ? currentTime + time : currentTime - time;
                    jobAbsoTime = dateformat.format(time);

                    // 将原来的相对时间转换成为绝对时间
                    line = line.replace(jobRelaTime,jobAbsoTime);
                }

                // 将结果写入文件中
                out.write(line);
                out.newLine();
                out.flush();
            }
        }
        out.close();
    }
}

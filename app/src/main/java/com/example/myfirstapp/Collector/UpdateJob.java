package com.example.myfirstapp.Collector;

import android.util.Log;

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

    public void updateResult() throws IOException {


        BufferedReader in = new BufferedReader(new FileReader(this.tmp_log_path));
        BufferedWriter out = new BufferedWriter(new FileWriter(this.result_log_path,true));

        String line;

        // TODO: 将QuotaTracker的Current elapsed time 作为当前时间。将Recently completed jobs中的时间转换成当前时间

        // 提取Current elapsed time



        // Current elapsed time:的正则匹配项
        Pattern elapsedTimePattern = Pattern.compile("Current elapsed time://s+(//d+)");
        Matcher elapsedTimeMatch;

        // 匹配Recently completed jobs的正则表达式
        String jobsRegex = "Recently completed jobs:";





    }

    public void relaToAbsolute() throws IOException{

        // 读取文件
        BufferedReader in = new BufferedReader(new FileReader(this.tmp_log_path));
        BufferedWriter out = new BufferedWriter(new FileWriter(this.result_log_path,true));

        // date
        SimpleDateFormat relaTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
        String dateStr = dateformat.format(System.currentTimeMillis());


        String line;

        // Current elapsed time:的正则匹配项
        Pattern elapsedTimePattern = Pattern.compile("Current elapsed time://s+(//d+)");
        Matcher elapsedTimeMatch;
        String elapsedTime = null;

        // Recently completed jobs:的正则匹配项
        String completedJobRegex = "Recently completed jobs:";
        Boolean completedJobFlag = false;

        // job的相对时间
        Pattern jobTimePattern = Pattern.compile("(\\+?-?\\d*m?\\d*s?\\d*ms)");
        Matcher jobTimeMatch;
        String jobRelaTime;
        String jobAbsoTime = null;
        long time;

        String sign;


        // job的执行时间



        while((line = in.readLine())!= null){
            // TODO:找到第二个ElapsedTime类型
            elapsedTimeMatch = elapsedTimePattern.matcher(line);
            if(elapsedTimeMatch.find()){
                // 获得对应的时间，可能会重置
                elapsedTime = elapsedTimeMatch.group(1);

            }

            // 是否到达Recently completed jobs段
            if(!completedJobFlag)
                completedJobFlag = Pattern.matches(completedJobRegex,line);

            // 获得相对时间
            if(completedJobFlag){
                //匹配到新的job
                jobTimeMatch = jobTimePattern.matcher(line);
                if(jobTimeMatch.find()){
                    jobRelaTime = jobTimeMatch.group(1);
                    // 将相对时间转换为ElapsedTime类型
                    try {
                        sign = jobRelaTime.substring(0,1);
                        jobRelaTime = jobRelaTime.substring(1);
                        time = relaTimeFormat.parse(jobRelaTime).getTime();

                        // 相减后转换成日期格式
                        time = sign.equals("+") ? Integer.valueOf(elapsedTime).intValue() + time : Integer.valueOf(elapsedTime).intValue() - time;
                        jobAbsoTime = dateformat.format(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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

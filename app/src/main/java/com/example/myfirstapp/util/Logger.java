package com.example.myfirstapp.util;

import com.example.myfirstapp.Collector.UpdateAlarm;
import com.example.myfirstapp.Collector.UpdateJob;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Logger {

    // 收集器
    UpdateAlarm updateAlarm;
    UpdateJob updateJob;

    FileMerge fileMerge;

    //最终写入文件
    private String log_path;

    //临时存放文件
    private String alarm_tmp_log_path;
    private String job_tmp_log_path;
    private String result_log_path;
    private String alarm_result_log_path;
    private String job_result_log_path;

    // 默认文件名
    private String default_result_log_path = "result.log";
    private String default_alarm_result_log_path="alarmResult.log";
    private String default_job_result_log_path="jobResult.log";
    private String default_tmp_log_path = "tmp.log";

    //当前路径
    private String mDir;

    public static String TAG = Logger.class.getSimpleName();

    public Logger(String dir) {
        this.mDir = dir;
        this.alarm_tmp_log_path = this.mDir+"/"+"alarm_"+this.default_tmp_log_path;
        this.job_tmp_log_path = this.mDir+"/"+"job_"+this.default_tmp_log_path;
        this.result_log_path = this.mDir+ "/" + this.default_result_log_path;
        this.alarm_result_log_path = this.mDir+ "/" +this.default_alarm_result_log_path;
        this.job_result_log_path = this.mDir + "/" + this.default_job_result_log_path;

        // alarmUpdate
         updateAlarm = new UpdateAlarm(this.alarm_tmp_log_path,this.alarm_result_log_path);

         updateJob = new UpdateJob(this.job_tmp_log_path,this.job_result_log_path);

         fileMerge = new FileMerge();
        // 若存在result文件，则删除，防止在上一次的基础上继续生成
        File result_file = new File(this.result_log_path);
        if(result_file.exists())
            result_file.delete();

        // TODO: 在result文件创建alarm段
        // 在alarm和job的临时文件夹中预先写入信息
        try {
            BufferedWriter alarm_out = new BufferedWriter(new FileWriter(this.alarm_result_log_path));
            BufferedWriter job_out = new BufferedWriter(new FileWriter(this.job_result_log_path));
            alarm_out.write("Alarm History:");
            alarm_out.newLine();
            job_out.write("Job History:");
            job_out.newLine();

            alarm_out.flush();
            alarm_out.close();
            job_out.flush();
            job_out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 导出log文件
    public void logTmp() throws IOException{
        updateAlarm.updateTmp();
        updateJob.updateTmp();

    }

    // 更新最终文件
    public void updateResult() throws IOException{
        updateAlarm.updateResult();
        // 获得当前时间供job使用，为了区分不同的job信息
        // 先
        long rtcTime = System.currentTimeMillis();
        updateJob.updateResult(rtcTime);

        ArrayList<String> fpath = new ArrayList<>();
        fpath.add(job_result_log_path);
        fpath.add(alarm_result_log_path);

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMddHHmmssSSS");
        //   获取当前时间并作为时间戳命名文件
        String timeStamp=simpleDateFormat.format(new Date());
//        this.result_log_path = this.mDir + "/" + "result_" + timeStamp + ".log";
        this.result_log_path = this.mDir + "/" + "result.log";
        // 合并alarm与job文件
        fileMerge.mergeFiles(fpath,this.result_log_path);
    }

}

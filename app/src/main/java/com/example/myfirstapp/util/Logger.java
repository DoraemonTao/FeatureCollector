package com.example.myfirstapp.util;

import com.example.myfirstapp.Collector.UpdateAlarm;
import com.example.myfirstapp.Collector.UpdateJob;

import java.io.File;
import java.io.IOException;

public class Logger {

    // 收集器
    UpdateAlarm updateAlarm;
    UpdateJob updateJob;

    //最终写入文件
    private String log_path;

    //临时存放文件
    private String alarm_tmp_log_path;
    private String job_tmp_log_path;
    private String result_log_path;

    // 默认文件名
    private String default_result_log_path = "result.log";
    private String default_tmp_log_path = "tmp.log";

    //当前路径
    private String mDir;

    public static String TAG = Logger.class.getSimpleName();

    public Logger(String dir) {
        this.mDir = dir;
        this.alarm_tmp_log_path = this.mDir+"/"+"alarm_"+this.default_tmp_log_path;
        this.job_tmp_log_path = this.mDir+"/"+"job_"+this.default_tmp_log_path;
        this.result_log_path = this.mDir+ "/" + this.default_result_log_path;

        // alarmUpdate
         updateAlarm = new UpdateAlarm(this.alarm_tmp_log_path,this.result_log_path);

         updateJob = new UpdateJob(this.job_tmp_log_path,this.result_log_path);
        // 若存在result文件，则删除，防止在上一次的基础上继续生成
        File result_file = new File(this.result_log_path);
        if(result_file.exists())
            result_file.delete();
    }

    // 导出log文件
    public void logTmp() throws IOException{
        updateAlarm.updateTmp();
        updateJob.updateTmp();
    }

    // 更新最终文件
    public void updateResult() throws IOException{
        updateAlarm.updateResult();
    }
}

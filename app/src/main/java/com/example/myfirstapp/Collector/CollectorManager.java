package com.example.myfirstapp.Collector;

import android.content.Context;
import android.util.Log;

import com.example.myfirstapp.util.Logger;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class CollectorManager implements DataCollector {

    private static String TAG = CollectorManager.class.getSimpleName();

    // 刷新间隔，暂定为1s
    private static final long DEFAULT_REFRESH_INTERVAL = 1000;

    // job采集时间
    private static final long JOB_DEFAULT_REFRESH_INTERVAL = 2*60*60*1000;

    private long refresh_interval ;

    // 定时作业配置
    private Timer mTimer;
    private TT mTimerTask;

    // Logger
    private Logger logger;



    public CollectorManager(Context context,String dir){
        this.refresh_interval = DEFAULT_REFRESH_INTERVAL;
        this.logger = new Logger(String.valueOf(dir));

        this.mTimer = new Timer();
        this.mTimerTask = new TT();
    }

    @Override
    public void start() {
        Log.e(TAG,"Start!");
        this.mTimer.schedule(this.mTimerTask,
                this.refresh_interval,
                this.refresh_interval);
    }

    @Override
    public void stop() {
        Log.e(TAG,"Stop!");
        this.mTimer.cancel();
    }

    // 重写运行方法
    private class TT extends TimerTask{
        @Override
        public void run() {
            try {
                long currentTime = System.currentTimeMillis();
                CollectorManager.this.logger.logTmp();
                CollectorManager.this.logger.updateResult(currentTime);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

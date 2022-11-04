package com.example.myfirstapp.util;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 针对
public class TimeConvert {
    public TimeConvert(){

    }

    // 相对时间转化成绝对时间，毫秒级
    public long relaToAbso(String relaTime){
        // relaTime Format： [**d][**h][**s][**ms]
        // AbsoluteTime Format: *******
        // relaTime :Android导出的设置

        long absoTime=0;

        // DateMatcher
        Pattern dayTimePattern = Pattern.compile("(\\d+)d");
        Matcher dayTimeMatch;


        // HourMatcher
        Pattern hourTimePattern = Pattern.compile("(\\d+)h");
        Matcher hourTimeMatch;

        // MinMatcher
        Pattern minTimePattern = Pattern.compile("(\\d+)m");
        Matcher minTimeMatch;

        // SecondMatcher
        Pattern secTimePattern = Pattern.compile("(\\d+)s");
        Matcher secTimeMatch;

        // MillMatcher
        Pattern millTimePattern = Pattern.compile("(\\d+)ms");
        Matcher millTimeMatch;


        dayTimeMatch = dayTimePattern.matcher(relaTime);
        hourTimeMatch = hourTimePattern.matcher(relaTime);
        minTimeMatch = minTimePattern.matcher(relaTime);
        secTimeMatch = secTimePattern.matcher(relaTime);
        millTimeMatch = millTimePattern.matcher(relaTime);

        // calculate milli
        if (dayTimeMatch.find())
            absoTime += Integer.valueOf(dayTimeMatch.group(1)).intValue() * 24 *60 * 60 * 1000;
        if (hourTimeMatch.find())
            absoTime += Integer.valueOf(hourTimeMatch.group(1)).intValue() * 60 * 60 * 1000;
        if (minTimeMatch.find())
            absoTime += Integer.valueOf(minTimeMatch.group(1)).intValue() * 60 * 1000;
        if (secTimeMatch.find())
            absoTime += Integer.valueOf(secTimeMatch.group(1)).intValue()  * 1000;
        if (millTimeMatch.find())
            absoTime += Integer.valueOf(millTimeMatch.group(1)).intValue();


        return absoTime;

    }

    // 将elpased类型转换成date类型
    public String elapsedToDate(long nowRTC,long nowElpased,long elapsedTime){
        // date
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = dateformat.format(nowRTC-nowElpased+elapsedTime);

        return dateStr;
    }

    // 将elapsed类型转换成RTC类型
    public String elapsedToRTC(long nowRTC,long nowElpased,long elapsedTime){
        String date = String.valueOf(nowRTC-nowElpased+elapsedTime);
        return date;
    }

    // 将RTC类型转换成date类型
    public String rtcToDate(long rtcTime) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = dateformat.format(rtcTime);

        return dateStr;
    }


}

package com.example.myfirstapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 针对
public class TimeConvert {
    public TimeConvert(){

    }

    // 相对时间转化成绝对时间，毫秒级
    public int relaToAbso(String relaTime){
        // relaTime Format： [**d][**h][**s][**ms]
        // AbsoluteTime Format: *******
        // relaTime :Android导出的设置

        int absoTime=0;

        //DateMatcher
        Pattern dayTimePattern = Pattern.compile("(\\d+)d");
        Matcher dayTimeMatch;


        //HourMatcher
        Pattern hourTimePattern = Pattern.compile("(\\d+)h");
        Matcher hourTimeMatch;

        //SecondMatcher
        Pattern secTimePattern = Pattern.compile("(\\d+)s");
        Matcher secTimeMatch;

        //MillMatcher
        Pattern millTimePattern = Pattern.compile("(\\d+)ms");
        Matcher millTimeMatch;


        dayTimeMatch = dayTimePattern.matcher(relaTime);
        hourTimeMatch = hourTimePattern.matcher(relaTime);
        secTimeMatch = secTimePattern.matcher(relaTime);
        millTimeMatch = millTimePattern.matcher(relaTime);

        // calculate milli
        if (dayTimeMatch.find())
            absoTime += Integer.valueOf(dayTimeMatch.group(1)).intValue() * 24 * 60 * 1000;
        if (hourTimeMatch.find())
            absoTime += Integer.valueOf(hourTimeMatch.group(1)).intValue()  * 60 * 1000;
        if (secTimeMatch.find())
            absoTime += Integer.valueOf(secTimeMatch.group(1)).intValue()  * 1000;
        if (millTimeMatch.find())
            absoTime += Integer.valueOf(millTimeMatch.group(1)).intValue() ;

        return absoTime;

    }
}

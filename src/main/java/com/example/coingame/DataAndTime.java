package com.example.coingame;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataAndTime {

    public static String getFormattedDateAndTimeNow(){
        LocalDateTime myDateObj = getLocalDateAndTimeNow();
        DateTimeFormatter myFormatObj = getFormatDateAndTimeWithPattern("dd-MM-yyyy HH:mm:ss");
        return getDateAndTimeFormatToString(myDateObj, myFormatObj);
    }

    public static String getTimeNow(){
        LocalDateTime myDateObj = getLocalDateAndTimeNow();
        DateTimeFormatter myFormatObj = getFormatDateAndTimeWithPattern("HH:mm:ss");
        return getDateAndTimeFormatToString(myDateObj, myFormatObj);
    }

    public static String getDateNow(){
        LocalDateTime myDateObj = getLocalDateAndTimeNow();
        DateTimeFormatter myFormatObj = getFormatDateAndTimeWithPattern("dd-MM-yyyy");
        return getDateAndTimeFormatToString(myDateObj, myFormatObj);
    }

    private static String getDateAndTimeFormatToString(LocalDateTime myDateObj, DateTimeFormatter myFormatObj){
        return myDateObj.format(myFormatObj);
    }

    private static LocalDateTime getLocalDateAndTimeNow(){
        return LocalDateTime.now();
    }

    private static DateTimeFormatter getFormatDateAndTimeWithPattern(String pattern){
        return DateTimeFormatter.ofPattern(pattern);
    }

    public static long getEpochSeconds(){
        return java.time.Instant.now().getEpochSecond();
    }

    public static long getEpochMilliSeconds(){
        return java.time.Instant.now().toEpochMilli();
    }
}

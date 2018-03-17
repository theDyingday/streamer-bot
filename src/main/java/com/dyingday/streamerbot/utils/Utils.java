package com.dyingday.streamerbot.utils;

public class Utils
{
    public static String getStringFromArray(String[] strings)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(String string : strings) stringBuilder.append(string).append(" ");
        return stringBuilder.toString();
    }

    public static String getTimeStamp(long videoLength)
    {
        videoLength /= 1000;
        return String.format("[%02d:%02d]", videoLength / 60, videoLength % 60);
    }
}

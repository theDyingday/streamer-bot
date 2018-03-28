package com.dyingday.streamerbot.utils;

public class Utils
{
    public static String getStringFromArray(String[] strings)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(String string : strings) stringBuilder.append(string).append(" ");
        return stringBuilder.toString();
    }

    public static String getStringFromArray(String[] strings, int startPos, int endPos)
    {
        String[] arr = new String[endPos - startPos];
        for(int i = 0; i < strings.length; i++)
        {
            if(i >= startPos && i < endPos)
                arr[i - startPos] = strings[i];
        }
        return getStringFromArray(arr);
    }

    public static String getTimeStamp(long videoLength)
    {
        videoLength /= 1000;
        return String.format("[%02d:%02d]", videoLength / 60, videoLength % 60);
    }
}

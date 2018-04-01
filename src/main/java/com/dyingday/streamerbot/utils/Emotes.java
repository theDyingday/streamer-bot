package com.dyingday.streamerbot.utils;

import java.util.HashMap;
import java.util.Map;

public class Emotes
{
    private static Map<Integer, String> numbers = new HashMap<>();

    static {
        numbers.put(0, "zero");
        numbers.put(1, "one");
        numbers.put(2, "two");
        numbers.put(3, "three");
        numbers.put(4, "four");
        numbers.put(5, "five");
        numbers.put(6, "six");
        numbers.put(7, "seven");
        numbers.put(8, "eight");
        numbers.put(9, "nine");
    }

    public static String getNumberEmote(int number)
    {
        if(numbers.containsKey(number)) return numbers.get(number);
        return "";
    }

    public static int getNumber(String number)
    {
        for(int check : numbers.keySet())
            if(numbers.get(check).equalsIgnoreCase(number)) return check;
        return -1;
    }
}

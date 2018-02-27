package com.dyingday.utils;

import com.dyingday.objects.Server;
import net.dv8tion.jda.core.JDA;

import java.util.ArrayList;
import java.util.HashMap;

public class Reference
{
    public static Reference reference;
    public static Reference getReference()
    {
        if(reference == null) reference = new Reference();

        return reference;
    }

    public HashMap<Long, Server> servers = new HashMap<>();

    public JDA jda;

    // Add Link: https://discordapp.com/api/oauth2/authorize?client_id=418012734509285376&permissions=1559456870&scope=bot
}

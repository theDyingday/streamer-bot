package com.dyingday.utils;

import com.dyingday.commands.BaseCommand;
import com.dyingday.commands.CommandMap;
import com.dyingday.objects.Server;
import net.dv8tion.jda.core.JDA;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Reference
{
    public static Reference reference;
    public static Reference getReference()
    {
        if(reference == null) reference = new Reference();

        return reference;
    }

    // Discord Embed Colours
    public final Color INFO_COLOUR = Color.WHITE;

    public HashMap<Long, Server> servers = new HashMap<>();
    public Map<String, BaseCommand> commands = new HashMap<>();

    public JDA jda;
    public CommandMap commandMap;

    public Collection<BaseCommand> getCommands()
    {
        return commands.values();
    }

    // Add Link: https://discordapp.com/api/oauth2/authorize?client_id=418012734509285376&permissions=1559456870&scope=bot
}

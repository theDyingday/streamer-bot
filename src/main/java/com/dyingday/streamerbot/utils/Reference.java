package com.dyingday.streamerbot.utils;

import com.dyingday.streamerbot.commands.BaseCommand;
import com.dyingday.streamerbot.commands.CommandMap;
import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.twitch.TwitchChannel;
import com.dyingday.streamerbot.twitch.TwitchHandler;
import net.dv8tion.jda.core.JDA;

import java.awt.*;
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

    public final int TWITCH_PORT = 6667;
    public final String TWITCH_OAUTH = "";
    public final String DISCORD_TOKEN = "";

    public Map<Long, DiscordGuild> discordGuilds = new HashMap<>();
    public Map<String, BaseCommand> commands = new HashMap<>();
    public Map<TwitchChannel, DiscordGuild> twitchConnections = new HashMap<>();


    public JDA jda;
    public TwitchHandler twitch;
    public CommandMap commandMap;

    public Collection<BaseCommand> getCommands()
    {
        return commands.values();
    }

    // Add Link: https://discordapp.com/api/oauth2/authorize?client_id=418012734509285376&permissions=1559456870&scope=bot
}

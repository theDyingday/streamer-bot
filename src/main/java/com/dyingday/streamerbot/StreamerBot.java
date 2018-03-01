package com.dyingday.streamerbot;

import com.dyingday.streamerbot.commands.CommandMap;
import com.dyingday.streamerbot.discord.DiscordMessageListener;
import com.dyingday.streamerbot.discord.DisordEventListener;
import com.dyingday.streamerbot.twitch.TwitchHandler;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class StreamerBot
{
    private Reference reference = Reference.getReference();

    public StreamerBot() throws LoginException, InterruptedException
    {
        reference.jda = new JDABuilder(AccountType.BOT).setToken(reference.DISCORD_TOKEN).buildBlocking();
        reference.jda.addEventListener(new DisordEventListener());
        reference.jda.addEventListener(new DiscordMessageListener());

        reference.commandMap = new CommandMap();

        new TwitchHandler();
    }

    public static void main(String[] args)
    {

        try
        {
            new StreamerBot();
        }
        catch (LoginException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}

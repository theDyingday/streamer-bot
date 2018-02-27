package com.dyingday.streamerbot;

import com.dyingday.streamerbot.commands.CommandMap;
import com.dyingday.streamerbot.discord.BotEventListener;
import com.dyingday.streamerbot.discord.MessageListener;
import com.dyingday.streamerbot.twitch.TwitchHandler;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import org.jibble.pircbot.IrcException;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class StreamerBot
{
    private Reference reference = Reference.getReference();

    public StreamerBot() throws LoginException, InterruptedException, IOException, IrcException
    {
        reference.jda = new JDABuilder(AccountType.BOT).setToken(reference.DISCORD_TOKEN).buildBlocking();
        reference.jda.addEventListener(new BotEventListener());
        reference.jda.addEventListener(new MessageListener());

        reference.commandMap = new CommandMap();

        reference.twitch = new TwitchHandler();
        reference.twitch.init_twitch();
    }

    public static void main(String[] args)
    {
        try
        {
            new StreamerBot();
        }
        catch (LoginException | InterruptedException | IOException | IrcException e)
        {
            e.printStackTrace();
        }
    }
}

package com.dyingday;

import com.dyingday.commands.CommandMap;
import com.dyingday.listeners.BotEventListener;
import com.dyingday.listeners.MessageListener;
import com.dyingday.utils.Reference;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class StreamerBot
{
    private Reference reference = Reference.getReference();

    public StreamerBot() throws LoginException, InterruptedException
    {
        reference.jda = new JDABuilder(AccountType.BOT).setToken("token").buildBlocking();
        reference.jda.addEventListener(new BotEventListener());
        reference.jda.addEventListener(new MessageListener());

        reference.commandMap = new CommandMap();
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

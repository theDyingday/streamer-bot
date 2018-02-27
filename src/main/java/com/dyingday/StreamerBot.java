package com.dyingday;

import com.dyingday.listeners.BotEventListener;
import com.dyingday.utils.Reference;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class StreamerBot
{
    private Reference reference = Reference.getReference();

    public StreamerBot() throws LoginException, InterruptedException
    {
        reference.jda = new JDABuilder(AccountType.BOT).setToken("NDE4MDEyNzM0NTA5Mjg1Mzc2.DXbZoA.2AhIM_1I7LmG4rVxmINuYmTlwQ8").buildBlocking();
        reference.jda.addEventListener(new BotEventListener());
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

package com.dyingday.listeners;

import com.dyingday.objects.Server;
import com.dyingday.utils.Reference;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter
{
    private Reference reference = Reference.getReference();

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot()) return;
        System.out.println(reference.servers.size());
        if(reference.servers.containsKey(event.getGuild().getIdLong()))
        {
            Server server = reference.servers.get(event.getGuild().getIdLong());

            if(!event.getMessage().getContentRaw().startsWith(server.getCommandChar())) return;

            String[] splitString = event.getMessage().getContentRaw().split("");

            reference.commandMap.command(event.getAuthor(), event.getMessage(), event);
        }
    }
}

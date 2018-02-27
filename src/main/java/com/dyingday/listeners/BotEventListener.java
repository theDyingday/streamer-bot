package com.dyingday.listeners;

import com.dyingday.objects.Server;
import com.dyingday.utils.Reference;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class BotEventListener implements EventListener
{
    private Reference reference = Reference.getReference();

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof GuildJoinEvent)
        {
            new Server(((GuildJoinEvent) event).getGuild().getIdLong());
        }
        else if(event instanceof GuildUpdateOwnerEvent)
        {
            reference.servers.get(((GuildUpdateOwnerEvent) event).getGuild().getIdLong()).setOwner(((GuildUpdateOwnerEvent) event).getGuild().getOwner());
        }
    }
}

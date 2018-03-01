package com.dyingday.streamerbot.discord;

import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class DisordEventListener implements EventListener
{
    private Reference reference = Reference.getReference();

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof GuildJoinEvent)
        {
            new DiscordGuild(((GuildJoinEvent) event).getGuild().getIdLong());
        }
        else if(event instanceof GuildUpdateOwnerEvent)
        {
            reference.discordGuilds.get(((GuildUpdateOwnerEvent) event).getGuild().getIdLong()).setOwner(((GuildUpdateOwnerEvent) event).getGuild().getOwner());
        }
    }
}

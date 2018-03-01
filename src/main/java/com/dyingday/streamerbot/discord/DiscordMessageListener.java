package com.dyingday.streamerbot.discord;

import com.dyingday.streamerbot.twitch.TwitchChannel;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordMessageListener extends ListenerAdapter
{
    private Reference reference = Reference.getReference();

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot()) return;
        if(reference.discordGuilds.containsKey(event.getGuild().getIdLong()))
        {
            DiscordGuild discordGuild = reference.discordGuilds.get(event.getGuild().getIdLong());

            if(!event.getMessage().getContentRaw().startsWith(discordGuild.getCommandChar()))
            {
                for(TwitchChannel channel : discordGuild.getConnectedTwitchChannels())
                {
                    if(channel.isStreaming() && channel.getDiscordChannel().equals(event.getTextChannel()))
                    {
                        reference.twitch.sendMessage(channel.getChannel(), event.getMember().getNickname() + ": " + event.getMessage());
                    }
                }
                return;
            }

            reference.commandMap.discordCommand(event, discordGuild.getCommandChar());
        }
    }
}

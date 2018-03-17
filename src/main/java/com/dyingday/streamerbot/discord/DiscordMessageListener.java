package com.dyingday.streamerbot.discord;

import com.dyingday.streamerbot.twitch.TwitchChannel;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordMessageListener extends ListenerAdapter
{
    private Reference reference = Reference.getReference();

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot()) return;
        if(event.getChannel() instanceof PrivateChannel && event.getAuthor().getId().equalsIgnoreCase("264777067411931137") && event.getMessage().getContentRaw().equalsIgnoreCase("/stop"))
        {
            reference.jda.shutdown();
            System.exit(0);
        }
        if(event.getChannel() instanceof PrivateChannel || reference.discordGuilds.containsKey(event.getGuild().getIdLong()))
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
            if(event.getMessage().getContentRaw().startsWith(discordGuild.getCommandChar())) reference.commandMap.discordCommand(event, discordGuild.getCommandChar());
        }
    }
}

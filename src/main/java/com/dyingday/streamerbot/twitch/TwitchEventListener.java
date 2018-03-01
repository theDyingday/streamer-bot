package com.dyingday.streamerbot.twitch;

public class TwitchEventListener extends TwitchHandler
{
    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message)
    {
        TwitchChannel twitchChannel = reference.twitch.getChannel(channel);
        if(twitchChannel == null) /*TODO: Shouldn't happen but fix this up in case it goes wrong*/ return;

        if(!message.startsWith(twitchChannel.getCommandChar()))
        {
            twitchChannel.getDiscordChannel().sendMessage(sender + ": " + message).queue();
            return;
        }
        // Only relay messages if the channel is streaming
        if(twitchChannel.isStreaming()) reference.commandMap.twitchCommand(twitchChannel, message, sender);
    }
}

package com.dyingday.streamerbot.twitch;

public class EventListener extends TwitchHandler
{
    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message)
    {
        TwitchChannel twitchChannel = getChannel(channel);
        if(twitchChannel == null) /*TODO: Shouldn't happen but fix this up in case it goes wrong*/ return;

        reference.jda.getTextChannelById(twitchChannel.getDiscordChannelID()).sendMessage(sender + ": " + message).queue();
    }
}

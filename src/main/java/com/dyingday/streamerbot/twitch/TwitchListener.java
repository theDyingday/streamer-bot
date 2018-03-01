package com.dyingday.streamerbot.twitch;

import net.engio.mbassy.listener.Handler;
import org.kitteh.irc.client.library.event.channel.ChannelJoinEvent;
import org.kitteh.irc.client.library.event.channel.ChannelPartEvent;

public class TwitchListener
{
    @Handler
    public void onUserJoin(ChannelJoinEvent event)
    {
        if(!event.getClient().isUser(event.getUser()))
        {
            TwitchHandler.getTwitchChannel(event.getChannel()).addUser(event.getUser());
        }
    }

    @Handler
    public void onUserLeave(ChannelPartEvent event)
    {
        if(!event.getClient().isUser(event.getUser()))
        {
            TwitchHandler.getTwitchChannel(event.getChannel()).removeUser(event.getUser());
            event.getChannel();
        }
    }
}

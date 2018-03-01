package com.dyingday.streamerbot.twitch;

import com.dyingday.streamerbot.utils.Reference;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;

public class NewTwitchHandler
{
    private static Reference reference = Reference.getReference();

    public NewTwitchHandler()
    {
        reference.twitchClient = Client.builder().nick(reference.TWITCH_NAME).serverHost(reference.TWITCH_SERVER).serverPort(reference.TWITCH_PORT).buildAndConnect();
    }

    public static NewTwitchChannel getTwitchChannel(Channel channel)
    {
        for(NewTwitchChannel twitchChannel : reference.newTwitchConnections.keySet())
        {
            if(twitchChannel.getChannel().equals(channel)) return twitchChannel;
        }
        return null;
    }
}

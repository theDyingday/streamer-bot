package com.dyingday.streamerbot.twitch;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;

public class TwitchHandler
{
    private static Reference reference = Reference.getReference();

    public TwitchHandler()
    {
        reference.twitch = Client.builder().nick(reference.TWITCH_NAME).serverPassword(reference.TWITCH_OAUTH).serverHost(reference.TWITCH_SERVER).serverPort(reference.TWITCH_PORT).buildAndConnect();
    }

    public static void addTwitchChannel(Member owner, String channelname, TextChannel channel, DiscordGuild discordGuild)
    {
        TwitchChannel twitchChannel = new TwitchChannel(owner, channelname, channel, discordGuild);
        reference.twitchConnections.put(twitchChannel, discordGuild);
        discordGuild.addTwitchChannel(twitchChannel);
    }

    public static TwitchChannel getTwitchChannel(Channel channel)
    {
        for(TwitchChannel twitchChannel : reference.twitchConnections.keySet())
        {
            if(twitchChannel.getChannel().equals(channel)) return twitchChannel;
        }
        return null;
    }
}

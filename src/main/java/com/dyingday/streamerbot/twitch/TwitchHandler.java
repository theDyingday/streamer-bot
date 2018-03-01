package com.dyingday.streamerbot.twitch;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.io.IOException;
import java.util.ArrayList;

public class TwitchHandler extends PircBot
{
    static Reference reference = Reference.getReference();

    public TwitchHandler()
    {
        try
        {
            connect(reference.TWITCH_SERVER, reference.TWITCH_PORT);
        }
        catch (IOException | IrcException e)
        {
            e.printStackTrace();
        }
    }

    public void addTwitchChannel(Member owner, String channelName, TextChannel discordChannel, DiscordGuild discordGuild)
    {
        reference.twitchConnections.put(new TwitchChannel(owner, channelName, discordChannel), discordGuild);
    }

    public TwitchChannel getChannel(String channelName)
    {
        for(TwitchChannel channel : reference.twitchConnections.keySet())
        {
            if(channel.getChannelName().equals(channelName)) return channel;
        }

        return null;
    }

    public DiscordGuild getConnectedDiscordGuild(TwitchChannel channel)
    {
        return reference.twitchConnections.get(channel);
    }

    public DiscordGuild getConnectedDiscordGuild(String channelName)
    {
        TwitchChannel channel = getChannel(channelName);
        return channel == null ? null : getConnectedDiscordGuild(channel);
    }

    public void joinChannel(TwitchChannel channel)
    {
        joinChannel(channel.getChannelName());
    }

    public void joinAllChannels()
    {
        for(TwitchChannel channel : reference.twitchConnections.keySet()) joinChannel(channel);
    }

    public void sendMessage(TwitchChannel channel, String message)
    {
        sendMessage(channel.getChannelName(), message);
    }

    public void sendPrivateMessage(String username, String message)
    {
        sendMessage(username, message);
    }

    public void kickUser(TwitchChannel channel, String username, String reason)
    {
        kick(channel.getChannelName(), username, reason);
    }

    public void kickUser(TwitchChannel channel, String username)
    {
        kick(channel.getChannelName(), username);
    }

    public void opUser(TwitchChannel channel, String username)
    {
        op(channel.getChannelName(), username);
        User user = getUserInChannel(channel, username);
        if(user != null) channel.addOp(user);
    }

    public void deopUser(TwitchChannel channel, String username)
    {
        deOp(channel.getChannelName(), username);
        User user = getUserInChannel(channel, username);
        if(user != null) channel.removeOp(user);
    }

    public User[] getOpsInChannel(TwitchChannel channel)
    {
        ArrayList<User> users = new ArrayList<>();
        for(User user : getUsersInChannel(channel))
        {
            if(user.isOp()) users.add(user);
        }
        User[] userArr = new User[users.size()];

        return users.toArray(userArr);
    }

    public User[] getUsersInChannel(TwitchChannel channel)
    {
        return getUsers(channel.getChannelName());
    }

    public User getUserInChannel(TwitchChannel channel, String username)
    {
        for(User user : getUsersInChannel(channel))
        {
            if(user.getNick().equals(username)) return user;
        }
        return null;
    }
}

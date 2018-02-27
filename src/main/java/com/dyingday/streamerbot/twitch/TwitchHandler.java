package com.dyingday.streamerbot.twitch;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.utils.Reference;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.io.IOException;
import java.util.ArrayList;

public class TwitchHandler extends PircBot
{
    static Reference reference = Reference.getReference();

    public void init_twitch() throws IOException, IrcException
    {
        connect("irc.twitch.tv", reference.TWITCH_PORT, reference.TWITCH_OAUTH);
        setVerbose(true);
        System.out.println("Successfully logged into Twitch!");
    }

    public void addTwitchChannel(String channelName, long discordChannelID, DiscordGuild discordGuild)
    {
        reference.twitchConnections.put(new TwitchChannel(channelName, discordChannelID), discordGuild);
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

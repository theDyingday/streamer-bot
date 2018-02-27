package com.dyingday.streamerbot.twitch;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TwitchChannel
{
    private String channelName;
    private long discordChannelID;
    private ArrayList<User> OPs = new ArrayList<>();
    private Map<String, String> replies = new HashMap<>();

    public TwitchChannel(String channelName, long discordChannelID)
    {
        this.channelName = "#" + channelName;
        this.discordChannelID = discordChannelID;
    }

    public void addOp(User user)
    {
        OPs.add(user);
    }

    public void removeOp(User user)
    {
        if(OPs.contains(user)) OPs.remove(user);
    }

    public User[] getOPs()
    {
        User[] users = new User[OPs.size()];
        return OPs.toArray(users);
    }

    public String getChannelName()
    {
        return channelName;
    }

    public String getRawChannelName()
    {
        return channelName.replace("#", "");
    }

    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public long getDiscordChannelID()
    {
        return discordChannelID;
    }

    public void setDiscordChannelID(long discordChannelID)
    {
        this.discordChannelID = discordChannelID;
    }
}

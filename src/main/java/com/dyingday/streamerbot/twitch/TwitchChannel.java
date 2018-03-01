package com.dyingday.streamerbot.twitch;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.jibble.pircbot.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TwitchChannel
{
    private Member owner;
    private String channelName, commandChar = "/";
    private TextChannel discordChannel;
    private ArrayList<User> OPs = new ArrayList<>();
    private Map<String, String> replies = new HashMap<>();

    private boolean streaming = false;

    public TwitchChannel(Member owner, String channelName, TextChannel discordChannel)
    {
        this.owner = owner;
        this.channelName = "#" + channelName;
        this.discordChannel = discordChannel;
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

    public Member getOwner()
    {
        return owner;
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

    public TextChannel getDiscordChannel()
    {
        return discordChannel;
    }

    public void setDiscordChannelID(TextChannel discordChannel)
    {
        this.discordChannel = discordChannel;
    }

    public String getCommandChar()
    {
        return commandChar;
    }

    public void setCommandChar(String commandChar)
    {
        this.commandChar = commandChar;
    }

    public boolean isStreaming()
    {
        return streaming;
    }

    public void setStreaming(boolean streaming)
    {
        this.streaming = streaming;
    }
}

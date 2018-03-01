package com.dyingday.streamerbot.twitch;

import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewTwitchChannel
{
    private Reference reference = Reference.getReference();

    private Member owner;
    private Channel channel;
    private TextChannel discordChannel;
    private ArrayList<User> OPs = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private Map<String, String> replies = new HashMap<>();
    private boolean streaming = false;

    public NewTwitchChannel(Member owner, String channelName, TextChannel discordChannel)
    {
        this.owner = owner;
        this.discordChannel = discordChannel;

        reference.twitchClient.addChannel("#" + channelName);
        this.channel = reference.twitchClient.getChannel("#" + channelName).get();
    }

    public void addOp(User op)
    {
        if(!OPs.contains(op)) OPs.add(op);
    }

    public void removeOp(User op)
    {
        if(OPs.contains(op)) OPs.remove(op);
    }

    public void addUser(User user)
    {
        if(!users.contains(user)) users.add(user);
    }

    public void removeUser(User user)
    {
        if(users.contains(user)) users.remove(user);
    }

    public Channel getChannel()
    {
        return channel;
    }

    public Member getOwner()
    {
        return owner;
    }

    public void setOwner(Member owner)
    {
        this.owner = owner;
    }

    public TextChannel getDiscordChannel() {
        return discordChannel;
    }

    public void setDiscordChannel(TextChannel discordChannel)
    {
        this.discordChannel = discordChannel;
    }

    public User[] getOPs()
    {
        return OPs.toArray(new User[OPs.size()]);
    }

    public boolean isStreaming()
    {
        return streaming;
    }

    public void toggleStreaming()
    {
        streaming = !streaming;
    }

    public void setStreaming(boolean streaming)
    {
        this.streaming = streaming;
    }
}

package com.dyingday.streamerbot.twitch;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;

import java.util.ArrayList;

public class TwitchChannel
{
    private Reference reference = Reference.getReference();

    private Member owner;
    private Channel channel;
    private DiscordGuild discordGuild;
    private TextChannel discordChannel;
    private String commandChar = "/";
    private ArrayList<User> OPs = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private boolean streaming = false;

    public TwitchChannel(Member owner, String channelName, TextChannel discordChannel, DiscordGuild discordGuild)
    {
        this.owner = owner;
        this.discordChannel = discordChannel;
        this.discordGuild = discordGuild;

        reference.twitch.addChannel("#" + channelName);
        this.channel = reference.twitch.getChannel("#" + channelName).get();
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

    public boolean setDiscordChannel(TextChannel discordChannel)
    {
        if(this.discordGuild.getGuild().getTextChannels().contains(discordChannel))
        {
            this.discordChannel = discordChannel;
            return true;
        }
        return false;
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

    public DiscordGuild getDiscordGuild()
    {
        return discordGuild;
    }

    public String getCommandChar()
    {
        return commandChar;
    }
}

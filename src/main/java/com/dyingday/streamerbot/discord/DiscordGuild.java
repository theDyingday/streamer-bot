package com.dyingday.streamerbot.discord;

import com.dyingday.streamerbot.music.GuildMusicManager;
import com.dyingday.streamerbot.music.MusicManager;
import com.dyingday.streamerbot.twitch.TwitchChannel;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import java.util.ArrayList;

public class DiscordGuild
{
    private final long guildID;
    private Member owner;
    private String commandChar = "/";
    private final GuildMusicManager guildMusicManager;
    private Reference reference = Reference.getReference();
    private ArrayList<TwitchChannel> connectedTwitchChannels = new ArrayList<>();

    public DiscordGuild(long guildID)
    {
        this.guildID = guildID;
        this.guildMusicManager = MusicManager.getGuildMusicManager(this);

        reference.discordGuilds.put(guildID, this);

        owner = getGuild().getOwner();

        owner.getUser().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Thank you for adding the Streamer Bot to your guild!\nEnjoy your time with it!").queue();
            // TODO: Implement Bot Setup
        });
    }

    public void addTwitchChannel(TwitchChannel channel)
    {
        if(!connectedTwitchChannels.contains(channel)) connectedTwitchChannels.add(channel);
    }

    public void removeTwitchChannel(TwitchChannel channel)
    {
        if(connectedTwitchChannels.contains(channel)) connectedTwitchChannels.remove(channel);
    }

    public TwitchChannel[] getConnectedTwitchChannels()
    {
        return connectedTwitchChannels.toArray(new TwitchChannel[connectedTwitchChannels.size()]);
    }

    public Guild getGuild()
    {
        return reference.jda.getGuildById(guildID);
    }

    public long getGuildID()
    {
        return guildID;
    }

    public Member getOwner()
    {
        return owner;
    }

    public void setOwner(Member owner)
    {
        this.owner = owner;
    }

    public String getCommandChar()
    {
        return commandChar;
    }

    public GuildMusicManager getGuildMusicManager()
    {
        return guildMusicManager;
    }
}

package com.dyingday.streamerbot.discord;

import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class DiscordGuild
{
    private final long guildID;
    private Member owner;
    private String commandChar = "/";
    private Reference reference = Reference.getReference();

    public DiscordGuild(long guildID)
    {
        this.guildID = guildID;

        reference.discordGuilds.put(guildID, this);

        owner = getGuild().getOwner();

        owner.getUser().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Thank you for adding the Streamer Bot to your guild!\nEnjoy your time with it!").queue();
            // TODO: Implement Bot Setup
        });
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
}

package com.dyingday.objects;

import com.dyingday.utils.Reference;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

public class Server
{
    private final long guildID;
    private Member owner;
    private char commandChar = '/';
    private Reference reference = Reference.getReference();

    public Server(long guildID)
    {
        this.guildID = guildID;

        reference.servers.put(guildID, this);

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

    public void setOwner(Member member)
    {
        this.owner = owner;
    }

    public char getCommandChar()
    {
        return commandChar;
    }
}

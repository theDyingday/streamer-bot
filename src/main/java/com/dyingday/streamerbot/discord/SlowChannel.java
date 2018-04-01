package com.dyingday.streamerbot.discord;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.HashMap;

public class SlowChannel
{
    private TextChannel channel;
    private int timeout;
    private HashMap<Member, OffsetDateTime> lastMessageTime = new HashMap<>();

    public SlowChannel(TextChannel channel, int timeout)
    {
        this.channel = channel;
        this.timeout = timeout;
    }

    public boolean canMessage(Member member, OffsetDateTime newMessageTime)
    {
        if(!lastMessageTime.containsKey(member))
        {
            lastMessageTime.put(member, newMessageTime);
            return true;
        }
        if(lastMessageTime.get(member).getSecond() + timeout <= newMessageTime.getSecond())
        {
            lastMessageTime.replace(member, newMessageTime);
            return true;
        }
        return false;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }
}

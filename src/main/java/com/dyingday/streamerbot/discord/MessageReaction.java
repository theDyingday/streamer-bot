package com.dyingday.streamerbot.discord;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageReaction
{
    private Message message;
    private ArrayList<Member> membersCanReact = new ArrayList<>();
    private ArrayList<String> emotesCanAdd = new ArrayList<>();

    public MessageReaction(Message message, Member[] membersCanReact, String[] emotesCanAdd)
    {
        this.message = message;
        this.membersCanReact.addAll(Arrays.asList(membersCanReact));
        this.emotesCanAdd.addAll(Arrays.asList(emotesCanAdd));
    }

    public boolean canMemberReact(Member member)
    {
        return membersCanReact.contains(member);
    }

    public boolean canAddEmote(String emote)
    {
        return emotesCanAdd.contains(emote);
    }

    public Message getMessage()
    {
        return message;
    }
}

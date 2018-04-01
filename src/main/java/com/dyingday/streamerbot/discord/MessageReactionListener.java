package com.dyingday.streamerbot.discord;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class MessageReactionListener
{
    private Message message;
    private ArrayList<Member> membersCanReact = new ArrayList<>();
    private ArrayList<String> emotesCanAdd = new ArrayList<>();

    public MessageReactionListener(Message message, Member[] membersCanReact, String[] emotesCanAdd)
    {
        this.message = message;
        this.membersCanReact.addAll(Arrays.asList(membersCanReact));
        this.emotesCanAdd.addAll(Arrays.asList(emotesCanAdd));

        ReactionHandler.addListener(this);
    }

    void setMessage(Message message)
    {
        this.message = message;
    }

    void removeListener()
    {
        ReactionHandler.removeListener(this);
    }

    void setMembersCanReact(ArrayList<Member> membersCanReact)
    {
        this.membersCanReact = membersCanReact;
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

    public abstract void onReaction(String emote, MessageReactionAddEvent event);
}

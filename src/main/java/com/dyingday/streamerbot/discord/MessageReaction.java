package com.dyingday.streamerbot.discord;

import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageReaction
{
    private Reference reference = Reference.getReference();
    private Message message;
    private ArrayList<Member> membersCanReact = new ArrayList<>();
    private ArrayList<Emote> emotesCanAdd = new ArrayList<>();

    public MessageReaction(Message message, List<Member> membersCanReact, List<Emote> emotesCanAdd)
    {
        this.message = message;
        this.membersCanReact.addAll(membersCanReact);
        this.emotesCanAdd.addAll(emotesCanAdd);
    }

    public void addEmote(Emote emote)
    {
        if(!emotesCanAdd.contains(emote)) emotesCanAdd.add(emote);
        if(!message.getReactions().contains(emote)) message.addReaction(emote).queue();
    }

    public void removeEmote(Emote emote)
    {
        if(emotesCanAdd.contains(emote)) emotesCanAdd.remove(emote);
        if(message.getReactions().contains(emote)) message.getChannel().removeReactionById(message.getId(), emote).queue();
    }

    public void addMember(Member member)
    {
        if(!membersCanReact.contains(member)) membersCanReact.add(member);
    }

    public void removeMember(Member member)
    {
        if(membersCanReact.contains(member)) membersCanReact.remove(member);
    }

    public boolean memberCanReact(Member member)
    {
        return membersCanReact.contains(member);
    }

    public boolean emoteCanAdd(Emote emote)
    {
        return emotesCanAdd.contains(emote);
    }

    public Message getMessage()
    {
        return message;
    }

}

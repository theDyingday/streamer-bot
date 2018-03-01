package com.dyingday.streamerbot.discord;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;

public class MessageReactHandler extends ListenerAdapter
{
    private ArrayList<MessageReaction> messageReactions = new ArrayList<>();

    public static void addReactionsToMessage(Message message, Emote[] emotes)
    {
        for(Emote emote : emotes) addReactionToMessage(message, emote);
    }

    public static void addReactionToMessage(Message message, Emote emote)
    {
        message.addReaction(emote).queue();
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
        for(MessageReaction messageReaction : messageReactions)
        {
            if(messageReaction.getMessage().getId().equalsIgnoreCase(event.getMessageId()))
            {
                if(messageReaction.memberCanReact(event.getMember()))
                {
                    if(messageReaction.emoteCanAdd(event.getReactionEmote().getEmote()))
                    {
                        // TODO: Deal with message callback
                        return;
                    }
                }

                event.getReaction().removeReaction().queue();
            }
        }
    }
}

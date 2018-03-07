package com.dyingday.streamerbot.discord;

import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReactionHandler extends ListenerAdapter
{
    private static Map<ReactionListener, MessageReaction> reactionListeners = new HashMap<>();

    public static void addListener(ReactionListener reactionListener, MessageReaction messageReaction)
    {
        if(!reactionListeners.keySet().contains(reactionListener)) reactionListeners.put(reactionListener, messageReaction);
    }

    public static void removeListener(ReactionListener reactionListener)
    {
        if(reactionListeners.keySet().contains(reactionListener)) reactionListeners.remove(reactionListener);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
        for(ReactionListener reactionListener : reactionListeners.keySet())
            reactionListener.onReaction(EmojiParser.parseToAliases(event.getReactionEmote().getName()).replaceAll(":", ""), reactionListeners.get(reactionListener));
    }
}

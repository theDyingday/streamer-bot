package com.dyingday.streamerbot.discord;

import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReactionHandler extends ListenerAdapter
{
    private static List<MessageReactionListener> listeners = new ArrayList<>();

    public static void addListener(MessageReactionListener listener)
    {
        if(!listeners.contains(listener)) listeners.add(listener);
    }

    protected static void removeListener(MessageReactionListener listener)
    {
        if(listeners.contains(listener)) listeners.remove(listener);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
        if(event.getUser().isBot()) return;
        // Used a copy array to avoid Concurrent Modification Exception; Maybe try a more elegant solution?
        MessageReactionListener[] copyArray = new MessageReactionListener[listeners.size()];
        listeners.toArray(copyArray);
        for(MessageReactionListener listener : copyArray)
        {
            String emoji = EmojiParser.parseToAliases(event.getReactionEmote().getName()).replaceAll(":", "");
            if(listener.getMessage().getId().equals(event.getMessageId()) && listener.canAddEmote(emoji) && listener.canMemberReact(event.getMember()))
            {
                listener.onReaction(emoji, event);
                event.getReaction().removeReaction(event.getUser()).queue();
            }
        }
    }
}

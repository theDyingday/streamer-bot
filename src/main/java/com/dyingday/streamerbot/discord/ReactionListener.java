package com.dyingday.streamerbot.discord;

public interface ReactionListener
{
    void onReaction(String emote, MessageReaction messageReaction);
}

package com.dyingday.streamerbot.discord;

import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;

public class DiscordGuildConfig
{
    private boolean moderating = true;
    private ArrayList<TextChannel> channelsToModerate = new ArrayList<>();
}

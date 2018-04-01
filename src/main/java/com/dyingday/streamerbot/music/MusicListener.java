package com.dyingday.streamerbot.music;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.utils.Emotes;
import com.dyingday.streamerbot.utils.Reference;
import com.dyingday.streamerbot.utils.Utils;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.*;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;

public class MusicListener extends AudioEventAdapter
{
    private Reference reference = Reference.getReference();

    @Override
    public void onEvent(AudioEvent event)
    {
        DiscordGuild discordGuild = reference.audioPlayers.get(event.player);

        if(event instanceof PlayerPauseEvent)
        {
            onPlayerPause(event.player, event.player.getPlayingTrack(), discordGuild);
        }
        else if(event instanceof PlayerResumeEvent)
        {
            onPlayerResume(event.player, event.player.getPlayingTrack(), discordGuild);
        }
        else if(event instanceof TrackStartEvent)
        {
            onTrackStart(event.player, ((TrackStartEvent) event).track, discordGuild);
        }
        else if(event instanceof TrackEndEvent)
        {
            onTrackEnd(event.player, ((TrackEndEvent) event).track, ((TrackEndEvent) event).endReason, discordGuild);
        }
        else if(event instanceof TrackExceptionEvent)
        {
            onTrackException(event.player, ((TrackExceptionEvent) event).track, ((TrackExceptionEvent) event).exception, discordGuild);
        }
        else if(event instanceof TrackStuckEvent)
        {
            onTrackStuck(event.player, ((TrackStuckEvent) event).track, ((TrackStuckEvent) event).thresholdMs, discordGuild);
        }
    }

    private void onPlayerPause(AudioPlayer player, AudioTrack track, DiscordGuild discordGuild)
    {
        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.GREEN).setAuthor("Song Paused: ").setTitle(track.getInfo().title + " - " + track.getInfo().author + " - " + Utils.getTimeStamp(track.getDuration()), track.getInfo().uri).setTimestamp(Instant.now());
        ArrayList<AudioTrack> queue = discordGuild.getGuildMusicManager().getQueue();
        if(queue.size() != 0) embedBuilder.appendDescription("**Queue:**" + "\n");
        for(int i = 0; i < (queue.size() > 9 ? 9 : queue.size()); i++)
        {
            embedBuilder.appendDescription(":" + Emotes.getNumberEmote(i+1) + ": :- " + queue.get(i).getInfo().title + " - " + queue.get(i).getInfo().author + " - " + Utils.getTimeStamp(queue.get(i).getDuration()));
        }
        discordGuild.getMusicChannel().sendMessage(embedBuilder.build()).queue();
    }

    private void onPlayerResume(AudioPlayer player, AudioTrack track, DiscordGuild discordGuild)
    {
            onTrackStart(player, track, discordGuild);
    }

    private void onTrackStart(AudioPlayer player, AudioTrack track, DiscordGuild discordGuild)
    {
        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.GREEN).setAuthor("Now Playing: ").setTitle(track.getInfo().title + " - " + track.getInfo().author + " - " + Utils.getTimeStamp(track.getDuration()), track.getInfo().uri).setTimestamp(Instant.now());
        if(track.getInfo().uri.startsWith("https://www.youtube.com")) {
            embedBuilder.setImage("https://img.youtube.com/vi/" + track.getInfo().uri.replace("https://www.youtube.com/watch?v=", "") + "/0.jpg");
        }
        ArrayList<AudioTrack> queue = discordGuild.getGuildMusicManager().getQueue();
        if(queue.size() != 0) embedBuilder.appendDescription("**Queue:**" + "\n");
        for(int i = 0; i < (queue.size() > 9 ? 9 : queue.size()); i++)
        {
            embedBuilder.appendDescription(":" + Emotes.getNumberEmote(i+1) + ": :- " + queue.get(i).getInfo().title + " - " + queue.get(i).getInfo().author + " - " + Utils.getTimeStamp(queue.get(i).getDuration()));
        }
        discordGuild.getMusicChannel().sendMessage(embedBuilder.build()).queue();
    }

    private void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason, DiscordGuild discordGuild)
    {
    }

    private void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception, DiscordGuild discordGuild)
    {
    }

    private void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs, DiscordGuild discordGuild)
    {
    }
}

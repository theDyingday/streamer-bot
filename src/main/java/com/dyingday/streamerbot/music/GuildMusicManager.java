package com.dyingday.streamerbot.music;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.utils.Emotes;
import com.dyingday.streamerbot.utils.Reference;
import com.dyingday.streamerbot.utils.Utils;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GuildMusicManager
{
    private Reference reference = Reference.getReference();

    public final AudioPlayer player;
    private AudioPlayerSendHandler sendHandler;

    public final BlockingQueue<AudioTrack> queue;
    public final DiscordGuild discordGuild;

    public GuildMusicManager(AudioPlayerManager manager, DiscordGuild discordGuild)
    {
        this.player = manager.createPlayer();
        this.queue = new LinkedBlockingQueue<>();
        this.discordGuild = discordGuild;

        this.player.addListener(new MusicListener());

        reference.audioPlayers.put(player, discordGuild);
    }

    public void addToQueue(AudioTrack track)
    {
        if(!player.startTrack(track, true))
        {
            queue.offer(track);
            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.GREEN).setAuthor("Added to Queue: ").setTitle(track.getInfo().title + " - " + track.getInfo().author + " - " + Utils.getTimeStamp(track.getDuration()), track.getInfo().uri).setTimestamp(Instant.now());
            embedBuilder.appendDescription("**Currently Playing:** " + track.getInfo().title + " - " + track.getInfo().author + " - " + Utils.getTimeStamp(track.getDuration()));
            ArrayList<AudioTrack> queue = discordGuild.getGuildMusicManager().getQueue();
            if(queue.size() != 0) embedBuilder.appendDescription("\n" + "**Queue:**" + "\n");
            for(int i = 0; i < (queue.size() > 9 ? 9 : queue.size()); i++)
            {
                embedBuilder.appendDescription(":" + Emotes.getNumberEmote(i+1) + ": :- " + queue.get(i).getInfo().title + " - " + queue.get(i).getInfo().author + " - " + Utils.getTimeStamp(queue.get(i).getDuration()));
            }
            discordGuild.getMusicChannel().sendMessage(embedBuilder.build()).queue();
        }
    }

    public void nextTrack()
    {
        player.startTrack(queue.poll(), false);
    }

    public void stop()
    {
        queue.clear();
        player.stopTrack();
    }

    public ArrayList<AudioTrack> getQueue()
    {
        ArrayList<AudioTrack> queue = new ArrayList<>();
        queue.addAll(this.queue);
        return queue;
    }

    public AudioPlayerSendHandler getSendHandler()
    {
        if(sendHandler == null) sendHandler = new AudioPlayerSendHandler(player);
        return sendHandler;
    }
}

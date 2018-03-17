package com.dyingday.streamerbot.music;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.utils.Reference;
import com.dyingday.streamerbot.utils.Utils;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
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
            discordGuild.getMusicChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setAuthor("Added to Queue:").setTitle(track.getInfo().title + " - " + track.getInfo().author, track.getInfo().uri).setFooter(Utils.getTimeStamp(track.getDuration()), null).build()).queue();
        }
    }

    public void nextTrack()
    {
        player.startTrack(queue.poll(), false);
    }

    public AudioPlayerSendHandler getSendHandler()
    {
        if(sendHandler == null) sendHandler = new AudioPlayerSendHandler(player);
        return sendHandler;
    }
}

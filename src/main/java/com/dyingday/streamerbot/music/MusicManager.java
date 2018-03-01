package com.dyingday.streamerbot.music;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.utils.Reference;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;

public class MusicManager
{
    private Reference reference = Reference.getReference();

    private final AudioPlayerManager apm;
    public final Map<DiscordGuild, GuildMusicManager> guildMusicManagers;

    public MusicManager()
    {
        this.apm = new DefaultAudioPlayerManager();
        this.guildMusicManagers = new HashMap<>();

        AudioSourceManagers.registerLocalSource(apm);
        AudioSourceManagers.registerRemoteSources(apm);
    }

    public synchronized GuildMusicManager getGuildMusicManager(DiscordGuild discordGuild)
    {
        GuildMusicManager guildMusicManager = guildMusicManagers.get(discordGuild);

        if(guildMusicManager == null)
        {
            guildMusicManager = new GuildMusicManager(apm, discordGuild);
        }

        discordGuild.getGuild().getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

        return guildMusicManager;
    }

    public AudioPlayer getGuildAudioPlayer(DiscordGuild guild)
    {
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);
        return guildMusicManager.player;
    }

    public void loadAndPlayURL(final TextChannel channel, final String trackURL)
    {
        DiscordGuild discordGuild = reference.discordGuilds.get(channel.getGuild().getIdLong());

        final GuildMusicManager guildMusicManager = getGuildMusicManager(discordGuild);
        apm.loadItemOrdered(guildMusicManager, trackURL, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                channel.sendMessage("Adding to queue: " + track.getInfo().title).queue();
                play(discordGuild, guildMusicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if(firstTrack == null)
                {
                    firstTrack = playlist.getTracks().get(0);
                }

                channel.sendMessage("Adding to queue: " + firstTrack.getInfo().title + " (first track of playlist: " + playlist.getName() + ")").queue();

                play(discordGuild, guildMusicManager, firstTrack);
            }

            @Override
            public void noMatches()
            {
                channel.sendMessage("Nothing found by the url: " + trackURL).queue();
            }

            @Override
            public void loadFailed(FriendlyException e)
            {
                channel.sendMessage("Could not play: " + e.getMessage()).queue();
            }
        });
    }

    public void play(DiscordGuild discordGuild, GuildMusicManager guildMusicManager, AudioTrack track)
    {
        connectToFirstVoiceChannel(discordGuild.getGuild().getAudioManager());
        guildMusicManager.addToQueue(track);
    }

    public void skip(DiscordGuild discordGuild)
    {
        GuildMusicManager musicManager = getGuildMusicManager(discordGuild);
        musicManager.nextTrack();
    }

    public void connectToFirstVoiceChannel(AudioManager manager)
    {
        if(!manager.isConnected() && !manager.isAttemptingToConnect())
        {
            for(VoiceChannel channel : manager.getGuild().getVoiceChannels())
            {
                manager.openAudioConnection(channel);
                break;
            }
        }
    }

    public void connectToVoiceChannel(AudioManager manager, VoiceChannel channel)
    {
        manager.openAudioConnection(channel);
    }

    public static MusicManager musicManager;
    public static MusicManager getMusicManager()
    {
        if(musicManager == null) musicManager = new MusicManager();
        return musicManager;
    }
}

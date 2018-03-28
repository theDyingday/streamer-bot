package com.dyingday.streamerbot.music;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.utils.Reference;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchProvider;
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
    private static Reference reference = Reference.getReference();

    private static final AudioPlayerManager apm = new DefaultAudioPlayerManager();
    public static final Map<DiscordGuild, GuildMusicManager> guildMusicManagers = new HashMap<>();
    private static YoutubeAudioSourceManager yasm = new YoutubeAudioSourceManager();
    public static YoutubeSearchProvider ytSearch = new YoutubeSearchProvider(yasm);

    public MusicManager()
    {
        AudioSourceManagers.registerLocalSource(apm);
        AudioSourceManagers.registerRemoteSources(apm);
    }

    public static synchronized GuildMusicManager getGuildMusicManager(DiscordGuild discordGuild)
    {
        GuildMusicManager guildMusicManager = guildMusicManagers.get(discordGuild);

        if(guildMusicManager == null)
        {
            guildMusicManager = new GuildMusicManager(apm, discordGuild);
            guildMusicManagers.put(discordGuild, guildMusicManager);
            discordGuild.getGuild().getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
        }

        return guildMusicManager;
    }

    public static AudioPlayer getGuildAudioPlayer(DiscordGuild guild)
    {
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);
        return guildMusicManager.player;
    }

    public static void loadAndPlayURL(final TextChannel channel, final String trackURL)
    {
        DiscordGuild discordGuild = reference.discordGuilds.get(channel.getGuild().getIdLong());

        final GuildMusicManager guildMusicManager = getGuildMusicManager(discordGuild);
        apm.loadItemOrdered(guildMusicManager, trackURL, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                channel.sendMessage("Adding to queue: " + track.getInfo().title).queue();
                play(discordGuild, track);
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

                play(discordGuild, firstTrack);
            }

            @Override
            public void noMatches()
            {
                channel.sendMessage("Nothing found by that url! ").queue();
            }

            @Override
            public void loadFailed(FriendlyException e)
            {
                channel.sendMessage("Could not play: " + e.getMessage()).queue();
            }
        });
    }

    public static void play(DiscordGuild discordGuild, AudioTrack track)
    {
        connectToFirstVoiceChannel(discordGuild.getGuild().getAudioManager());
        discordGuild.getGuildMusicManager().addToQueue(track);
    }

    public static void skip(DiscordGuild discordGuild)
    {
        GuildMusicManager musicManager = getGuildMusicManager(discordGuild);
        musicManager.nextTrack();
    }

    public static void connectToFirstVoiceChannel(AudioManager manager)
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

    public static void connectToVoiceChannel(AudioManager manager, VoiceChannel channel)
    {
        manager.openAudioConnection(channel);
    }

    public static boolean isConnectedToVoice(AudioManager manager)
    {
        return manager.isConnected();
    }
}

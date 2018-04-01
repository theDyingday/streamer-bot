package com.dyingday.streamerbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler
{
    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    public AudioPlayerSendHandler(AudioPlayer player)
    {
        this.audioPlayer = player;
    }

    @Override
    public boolean canProvide()
    {
        lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    @Override
    public byte[] provide20MsAudio()
    {
        return lastFrame.data;
    }

    @Override
    public boolean isOpus()
    {
        return true;
    }
}

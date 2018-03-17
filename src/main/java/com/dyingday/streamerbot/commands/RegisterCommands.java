package com.dyingday.streamerbot.commands;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.discord.YouTubeSearchReaction;
import com.dyingday.streamerbot.music.MusicManager;
import com.dyingday.streamerbot.utils.Emotes;
import com.dyingday.streamerbot.utils.ExecutorType;
import com.dyingday.streamerbot.utils.Reference;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;
import java.time.Instant;
import java.util.List;

import static com.dyingday.streamerbot.utils.Utils.getStringFromArray;
import static com.dyingday.streamerbot.utils.Utils.getTimeStamp;

public class RegisterCommands
{
    private Reference reference = Reference.getReference();

    @Command(name = "info", description = "Lists all commands and usages", usage = "info", type = ExecutorType.ALL, maxArgs = 0)
    private void info(User user)
    {
        user.openPrivateChannel().queue(privateChannel -> {
            for(BaseCommand command : reference.getCommands())
            {
                EmbedBuilder emb = new EmbedBuilder().setColor(reference.INFO_COLOUR)
                                    .setTitle(command.getName())
                                    .setDescription(command.getDescription())
                                    .addField("**Usage: **", command.getUsage(), false)
                                    .setAuthor("Command Maestro", null, "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/Infobox_info_icon.svg/1024px-Infobox_info_icon.svg.png")
                                    .setTimestamp(Instant.now());

                privateChannel.sendMessage(emb.build()).queue();
            }
        });
    }

    @Command(name = "join", description = "Joins the bot to voice channel", usage = "join [VoiceChannelName]", channel = ChannelType.GROUP, maxArgs = 1)
    private void join(String[] args, DiscordGuild discordGuild, TextChannel channel)
    {
        if(args.length == 0)
        {
            MusicManager.connectToFirstVoiceChannel(discordGuild.getGuild().getAudioManager());
        }
        else
        {
            List<VoiceChannel> channels = discordGuild.getGuild().getVoiceChannelsByName(args[0], true);
            if(channels.size() == 0)
            {
                MusicManager.connectToFirstVoiceChannel(discordGuild.getGuild().getAudioManager());
                channel.sendMessage("Could not find a channel with that name! Joining default channel; Feel free to move me using /move!").queue();
                return;
            }
            else
            {
                MusicManager.connectToVoiceChannel(discordGuild.getGuild().getAudioManager(), channels.get(0));
            }
        }
        channel.sendMessage("Successfully joined channel: " + discordGuild.getGuild().getAudioManager().getConnectedChannel().getName()).queue();
    }

    @Command(name = "search", description = "Searches for something...", channel = ChannelType.GROUP, minArgs = 1)
    private void search(TextChannel channel, Member member, String[] args)
    {
        StringBuilder query = new StringBuilder();
        for(String string : args) query.append(string).append("\n");
        AudioPlaylist results = (AudioPlaylist) MusicManager.ytSearch.loadSearchResult(query.toString());
        List<AudioTrack> tracks = results.getTracks();
        EmbedBuilder embMessage = new EmbedBuilder().setColor(Color.RED).setAuthor("YouTube Search Results for: " + getStringFromArray(args), null,"https://storage.googleapis.com/gweb-uniblog-publish-prod/images/YouTube.max-2800x2800.png").setFooter("Page 1", null);
        for(AudioTrack track : tracks)
        {
            if(tracks.indexOf(track) > 8) break;
            embMessage.appendDescription(":" + Emotes.getNumberEmote(tracks.indexOf(track) + 1) + ": - " + getTimeStamp(track.getInfo().length) + " " + track.getInfo().title + " - " + track.getInfo().author + "\n");
        }
        channel.sendMessage(embMessage.build()).queue(message ->
        {
            for(int i = 1; i < (tracks.size() >= 10 ? 10 : tracks.size()); i++)
            {
                message.addReaction(EmojiParser.parseToUnicode(":" + Emotes.getNumberEmote(i) + ":")).queue();
            }
            if(tracks.size() > 9) message.addReaction(EmojiParser.parseToUnicode(":arrow_right:")).queue();

            new YouTubeSearchReaction(message, new Member[]{member}, new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "arrow_right"}, results, getStringFromArray(args));
        });
    }

    @Command(name = "play")
    private void play(TextChannel channel, String[] args, DiscordGuild discordGuild)
    {
        if(args.length == 0) MusicManager.getGuildAudioPlayer(discordGuild).setPaused(false);
        MusicManager.loadAndPlayURL(channel, args[0]);
    }

    @Command(name = "skip", description = "Skips current track!", channel = ChannelType.GROUP, maxArgs = 0)
    private void skip(Guild guild)
    {
        MusicManager.skip(reference.discordGuilds.get(guild.getIdLong()));
    }

    @Command(name = "pause", description = "Pauses the currently playing song!", channel = ChannelType.GROUP, maxArgs = 0)
    private void pause(DiscordGuild discordGuild)
    {
        MusicManager.getGuildAudioPlayer(discordGuild).setPaused(true);
    }

    @Command(name = "smc")
    private void smc(TextChannel channel, DiscordGuild discordGuild)
    {
        discordGuild.setMusicChannel(channel);
    }
}

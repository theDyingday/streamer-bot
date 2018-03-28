package com.dyingday.streamerbot.discord;

import com.dyingday.streamerbot.music.MusicManager;
import com.dyingday.streamerbot.utils.Emotes;
import com.dyingday.streamerbot.utils.Reference;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.dyingday.streamerbot.utils.Utils.getTimeStamp;

public class YouTubeSearchReaction extends MessageReactionListener
{
    private final AudioPlaylist ytSearchPlaylist;
    private final List<AudioTrack> tracks = new ArrayList<>();
    private final String searchParams;
    private int page = 1;

    public YouTubeSearchReaction(Message message, Member[] members, AudioPlaylist ytSearchPlaylist, String searchParams)
    {
        super(message, members, new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "arrow_right", "arrow_left", "x"});
        this.ytSearchPlaylist = ytSearchPlaylist;
        this.tracks.addAll(ytSearchPlaylist.getTracks());
        this.searchParams = searchParams;
    }

    @Override
    public void onReaction(String emote, MessageReactionAddEvent event)
    {
        if (emote.equalsIgnoreCase("x"))
        {
            removeListener();
            getMessage().delete().queue();
        }
        else if (emote.equalsIgnoreCase("arrow_right"))
        {
            getMessage().clearReactions().queue(message -> {
                int emoteNumber = 0;
                page++;
                EmbedBuilder embMessage = new EmbedBuilder().setColor(Color.RED).setAuthor("YouTube Search Results for: " + searchParams, null, "https://storage.googleapis.com/gweb-uniblog-publish-prod/images/YouTube.max-2800x2800.png").setFooter("Page " + page, null);

                for (int i = 9 * (page - 1); i < page * 9; i++)
                {
                    if (i >= tracks.size()) break;
                    embMessage.appendDescription(":" + Emotes.getNumberEmote(i % 9 + 1) + ": - " + getTimeStamp(tracks.get(i).getInfo().length) + " " + tracks.get(i).getInfo().title + " - " + tracks.get(i).getInfo().author + "\n");
                    emoteNumber++;
                }
                getMessage().editMessage(embMessage.build()).queue();
                getMessage().addReaction(EmojiParser.parseToUnicode(":arrow_left:")).queue();
                for (int i = 0; i < emoteNumber; i++)
                {
                    getMessage().addReaction(EmojiParser.parseToUnicode(":" + Emotes.getNumberEmote(i + 1) + ":")).queue();
                }
                if (emoteNumber == 9 && page * 9 != tracks.size())
                    getMessage().addReaction(EmojiParser.parseToUnicode(":arrow_right:")).queue();
                getMessage().addReaction(EmojiParser.parseToUnicode(":x:")).queue();
            });
        }
        else if (emote.equalsIgnoreCase("arrow_left"))
        {
            getMessage().clearReactions().queue(message -> {
                page--;
                EmbedBuilder embMessage = new EmbedBuilder().setColor(Color.RED).setAuthor("YouTube Search Results for: " + searchParams, null, "https://storage.googleapis.com/gweb-uniblog-publish-prod/images/YouTube.max-2800x2800.png").setFooter("Page " + page, null);

                for (int i = 9 * (page - 1); i < page * 9; i++)
                {
                    if (i > tracks.size()) break;
                    embMessage.appendDescription(":" + Emotes.getNumberEmote(i % 9 + 1) + ": - " + getTimeStamp(tracks.get(i).getInfo().length) + " " + tracks.get(i).getInfo().title + " - " + tracks.get(i).getInfo().author + "\n");
                }
                getMessage().editMessage(embMessage.build()).queue();
                if(page != 1)
                {
                    getMessage().addReaction(EmojiParser.parseToUnicode(":arrow_left:")).queue();
                }
                for(int i = 0; i < 9; i++)
                {
                    getMessage().addReaction(EmojiParser.parseToUnicode(":" + Emotes.getNumberEmote(i+1) + ":")).queue();
                }
                getMessage().addReaction(EmojiParser.parseToUnicode(":arrow_right:")).queue();
                getMessage().addReaction(EmojiParser.parseToUnicode(":x:")).queue();
            });
        }
        else
        {
            DiscordGuild guild = Reference.getReference().discordGuilds.get(getMessage().getGuild().getIdLong());
            MusicManager.play(guild, tracks.get(Emotes.getNumber(emote) + (page - 1) * 9 - 1));
            getMessage().delete().queue();
            removeListener();
        }
    }
}

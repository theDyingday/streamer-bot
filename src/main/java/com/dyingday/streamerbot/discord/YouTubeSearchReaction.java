package com.dyingday.streamerbot.discord;

import com.dyingday.streamerbot.music.MusicManager;
import com.dyingday.streamerbot.utils.Emotes;
import com.dyingday.streamerbot.utils.Reference;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

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

    public YouTubeSearchReaction(Message message, Member[] members, String[] emotes, AudioPlaylist ytSearchPlaylist, String searchParams)
    {
        super(message, members, emotes);
        this.ytSearchPlaylist = ytSearchPlaylist;
        this.tracks.addAll(ytSearchPlaylist.getTracks());
        this.searchParams = searchParams;
    }

    @Override
    public void onReaction(String emote, Member member)
    {
        if (emote.equalsIgnoreCase("x"))
        {
            getMessage().clearReactions().queue();
            removeListener();
        }
        else if (emote.equalsIgnoreCase("arrow_right"))
        {
            page++;
            EmbedBuilder embMessage = new EmbedBuilder().setColor(Color.RED).setAuthor("YouTube Search Results for: " + searchParams, null, "https://storage.googleapis.com/gweb-uniblog-publish-prod/images/YouTube.max-2800x2800.png").setFooter("Page " + page, null);

            for (int i = 9 * (page - 1) + 1; i <= page * 9; i++)
            {
                if (i >= tracks.size()) return;
                embMessage.appendDescription(":" + Emotes.getNumberEmote(tracks.indexOf(tracks.get(i)) - (9 * (page - 1))) + ": - " + getTimeStamp(tracks.get(i).getInfo().length) + " " + tracks.get(i).getInfo().title + " - " + tracks.get(i).getInfo().author + "\n");
            }
            getMessage().editMessage(embMessage.build()).queue();
        }
        else if (emote.equalsIgnoreCase("arrow_left"))
        {
            page--;
            EmbedBuilder embMessage = new EmbedBuilder().setColor(Color.RED).setAuthor("YouTube Search Results for: " + searchParams, null, "https://storage.googleapis.com/gweb-uniblog-publish-prod/images/YouTube.max-2800x2800.png").setFooter("Page " + page, null);

            for (int i = (page * 9) - 8; i <= page * 9; i++)
            {
                embMessage.appendDescription(":" + Emotes.getNumberEmote(tracks.indexOf(tracks.get(i)) + 1 - (9 * page)) + ": - " + getTimeStamp(tracks.get(i).getInfo().length) + " " + tracks.get(i).getInfo().title + " - " + tracks.get(i).getInfo().author + "\n");
            }
            getMessage().editMessage(embMessage.build()).queue();
        }
        else
        {
            DiscordGuild guild = Reference.getReference().discordGuilds.get(getMessage().getGuild().getIdLong());
            MusicManager.play(guild, tracks.get(Emotes.getNumber(emote)));
        }
    }
}

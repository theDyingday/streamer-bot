package com.dyingday.streamerbot.commands;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.discord.HangmanGame;
import com.dyingday.streamerbot.discord.YouTubeSearchReaction;
import com.dyingday.streamerbot.music.MusicManager;
import com.dyingday.streamerbot.utils.Emotes;
import com.dyingday.streamerbot.utils.ExecutorType;
import com.dyingday.streamerbot.utils.Reference;
import com.dyingday.streamerbot.utils.Utils;
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
    public static final String NO_USAGE = "No usage has been provided for this command!";

    @Command(name = "info", description = "Lists all commands and usages", usage = "info", type = ExecutorType.ALL, channel = ChannelType.GROUP, maxArgs = 0)
    private void info(User user, DiscordGuild discordGuild)
    {
        user.openPrivateChannel().queue(privateChannel -> {
            for(BaseCommand command : reference.getCommands())
            {
                EmbedBuilder emb = new EmbedBuilder().setColor(reference.INFO_COLOUR)
                                    .setTitle(command.getName())
                                    .setDescription(command.getDescription())
                                    .addField("**Usage: **", command.getUsage().equals(NO_USAGE) ? command.getUsage() : discordGuild.getCommandChar() + command.getUsage(), false)
                                    .setAuthor("Command Maestro", null, "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/Infobox_info_icon.svg/1024px-Infobox_info_icon.svg.png")
                                    .setTimestamp(Instant.now());

                privateChannel.sendMessage(emb.build()).queue();
            }
        });
    }

    // Moderation Commands

    @Command(name = "mod", description = "Adds Mod status to Members/Role\nIf assigning Role, Role name must be specified as argument", usage = "mod @Dyingday\nmod @Dyingday @Alcmene\nmod @admin", channel = ChannelType.GROUP, minArgs = 1)
    private void mod(Message message, DiscordGuild discordGuild, String[] args, TextChannel channel)
    {
        if(message.getMentionedMembers().size() == 0)
        {
            if(message.getMentionedRoles().size() == 0)
            {
                channel.sendMessage("You must specify either a Role or a Member via by mentioning them!").queue();
                return;
            }
            for(Role role : message.getMentionedRoles())
            {
                discordGuild.addMod(role);
                channel.sendMessage("Succesfully added " + role.getName() + " role as a Moderator Role!").queue();
            }


        }
        for(Member member : message.getMentionedMembers())
        {
            discordGuild.addMod(member);
            channel.sendMessage("Succesfully added " + member.getEffectiveName() + " as a Moderator!").queue();
        }
    }

    @Command(name = "unmod", description = "Removes Mod status from Members/Roles\nIf assigning Role, Role name must be specified as argument", usage = "mod @Dyingday\nmod @Dyingday @Alcmene\nmod @admin", channel = ChannelType.GROUP, minArgs = 1)
    private void unmod(Message message, DiscordGuild discordGuild, String[] args, TextChannel channel)
    {
        if(message.getMentionedMembers().size() == 0)
        {
            if(message.getMentionedRoles().size() == 0)
            {
                channel.sendMessage("You must specify either a Role or a Member by mentioning them!").queue();
                return;
            }
            for(Role role : message.getMentionedRoles())
            {
                discordGuild.removeMod(role);
                channel.sendMessage("Succesfully removed " + role.getName() + " role as a Moderator Role!").queue();
            }


        }
        for(Member member : message.getMentionedMembers())
        {
            discordGuild.removeMod(member);
            channel.sendMessage("Succesfully removed " + member.getEffectiveName() + " as a Moderator!").queue();
        }
    }

    @Command(name = "kick", description = "Kicks the specified player!", usage = "kick <@Member> [Reason]", channel = ChannelType.GROUP, minArgs = 1)
    private void kick(Message message, TextChannel channel, Guild guild, String[] args)
    {
        List<Member> mentions = message.getMentionedMembers();
        if(mentions.size() == 0)
        {
            //TODO: Send error
            return;
        }
        if(args.length == 1)
            guild.getController().kick(mentions.get(0)).queue();
        else
            guild.getController().kick(mentions.get(0), Utils.getStringFromArray(args, 1, args.length)).queue();
    }

    @Command(name = "ban", description = "Bans the specified player!", usage = "ban <@Member> [Reason]", channel = ChannelType.GROUP, minArgs = 1)
    private void ban(Message message, TextChannel channel, Guild guild, String[] args)
    {
        List<Member> mentions = message.getMentionedMembers();
        if(mentions.size() == 0)
        {
            //TODO: Send error
            return;
        }
        if(args.length == 1)
            guild.getController().ban(mentions.get(0), 0).queue();
        else
            guild.getController().ban(mentions.get(0), 0, Utils.getStringFromArray(args, 1, args.length)).queue();
    }

    @Command(name = "tempban", description = "Bans the specified player for given number of days!", usage = "ban <@Member> <days> [Reason]", channel = ChannelType.GROUP, minArgs = 1)
    private void tempban(Message message, TextChannel channel, Guild guild, String[] args)
    {
        List<Member> mentions = message.getMentionedMembers();
        int numdays;
        if(mentions.size() == 0)
        {
            //TODO: Send error
            return;
        }
        try
        {
            numdays = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e)
        {
            //TODO: Send error
            return;
        }
        if(args.length == 2)
            guild.getController().ban(mentions.get(0), numdays).queue();
        else
            guild.getController().ban(mentions.get(0), numdays, Utils.getStringFromArray(args, 1, args.length)).queue();
    }

    @Command(name = "slow", description = "Slows down channel\nUsers can only send 1 message every specified period of time\nAuthorised users bypass this!", usage = "slow 5", minArgs = 1, maxArgs = 1, channel = ChannelType.GROUP)
    private void slow(TextChannel channel, DiscordGuild discordGuild, String[] args)
    {
        if(args[0].equalsIgnoreCase("off"))
        {
            discordGuild.removeSlowChannel(channel);
            channel.sendMessage("This channel is no longer in Slow Mode! Feel free to talk to your hearts content!").queue();
            return;
        }
        discordGuild.setSlowChannel(channel, Integer.parseInt(args[0]));
        channel.sendMessage("This channel has been set to Slow Mode! You can only speak once every " + Integer.parseInt(args[0]) + " seconds!").queue();
    }

    @Command(name = "lock", description = "Locks the channel!", usage = "lock", maxArgs = 0, channel = ChannelType.GROUP)
    private void lock(DiscordGuild discordGuild, TextChannel channel)
    {
        discordGuild.lockChannel(channel);
        channel.sendMessage("This channel has been locked :lock:!").queue();
    }

    @Command(name = "unlock", description = "Unlocks the channel!", usage = "unlock", maxArgs = 0, channel = ChannelType.GROUP)
    private void unlock(DiscordGuild discordGuild, TextChannel channel)
    {
        discordGuild.unlockChannel(channel);
        channel.sendMessage("This channel has been unlocked :unlock:!").queue();
    }

    // Music Channel Commands

    @Command(name = "join", description = "Joins the bot to voice channel", usage = "join [VoiceChannelName]", channel = ChannelType.GROUP)
    private void join(String[] args, DiscordGuild discordGuild, TextChannel channel)
    {
        if(args.length == 0)
        {
            MusicManager.connectToFirstVoiceChannel(discordGuild.getGuild().getAudioManager());
        }
        else
        {
            List<VoiceChannel> channels = discordGuild.getGuild().getVoiceChannelsByName(Utils.getStringFromArray(args), true);
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

    @Command(name = "search", description = "Searches YouTube for given parameters", usage = "search I know a song that'll get on your nerves", channel = ChannelType.GROUP, minArgs = 1)
    private void search(TextChannel channel, Member member, String[] args, Message origMessage)
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
            message.addReaction(EmojiParser.parseToUnicode(":x:")).queue();
            new YouTubeSearchReaction(message, new Member[]{member}, results, getStringFromArray(args));
        });
        origMessage.delete().queue();
    }

    @Command(name = "play")
    private void play(TextChannel channel, String[] args, DiscordGuild discordGuild)
    {
        if(args.length == 0)
        {
            MusicManager.getGuildAudioPlayer(discordGuild).setPaused(false);
            return;
        }
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

    @Command(name = "leave", description = "Bot will leave the voice channel", usage = "leave", channel = ChannelType.GROUP, maxArgs = 0)
    private void leave(DiscordGuild discordGuild)
    {
        discordGuild.getGuildMusicManager().stop();
        discordGuild.getGuild().getAudioManager().closeAudioConnection();
    }

    // Games

    @Command(name = "hangman", description = "Bot plays Hangman!", usage = "hangman", maxArgs = 2)
    private void hangman(DiscordGuild discordGuild, User user, TextChannel channel, Message message, Member member, String[] args)
    {
        if(args.length != 0)
        {
            if (args[0].equalsIgnoreCase("join"))
            {
                HangmanGame game = discordGuild.getHangmanGame(Integer.parseInt(args[1]));
                if (game == null)
                {
                    channel.sendMessage("No Hangman Game was found with that ID!").queue();
                    return;
                }
                if (game.addPlayer(member))
                {
                    channel.sendMessage(member.getEffectiveName() + " has been added to Hangman Game: " + game.getId()).queue();
                } else
                    {
                    channel.sendMessage("Sorry, " + member.getNickname() + " but that game is full!").queue();
                }
                return;
            }
            else if (args[0].equalsIgnoreCase("leave"))
            {
                HangmanGame game = discordGuild.getHangmanGame(Integer.parseInt(args[1]));
                if (game == null)
                {
                    channel.sendMessage("No Hangman Game was found with that ID!").queue();
                    return;
                }
                game.removePlayer(member);
                return;
            }
        }
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        String[] alphabetR = new String[27];
        for(int i = 0; i < alphabet.length; i++)
            alphabetR[i] = "regional_indicator_symbol_" + alphabet[i];
        alphabetR[26] = "x";
        int gameNum = discordGuild.getNumHangmanGames() + 1;
        channel.sendMessage("If you would like to join in this game of hangman, please type " + discordGuild.getCommandChar() + "hangman join " + gameNum + "\nGame will start in 30 seconds.").queue(message1 ->
        {
            HangmanGame hg = new HangmanGame(message1, new Member[]{member}, alphabetR, gameNum, discordGuild, member);
            discordGuild.addHungmanGame(hg);
        });

    }
}

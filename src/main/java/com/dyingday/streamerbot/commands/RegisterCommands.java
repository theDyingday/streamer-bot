package com.dyingday.streamerbot.commands;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.music.MusicManager;
import com.dyingday.streamerbot.utils.ExecutorType;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.time.Instant;
import java.util.List;

public class RegisterCommands
{
    private Reference reference = Reference.getReference();
    private MusicManager musicManager = MusicManager.getMusicManager();

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

    @Command(name = "twitch", description = "Add/Remove a twitch account!", usage = "twitch <add|remove> <twitch stream username> <channel name>", channel = ChannelType.GROUP, minArgs = 3, maxArgs = 3)
    private void twitch(String[] args, Guild guild, Member member)
    {
        if(args[0].equalsIgnoreCase("add"))
        {
            // TODO: Implement adding twitch account!

        }
        else if(args[1].equalsIgnoreCase("remove"))
        {
            // TODO: Sort out removing a twitch connection!
        }
    }

    @Command(name = "join", description = "Joins the bot to voice channel", usage = "join [VoiceChannelName]", channel = ChannelType.GROUP, maxArgs = 1)
    private void join(String[] args, DiscordGuild discordGuild, TextChannel channel)
    {
        if(args.length == 0)
        {
            musicManager.connectToFirstVoiceChannel(discordGuild.getGuild().getAudioManager());
        }
        else
        {
            List<VoiceChannel> channels = discordGuild.getGuild().getVoiceChannelsByName(args[0], true);
            if(channels.size() == 0)
            {
                musicManager.connectToFirstVoiceChannel(discordGuild.getGuild().getAudioManager());
                channel.sendMessage("Could not find a channel with that name! Joining default channel; Feel free to move me using /move!").queue();
                return;
            }
            else
            {
                musicManager.connectToVoiceChannel(discordGuild.getGuild().getAudioManager(), channels.get(0));
            }
        }
        channel.sendMessage("Successfully joined channel: " + discordGuild.getGuild().getAudioManager().getConnectedChannel().getName()).queue();
    }
}

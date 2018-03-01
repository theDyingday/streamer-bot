package com.dyingday.streamerbot.commands;

import com.dyingday.streamerbot.utils.ExecutorType;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.time.Instant;
import java.util.List;

public class RegisterCommands
{
    private Reference reference = Reference.getReference();

    @Command(name = "info", description = "Lists all commands and usages", usage = "/info", type = ExecutorType.ALL, maxArgs = 0)
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

    @Command(name = "twitch", description = "Add/Remove a twitch account!", usage = "/twitch <add|remove> <twitch stream username> <channel name>", channel = ChannelType.GROUP, minArgs = 3, maxArgs = 3)
    private void twitch(String[] args, Guild guild, Member member)
    {
        if(args[0].equalsIgnoreCase("add"))
        {
            List<TextChannel> textChannels = reference.jda.getTextChannelsByName(args[2], true);
            for(TextChannel channel : textChannels)
                if(channel.getGuild().equals(guild)) reference.twitch.addTwitchChannel(member, args[1], channel, reference.discordGuilds.get(guild.getIdLong()));
        }
        else if(args[1].equalsIgnoreCase("remove"))
        {
            // TODO: Sort out removing twitch!
        }
    }
}

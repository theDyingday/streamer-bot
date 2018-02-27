package com.dyingday.streamerbot.commands;

import com.dyingday.streamerbot.utils.ExecutorType;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

import java.time.Instant;

public class RegisterCommands
{
    private Reference reference = Reference.getReference();

    @Command(name = "info", description = "Lists all commands and usages", usage = "/info", type = ExecutorType.ALL)
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
}

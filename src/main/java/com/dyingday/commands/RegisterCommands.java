package com.dyingday.commands;

import com.dyingday.StreamerBot;
import com.dyingday.utils.ExecutorType;
import com.dyingday.utils.Reference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

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
                                    .setAuthor("Command Maestro", null, "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/Infobox_info_icon.svg/1024px-Infobox_info_icon.svg.png");
                /*TODO: Implement Temporal Accessor.setTimestamp()*/

                privateChannel.sendMessage(emb.build()).queue();
            }
        });
    }
}

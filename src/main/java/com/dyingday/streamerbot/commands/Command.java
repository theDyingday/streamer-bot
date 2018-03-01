package com.dyingday.streamerbot.commands;

import com.dyingday.streamerbot.utils.ExecutorType;
import net.dv8tion.jda.core.entities.ChannelType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command
{
    String name();
    String description() default "No description has been provided for this discordCommand!";
    String usage() default "No usage has been provided for this discordCommand!";
    int maxArgs() default -1;
    int minArgs() default -1;
    ChannelType channel() default ChannelType.UNKNOWN; /* Unknown is used as a way of setting it to Private or Guild Channel*/
    ExecutorType type() default ExecutorType.ALL;
    // TODO: Implement Command Permission Value (maybe...)
}

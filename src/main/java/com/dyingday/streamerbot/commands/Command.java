package com.dyingday.streamerbot.commands;

import com.dyingday.streamerbot.utils.ExecutorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command
{
    String name();
    String description() default "No description has been provided for this command!";
    String usage() default "No usage has been provided for this command!";
    ExecutorType type() default ExecutorType.ALL;
    // TODO: Implement Command Permission Value (maybe...)
}

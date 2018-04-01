package com.dyingday.streamerbot.commands;

import com.dyingday.streamerbot.utils.ExecutorType;
import net.dv8tion.jda.core.entities.ChannelType;

import java.lang.reflect.Method;

public final class BaseCommand
{
    private final String name, description, usage;
    private final int maxArgs, minArgs;
    private final ChannelType channelType;
    private final ExecutorType executorType;
    private final Object object;
    private final Method method;

    public BaseCommand(String name, String description, String usage, int maxArgs, int minArgs, ChannelType channelType, ExecutorType executorType, Object object, Method method)
    {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.maxArgs = maxArgs;
        this.minArgs = minArgs;
        this.channelType = channelType;
        this.executorType = executorType;
        this.object = object;
        this.method = method;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUsage()
    {
        return usage;
    }

    public int getMaxArgs()
    {
        return maxArgs;
    }

    public int getMinArgs()
    {
        return minArgs;
    }

    public ChannelType getChannelType()
    {
        return channelType;
    }

    public ExecutorType getExecutorType()
    {
        return executorType;
    }

    public Object getObject()
    {
        return object;
    }

    public Method getMethod()
    {
        return method;
    }
}

package com.dyingday.streamerbot.commands;

import com.dyingday.streamerbot.utils.ExecutorType;

import java.lang.reflect.Method;

public final class BaseCommand
{
    private final String name, description, usage;
    private final ExecutorType executorType;
    private final Object object;
    private final Method method;

    public BaseCommand(String name, String description, String usage, ExecutorType executorType, Object object, Method method)
    {
        this.name = name;
        this.description = description;
        this.usage = usage;
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

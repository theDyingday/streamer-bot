package com.dyingday.streamerbot.commands;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public final class CommandMap
{
    private Reference reference = Reference.getReference();

    public CommandMap()
    {
        registerCommands(new RegisterCommands());
    }

    private void registerCommands(Object... objects)
    {
        for(Object object : objects)
        {
            registerCommand(object);
        }
    }

    private void registerCommand(Object object)
    {
        for(Method method : object.getClass().getDeclaredMethods())
        {
            if(method.isAnnotationPresent(Command.class))
            {
                Command command = method.getAnnotation(Command.class);
                method.setAccessible(true);
                BaseCommand baseCommand = new BaseCommand(command.name(), command.description(), command.usage(), command.type(), object, method);
                reference.commands.put(command.name().toLowerCase(), baseCommand);
            }
        }
    }

    public void command(MessageReceivedEvent event)
    {
        Object[] object = getCommand(event.getMessage().getContentRaw());
        BaseCommand baseCommand = (BaseCommand) object[0];
        if(object[0] == null)
        {
            return;
        }
        try
        {
            execute(baseCommand, ((BaseCommand) object[0]).getName(), (String[])object[1], event);
        }
        catch (Exception e)
        {
            System.out.println("The method " + baseCommand.getMethod().getName() + " isn't correct!");
        }
    }

    private Object[] getCommand(String command)
    {
        command = command.replace("/", "");
        String[] commandSplit = command.split(" ");
        String[] args = new String[commandSplit.length-1];

        System.arraycopy(commandSplit, 1, args, 0, commandSplit.length - 1);

        BaseCommand baseCommand = reference.commands.get(commandSplit[0]);
        return new Object[]{baseCommand, args};
    }

    private void execute(BaseCommand baseCommand, String command, String[] args, MessageReceivedEvent event)
    {
        Parameter[] parameters = baseCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for(int i = 0; i < parameters.length; i++)
        {
            if(parameters[i].getType() == String[].class) objects[i] = args;
            else if(parameters[i].getType() == User.class) objects[i] = event.getAuthor();
            else if(parameters[i].getType() == TextChannel.class) objects[i] = event.getTextChannel();
            else if(parameters[i].getType() == PrivateChannel.class) objects[i] = event.getPrivateChannel();
            else if(parameters[i].getType() == Guild.class) objects[i] = event.getGuild();
            else if(parameters[i].getType() == String.class) objects[i] = command;
            else if(parameters[i].getType() == Message.class) objects[i] = event.getMessage();
            else if(parameters[i].getType() == JDA.class) objects[i] = reference.jda;
            else if(parameters[i].getType() == MessageChannel.class) objects[i] = event.getChannel();
            else if(parameters[i].getType() == BaseCommand.class) objects[i] = baseCommand;
            else if(parameters[i].getType() == DiscordGuild.class) objects[i] = reference.discordGuilds.get(event.getGuild().getIdLong());
        }
        try
        {
            baseCommand.getMethod().invoke(baseCommand.getObject(), objects);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
}

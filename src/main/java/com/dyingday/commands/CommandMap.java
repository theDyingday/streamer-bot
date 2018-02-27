package com.dyingday.commands;

import com.dyingday.objects.Server;
import com.dyingday.utils.ExecutorType;
import com.dyingday.utils.Reference;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    public boolean command(User user, Message message, MessageReceivedEvent event)
    {
        Object[] object = getCommand(message.getContentRaw());
        BaseCommand baseCommand = (BaseCommand) object[0];
        if(object[0] == null)
        {
            return false;
        }
        try
        {
            execute(baseCommand, ((BaseCommand) object[0]).getName(), (String[])object[1], message);
        }
        catch (Exception e)
        {
            System.out.println("The method " + baseCommand.getMethod().getName() + " isn't correct!");
        }
        return true;
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

    private void execute(BaseCommand baseCommand, String command, String[] args, Message message)
    {
        Parameter[] parameters = baseCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for(int i = 0; i < parameters.length; i++)
        {
            if(parameters[i].getType() == String[].class) objects[i] = args;
            else if(parameters[i].getType() == User.class) objects[i] = message == null ? null : message.getAuthor();
            else if(parameters[i].getType() == TextChannel.class) objects[i] = message == null ? null : message.getTextChannel();
            else if(parameters[i].getType() == PrivateChannel.class) objects[i] = message == null ? null : message.getPrivateChannel();
            else if(parameters[i].getType() == Guild.class) objects[i] = message == null ? null : message.getGuild();
            else if(parameters[i].getType() == String.class) objects[i] = command;
            else if(parameters[i].getType() == Message.class) objects[i] = message;
            else if(parameters[i].getType() == JDA.class) objects[i] = reference.jda;
            else if(parameters[i].getType() == MessageChannel.class) objects[i] = message == null ? null : message.getChannel();
            else if(parameters[i].getType() == BaseCommand.class) objects[i] = baseCommand;
            else if(parameters[i].getType() == Server.class) objects[i] = message == null ? null : reference.servers.get(message.getGuild().getIdLong());
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

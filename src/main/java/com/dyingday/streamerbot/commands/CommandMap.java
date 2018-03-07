package com.dyingday.streamerbot.commands;

import com.dyingday.streamerbot.discord.DiscordGuild;
import com.dyingday.streamerbot.twitch.TwitchChannel;
import com.dyingday.streamerbot.utils.ExecutorType;
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
                BaseCommand baseCommand = new BaseCommand(command.name(), command.description(), command.usage(), command.maxArgs(), command.minArgs(), command.channel(), command.type(), object, method);
                reference.commands.put(command.name().toLowerCase(), baseCommand);
            }
        }
    }

    // Discord Command
    public void discordCommand(MessageReceivedEvent event, String commandChar)
    {
        Object[] object = getCommand(event.getMessage().getContentRaw(), commandChar);
        BaseCommand command = (BaseCommand) object[0];
        String[] args = (String[]) object[1];
        if(object[0] == null )
        {
            return;
        }
        else if((event.getChannel() instanceof PrivateChannel && command.getChannelType() == ChannelType.GROUP)
                || event.getChannel() instanceof TextChannel && command.getChannelType() == ChannelType.PRIVATE)
        {
            return;
        }
        else if(command.getMaxArgs() != -1 && args.length > command.getMaxArgs())
        {
            event.getChannel().sendMessage("You have entered too many arguments!").queue();
            return;
        }
        else if(command.getMinArgs() != -1 && args.length < command.getMinArgs())
        {
            event.getChannel().sendMessage("You have entered too few arguments!").queue();
            return;
        }
        else if(command.getExecutorType() == ExecutorType.TWITCH)
        {
            return;
        }
        try
        {
            execute(command, args, event);
        }
        catch (Exception e)
        {
            System.out.println("The method " + command.getMethod().getName() + " isn't correct!");
        }
    }

    public void twitchCommand(TwitchChannel channel, String message, String sender)
    {
        Object[] object = getCommand(message, channel.getCommandChar());
        BaseCommand command = (BaseCommand) object[0];
        String[] args = (String[]) object[1];
        if(object[0] == null) return;
        else if(command.getMaxArgs() != -1 && args.length > command.getMaxArgs())
        {
            reference.twitch.sendMessage(channel.getChannel(), sender + " you have entered too many arguments for the " + command.getName() + " command!");
            return;
        }
        else if(command.getMinArgs() != -1 && args.length < command.getMinArgs())
        {
            reference.twitch.sendMessage(channel.getChannel(), sender + " you have entered too few arguments for the " + command.getName() + " command!");
            return;
        }
        else if(command.getExecutorType() == ExecutorType.DISCORD)
        {
            return;
        }
        try
        {
            execute(command, args, channel, sender);
        }
        catch (Exception e)
        {
            System.out.println("The method " + command.getMethod().getName() + " isn't correct!");
        }
    }

    private Object[] getCommand(String message, String commandChar)
    {
        message = message.replace(commandChar, "");
        String[] commandSplit = message.split(" ");
        String[] args = new String[commandSplit.length-1];

        System.arraycopy(commandSplit, 1, args, 0, commandSplit.length - 1);

        BaseCommand baseCommand = reference.commands.get(commandSplit[0]);
        return new Object[]{baseCommand, args};
    }

    private void execute(BaseCommand command, String[] args, MessageReceivedEvent event)
    {
        Parameter[] parameters = command.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for(int i = 0; i < parameters.length; i++)
        {
            if(parameters[i].getType() == String[].class) objects[i] = args;
            else if(parameters[i].getType() == User.class) objects[i] = event.getAuthor();
            else if(parameters[i].getType() == TextChannel.class) objects[i] = event.getTextChannel();
            else if(parameters[i].getType() == PrivateChannel.class) objects[i] = event.getPrivateChannel();
            else if(parameters[i].getType() == Guild.class) objects[i] = event.getGuild();
            else if(parameters[i].getType() == String.class) objects[i] = command.getName();
            else if(parameters[i].getType() == Message.class) objects[i] = event.getMessage();
            else if(parameters[i].getType() == JDA.class) objects[i] = reference.jda;
            else if(parameters[i].getType() == MessageChannel.class) objects[i] = event.getChannel();
            else if(parameters[i].getType() == BaseCommand.class) objects[i] = command;
            else if(parameters[i].getType() == DiscordGuild.class) objects[i] = reference.discordGuilds.get(event.getGuild().getIdLong());
        }
        try
        {
            command.getMethod().invoke(command.getObject(), objects);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    private void execute(BaseCommand command, String[] args, TwitchChannel channel, String sender) {
        Parameter[] parameters = command.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++)
        {
            if(parameters[i].getType() == String[].class) objects[i] = args;
            else if(parameters[i].getType() == String.class) objects[i] = sender;
            else if(parameters[i].getType() == TwitchChannel.class) objects[i] = channel;
            else if(parameters[i].getType() == DiscordGuild.class) objects[i] = reference.twitchConnections.get(channel);
        }
        try
        {
            command.getMethod().invoke(command.getObject(), objects);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
}

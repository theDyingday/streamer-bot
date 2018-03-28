package com.dyingday.streamerbot.discord;

import com.dyingday.streamerbot.music.GuildMusicManager;
import com.dyingday.streamerbot.music.MusicManager;
import com.dyingday.streamerbot.twitch.TwitchChannel;
import com.dyingday.streamerbot.utils.Reference;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscordGuild
{
    private final long guildID;
    private final Guild guild;
    private Member owner;
    private String commandChar = "/";
    private final GuildMusicManager guildMusicManager;
    private TextChannel musicChannel;
    private Reference reference = Reference.getReference();
    private ArrayList<TwitchChannel> connectedTwitchChannels = new ArrayList<>();
    private ArrayList<String> bannedWords = new ArrayList<>();
    private ArrayList<Member> moderators = new ArrayList<>();
    private ArrayList<Role> moderatorRoles = new ArrayList<>();
    private ArrayList<HangmanGame> hangmanGames = new ArrayList<>();
    private Map<User, Integer> userWarnings = new HashMap<>();
    private Map<TextChannel, SlowChannel> slowChannels = new HashMap<>();
    private Map<TextChannel, List<PermissionOverride>> lockedChannels = new HashMap<>();
    private int maxWarnings = 3;
    private int numHangmanGames = 0;

    public DiscordGuild(long guildID, boolean setup)
    {
        this.guildID = guildID;
        this.guild = reference.jda.getGuildById(guildID);
        this.guildMusicManager = MusicManager.getGuildMusicManager(this);

        reference.discordGuilds.put(guildID, this);

        owner = getGuild().getOwner();

        if(!setup) return;

        owner.getUser().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Thank you for adding the Streamer Bot to your guild!\nEnjoy your time with it!").queue();
            // TODO: Implement Bot Setup
        });
    }

    public void addMod(Member member)
    {
        if(moderators.contains(member)) return;
        moderators.add(member);
    }

    public void removeMod(Member member)
    {
        if(!moderators.contains(member)) return;
        moderators.remove(member);
    }

    public void addMod(Role role)
    {
        if(moderatorRoles.contains(role)) return;
        moderatorRoles.add(role);
    }

    public void removeMod(Role role)
    {
        if(!moderatorRoles.contains(role)) return;
        moderatorRoles.remove(role);
    }

    public HangmanGame getHangmanGame(int gameID)
    {
        for(HangmanGame game : hangmanGames)
            if(game.getId() == gameID)
                return game;
        return null;
    }

    public void endHangmanGame(HangmanGame hg)
    {
        hangmanGames.remove(hg);
    }

    public int getNumHangmanGames()
    {
        return numHangmanGames;
    }

    public void addHungmanGame(HangmanGame hr)
    {
        numHangmanGames++;
        hangmanGames.add(hr);
    }

    public void addTwitchChannel(TwitchChannel channel)
    {
        if(!connectedTwitchChannels.contains(channel)) connectedTwitchChannels.add(channel);
    }

    public void removeTwitchChannel(TwitchChannel channel)
    {
        if(connectedTwitchChannels.contains(channel)) connectedTwitchChannels.remove(channel);
    }

    public TwitchChannel[] getConnectedTwitchChannels()
    {
        return connectedTwitchChannels.toArray(new TwitchChannel[connectedTwitchChannels.size()]);
    }

    public Guild getGuild()
    {
        return reference.jda.getGuildById(guildID);
    }

    public long getGuildID()
    {
        return guildID;
    }

    public Member getOwner()
    {
        return owner;
    }

    public void setOwner(Member owner)
    {
        this.owner = owner;
    }

    public String getCommandChar()
    {
        return commandChar;
    }

    public GuildMusicManager getGuildMusicManager()
    {
        return guildMusicManager;
    }

    public void setMusicChannel(TextChannel musicChannel)
    {
        this.musicChannel = musicChannel;
    }

    public TextChannel getMusicChannel()
    {
        return musicChannel == null ? getGuild().getDefaultChannel() : musicChannel;
    }

    public ArrayList<String> getBannedWords()
    {
        return bannedWords;
    }

    public boolean isWarned(User user)
    {
        return userWarnings.keySet().contains(user);
    }

    public int getWarnings(User user)
    {
        if(!isWarned(user)) return 0;
        return userWarnings.get(user);
    }

    public void addWarning(User user)
    {
        if(!userWarnings.containsKey(user))
        {
            userWarnings.put(user, 1);
            return;
        }
        userWarnings.replace(user, userWarnings.get(user) + 1);
        if(userWarnings.get(user) > maxWarnings)
        {
            getGuild().getController().ban(user, 0, "User surpassed the maximum number of warnings!").queue();
            userWarnings.remove(user);
        }
    }

    public void removeWarning(User user)
    {
        userWarnings.replace(user, userWarnings.get(user) - 1);
        if(userWarnings.get(user) == 0) userWarnings.remove(user);
    }

    public boolean isMemberMod(Member member)
    {
        return moderators.contains(member);
    }

    public ArrayList<Member> getModerators()
    {
        return moderators;
    }

    public void setSlowChannel(TextChannel channel, int timeout)
    {
        if(slowChannels.containsKey(channel))
        {
            slowChannels.get(channel).setTimeout(timeout);
            return;
        }
        slowChannels.put(channel, new SlowChannel(channel, timeout));
    }

    public void removeSlowChannel(TextChannel channel)
    {
        if(slowChannels.containsKey(channel)) slowChannels.remove(channel);
    }

    public boolean isMessageAllowed(TextChannel channel, Member member, Message message)
    {
        for(Role role : moderatorRoles)
            if(member.getRoles().contains(role)) return true;
        return !slowChannels.containsKey(channel) || moderators.contains(member) || slowChannels.get(channel).canMessage(member, message.getCreationTime());
    }

    public void lockChannel(TextChannel channel)
    {
        lockedChannels.put(channel, channel.getPermissionOverrides());
        for(PermissionOverride po : channel.getPermissionOverrides())
            channel.getPermissionOverride(po.getRole()).delete().queue();
        channel.getPermissionOverride(guild.getPublicRole()).getManager().deny(Permission.MESSAGE_WRITE).grant(Permission.MESSAGE_READ).queue();
        for(Role role : moderatorRoles)
        {
            try {
                channel.createPermissionOverride(role).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_READ).queue();
            }
            catch (IllegalStateException e)
            {
                channel.getPermissionOverride(role).getManager().grant(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
            }

        }
    }

    public void unlockChannel(TextChannel channel)
    {
        channel.getPermissionOverride(guild.getPublicRole()).delete().queue();
        for(Role role : moderatorRoles)
            channel.getPermissionOverride(role).delete().queue();
        for(PermissionOverride po : lockedChannels.get(channel))
        {
            if(po.getRole() == guild.getPublicRole())
            {
                channel.getPermissionOverride(po.getRole()).getManager().grant(po.getAllowed()).deny(po.getDenied()).queue();
                continue;
            }
            channel.createPermissionOverride(po.getRole()).setAllow(po.getAllowed()).setDeny(po.getDenied()).queue();
        }
        lockedChannels.remove(channel);
    }
}

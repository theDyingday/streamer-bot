package com.dyingday.streamerbot.discord;

import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HangmanGame extends MessageReactionListener
{
    private String sol = "happiness";
    private int id;
    private Message originMessage;
    private DiscordGuild discordGuild;
    private List<Member> players = new ArrayList<>();
    private Member current;
    private Member last;
    private int wrong = 0;
    private List<Character> guesses = new ArrayList<>();
    private List<Character> correctGuesses = new ArrayList<>();
    private EmbedBuilder currentMessageEmb;
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private final String imageURL = "https://www.oligalma.com/downloads/images/hangman/files/";

    static int countdown = 5;

    public HangmanGame(Message originMessage, Member[] membersCanReact, String[] emotesCanAdd, int id, DiscordGuild discordGuild, Member member)
    {
        super(originMessage, membersCanReact, emotesCanAdd);
        this.id = id;
        this.originMessage = originMessage;
        this.discordGuild = discordGuild;
        this.current = member;
        players.add(member);

        Timer countdownTimer = new Timer("HangmanCountdown" + id);
        TimerTask countdownTimerTask = new TimerTask() {
            @Override
            public void run() {
                countdown--;
                originMessage.editMessage("If you would like to join in this game of hangman, please type " + discordGuild.getCommandChar() + "hangman join " + id + "\nGame will start in " + countdown + " seconds.").queue();
                if(countdown == 0)
                {
                    startGame();
                    countdownTimer.cancel();
                    countdownTimer.purge();
                }
            }
        };
        countdownTimer.scheduleAtFixedRate(countdownTimerTask, 1000, 1000);
    }

    @Override
    public void onReaction(String emote, MessageReactionAddEvent event)
    {
        nextPlayer();
        boolean right = false;
        if(emote.equalsIgnoreCase("x"))
        {
            discordGuild.endHangmanGame(this);
            removeListener();
            return;
        }
        char guess = emote.charAt(emote.length() - 1);
        if(guesses.contains(guess))
        {
            event.getReaction().removeReaction().queue();
            return;
        }
        currentMessageEmb.setDescription("");
        for(char letter : sol.toCharArray())
        {
            if(letter == guess || correctGuesses.contains(letter))
            {
                currentMessageEmb.appendDescription(":regional_indicator_" + letter + ":");
                if(!correctGuesses.contains(letter)) correctGuesses.add(letter);
                if(letter == guess) right = true;
            }
            else
            {
                currentMessageEmb.appendDescription(":heavy_minus_sign:");
            }
        }
        guesses.add(guess);
        if(!right)
            wrong++;
        currentMessageEmb.setImage(imageURL + String.valueOf(wrong) + ".jpg");
        if(wrong == 10)
        {
            getMessage().clearReactions().queue();
            currentMessageEmb.setTitle("Hangman - You LOST!");
            currentMessageEmb.setDescription("");
            for(char letter : sol.toCharArray())
                currentMessageEmb.appendDescription(":regional_indicator_" + letter + ":");
            currentMessageEmb.setFooter("", null);
        }
        else if (!right)
            currentMessageEmb.setTitle("Hangman - Letter " + guess + " is incorrect! - " + current.getEffectiveName() + "'s turn next!");
        else
            currentMessageEmb.setTitle("Hangman - " + last.getEffectiveName() + " got the letter " + guess + " correct! - " + current.getEffectiveName() + "'s turn next!");
        getMessage().editMessage(currentMessageEmb.build()).queue();
    }

    private void startGame()
    {
        EmbedBuilder hmEmb = new EmbedBuilder().setColor(Color.BLUE).setTitle("Hangman - Guess the Word!").setImage(imageURL + "0.jpg").setFooter("React with the letter you wish to test! (use regional_indicator ones. Example: :regional_indicator_a:", null);
        for(int i = 0; i < sol.length(); i++)
            hmEmb.appendDescription(":heavy_minus_sign:");
        currentMessageEmb = hmEmb;
        originMessage.getChannel().sendMessage(hmEmb.build()).queue(message -> {
            message.addReaction(EmojiParser.parseToUnicode(":x:")).queue();
            setMessage(message);
        });
    }

    private void nextPlayer()
    {
        last = current;
        current = players.get(players.indexOf(current) + 1 % players.size());
        ArrayList<Member> canReact = new ArrayList<>();
        canReact.add(current);
        setMembersCanReact(canReact);
    }

    public boolean addPlayer(Member member)
    {
        if(players.size() == 4)
        {
            return false;
        }
        players.add(member);
        return true;
    }

    public void removePlayer(Member member)
    {
        if(players.contains(member))
        {
            players.remove(member);
            originMessage.getChannel().sendMessage(member.getEffectiveName() + " is no longer participating in Hangman game " + id + "!").queue();
            if(current == member)
                nextPlayer();
        }
    }

    public int getId()
    {
        return id;
    }
}

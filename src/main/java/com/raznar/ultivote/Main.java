package com.raznar.ultivote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.raznar.ultivote.commands.UltiVoteCMD;
import com.raznar.ultivote.commands.VoteCMD;
import com.raznar.ultivote.listeners.ConnectionListener;
import com.raznar.ultivote.listeners.VoteListener;
import com.raznar.ultivote.utils.Closer;
import com.vexsoftware.votifier.model.Vote;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class Main extends JavaPlugin {

    public static final Gson GSON = new GsonBuilder().create();
    public static final JsonParser PARSER = new JsonParser();

    @Getter private static Main instance;

    private final int latestConfigVersion = 1;
    /** The vote count until it reaches the goal to activate the Vote Party */
    @Getter private int voteCount = 0;
    /** The file to store the late rewarded votes */
    private final File lateRewardsFile = new File(this.getDataFolder(), "late-rewards.json");

    /**
     * Stores all {@link Vote} data for those who didn't get their rewards yet
     * Each data will be deleted if the rewards are given to the players accordingly
     * <p>
     * NOTE: The reason I put it as a concurrent set is so we can support multi-data modifications
     * although it can be performed by using the synchronized block or duplicating data
     * I prefer the lazy way. ~Alviannn
     * <p>
     * WARNING: Please don't use concurrent related codes unless it's needed.
     */
    @Getter private final Set<Vote> lateRewards = ConcurrentHashMap.newKeySet();

    @Override
    public void onEnable() {
        instance = this;
        this.getLogger().info("#--------------------------------------------#\n" +
                "#      _   _ _ _   ___     __    _           #\n" +
                "#     | | | | | |_(_) \\   / /__ | |_ ___     #\n" +
                "#     | | | | | __| |\\ \\ / / _ \\| __/ _ \\    #\n" +
                "#     | |_| | | |_| | \\ V / (_) | ||  __/    #\n" +
                "#      \\___/|_|\\__|_|  \\_/ \\___/ \\__\\___|    #\n" +
                "#                                            #\n" +
                "#--------------------------------------------#");
        this.getLogger().info("Loading....");

        this.setupConfig();
        this.readLateRewards();

        this.setupListener();
        this.setupCommands();

        // this task will handle the late rewards vote
        //
        // so every players who hasn't received their rewards yet
        // will be given the rewards later after a certain amount of time (in this case every 10 seconds)
        //
        // this also handles saving the late rewards data
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            this.saveLateRewards();

            val duplicateList = new HashSet<>(lateRewards);
            if (duplicateList.isEmpty())
                return;

            for (Vote vote : duplicateList) {
                val receiver = Bukkit.getPlayerExact(vote.getUsername());
                if (receiver == null)
                    continue;

                this.giveVoteRewards(vote);
            }
        }, 20L, 200L);
        this.getLogger().info("Successfully loaded!");
    }

    @Override
    public void onDisable() {
        instance = null;

        this.saveLateRewards();
        lateRewards.clear();
    }

    /**
     * Handles increasing the vote count by also saving the value to the config.yml
     *
     * @return the {@link #voteCount} after getting incremented
     */
    @SuppressWarnings("UnusedReturnValue")
    public int increaseVoteCount() {
        voteCount++;

        this.getConfig().set("vote-count", voteCount);
        this.saveConfig();

        return voteCount;
    }

    /**
     * Handles giving vote rewards to the player
     * if the player is somehow offline it'll be automatically saved in the 'late rewards' list
     *
     * @param vote the vote data
     */
    public void giveVoteRewards(Vote vote) {
        val username = vote.getUsername();
        val receiver = Bukkit.getPlayerExact(username);

        if (receiver == null) {
            // stores the vote data if the player is offline
            lateRewards.add(vote);
            return;
        }

        // removes the vote data if the player is online
        // this is to clean if the player has the data stored inside the list
        lateRewards.remove(vote);

        val commands = this.getConfig().getStringList("on-player-vote.commands");
        val soundSection = this.getConfig().getConfigurationSection("on-player-vote.sound");
        val soundEffect = soundSection.getString("sound-effect");
        val effect = this.getConfig().getString("on-player-vote.effect");

        // --- Play a sound effect

        if (!soundEffect.isEmpty() && !soundEffect.equals("NONE"))
            receiver.playSound(receiver.getLocation(),
                    Sound.valueOf(soundEffect),
                    (float) soundSection.get("sound.volume"),
                    (float) soundSection.get("sound.pitch")
            );

        // --- Play an effect

        if (!effect.isEmpty() && !effect.equals("NONE"))
            receiver.playEffect(receiver.getLocation(), Effect.valueOf(effect), (Object) 1);

        // --- Executes commands as console

        if (!commands.isEmpty())
            for (String cmd : commands)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", receiver.getName()));
    }

    private void readLateRewards() {
        if (!lateRewardsFile.exists())
            return;

        try (Closer closer = new Closer()) {
            val reader = closer.add(new FileReader(lateRewardsFile));
            // grabs the all old vote data
            val list = PARSER.parse(reader).getAsJsonArray();

            for (val element : list) {
                val data = element.getAsJsonObject();
                val vote = GSON.fromJson(data, Vote.class);

                lateRewards.add(vote);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveLateRewards() {
        try (Closer closer = new Closer()) {
            val fileWriter = closer.add(new FileWriter(lateRewardsFile));
            val writer = closer.add(new PrintWriter(fileWriter));

            val jsonString = GSON.toJson(lateRewards);
            writer.println(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * setups the listeners
     */
    private void setupListener() {
        val manager = Bukkit.getPluginManager();

        manager.registerEvents(new VoteListener(this), this);
        manager.registerEvents(new ConnectionListener(this), this);
    }

    /**
     * setups the commands
     */
    private void setupCommands() {
        this.getCommand("ultivote").setExecutor(new UltiVoteCMD(this));
        this.getCommand("vote").setExecutor(new VoteCMD(this));
    }

    /**
     * setups the config.yml
     * this includes a config update checker
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void setupConfig() {
        File folder = this.getDataFolder();

        if (!folder.exists())
            folder.mkdirs();

        // gets config.yml from plugins/UltiVote
        File configFile = new File(folder, "config.yml");

        if (!configFile.exists())
            this.saveDefaultConfig();

        int currentConfigVersion = this.getConfig().getInt("config-version");

        // if they're using an older config.yml, tell them that its outdated
        // if they have the latest config or they're using a 'secret' code as their config-version, return
        if (currentConfigVersion != latestConfigVersion && currentConfigVersion != 6969)
            this.getLogger().severe("Config.yml is outdated. Please update your config.yml!");
    }

}
package me.tulio.yang;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.arena.Arena;
import me.tulio.yang.arena.ArenaListener;
import me.tulio.yang.chat.impl.Chat;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.clan.ClanListener;
import me.tulio.yang.essentials.Essentials;
import me.tulio.yang.essentials.EssentialsListener;
import me.tulio.yang.essentials.MainCommand;
import me.tulio.yang.essentials.command.CreateWorldCommand;
import me.tulio.yang.event.Event;
import me.tulio.yang.event.game.EventGame;
import me.tulio.yang.event.game.EventGameListener;
import me.tulio.yang.event.game.map.EventGameMap;
import me.tulio.yang.hotbar.Hotbar;
import me.tulio.yang.hotbar.HotbarListener;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.kit.KitEditorListener;
import me.tulio.yang.knockback.Knockback;
import me.tulio.yang.leaderboard.Leaderboard;
import me.tulio.yang.leaderboard.LeaderboardListener;
import me.tulio.yang.leaderboard.PlaceholderAPI;
import me.tulio.yang.match.Match;
import me.tulio.yang.match.MatchListener;
import me.tulio.yang.nametags.NameTag;
import me.tulio.yang.nametags.YangTags;
import me.tulio.yang.party.Party;
import me.tulio.yang.party.PartyListener;
import me.tulio.yang.party.classes.ClassTask;
import me.tulio.yang.party.classes.archer.ArcherClass;
import me.tulio.yang.party.classes.bard.BardEnergyTask;
import me.tulio.yang.party.classes.bard.BardListener;
import me.tulio.yang.party.classes.rogue.RogueClass;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileListener;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.profile.category.Category;
import me.tulio.yang.profile.category.CategoryCreateListener;
import me.tulio.yang.profile.deatheffects.DeathEffect;
import me.tulio.yang.profile.file.impl.FlatFileIProfile;
import me.tulio.yang.profile.file.impl.MongoDBIProfile;
import me.tulio.yang.profile.modmode.ModModeListener;
import me.tulio.yang.queue.Queue;
import me.tulio.yang.queue.QueueListener;
import me.tulio.yang.scoreboard.BoardAdapter;
import me.tulio.yang.scoreboard.impl.Assemble;
import me.tulio.yang.tablist.TabAdapter;
import me.tulio.yang.tablist.TabType;
import me.tulio.yang.tablist.impl.TabList;
import me.tulio.yang.tournament.TournamentListener;
import me.tulio.yang.utilities.DiscordWebhook;
import me.tulio.yang.utilities.InventoryUtil;
import me.tulio.yang.utilities.Server;
import me.tulio.yang.utilities.TaskUtil;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.CommandManager;
import me.tulio.yang.utilities.file.languaje.LanguageConfigurationFile;
import me.tulio.yang.utilities.file.type.BasicConfigurationFile;
import me.tulio.yang.utilities.license.License;
import me.tulio.yang.utilities.menu.MenuListener;
import me.tulio.yang.utilities.playerversion.PlayerVersionHandler;
import me.tulio.yang.utilities.rank.RankManager;
import me.tulio.yang.utilities.string.Animation;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bstats.charts.SimpleBarChart;
import org.bstats.charts.SingleLineChart;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class Yang extends JavaPlugin {

    private LanguageConfigurationFile lang;
    private BasicConfigurationFile mainConfig, databaseConfig, arenasConfig, kitsConfig, eventsConfig,
            scoreboardConfig, coloredRanksConfig, tabLobbyConfig, tabEventConfig, tabSingleFFAFightConfig,
            tabSingleTeamFightConfig, tabPartyFFAFightConfig, tabPartyTeamFightConfig, leaderboardConfig,
            langConfig, hotbarConfig, playersConfig, clansConfig, worldsConfig, optionsConfig, queueConfig,
            deathEffectsInvConfig, discordWebhookConfig;
    private Essentials essentials;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private RankManager rankManager;
    private License license;
    public boolean placeholderAPI = false, serverLoaded = false, lunarClient = false, webHook = false;
    public int inQueues, inFightsTotal, inFightsUnRanked, inFightsRanked, bridgeRounds, rankedSumoRounds;
    public TabList tabList;
    public Assemble assemble;
    public DiscordWebhook discordWebhook;

    @Override
    public void onEnable() {
        loadConfig();
        if (!getDescription().getAuthors().contains("Arjona") || !getDescription().getName().equals("Yang")) {
            for (int i = 0; i < 30; i++)
                Bukkit.getConsoleSender().sendMessage(CC.translate("&cERROR | The plugin.yml has been change? :)"));
            Bukkit.getPluginManager().disablePlugins();
            Bukkit.shutdown();
            return;
        }

        this.license = new License(getConfig().getString("LICENSE"), Server.getIP() + ":" + getServer().getPort(), this);
        CC.successfullyLicense();
        license.check();

        if (license.isValid()) {
            CC.checkLicense(license);
            loadConfigMethods();
            loadSaveMethod();
            loadEssentials();
            loadWebhook();
            initManagers();

            registerListeners();
            registerCommands();

            removeCrafting();
            setUpWorld();

            loadPackets();
            loadbStats();

            runTasks();

            CC.loadPlugin();
        } else {
            CC.failedLicense();
            TaskUtil.runLater(() -> {
                Bukkit.shutdown();
                System.exit(0);
            }, 20L);
        }
    }

    @Override
    public void onDisable() {
        if (tabList != null) tabList.disable();
        if (assemble != null) assemble.getRunnable().stop();
        for (Profile value : Profile.getProfiles().values()) {
            if (value.isOnline()) value.save();
        }
        if (Profile.getIProfile() instanceof FlatFileIProfile) {
            playersConfig.save();
            playersConfig.reload();
        }
        if (EventGame.getActiveGame() != null) EventGame.getActiveGame().getGameLogic().cancelEvent();
        Match.cleanup();
        for (Clan value : Clan.getClans().values()) {
            value.save();
        }
        for (Kit kit : Kit.getKits()) {
            kit.save();
        }
        kitsConfig.reload();
        for (Arena arena : Arena.getArenas()) {
            arena.save();
        }
        arenasConfig.reload();
        Category.save();
        mainConfig.reload();
        worldsConfig.getConfiguration().set("WORLDS", CreateWorldCommand.VOID_WORLDS);
        worldsConfig.save();
        worldsConfig.reload();
    }

    private void initManagers() {
        this.essentials = new Essentials(this);
        this.rankManager = new RankManager();
        Hotbar.init();
        Kit.init();
        Arena.init();
        Profile.init();
        Match.init();
        Party.init();
        Knockback.init();
        Event.init();
        EventGameMap.init();
        Category.init();
        Clan.init();
        Queue.init();
        Animation.init();
        NameTag.hook();
        BoardAdapter.hook();
        Leaderboard.init();
        PlayerVersionHandler.init();
        Chat.init();
        DeathEffect.init();
        NameTag.registerProvider(new YangTags());
        if (mainConfig.getBoolean("TABLIST_ENABLE")) tabList = new TabList(this, new TabAdapter());
        if (mainConfig.getBoolean("MATCH.ENABLE_LUNAR_THINGS")) {
            if (getServer().getPluginManager().getPlugin("LunarClient-API") != null) {
                lunarClient = true;
            } else {
                System.out.println("[Yang] BukkitAPI is not installed!");
            }
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            System.out.println("[Yang] PlaceholderAPI found, registering placeholders");
            placeholderAPI = true;
            new PlaceholderAPI().register();
        }
    }

    private void loadConfig() {
        this.mainConfig = new BasicConfigurationFile(this, "config");
        this.lang = new LanguageConfigurationFile(this, "languages/lang");
        this.databaseConfig = new BasicConfigurationFile(this, "database");
        this.coloredRanksConfig = new BasicConfigurationFile(this, "colored-ranks");
        this.scoreboardConfig = new BasicConfigurationFile(this, "scoreboard");
        this.langConfig = new BasicConfigurationFile(this, "global-lang");
        this.hotbarConfig = new BasicConfigurationFile(this, "hotbar");
        this.discordWebhookConfig = new BasicConfigurationFile(this, "discord-webhook");
        this.eventsConfig = new BasicConfigurationFile(this, "inventories/events");
        this.leaderboardConfig = new BasicConfigurationFile(this, "inventories/leaderboard");
        this.optionsConfig = new BasicConfigurationFile(this, "inventories/options");
        this.queueConfig = new BasicConfigurationFile(this, "inventories/queue");
        this.deathEffectsInvConfig = new BasicConfigurationFile(this, "inventories/death-effects");
        this.tabEventConfig = new BasicConfigurationFile(this, "tablist/event");
        this.tabLobbyConfig = new BasicConfigurationFile(this, "tablist/lobby");
        this.tabSingleFFAFightConfig = new BasicConfigurationFile(this, "tablist/SingleFFAFight");
        this.tabSingleTeamFightConfig = new BasicConfigurationFile(this, "tablist/SingleTeamFight");
        this.tabPartyFFAFightConfig = new BasicConfigurationFile(this, "tablist/PartyFFAFight");
        this.tabPartyTeamFightConfig = new BasicConfigurationFile(this, "tablist/PartyTeamFight");
        this.arenasConfig = new BasicConfigurationFile(this, "logs/arenas");
        this.kitsConfig = new BasicConfigurationFile(this, "logs/kits");
        this.worldsConfig = new BasicConfigurationFile(this, "logs/worlds");

        if (mainConfig.getString("SAVE_METHOD").equals("FILE") ||
                mainConfig.getString("SAVE_METHOD").equals("FLATFILE")) {
            this.playersConfig = new BasicConfigurationFile(this, "logs/players");
            this.clansConfig = new BasicConfigurationFile(this, "logs/clans");
        }
    }

    private void loadConfigMethods() {
        switch (mainConfig.getString("SAVE_METHOD")) {
            case "MONGO": case "MONGODB":
                Profile.iProfile = new MongoDBIProfile();
                break;
            case "FLATFILE": case "FILE":
                Profile.iProfile = new FlatFileIProfile();
                break;
        }

        switch (mainConfig.getString("DEFAULT_TAB_TYPE")) {
            case "NORMAL":
                TabList.DEFAULT_TAB_TYPE = TabType.CUSTOM;
                break;
            case "WEIGHT":
                TabList.DEFAULT_TAB_TYPE = TabType.WEIGHT;
                break;
        }
    }

    private void loadSaveMethod() {
        if (Profile.getIProfile() instanceof MongoDBIProfile) {
            try {
                if (databaseConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
                    mongoDatabase = new MongoClient(
                            new ServerAddress(
                                    databaseConfig.getString("MONGO.HOST"),
                                    databaseConfig.getInteger("MONGO.PORT")
                            ),
                            MongoCredential.createCredential(
                                    databaseConfig.getString("MONGO.AUTHENTICATION.USERNAME"),
                                    databaseConfig.getString("MONGO.AUTHENTICATION.DATABASE"),
                                    databaseConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray()
                            ),
                            MongoClientOptions.builder().build()
                    ).getDatabase(databaseConfig.getString("MONGO.DATABASE"));
                } else {
                    mongoDatabase = new MongoClient(databaseConfig.getString("MONGO.HOST"), databaseConfig.getInteger("MONGO.PORT"))
                            .getDatabase(databaseConfig.getString("MONGO.DATABASE"));
                }
            } catch (Exception e) {
                System.out.println("The Yang plugin was disabled as it failed to connect to the MongoDB");
                Bukkit.getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    private void runTasks() {
        TaskUtil.runTimer(() -> {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        for (Player other : Bukkit.getOnlinePlayers()) {
                            TaskUtil.runAsync(() -> NameTag.reloadPlayer(player, other));
                        }
                    }
                },
                20L, 20L);
        TaskUtil.runTimerAsync(new ClassTask(), 5L, 5L);
        TaskUtil.runTimer(new BardEnergyTask(), 15L, 20L);
        TaskUtil.runTimer(() -> {
            for (Profile value : Profile.getProfiles().values()) {
                if (value.isOnline() && value.getRematchData() != null) value.getRematchData().validate();
            }
        }, 20L, 20L);
        TaskUtil.runLater(() -> serverLoaded = true, 60L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(get(), () -> {
            BoardAdapter.date = BoardAdapter.getDateFormat().format(new Date());
        }, 0L, 20 * 60 * 5);
    }

    private void setUpWorld() {
        for (String s : worldsConfig.getConfiguration().getStringList("WORLDS")) {
            if (new File(s).exists()) {
                Bukkit.createWorld(new WorldCreator(s));
                CreateWorldCommand.VOID_WORLDS.add(s);
            }
        }

        // Set the difficulty for each world to HARD
        // Clear the droppedItems for each world
        for (World world : getServer().getWorlds()) {
            world.setDifficulty(Difficulty.HARD);
            world.setGameRuleValue("doDaylightCycle", "false");
            getEssentials().clearEntities(world);
        }
    }

    private void removeCrafting() {
        for (Material craft : Arrays.asList(
                Material.WORKBENCH,
                Material.STICK,
                Material.WOOD_PLATE,
                Material.WOOD_BUTTON,
                Material.SNOW_BLOCK,
                Material.STONE_BUTTON)) {
            InventoryUtil.removeCrafting(craft);
        }
    }

    private void registerListeners() {
        for (Listener listener : Arrays.asList(
                new KitEditorListener(),
                new PartyListener(),
                new ProfileListener(),
                new PartyListener(),
                new MatchListener(),
                new QueueListener(),
                new ArenaListener(),
                new EventGameListener(),
                new BardListener(),
                new ArcherClass(),
                new RogueClass(),
                new ClanListener(),
                new EssentialsListener(),
                new MenuListener(),
                new LeaderboardListener(),
                new TournamentListener(),
                new HotbarListener(),
                new CategoryCreateListener())) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
        if (getMainConfig().getBoolean("MOD_MODE")) getServer().getPluginManager().registerEvents(new ModModeListener(), this);
    }

    public void registerCommands() {
        new CommandManager(this, Lists.newArrayList());
        MainCommand.init();
    }

    private void loadEssentials() {
        this.bridgeRounds = getMainConfig().getInteger("MATCH.ROUNDS_BRIDGE");
        this.rankedSumoRounds = getMainConfig().getInteger("MATCH.ROUNDS_RANKED_SUMO");
    }

    private void loadWebhook() {
        if (!discordWebhookConfig.getString("WEBHOOK").isEmpty()) {
            this.webHook = true;
            try {
                this.discordWebhook = new DiscordWebhook(discordWebhookConfig.getString("WEBHOOK"));
            } catch (Exception e) {
                System.out.println("The Yang plugin was disabled as it failed to connect to the Discord Webhook");
                Bukkit.getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    private void loadbStats() {
        Metrics metrics = new Metrics(this, 15985);
        metrics.addCustomChart(new SingleLineChart("Players", () -> Profile.getProfiles().size()));
        metrics.addCustomChart(new SingleLineChart("In Fights", this::getInFightsTotal));
    }

    private void loadPackets() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Client.BLOCK_PLACE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPlayer().getItemInHand().getType() == Material.MUSHROOM_SOUP) {
                    Player player = event.getPlayer();
                    if (Profile.get(player.getUniqueId()).getState() == ProfileState.FIGHTING) {
                        Match match = Profile.get(player.getUniqueId()).getMatch();

                        if (match.getKit().getGameRules().isSoup()) {
                            double healthAdd = 7.0;
                            if (player.getHealth() + healthAdd >= player.getMaxHealth()) {
                                player.setHealth(player.getMaxHealth());
                            } else if (player.getHealth() + healthAdd < player.getMaxHealth()) {
                                player.setHealth(player.getHealth() + healthAdd);
                            }

                            if (Yang.get().getMainConfig().getBoolean("MATCH.REMOVE_SOUP_ON_CONSUME")) {
                                player.setItemInHand(new ItemStack(Material.AIR));
                            } else {
                                player.setItemInHand(new ItemStack(Material.BOWL));
                            }
                        }
                    }
                }
            }
        });
    }

    public static Yang get(){
        return getPlugin(Yang.class);
    }

}

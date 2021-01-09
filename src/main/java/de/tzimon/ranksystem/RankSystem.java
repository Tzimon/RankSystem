package de.tzimon.ranksystem;

import de.tzimon.ranksystem.commands.RankCommand;
import de.tzimon.ranksystem.listeners.PostLoginEventListener;
import de.tzimon.ranksystem.managers.ConfigManager;
import de.tzimon.ranksystem.managers.FileManager;
import de.tzimon.ranksystem.managers.PlayerManager;
import de.tzimon.ranksystem.managers.RankManager;
import de.tzimon.ranksystem.utils.PermissionLoader;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class RankSystem extends Plugin {

    private static RankSystem plugin;

    private RankManager rankManager;
    private PlayerManager playerManager;
    private ConfigManager configManager;

    public String prefix = "§6RankSystem §8┃ §r";
    public String noPermission = "§cYou don't have the permission to do that";

    public RankSystem() {
        RankSystem.plugin = this;
    }

    @Override
    public void onEnable() {
        this.rankManager = new RankManager();
        this.playerManager = new PlayerManager();

        PermissionLoader.load();

        final ProxyServer proxyServer = ProxyServer.getInstance();
        final PluginManager pluginManager = proxyServer.getPluginManager();

        this.loadConfig();
        this.loadCommands(pluginManager);
        this.loadListeners(pluginManager);
    }

    @Override
    public void onDisable() {
        this.configManager.reloadConfig();

        FileManager.saveAll();
    }

    private void loadConfig() {
        this.configManager = new ConfigManager();

        this.configManager.setDefault("messages.prefix", this.prefix);
        this.configManager.setDefault("messages.noPermission", this.noPermission);

        this.configManager.setDefault("permissions.rank.command", "ranks");
        this.configManager.setDefault("permissions.rank.show", "ranks");
        this.configManager.setDefault("permissions.rank.modify", "ranks.modify");
        this.configManager.setDefault("permissions.rank.assign", "ranks.assign");
        this.configManager.setDefault("permissions.rank.display", "ranks.display");

        this.configManager.saveConfig();
        this.configManager.reloadConfig();

        this.prefix = this.configManager.getConfig().getString("messages.prefix");
        this.noPermission = this.configManager.getConfig().getString("messages.noPermission");
    }

    private void loadCommands(final PluginManager pluginManager) {
        pluginManager.registerCommand(this, new RankCommand());
    }

    private void loadListeners(final PluginManager pluginManager) {
        pluginManager.registerListener(this, new PostLoginEventListener());
    }

    public static void log(String s) {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(s));
    }

    public static RankSystem getPlugin() {
        return RankSystem.plugin;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

}

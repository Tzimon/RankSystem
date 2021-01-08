package de.tzimon.ranksystem;

import de.tzimon.ranksystem.commands.RankCommand;
import de.tzimon.ranksystem.managers.ConfigManager;
import de.tzimon.ranksystem.managers.FileManager;
import de.tzimon.ranksystem.utils.PermissionLoader;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Stream;

public class RankSystem extends Plugin {

    private static RankSystem plugin;

    private ConfigManager configManager;

    public String prefix = "§6RankSystem §8┃ §r";
    public String noPermission = "§cYou don't have the permission to do that";

    public RankSystem() {
        RankSystem.plugin = this;
    }

    @Override
    public void onEnable() {
        this.loadConfig();
        this.loadCommands();

        PermissionLoader.load();

        Set<Permission> permissions = Permission.get("*").getCorresponding();
        Stream<Permission> stream = permissions.stream().sorted(Comparator.comparing(Permission::getName));
        stream.forEach(s -> log(s.getFullPath()));
    }

    @Override
    public void onDisable() {
        this.configManager.reloadConfig();

        if (!this.configManager.getConfig().getBoolean("settings.config.autoSave"))
            FileManager.saveAll();
    }

    private void loadConfig() {
        this.configManager = new ConfigManager();

        this.configManager.setDefault("settings.config.autoSave", true);

        this.configManager.setDefault("messages.prefix", this.prefix);
        this.configManager.setDefault("messages.noPermission", this.noPermission);

        this.configManager.setDefault("permissions.rank.command", "ranks");
        this.configManager.setDefault("permissions.rank.modify", "ranks.modify");
        this.configManager.setDefault("permissions.rank.assign", "ranks.assign");
        this.configManager.setDefault("permissions.rank.display", "ranks.display");

        this.configManager.saveConfig();
        this.configManager.reloadConfig();

        this.prefix = this.configManager.getConfig().getString("messages.prefix");
        this.noPermission = this.configManager.getConfig().getString("messages.noPermission");
    }

    private void loadCommands() {
        final ProxyServer proxyServer = ProxyServer.getInstance();
        final PluginManager pluginManager = proxyServer.getPluginManager();

        pluginManager.registerCommand(this, new RankCommand());
    }

    public static void log(String s) {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(s));
    }

    public static RankSystem getPlugin() {
        return RankSystem.plugin;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

}

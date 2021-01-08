package de.tzimon.ranksystem;

import de.tzimon.ranksystem.managers.ConfigManager;
import de.tzimon.ranksystem.managers.FileManager;
import net.md_5.bungee.api.plugin.Plugin;

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
    }

    @Override
    public void onDisable() {
        this.configManager.reload
        FileManager.saveAll();
    }

    private void loadConfig() {
        this.configManager = new ConfigManager();

        this.configManager.setDefault("settings.config.autoSave", true);

        this.configManager.setDefault("messages.prefix", this.prefix);
        this.configManager.setDefault("messages.noPermission", this.noPermission);

        this.configManager.setDefault("permissions.rank.modify", "ranks.modify");
        this.configManager.setDefault("permissions.rank.assign", "ranks.assign");
        this.configManager.setDefault("permissions.rank.display", "ranks.display");

        this.configManager.saveConfig();
    }

    public static RankSystem getPlugin() {
        return RankSystem.plugin;
    }

}

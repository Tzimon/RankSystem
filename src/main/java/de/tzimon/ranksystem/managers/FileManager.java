package de.tzimon.ranksystem.managers;

import de.tzimon.ranksystem.RankSystem;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class FileManager {

    private static Set<FileManager> fileManagers = new HashSet<>();

    private RankSystem plugin;

    private File file;
    private Configuration config;

    public FileManager() {
        fileManagers.add(this);

        this.plugin = RankSystem.getPlugin();

        this.file = new File(this.plugin.getDataFolder() + "/" + this.getFileName());
        this.file.getParentFile().mkdirs();

        try {
            if (!this.file.exists())
                this.file.createNewFile();

            reloadConfig();
        } catch (IOException ignored) {
            ProxyServer.getInstance().getConsole().sendMessage(
                    new TextComponent(this.plugin.prefix + "§cUnable to create '" + this.getFullFileName() + "'"));
        }
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, this.file);
        } catch (IOException ignored) {
            ProxyServer.getInstance().getConsole().sendMessage(
                    new TextComponent(this.plugin.prefix + "§cUnable to save '" + this.getFullFileName() + "'"));
        }
    }

    public void reloadConfig() {
        this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.getFullFileName());
    }

    public Configuration getConfig() {
        return this.config;
    }

    private String getFullFileName() {
        return this.getFileName() + ".yml";
    }

    protected abstract String getFileName();

    public static void saveAll() {
        fileManagers.forEach(FileManager::saveConfig);
    }

}

package de.tzimon.ranksystem.managers;

import de.tzimon.ranksystem.RankSystem;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class FileManager {

    private static final Set<FileManager> FILE_MANAGERS = new HashSet<>();

    private final RankSystem plugin;

    private final File file;
    private Configuration config;

    public FileManager() {
        FILE_MANAGERS.add(this);

        this.plugin = RankSystem.getPlugin();

        this.file = new File(this.plugin.getDataFolder() + "/" + this.getFullFileName());
        this.file.getParentFile().mkdirs();

        try {
            if (!this.file.exists())
                this.file.createNewFile();

            this.reloadConfig();
        } catch (IOException ignored) {
            RankSystem.log(this.plugin.prefix + "§cUnable to create '" + this.getFullFileName() + "'");
        }
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, this.file);
        } catch (IOException ignored) {
            RankSystem.log(this.plugin.prefix + "§cUnable to save '" + this.getFullFileName() + "'");
        }
    }

    public void reloadConfig() {
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        } catch (IOException ignored) {
            RankSystem.log(this.plugin.prefix + "§cUnable to load '" + this.getFullFileName() + "'");
        }
    }

    public void set(String path, Object object) {
        this.plugin.getConfigManager().reloadConfig();

        this.getConfig().set(path, object);

        if (this.plugin.getConfigManager().getConfig().getBoolean("options.config.autoSave"))
            this.saveConfig();
    }

    public Configuration getConfig() {
        return this.config;
    }

    private String getFullFileName() {
        return this.getFileName() + ".yml";
    }

    protected abstract String getFileName();

    public static void saveAll() {
        FILE_MANAGERS.forEach(FileManager::saveConfig);
    }

}

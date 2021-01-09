package de.tzimon.ranksystem.managers;

import de.tzimon.ranksystem.RankSystem;

public class ConfigManager extends FileManager {

    private final RankSystem plugin;

    public ConfigManager() {
        this.plugin = RankSystem.getPlugin();
    }

    public void setDefault(final String path, final Object object) {
        if (!this.getConfig().contains(path))
            this.getConfig().set(path, object);
    }

    @Override
    protected String getFileName() {
        return "config";
    }

}

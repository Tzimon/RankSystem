package de.tzimon.ranksystem.managers;

public class ConfigManager extends FileManager {

    public void setDefault(String path, Object object) {
        if (!this.getConfig().contains(path))
            this.getConfig().set(path, object);
    }

    @Override
    protected String getFileName() {
        return "config";
    }

}

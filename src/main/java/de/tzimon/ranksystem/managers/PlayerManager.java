package de.tzimon.ranksystem.managers;

import de.tzimon.ranksystem.RankSystem;
import de.tzimon.ranksystem.utils.CustomPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class PlayerManager extends FileManager {

    private RankSystem plugin;

    @Override
    protected void load() {
        this.plugin = RankSystem.getPlugin();

        if (this.getConfig() == null || !this.getConfig().contains("players"))
            return;

        this.getConfig().getSection("players").getKeys().forEach(uuidString -> {
            final String path = "players." + uuidString + ".";

            final UUID uuid = UUID.fromString(uuidString);
            final CustomPlayer customPlayer = CustomPlayer.get(uuid);

            customPlayer.setName(this.getConfig().getString(path + "name"));

            if (this.getConfig().contains(path + "rank"))
                customPlayer.setRank(this.plugin.getRankManager().getDefaultRank());
        });
    }

    @Override
    public void saveConfig() {
        this.getConfig().set("players", null);

        CustomPlayer.getCustomPlayers().forEach((uuidString, customPlayer) -> {
            final String path = "players." + uuidString.toString() + ".";

            this.getConfig().set(path + "name", customPlayer.getName());

            if (customPlayer.getRank() != null)
                this.getConfig().set(path + "rank", customPlayer.getRank().getName());
        });

        super.saveConfig();
    }

    public CustomPlayer getPlayer(final String name) {
        for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (player.getName().equalsIgnoreCase(name))
                return CustomPlayer.get(player);
        }

        for (CustomPlayer player : CustomPlayer.getCustomPlayers().values()) {
            if (player.getName().equalsIgnoreCase(name))
                return player;
        }

        return null;
    }

    @Override
    protected String getFileName() {
        return "players";
    }

}

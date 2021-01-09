package de.tzimon.ranksystem.utils;

import de.tzimon.ranksystem.Rank;
import de.tzimon.ranksystem.RankSystem;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomPlayer {

    private static final Map<UUID, CustomPlayer> CUSTOM_PLAYERS = new HashMap<>();

    private RankSystem plugin;

    private final UUID uuid;
    private String name;

    private Rank rank;

    public static CustomPlayer get(final ProxiedPlayer player) {
        return CustomPlayer.get(player.getUniqueId());
    }

    public static CustomPlayer get(final UUID uuid) {
        if (!CUSTOM_PLAYERS.containsKey(uuid))
            CUSTOM_PLAYERS.put(uuid, new CustomPlayer(uuid));

        return CUSTOM_PLAYERS.get(uuid);
    }

    private CustomPlayer(final UUID uuid) {
        this.plugin = RankSystem.getPlugin();

        this.uuid = uuid;
        this.getPlayer();
        this.rank = this.plugin.getRankManager().getDefaultRank();
    }

    private ProxiedPlayer getPlayer() {
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(this.uuid);

        if (player == null || !player.isConnected())
            return null;

        this.name = player.getName();

        return player;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public static Map<UUID, CustomPlayer> getCustomPlayers() {
        return CUSTOM_PLAYERS;
    }

}

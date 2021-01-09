package de.tzimon.ranksystem.managers;

import de.tzimon.ranksystem.Rank;
import de.tzimon.ranksystem.RankSystem;
import de.tzimon.ranksystem.events.RankCreateEvent;
import de.tzimon.ranksystem.events.RankDeleteEvent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

import java.util.HashSet;
import java.util.Set;

public class RankManager extends FileManager {

    private RankSystem plugin;

    private Set<Rank> ranks;
    private Rank defaultRank = null;

    @Override
    protected void load() {
        this.plugin = RankSystem.getPlugin();

        this.ranks = new HashSet<>();

        if (this.getConfig() == null || !this.getConfig().contains("ranks"))
            return;

        this.getConfig().getSection("ranks").getKeys().forEach(rankName -> {
            final String path = "ranks." + rankName + ".";
            final Rank rank = new Rank(rankName);

            rank.getPermissions().addAll(this.getConfig().get(path + "permissions", rank.getPermissions()));
        });
    }

    @Override
    public void saveConfig() {
        this.getConfig().set("ranks", null);

        this.ranks.forEach(rank -> {
            final String path = "ranks." + rank.getName() + ".";
            this.getConfig().set(path + "permissions", rank.getPermissions());
        });

        super.saveConfig();
    }

    public Rank getRank(final String name) {
        for (Rank rank : this.ranks) {
            if (rank.getName().equalsIgnoreCase(name))
                return rank;
        }

        return null;
    }

    public boolean exists(final String name) {
        return this.getRank(name) != null;
    }

    public boolean createRank(final CommandSender sender, final String rankName) {
        if (this.exists(rankName))
            return false;

        final Rank rank = new Rank(rankName);
        this.ranks.add(rank);
        ProxyServer.getInstance().getPluginManager().callEvent(new RankCreateEvent(sender, rank));

        return true;
    }

    public boolean deleteRank(final CommandSender sender, final String rankName) {
        final Rank rank = this.getRank(rankName);

        if (rank == null)
            return false;

        this.ranks.remove(rank);
        ProxyServer.getInstance().getPluginManager().callEvent(new RankDeleteEvent(sender, rank));

        return true;
    }

    public final Set<Rank> getRanks() {
        return new HashSet<>(this.ranks);
    }

    public Rank getDefaultRank() {
        return defaultRank;
    }

    @Override
    protected String getFileName() {
        return "ranks";
    }

}

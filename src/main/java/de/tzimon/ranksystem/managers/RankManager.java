package de.tzimon.ranksystem.managers;

import de.tzimon.ranksystem.Permission;
import de.tzimon.ranksystem.Rank;
import de.tzimon.ranksystem.events.RankCreateEvent;
import de.tzimon.ranksystem.events.RankDeleteEvent;
import de.tzimon.ranksystem.utils.CustomPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RankManager extends FileManager {

    private Set<Rank> ranks;
    private Rank defaultRank;

    @Override
    protected void load() {
        this.ranks = new HashSet<>();

        if (this.getConfig() == null || !this.getConfig().contains("ranks"))
            return;

        this.getConfig().getSection("ranks").getKeys().forEach(rankName -> {
            final String path = "ranks." + rankName + ".";
            final Rank rank = this.createRank(ProxyServer.getInstance().getConsole(), rankName);

            this.getConfig().getStringList(path + "permissions").forEach(permission ->
                    rank.getPermissions().add(Permission.get(permission)));
        });

        if (this.getConfig().contains("default"))
            this.defaultRank = this.getRank(this.getConfig().getString("default"));
        else
            this.defaultRank = null;
    }

    @Override
    public void saveConfig() {
        this.getConfig().set("ranks", null);

        this.ranks.forEach(rank -> {
            final String path = "ranks." + rank.getName() + ".";
            this.getConfig().set(path + "permissions", new ArrayList<>(rank.getPermissions()));
        });

        if (this.defaultRank != null)
            this.getConfig().set("default", this.defaultRank.getName());

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

    public Rank createRank(final CommandSender sender, final String rankName) {
        if (this.exists(rankName))
            return null;

        final Rank rank = new Rank(rankName);
        this.ranks.add(rank);
        ProxyServer.getInstance().getPluginManager().callEvent(new RankCreateEvent(sender, rank));

        return rank;
    }

    public boolean deleteRank(final CommandSender sender, final String rankName) {
        final Rank rank = this.getRank(rankName);

        if (rank == null)
            return false;

        if (this.defaultRank == rank)
            this.defaultRank = null;

        this.ranks.remove(rank);
        ProxyServer.getInstance().getPluginManager().callEvent(new RankDeleteEvent(sender, rank));

        return true;
    }

    public final Set<Rank> getRanks() {
        return new HashSet<>(this.ranks);
    }

    public void setDefaultRank(Rank defaultRank) {
        CustomPlayer.getCustomPlayers().values().forEach(player -> {
            if (player.getRank() == this.defaultRank)
                player.setRank(defaultRank);
        });

        this.defaultRank = defaultRank;
    }

    public Rank getDefaultRank() {
        return defaultRank;
    }

    @Override
    protected String getFileName() {
        return "ranks";
    }

}

package de.tzimon.ranksystem.events;

import de.tzimon.ranksystem.Rank;
import de.tzimon.ranksystem.utils.CustomPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Event;

public class RankRevokeEvent extends Event {

    private final CommandSender sender;
    private final Rank before;
    private final CustomPlayer player;

    public RankRevokeEvent(final CommandSender sender, final Rank before, final CustomPlayer player) {
        this.sender = sender;
        this.before = before;
        this.player = player;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public Rank getBefore() {
        return this.before;
    }

    public CustomPlayer getPlayer() {
        return this.player;
    }

}

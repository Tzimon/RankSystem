package de.tzimon.ranksystem.events;

import de.tzimon.ranksystem.Rank;
import de.tzimon.ranksystem.utils.CustomPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Event;

public class RankAssignEvent extends Event {

    private final CommandSender sender;
    private final Rank before;
    private final Rank after;
    private final CustomPlayer player;

    public RankAssignEvent(final CommandSender sender, final Rank before, final Rank after, final CustomPlayer player) {
        this.sender = sender;
        this.before = before;
        this.after = after;
        this.player = player;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public Rank getBefore() {
        return this.before;
    }

    public Rank getAfter() {
        return this.after;
    }

    public CustomPlayer getPlayer() {
        return this.player;
    }

}

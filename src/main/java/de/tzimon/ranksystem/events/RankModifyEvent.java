package de.tzimon.ranksystem.events;

import de.tzimon.ranksystem.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Event;

public abstract class RankModifyEvent extends Event {

    protected CommandSender sender;
    protected Rank rank;

    public RankModifyEvent(CommandSender sender, Rank rank) {
        this.sender = sender;
        this.rank = rank;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public Rank getRank() {
        return this.rank;
    }

}

package de.tzimon.ranksystem.events;

import de.tzimon.ranksystem.Rank;
import net.md_5.bungee.api.CommandSender;

public class RankDefaultEvent extends RankModifyEvent {

    public RankDefaultEvent(final CommandSender sender, final Rank rank) {
        super(sender, rank);
    }

}

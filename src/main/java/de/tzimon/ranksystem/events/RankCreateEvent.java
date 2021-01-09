package de.tzimon.ranksystem.events;

import de.tzimon.ranksystem.Rank;
import net.md_5.bungee.api.CommandSender;

public class RankCreateEvent extends RankModifyEvent {

    public RankCreateEvent(CommandSender sender, Rank rank) {
        super(sender, rank);
    }

}

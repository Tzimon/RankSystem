package de.tzimon.ranksystem.commands.utils;

import de.tzimon.ranksystem.RankSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public enum CommandUtil {

    RANK("rank");

    private final RankSystem plugin;

    private final String name;

    CommandUtil(String name) {
        this.plugin = RankSystem.getPlugin();

        this.name = name;
    }

    public void sendUsage(final CommandSender sender) {
        sender.sendMessage(new TextComponent(this.plugin.prefix + "§6§lHelp:"));

        switch (this) {
            case RANK:
                this.sendUsageLine(sender, "rank create <name>", "Creates a new rank");
                this.sendUsageLine(sender, "rank delete <name>", "Deletes an existing rank");
                this.sendUsageLine(sender, "rank list", "Lists up all ranks");
                this.sendUsageLine(sender, "rank assign <rank> <player>", "Assigns a rank to a player");
                this.sendUsageLine(sender, "rank revoke <player>", "Revokes the current and assigns the default rank to a player");
                this.sendUsageLine(sender, "rank default <rank>", "Sets the default rank");
                this.sendUsageLine(sender, "rank permission add|remove <rank> <permission>", "Grants or revokes permissions to or from a rank");
                this.sendUsageLine(sender, "rank priority <rank> <priority>", "Sets a rank's priority");
                break;
            default:
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cNo help available"));
                break;
        }
    }

    private void sendUsageLine(final CommandSender sender, final String usage, final String description) {
        sender.sendMessage(new TextComponent(this.plugin.prefix + "§8- §e/" + usage + " §8- §7" + description));
    }

    public String getName() {
        return this.name;
    }

}

package de.tzimon.ranksystem.commands;

import de.tzimon.ranksystem.Rank;
import de.tzimon.ranksystem.RankSystem;
import de.tzimon.ranksystem.commands.utils.CommandUtil;
import de.tzimon.ranksystem.managers.ConfigManager;
import de.tzimon.ranksystem.managers.PlayerManager;
import de.tzimon.ranksystem.managers.RankManager;
import de.tzimon.ranksystem.utils.CustomPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.Set;

public class RankCommand extends Command {

    private final RankSystem plugin;
    private final RankManager rankManager;
    private final PlayerManager playerManager;
    private final ConfigManager configManager;

    public RankCommand() {
        super(CommandUtil.RANK.getName());

        this.plugin = RankSystem.getPlugin();
        this.rankManager = this.plugin.getRankManager();
        this.playerManager = this.plugin.getPlayerManager();
        this.configManager = this.plugin.getConfigManager();
    }

    public void execute(final CommandSender sender, final String[] args) {
        if (!sender.hasPermission(this.configManager.getConfig().getString("permissions.rank.command"))) {
            sender.sendMessage(new TextComponent(this.plugin.prefix + this.plugin.noPermission));
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            if (!sender.hasPermission(this.configManager.getConfig().getString("permissions.rank.modify"))) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + this.plugin.noPermission));
                return;
            }

            final String rankName = args[1];

            if (!this.rankManager.createRank(sender, rankName)) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cThat rank already exists"));
                return;
            }

            sender.sendMessage(new TextComponent(this.plugin.prefix + "§7The rank §6" + rankName + " §7was created"));

            if (sender != ProxyServer.getInstance().getConsole())
                RankSystem.log("§6" + sender.getName() + " §7created a new rank with the name §6" + rankName);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            if (!sender.hasPermission(this.configManager.getConfig().getString("permissions.rank.modify"))) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + this.plugin.noPermission));
                return;
            }

            final String rankName = args[1];

            if (!this.rankManager.deleteRank(sender, rankName)) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cThat rank does not exist"));
                return;
            }

            sender.sendMessage(new TextComponent(this.plugin.prefix + "§7The rank §6" + rankName + " §7was deleted"));

            if (sender != ProxyServer.getInstance().getConsole())
                RankSystem.log("§6" + sender.getName() + " §7deleted the rank with the name §6" + rankName);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            final Set<Rank> ranks = this.rankManager.getRanks();

            
        } else if (args.length == 3 && args[0].equalsIgnoreCase("assign")) {
            if (!sender.hasPermission(this.configManager.getConfig().getString("permissions.rank.assign"))) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + this.plugin.noPermission));
                return;
            }

            final String targetName = args[1];
            final String rankName = args[2];

            final CustomPlayer customPlayer = this.playerManager.getPlayer(targetName);

            if (customPlayer == null) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cThat player has never been online"));
                return;
            }

            final Rank rank = this.rankManager.getRank(rankName);

            if (rank == null) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cThat rank does not exist"));
                return;
            }

            customPlayer.setRank(rank);

            final String message = this.plugin.prefix + "§6" + customPlayer.getName() + " §7now has the §6"
                    + rank.getName() + " §7rank";
            sender.sendMessage(new TextComponent(message));

            if (sender != ProxyServer.getInstance().getConsole())
                RankSystem.log(message);
        } else {
            CommandUtil.RANK.sendUsage(sender);
        }
    }

}

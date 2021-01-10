package de.tzimon.ranksystem.commands;

import de.tzimon.ranksystem.Permission;
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

            for (String regex : Rank.FORBIDDEN_NAMES) {
                if (rankName.matches(regex)) {
                    sender.sendMessage(new TextComponent("§cThat name is not allowed"));
                    return;
                }
            }

            if (this.rankManager.createRank(sender, rankName) == null) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cThat rank already exists"));
                return;
            }

            sender.sendMessage(new TextComponent(this.plugin.prefix + "§7The rank §a" + rankName + " §7was created"));

            if (sender != ProxyServer.getInstance().getConsole())
                RankSystem.log("§a" + sender.getName() + " §7created a new rank with the name §a" + rankName);
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

            sender.sendMessage(new TextComponent(this.plugin.prefix + "§7The rank §a" + rankName + " §7was deleted"));

            if (sender != ProxyServer.getInstance().getConsole())
                RankSystem.log("§a" + sender.getName() + " §7deleted the rank with the name §a" + rankName);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (!sender.hasPermission(this.configManager.getConfig().getString("permissions.rank.show"))) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + this.plugin.noPermission));
                return;
            }

            final Set<Rank> ranks = this.rankManager.getRanks();

            if (ranks.isEmpty())
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cThere are no ranks"));
            else {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§6§lRanks:"));

                ranks.forEach(rank -> {
                    final boolean isDefault = rank == this.rankManager.getDefaultRank();
                    final StringBuilder messageBuilder = new StringBuilder();

                    messageBuilder.append(this.plugin.prefix).append("§8- §7").append(rank.getName());

                    if (isDefault)
                        messageBuilder.append(" §7(§aDefault§7)");

                    sender.sendMessage(new TextComponent(messageBuilder.toString()));
                });
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
            if (!sender.hasPermission(this.configManager.getConfig().getString("permissions.rank.show"))) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + this.plugin.noPermission));
                return;
            }

            final String rankName = args[1];
            final Rank rank = this.rankManager.getRank(rankName);

            if (rank == null) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cThat rank does not exist"));
                return;
            }

            final boolean isDefault = rank == this.rankManager.getDefaultRank();
            final Set<Permission> permissions = rank.getPermissions();

            sender.sendMessage(new TextComponent(this.plugin.prefix + "§6§lInfo:"));
            sender.sendMessage(new TextComponent(this.plugin.prefix + "§7Name: §a" + rank.getName()));
            sender.sendMessage(new TextComponent(this.plugin.prefix + "§7Default: " + (isDefault ? "§aYes" : "§cNo")));

            if (permissions.isEmpty())
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cNo permissions"));
            else {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§7Permissions:"));
                permissions.forEach(permission -> sender.sendMessage(new TextComponent(this.plugin.prefix + "§8- §7"
                        + permission.getFullPath())));
            }
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

            sender.sendMessage(new TextComponent(this.plugin.prefix + "§a" + customPlayer.getName() + " §7now has the §a"
                    + rank.getName() + " §7rank"));

            if (sender != ProxyServer.getInstance().getConsole())
                RankSystem.log(this.plugin.prefix + "§a" + sender.getName() + " §7gave §a" + customPlayer.getName()
                        + " §7the §a" + rank.getName() + " §7rank");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("revoke")) {
            if (!sender.hasPermission(this.configManager.getConfig().getString("permissions.rank.assign"))) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + this.plugin.noPermission));
                return;
            }

            final String targetName = args[1];
            final CustomPlayer customPlayer = this.playerManager.getPlayer(targetName);

            if (customPlayer == null) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cThat player has never been online"));
                return;
            }

            final Rank defaultRank = this.rankManager.getDefaultRank();
            final String defaultRankName = defaultRank == null ? "none" : defaultRank.getName();

            if (customPlayer.getRank() == defaultRank) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cThat player already has the default rank ("
                        + defaultRankName + ")"));
                return;
            }

            customPlayer.setRank(defaultRank);
            
            final String message = this.plugin.prefix + "§a" + customPlayer.getName() + " §7now has the default rank (§a"
                    + defaultRankName + "§7)";

            sender.sendMessage(new TextComponent(message));

            if (sender != ProxyServer.getInstance().getConsole())
                RankSystem.log(this.plugin.prefix + "§a" + sender.getName() + " §7gave §a" + customPlayer.getName()
                        + " §7the default rank (§a" + defaultRankName + "§7)");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("player")) {
            if (!sender.hasPermission(this.configManager.getConfig().getString("permissions.rank.show"))) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + this.plugin.noPermission));
                return;
            }

            final String targetName = args[1];
            final CustomPlayer customPlayer = this.playerManager.getPlayer(targetName);

            if (customPlayer == null) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cThat player has never been online"));
                return;
            }

            final Rank rank = customPlayer.getRank();

            if (rank == null)
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§a" + customPlayer.getName() + "§7 has no rank"));
            else
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§a" + customPlayer.getName() + " §7has the §a"
                        + rank.getName() + " §7rank"));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("default")) {
            if (!sender.hasPermission(this.configManager.getConfig().getString("permissions.rank.modify"))) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + this.plugin.noPermission));
                return;
            }

            final String rankName = args[1];
            final Rank rank = this.rankManager.getRank(rankName);

            if (rank == null) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cThat rank does not exist"));
                return;
            }

            if (rank == this.rankManager.getDefaultRank()) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§c" + rank.getName() + " is already the default rank"));
                return;
            }

            this.rankManager.setDefaultRank(rank);

            sender.sendMessage(new TextComponent(this.plugin.prefix + "§a" + rank.getName() + " §7is now the default rank"));

            if (sender != ProxyServer.getInstance().getConsole())
                RankSystem.log(this.plugin.prefix + "§a" + sender.getName() + " §7made §a" + rank.getName() + " §7the default rank");
        } else if (args.length == 4 && args[0].equalsIgnoreCase("permission")) {
            final String rankName = args[1];
            final String permissionName = args[3].toLowerCase();

            final Rank rank = this.rankManager.getRank(rankName);

            if (rank == null) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + "§cThat rank does not exist"));
                return;
            }

            final Permission permission;

            try {
                permission = Permission.get(permissionName);
            } catch (IllegalArgumentException ignored) {
                sender.sendMessage(new TextComponent("§cInvalid permission"));
                return;
            }

            final String action;

            switch (args[2].toLowerCase()) {
                case "add":
                    rank.getPermissions().add(permission);
                    action = "added to";
                    break;
                case "remove":
                    rank.getPermissions().remove(permission);
                    action = "removed from";
                    break;
                default:
                    CommandUtil.RANK.sendUsage(sender);
                    return;
            }

            final String message = this.plugin.prefix + "§7The §a" + permissionName + " §7permission was " + action
                    + " §7the §a" + rankName + " §7rank";

            sender.sendMessage(new TextComponent(message));

            if (sender != ProxyServer.getInstance().getConsole())
                RankSystem.log(message + " by §a" + sender.getName());
        } else {
            CommandUtil.RANK.sendUsage(sender);
        }
    }

}

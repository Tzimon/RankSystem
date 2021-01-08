package de.tzimon.ranksystem.commands;

import de.tzimon.ranksystem.RankSystem;
import de.tzimon.ranksystem.commands.utils.CommandUtil;
import de.tzimon.ranksystem.managers.ConfigManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class RankCommand extends Command {

    private final RankSystem plugin;
    private final ConfigManager configManager;

    public RankCommand() {
        super(CommandUtil.RANK.getName());

        this.plugin = RankSystem.getPlugin();
        this.configManager = this.plugin.getConfigManager();
    }

    public void execute(final CommandSender sender, final String[] args) {
        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            if (!sender.hasPermission(this.configManager.getConfig().getString("permissions.rank.modify"))) {
                sender.sendMessage(new TextComponent(this.plugin.prefix + this.plugin.noPermission));
                return;
            }

            String rankName = args[1];
        }
    }

}

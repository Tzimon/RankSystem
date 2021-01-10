package de.tzimon.ranksystem.listeners;

import de.tzimon.ranksystem.utils.CustomPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginEventListener implements Listener {

    @EventHandler
    public void handlePostLoginEvent(final PostLoginEvent event) {
         final ProxiedPlayer player = event.getPlayer();
        CustomPlayer.get(player).updateBungeeCordPermissions();
    }

}

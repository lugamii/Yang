package me.tulio.yang.leaderboard;

import me.tulio.yang.hotbar.Hotbar;
import me.tulio.yang.hotbar.HotbarItem;
import me.tulio.yang.leaderboard.menu.LeaderBoardMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class LeaderboardListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && (event.getAction() == Action.RIGHT_CLICK_AIR ||
                event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();
            HotbarItem hotbarItem = Hotbar.fromItemStack(event.getItem());

            if (hotbarItem != null) {
                if (hotbarItem == HotbarItem.LEADERBOARD_MENU) {
                    new LeaderBoardMenu(player).openMenu(player);
                    event.setCancelled(true);
                }
            }
        }
    }
}

// Decompiled with: CFR 0.152
// Class Version: 8
package me.tulio.yang.utilities.menu;

import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class MenuListener
implements Listener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onButtonPress(InventoryClickEvent event) {
        Player player = (Player)((Object)event.getWhoClicked());
        Menu openMenu = Menu.currentlyOpenedMenus.get(player.getName());
        if (openMenu != null) {
            if (event.getSlot() != event.getRawSlot()) {
                if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                    event.setCancelled(true);
                }
                return;
            }
            if (openMenu.getButtons().containsKey(event.getSlot())) {
                Button button = openMenu.getButtons().get(event.getSlot());
                boolean cancel = button.shouldCancel(player, event.getClick());
                if (!(cancel || event.getClick() != ClickType.SHIFT_LEFT && event.getClick() != ClickType.SHIFT_RIGHT)) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() != null) {
                        player.getInventory().addItem(new ItemStack[]{event.getCurrentItem()});
                    }
                } else {
                    event.setCancelled(cancel);
                }
                button.clicked(player, event.getClick());
                button.clicked(player, event.getSlot(), event.getClick(), event.getHotbarButton());
                button.clicked(player, event.getSlot(), event.getClick(), event.getHotbarButton(), event.getCursor());
                if (Menu.currentlyOpenedMenus.containsKey(player.getName())) {
                    Menu newMenu = Menu.currentlyOpenedMenus.get(player.getName());
                    if (newMenu == openMenu && openMenu.isUpdateAfterClick()) {
                        openMenu.setClosedByMenu(true);
                        newMenu.openMenu(player);
                    }
                } else if (button.shouldUpdate(player, event.getClick())) {
                    openMenu.setClosedByMenu(true);
                    openMenu.openMenu(player);
                }
            } else {
                if (event.getCurrentItem() != null) {
                    event.setCancelled(true);
                }
                if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player)((Object)event.getPlayer());
        Menu openMenu = Menu.currentlyOpenedMenus.get(player.getName());
        if (openMenu != null) {
            openMenu.onClose(player);
            Menu.currentlyOpenedMenus.remove(player.getName());
        }
    }
}

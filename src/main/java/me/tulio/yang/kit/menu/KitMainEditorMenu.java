package me.tulio.yang.kit.menu;

import com.google.common.collect.Maps;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class KitMainEditorMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.GREEN + "Main Editor";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        for (Kit kits : Kit.getKits()) {
            buttons.put(buttons.size(), new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(kits.getDisplayIcon())
                            .name(CC.GREEN + kits.getName())
                            .lore(CC.MENU_BAR, "&fEnabled&7: " + (kits.isEnabled() ? CC.GREEN + "Yes" : CC.RED + "No"), CC.MENU_BAR)
                            .build();
                }

                @Override
                public void clicked(Player player, ClickType clickType) {
                    player.closeInventory();
                    new KitEditorMenu(kits).openMenu(player);
                }
            });
        }

        return buttons;
    }
}

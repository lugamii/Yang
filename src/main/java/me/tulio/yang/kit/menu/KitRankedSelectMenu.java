package me.tulio.yang.kit.menu;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import me.tulio.yang.utilities.menu.button.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor
public class KitRankedSelectMenu extends Menu {

    private final Kit kit;

    @Override
    public String getTitle(Player player) {
        return CC.GREEN + kit.getName() + " Select Ranked";
    }

    @Override
    public int getSize() {
        return 9*3;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.IRON_SWORD)
                        .name(CC.GREEN + "UnRanked")
                        .lore("", CC.GRAY + "Select this to edit UnRanked slot menu.")
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                new KitSlotsMenu(kit, false).openMenu(player);
            }
        });

        buttons.put(15, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.DIAMOND_SWORD)
                        .name(CC.GREEN + "Ranked")
                        .lore("", CC.GRAY + "Select this to edit Ranked slot menu.")
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                new KitSlotsMenu(kit, true).openMenu(player);
            }
        });

        buttons.put(26, new BackButton(new KitEditorMenu(kit)));

        return buttons;
    }
}

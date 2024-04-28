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
public class KitLoadoutsMenu extends Menu {

    private final Kit kit;

    @Override
    public String getTitle(Player player) {
        return CC.GREEN + kit.getName() + " Loadouts";
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
                return new ItemBuilder(Material.CHEST)
                        .name(CC.GREEN + "Give Loadout")
                        .lore("", CC.GRAY + "The set inventory will be put into your inventory.")
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                player.getInventory().setArmorContents(kit.getKitLoadout().getArmor());
                player.getInventory().setContents(kit.getKitLoadout().getContents());
                player.updateInventory();
            }
        });

        buttons.put(15, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.ANVIL)
                        .name(CC.GREEN + "Set Loadout")
                        .lore("", CC.GRAY + "This will set the items you have in your inventory.")
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                kit.getKitLoadout().setArmor(player.getInventory().getArmorContents());
                kit.getKitLoadout().setContents(player.getInventory().getContents());
                kit.save();
            }
        });

        buttons.put(26, new BackButton(new KitEditorMenu(kit)));

        return buttons;
    }
}

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
public class KitSetIconMenu extends Menu {

    private final Kit kit;

    @Override
    public String getTitle(Player player) {
        return CC.GREEN + kit.getName() + " Set Icon Menu";
    }

    @Override
    public int getSize() {
        return 9*3;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(13, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.ITEM_FRAME)
                        .name(CC.GREEN + "Set Icon")
                        .lore("", CC.GREEN + "Drag the item here by clicking it to change the item")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot, ItemStack itemStack) {
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    player.sendMessage(CC.RED + "You must select an item to set as the icon!");
                    return;
                }

                kit.setDisplayIcon(itemStack);
                player.sendMessage(CC.GREEN + "You have set the icon for " + kit.getName() + " to " + itemStack.getType().name());
            }
        });

        buttons.put(getSize() - 1, new BackButton(new KitEditorMenu(kit)));

        return buttons;
    }
}

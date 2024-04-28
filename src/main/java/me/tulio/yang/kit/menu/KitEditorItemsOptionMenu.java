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
public class KitEditorItemsOptionMenu extends Menu {

    private final Kit kit;

    @Override
    public String getTitle(Player player) {
        return CC.GREEN + kit.getName() + " Select Option EditorItems";
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
                        .name(CC.GREEN + "Give Editor Items")
                        .lore("", CC.GRAY + "The set inventory will be put into your inventory.")
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                player.closeInventory();
                player.getInventory().setContents(kit.getEditRules().getEditorItems().toArray(new ItemStack[0]));
            }
        });

        buttons.put(15, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.ANVIL)
                        .name(CC.GREEN + "Set Editor Items")
                        .lore("", CC.GRAY + "This will set the items you have in your inventory.")
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                kit.getEditRules().getEditorItems().clear();
                for (ItemStack content : player.getInventory().getContents()) {
                    kit.getEditRules().getEditorItems().add(content);
                }
                player.sendMessage(CC.GREEN + "Editor Items set.");
            }
        });

        buttons.put(26, new BackButton(new KitEditRulesMenu(kit)));

        return buttons;
    }
}

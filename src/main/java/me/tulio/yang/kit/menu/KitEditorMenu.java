package me.tulio.yang.kit.menu;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.kit.KitEditorData;
import me.tulio.yang.profile.Profile;
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
public class KitEditorMenu extends Menu {

    private final Kit kit;

    {
        setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        return CC.GREEN + "Editing " + kit.getName();
    }

    @Override
    public int getSize() {
        return 9*3;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        // Loadouts option
        buttons.put(10, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.STAINED_GLASS_PANE)
                        .name("&aLoadout")
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                new KitLoadoutsMenu(kit).openMenu(player);
            }
        });

        // Rules option
        buttons.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.STAINED_GLASS_PANE)
                        .name("&aRules")
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                new KitEditRulesMenu(kit).openMenu(player);
            }
        });

        // Rename option
        buttons.put(12, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.STAINED_GLASS_PANE)
                        .name("&aRename")
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                Profile.get(player.getUniqueId()).setKitEditorStatus(new KitEditorData(kit));
                player.closeInventory();
                player.sendMessage(CC.GREEN + "Insert new kit name.");
                player.sendMessage(CC.GREEN + "If you need to cancel this process just write \"cancel\" in the chat");
            }
        });

        // Set Slots option
        buttons.put(13, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.STAINED_GLASS_PANE)
                        .name("&aSet Slots")
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                player.closeInventory();
                new KitRankedSelectMenu(kit).openMenu(player);
            }
        });

        // Toggle option
        buttons.put(14, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.STAINED_GLASS_PANE)
                        .name("&aToggle")
                        .lore(CC.MENU_BAR, "&fEnabled&7: " + (kit.isEnabled() ? CC.GREEN + "Yes" : CC.RED + "No"), CC.MENU_BAR)
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                kit.setEnabled(!kit.isEnabled());
            }
        });

        buttons.put(15, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.STAINED_GLASS_PANE)
                        .name("&aSet Icon")
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                new KitSetIconMenu(kit).openMenu(player);
            }
        });

        // Status option
        buttons.put(16, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.STAINED_GLASS_PANE)
                        .name("&aStatus")
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                player.closeInventory();
                new KitStatusMenu(kit).openMenu(player);
            }
        });

        buttons.put(26, new BackButton(new KitMainEditorMenu()));
        return buttons;
    }
}

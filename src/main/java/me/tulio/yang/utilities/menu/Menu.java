// Decompiled with: CFR 0.152
// Class Version: 8
package me.tulio.yang.utilities.menu;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Yang;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.menu.Button;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Setter
@Getter
public abstract class Menu {
    public static Map<String, Menu> currentlyOpenedMenus = new HashMap<>();
    protected Yang plugin = Yang.get();
    private Map<Integer, Button> buttons = new HashMap<>();
    private boolean autoUpdate = false;
    private boolean updateAfterClick = true;
    private boolean closedByMenu = false;
    private boolean placeholder = false;
    private Button placeholderButton = Button.placeholder(Material.STAINED_GLASS_PANE, (byte)15, " ");

    private ItemStack createItemStack(Player player, Button button) {
        ItemStack item = button.getButtonItem(player);
        if (item.getType() != Material.SKULL_ITEM) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                meta.setDisplayName(meta.getDisplayName());
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public void openMenu(Player player) {
        this.buttons = this.getButtons(player);
        Menu previousMenu = currentlyOpenedMenus.get(player.getName());
        Inventory inventory = null;
        int size = this.getSize() == -1 ? this.size(this.buttons) : this.getSize();
        boolean update = false;
        String title = CC.translate(this.getTitle(player));
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }
        if (player.getOpenInventory() != null) {
            if (previousMenu == null) {
                player.closeInventory();
            } else {
                int previousSize = player.getOpenInventory().getTopInventory().getSize();
                if (previousSize == size && player.getOpenInventory().getTopInventory().getTitle().equals(title)) {
                    inventory = player.getOpenInventory().getTopInventory();
                    update = true;
                } else {
                    previousMenu.setClosedByMenu(true);
                    player.closeInventory();
                }
            }
        }
        if (inventory == null) {
            inventory = Bukkit.createInventory(player, size, title);
        }
        inventory.setContents(new ItemStack[inventory.getSize()]);
        currentlyOpenedMenus.put(player.getName(), this);
        for (Map.Entry<Integer, Button> buttonEntry : this.buttons.entrySet()) {
            inventory.setItem(buttonEntry.getKey(), this.createItemStack(player, buttonEntry.getValue()));
        }
        if (this.isPlaceholder()) {
            for (int index = 0; index < size; ++index) {
                if (this.buttons.get(index) != null) continue;
                this.buttons.put(index, this.placeholderButton);
                inventory.setItem(index, this.placeholderButton.getButtonItem(player));
            }
        }
        if (update) {
            player.updateInventory();
        } else {
            player.openInventory(inventory);
        }
        this.onOpen(player);
        this.setClosedByMenu(false);
    }

    public int size(Map<Integer, Button> buttons) {
        int highest = 0;
        for (int buttonValue : buttons.keySet()) {
            if (buttonValue <= highest) continue;
            highest = buttonValue;
        }
        return (int)(Math.ceil((double)(highest + 1) / 9.0) * 9.0);
    }

    public int getSize() {
        return -1;
    }

    public int getSlot(int x, int y) {
        return 9 * y + x;
    }

    public abstract String getTitle(Player var1);

    public abstract Map<Integer, Button> getButtons(Player var1);

    public void onOpen(Player player) {
    }

    public void onClose(Player player) {
    }

}

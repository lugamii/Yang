// Decompiled with: CFR 0.152
// Class Version: 8
package me.tulio.yang.utilities.menu.button;

import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class JumpToMenuButton
extends Button {
    private final Menu menu;
    private final ItemStack itemStack;

    public JumpToMenuButton(Menu menu, ItemStack itemStack) {
        this.menu = menu;
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return this.itemStack;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        this.menu.openMenu(player);
    }
}

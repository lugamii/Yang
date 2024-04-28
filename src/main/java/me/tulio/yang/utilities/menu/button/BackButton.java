// Decompiled with: CFR 0.152
// Class Version: 8
package me.tulio.yang.utilities.menu.button;

import java.util.Arrays;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BackButton
extends Button {
    private Menu back;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.REDSTONE).name(CC.RED + CC.BOLD + "Back").lore(Arrays.asList(CC.RED + "Click here to return to", CC.RED + "the previous menu.")).build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Button.playNeutral(player);
        this.back.openMenu(player);
    }

    public BackButton(Menu back) {
        this.back = back;
    }
}

package me.tulio.yang.profile.meta.option.button;

import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.meta.option.menu.ProfileOptionButton;
import me.tulio.yang.tablist.TabType;
import me.tulio.yang.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ChangeTabTypeOptionButton extends ProfileOptionButton {

    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.CARPET).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.CARPET).build();
    }

    @Override
    public String getOptionName() {
        return "&d&lChange TabType";
    }

    @Override
    public String getDescription() {
        return "If you don't like this Tablist mode you can change it";
    }

    @Override
    public String getEnabledOption() {
        return "Default Type";
    }

    @Override
    public String getDisabledOption() {
        return "Weight Type";
    }

    @Override
    public boolean isEnabled(Player player) {
        return Profile.get(player.getUniqueId()).getTabType() == TabType.CUSTOM;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getTabType() == TabType.CUSTOM) profile.setTabType(TabType.WEIGHT);
        else profile.setTabType(TabType.CUSTOM);
    }
}

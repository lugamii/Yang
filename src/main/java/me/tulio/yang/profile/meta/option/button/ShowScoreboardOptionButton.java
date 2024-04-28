package me.tulio.yang.profile.meta.option.button;

import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.meta.option.menu.ProfileOptionButton;
import me.tulio.yang.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ShowScoreboardOptionButton extends ProfileOptionButton {

	@Override
	public String getOptionName() {
		return "&a&lShow Scoreboard";
	}

	@Override
	public ItemStack getEnabledItem(Player player) {
		return new ItemBuilder(Material.ITEM_FRAME).build();
	}

	@Override
	public ItemStack getDisabledItem(Player player) {
		return new ItemBuilder(Material.ITEM_FRAME).build();
	}

	@Override
	public String getDescription() {
		return "If enabled, a scoreboard will be displayed to you.";
	}

	@Override
	public String getEnabledOption() {
		return "Show you a scoreboard";
	}

	@Override
	public String getDisabledOption() {
		return "Do not show you a scoreboard";
	}

	@Override
	public boolean isEnabled(Player player) {
		return Profile.get(player.getUniqueId()).getOptions().showScoreboard();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		Profile profile = Profile.get(player.getUniqueId());
		profile.getOptions().showScoreboard(!profile.getOptions().showScoreboard());
	}

}

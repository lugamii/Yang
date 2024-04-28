package me.tulio.yang.duel.menu;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import me.tulio.yang.Yang;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
public class DuelSelectKitMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return Yang.get().getLangConfig().getString("DUEL.SELECT.KIT_MENU.TITLE");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();

		for (Kit kit : Kit.getKits()) {
			if (kit.isEnabled()) {
				buttons.put(buttons.size(), new SelectKitButton(kit));
			}
		}

		return buttons;
	}

	@Override
	public void onClose(Player player) {
		if (!isClosedByMenu()) {
			Profile profile = Profile.get(player.getUniqueId());
			profile.setDuelProcedure(null);
		}
	}

	@AllArgsConstructor
	private static class SelectKitButton extends Button {

		private final Kit kit;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(kit.getDisplayIcon())
					.name(Yang.get().getLangConfig().getString("DUEL.SELECT.KIT_MENU.NAME").replace("{name}", kit.getName()))
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getDuelProcedure() == null) {
				player.sendMessage(CC.RED + "Could not find duel procedure.");
				return;
			}

			// Update duel procedure
			profile.getDuelProcedure().setKit(kit);

			// Set closed by menu
			Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

			// Force close inventory
			player.closeInventory();

			// Open arena selection menu
			new DuelSelectArenaMenu().openMenu(player);
		}

	}

}

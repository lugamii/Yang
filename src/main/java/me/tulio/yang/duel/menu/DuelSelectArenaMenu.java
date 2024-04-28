package me.tulio.yang.duel.menu;

import lombok.AllArgsConstructor;
import me.tulio.yang.Yang;
import me.tulio.yang.arena.Arena;
import me.tulio.yang.arena.ArenaType;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DuelSelectArenaMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return Yang.get().getLangConfig().getString("DUEL.SELECT.ARENA_MENU.TITLE");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Profile profile = Profile.get(player.getUniqueId());

		Map<Integer, Button> buttons = new HashMap<>();

		Kit kit = profile.getDuelProcedure().getKit();
		for (Arena arena : Arena.getArenas()) {
			if (!arena.isSetup() || !arena.getKits().contains(kit.getName()) || arena.isBusy())
				continue;

			if (kit.isStandaloneType() && (arena.getType() == ArenaType.STANDALONE || arena.getType() == ArenaType.DUPLICATE)) {
				buttons.put(buttons.size(), new SelectArenaButton(arena));
			}
			else if (!kit.isStandaloneType() && arena.getType() == ArenaType.SHARED) {
				buttons.put(buttons.size(), new SelectArenaButton(arena));
			}
		}

		buttons.put(this.size(buttons) + 8, new RandomArenaButton());

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
	private static class RandomArenaButton extends Button {

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.valueOf(Yang.get().getLangConfig().getString("DUEL.SELECT.ARENA_MENU.RANDOM_MATERIAL")))
				.name(Yang.get().getLangConfig().getString("DUEL.SELECT.ARENA_MENU.RANDOM_NAME"))
				.build();
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
			Profile profile = Profile.get(player.getUniqueId());

			// Update and request the procedure
			profile.getDuelProcedure().setArena(Arena.getRandomArena(profile.getDuelProcedure().getKit()));

			// Set closed by menu
			Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

			// Force close inventory
			player.closeInventory();

			// Open the next menu
			new DuelSelectRoundsMenu().openMenu(player);
		}
	}

	@AllArgsConstructor
	private static class SelectArenaButton extends Button {

		private final Arena arena;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.valueOf(Yang.get().getLangConfig().getString("DUEL.SELECT.ARENA_MENU.MATERIAL")))
					.name(Yang.get().getLangConfig().getString("DUEL.SELECT.ARENA_MENU.NAME").replace("{name}", arena.getName()))
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());

			// Update and request the procedure
			profile.getDuelProcedure().setArena(this.arena);

			// Set closed by menu
			Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

			// Force close inventory
			player.closeInventory();

			// Open the next menu
			new DuelSelectRoundsMenu().openMenu(player);
		}

	}

}

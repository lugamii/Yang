package me.tulio.yang.queue.menu;

import lombok.AllArgsConstructor;
import me.tulio.yang.Locale;
import me.tulio.yang.Yang;
import me.tulio.yang.match.Match;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.queue.Queue;
import me.tulio.yang.utilities.BukkitReflection;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.file.type.BasicConfigurationFile;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class QueueSelectKitMenu extends Menu {

	private final boolean ranked;

	{
		setAutoUpdate(true);
		setUpdateAfterClick(true);
	}

	@Override
	public String getTitle(Player player) {
		if (ranked) return Yang.get().getQueueConfig().getString("QUEUE.RANKED_INVENTORY.TITLE");
		else return Yang.get().getQueueConfig().getString("QUEUE.UNRANKED_INVENTORY.TITLE");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		for (Queue queue : Queue.getQueues()) {
			if (queue.isRanked() == ranked && queue.getKit().isEnabled())
				buttons.put(ranked ? queue.getKit().getRankedSlot() : queue.getKit().getUnrankedSlot(), new SelectKitButton(queue));
		}

		return buttons;
	}

	@AllArgsConstructor
	private static class SelectKitButton extends Button {

		private final Queue queue;

		@Override
		public ItemStack getButtonItem(Player player) {
			List<String> lore = new ArrayList<>();
			BasicConfigurationFile config = Yang.get().getQueueConfig();
			for (String s : config.getStringList("QUEUE." + (queue.isRanked() ? "RANKED" : "UNRANKED") + "_INVENTORY.LORE")) {
				lore.add(s
						.replace("{bars}", CC.SB_BAR)
						.replace("{in-fight}", String.valueOf(Match.getInFightsCount(queue)))
						.replace("{in-queue}", String.valueOf(queue.getPlayers().size()))
						.replace("{kit}", queue.getKit().getName()));
			}

			ChatColor color = ChatColor.valueOf(config.getString("QUEUE." + (queue.isRanked() ? "RANKED" : "UNRANKED") + "_INVENTORY.NAME_COLOR"));
			boolean amount = Yang.get().getMainConfig().getBoolean("QUEUE.AMOUNT_PER_FIGHTS");

			return new ItemBuilder(queue.getKit().getDisplayIcon())
				.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
				.addItemFlag(ItemFlag.HIDE_ENCHANTS)
				.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
				.name(color + queue.getKit().getName())
				.amount(amount ? Match.getInFightsCount(queue) + 1 : 1)
				.lore(lore)
				.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.isBusy()) {
				new MessageFormat(Locale.QUEUE_YOU_CANNOT_QUEUE_NOW
						.format(profile.getLocale()))
						.send(player);
				return;
			}

			player.closeInventory();

			if (queue.isRanked()) {
				if (Queue.isPingRangeBoolean()) {
					if (BukkitReflection.getPing(player) > Queue.getPingRange()) {
						new MessageFormat(Locale.QUEUE_NOT_IN_PING_RANGE
								.format(profile.getLocale()))
								.send(player);
						return;
					}
				}
			}

			queue.addPlayer(player, queue.isRanked() ? profile.getKitData().get(queue.getKit()).getElo() : 0);
		}

	}
}

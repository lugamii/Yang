package me.tulio.yang.party.menu;

import lombok.AllArgsConstructor;
import me.tulio.yang.Locale;
import me.tulio.yang.Yang;
import me.tulio.yang.party.PartyEvent;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.file.type.BasicConfigurationFile;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartyEventSelectEventMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&a&oSelect an event";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		buttons.put(3, new SelectEventButton(PartyEvent.FFA));
		buttons.put(5, new SelectEventButton(PartyEvent.SPLIT));
		return buttons;
	}

	@AllArgsConstructor
	private static class SelectEventButton extends Button {

		private final PartyEvent partyEvent;

		@Override
		public ItemStack getButtonItem(Player player) {
			BasicConfigurationFile config = Yang.get().getLangConfig();
			if (partyEvent == PartyEvent.FFA) {
				List<String> lore = config.getStringList("PARTY.FFA.LORE");
				lore.replaceAll(s -> s
						.replace("{name}", partyEvent.getName())
						.replace("{bars}", CC.SB_BAR));
				return new ItemBuilder(Material.QUARTZ)
					.name(config.getString("PARTY.FFA.NAME")
							.replace("{name}", partyEvent.getName()))
					.lore(lore)
					.build();
			}

			List<String> lore = config.getStringList("PARTY.SPLIT.LORE");
			lore.replaceAll(s -> s
					.replace("{name}", partyEvent.getName())
					.replace("{bars}", CC.SB_BAR));
			return new ItemBuilder(Material.REDSTONE)
					.name(config.getString("PARTY.SPLIT.NAME")
							.replace("{name}", partyEvent.getName()))
					.lore(lore)
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getParty() == null) {
				new MessageFormat(Locale.PARTY_NOT_IN_A_PARTY
						.format(profile.getLocale()))
						.send(player);
				return;
			}

			for (Player member : profile.getParty().getListOfPlayers()) {
				Profile profileMember = Profile.get(member.getUniqueId());
				if (profileMember.getState() != ProfileState.LOBBY) {
					new MessageFormat(Locale.PARTY_REQUIRED_ALL_PLAYERS_ON_LOBBY
							.format(profileMember.getLocale()))
							.send(player);
					player.closeInventory();
					return;
				}
			}

			new PartyEventSelectKitMenu(partyEvent).openMenu(player);
		}

	}

}

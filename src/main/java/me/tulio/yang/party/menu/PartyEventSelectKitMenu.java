package me.tulio.yang.party.menu;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.tulio.yang.Locale;
import me.tulio.yang.arena.Arena;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.match.Match;
import me.tulio.yang.match.impl.BasicFreeForAllMatch;
import me.tulio.yang.match.impl.BasicTeamMatch;
import me.tulio.yang.match.impl.BasicTeamRoundMatch;
import me.tulio.yang.match.participant.MatchGamePlayer;
import me.tulio.yang.party.Party;
import me.tulio.yang.party.PartyEvent;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.match.participant.GameParticipant;
import me.tulio.yang.match.participant.TeamGameParticipant;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class PartyEventSelectKitMenu extends Menu {

	private PartyEvent partyEvent;

	@Override
	public String getTitle(Player player) {
		return "&b&oSelect a kit";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		for (Kit kit : Kit.getKits()) {
			if (kit.isEnabled()) {
				if (partyEvent == PartyEvent.FFA) {
					if (!(kit.getGameRules().isBoxing() || kit.getGameRules().isHcfTrap() || kit.getGameRules().isSkywars() || kit.getGameRules().isBridge())) {
						buttons.put(buttons.size(), new SelectKitButton(partyEvent, kit));
					}
				}
				else if (partyEvent == PartyEvent.SPLIT) {
					if (!(kit.getGameRules().isBoxing() || kit.getGameRules().isBridge())) {
						buttons.put(buttons.size(), new SelectKitButton(partyEvent, kit));
					}
				}
			}
		}

		return buttons;
	}

	@RequiredArgsConstructor
	private static class SelectKitButton extends Button {

		private final PartyEvent partyEvent;
		private final Kit kit;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(kit.getDisplayIcon())
					.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.addItemFlag(ItemFlag.HIDE_ENCHANTS)
					.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
					.name("&a" + kit.getName())
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

			player.closeInventory();

			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getParty() == null) {
				new MessageFormat(me.tulio.yang.Locale.PARTY_NOT_IN_A_PARTY
						.format(profile.getLocale()))
						.send(player);
				return;
			}

			if (profile.getParty().getPlayers().size() <= 1) {
				player.sendMessage(CC.RED + "You do not have enough players in your party to start an event.");
				return;
			}

			Party party = profile.getParty();
			Arena arena = Arena.getRandomArena(kit);

			if (arena == null) {
				new MessageFormat(Locale.DUEL_NO_ARENAS_AVAILABLE
						.format(profile.getLocale()))
						.send(player);
				return;
			}

			arena.setBusy(true);

			Match match;

			if (partyEvent == PartyEvent.FFA) {
				List<GameParticipant<MatchGamePlayer>> participants = new ArrayList<>();

				for (Player partyPlayer : party.getListOfPlayers()) {
					participants.add(new GameParticipant<>(
							new MatchGamePlayer(partyPlayer.getUniqueId(), partyPlayer.getName())));
				}

				match = new BasicFreeForAllMatch(null, kit, arena, participants);
			} else {
				Player partyLeader = party.getLeader();
				Player randomLeader = Bukkit.getPlayer(party.getPlayers().get(1));

				MatchGamePlayer leaderA = new MatchGamePlayer(partyLeader.getUniqueId(), partyLeader.getName());
				MatchGamePlayer leaderB = new MatchGamePlayer(randomLeader.getUniqueId(), randomLeader.getName());

				GameParticipant<MatchGamePlayer> participantA = new TeamGameParticipant<>(leaderA);
				GameParticipant<MatchGamePlayer> participantB = new TeamGameParticipant<>(leaderB);

				List<Player> players = new ArrayList<>(party.getListOfPlayers());
				Collections.shuffle(players);

				for (Player otherPlayer : players) {
					if (participantA.containsPlayer(otherPlayer.getUniqueId()) ||
					    participantB.containsPlayer(otherPlayer.getUniqueId())) {
						continue;
					}

					MatchGamePlayer gamePlayer = new MatchGamePlayer(otherPlayer.getUniqueId(), otherPlayer.getName());

					if (participantA.getPlayers().size() > participantB.getPlayers().size()) {
						participantB.getPlayers().add(gamePlayer);
					} else {
						participantA.getPlayers().add(gamePlayer);
					}
				}

				// Create match
				if (kit.getGameRules().isBridge()) match = new BasicTeamRoundMatch(null, kit, arena, false, participantA, participantB, 3);
				else match = new BasicTeamMatch(null, kit, arena, false, participantA, participantB);
			}

			// Start match
			match.start();
		}

	}

}

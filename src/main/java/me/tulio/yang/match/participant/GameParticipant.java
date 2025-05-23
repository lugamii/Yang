package me.tulio.yang.match.participant;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Locale;
import me.tulio.yang.match.lunar.BukkitAPI;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class GameParticipant<T extends GamePlayer> {

	@Getter private final T leader;
	@Getter @Setter private int roundWins;
	@Getter @Setter private boolean eliminated;
	@Getter @Setter private Location rally;

	public GameParticipant(T leader) {
		this.leader = leader;
	}

	public List<T> getPlayers() {
		return Lists.newArrayList(leader);
	}

	public boolean isAllDead() {
		return leader.isDead();
	}

	public int getAliveCount() {
		return leader.isDead() ? 0 : 1;
	}

	public boolean containsPlayer(UUID uuid) {
		return leader.getUuid().equals(uuid);
	}

	public String getConjoinedNames() {
		return leader.getUsername();
	}

	public void reset() {
		eliminated = false;
		for (GamePlayer gamePlayer : getPlayers()) {
			gamePlayer.setDead(false);
		}
	}

	public void sendMessage(String message) {
		for (GamePlayer gamePlayer : getPlayers()) {
			if (!gamePlayer.isDisconnected()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					player.sendMessage(message);
				}
			}
		}
	}

	public void sendMessage(Locale lang, MessageFormat messageFormat) {
		for (GamePlayer gamePlayer : getPlayers()) {
			if (!gamePlayer.isDisconnected()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					messageFormat.setMessage(lang.format(Profile.get(player.getUniqueId()).getLocale()));
					messageFormat.send(player);
				}
			}
		}
	}

	public void sendMessage(List<String> messages) {
		for (GamePlayer gamePlayer : getPlayers()) {
			if (!gamePlayer.isDisconnected()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					for (String message : messages) {
						player.sendMessage(message);
					}
				}
			}
		}
	}

	public void sendSound(Sound sound, float volume, float pitch) {
		for (GamePlayer gamePlayer : getPlayers()) {
			if (!gamePlayer.isDisconnected()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					player.playSound(player.getLocation(), sound, volume, pitch);
				}
			}
		}
	}

	public void sendRallyWaypoints() {
		for (GamePlayer gamePlayer : getPlayers()) {
			if (!gamePlayer.isDisconnected()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					BukkitAPI.sendWaypoint(player, rally);
				}
			}
		}
	}

	public void removeRallyWaypoints() {
		for (GamePlayer gamePlayer : getPlayers()) {
			if (!gamePlayer.isDisconnected()) {
				Player player = gamePlayer.getPlayer();

				if (player != null && rally != null) {
					BukkitAPI.removeWaypoint(player, rally);
				}
			}
		}
	}
}

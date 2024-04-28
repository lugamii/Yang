package me.tulio.yang.match;

import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Locale;
import me.tulio.yang.Yang;
import me.tulio.yang.arena.Arena;
import me.tulio.yang.hotbar.Hotbar;
import me.tulio.yang.hotbar.HotbarItem;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.knockback.Knockback;
import me.tulio.yang.match.events.MatchEndEvent;
import me.tulio.yang.match.events.MatchStartEvent;
import me.tulio.yang.match.impl.BasicTeamMatch;
import me.tulio.yang.match.impl.BasicTeamRoundMatch;
import me.tulio.yang.match.lunar.BukkitAPI;
import me.tulio.yang.match.participant.GameParticipant;
import me.tulio.yang.match.participant.GamePlayer;
import me.tulio.yang.match.participant.MatchGamePlayer;
import me.tulio.yang.match.task.MatchLogicTask;
import me.tulio.yang.match.task.MatchPearlCooldownTask;
import me.tulio.yang.match.task.MatchResetTask;
import me.tulio.yang.match.task.MatchSnapshotCleanupTask;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.profile.meta.ProfileKitData;
import me.tulio.yang.profile.visibility.VisibilityLogic;
import me.tulio.yang.queue.Queue;
import me.tulio.yang.scoreboard.ability.PandaAbility;
import me.tulio.yang.tournament.Tournament;
import me.tulio.yang.utilities.Cooldown;
import me.tulio.yang.utilities.PlayerUtil;
import me.tulio.yang.utilities.TaskUtil;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.chat.ChatComponentBuilder;
import me.tulio.yang.utilities.chat.ChatHelper;
import me.tulio.yang.utilities.string.MessageFormat;
import me.tulio.yang.utilities.string.TimeUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static me.tulio.yang.utilities.KitUtils.giveBridgeKit;

@Getter
public abstract class Match {

	@Getter protected static List<Match> matches = new ArrayList<>();
	@Getter public static World DEFAULT_WORLD = Bukkit.getWorld(Yang.get().getMainConfig().getString("MATCH.DEFAULT_MATCH_WORLD"));

	private final UUID matchId = UUID.randomUUID();
	private final Queue queue;
	protected final Kit kit;
	protected final Arena arena;
	protected final boolean ranked;
	@Setter protected MatchState state = MatchState.STARTING_ROUND;
	protected final List<MatchSnapshot> snapshots;
	protected final List<UUID> spectators;
	protected final List<Item> droppedItems;
	private final List<Location> placedBlocks;
	private final List<BlockState> changedBlocks;
	protected long timeData;
	protected MatchLogicTask logicTask;

	public Match(Queue queue, Kit kit, Arena arena, boolean ranked) {
		this.queue = queue;
		this.kit = kit;
		this.arena = arena;
		this.ranked = ranked;
		this.snapshots = new ArrayList<>();
		this.spectators = new ArrayList<>();
		this.droppedItems = new ArrayList<>();
		this.placedBlocks = new ArrayList<>();
		this.changedBlocks = new ArrayList<>();

		matches.add(this);
	}

	public void setupPlayer(Player player) {
		// Set the player as alive
		MatchGamePlayer gamePlayer = getGamePlayer(player);
		gamePlayer.setDead(false);

		// If the player disconnected, skip any operations for them
		if (gamePlayer.isDisconnected()) return;

		Profile profile = Profile.get(player.getUniqueId());

		// Reset the player's inventory
		PlayerUtil.reset(player);

		// Deny movement if the kit is sumo
		if (getKit().getGameRules().isSumo() || getKit().getGameRules().isBridge() || getKit().getGameRules().isParkour() ||
				!Yang.get().getMainConfig().getBoolean("MATCH.MOVE_BEFORE_START_MATCH")) {
			PlayerUtil.denyMovement(player);
		}

		// Set the player's max damage ticks and knockback
		player.setMaximumNoDamageTicks(getKit().getGameRules().getHitDelay());
		Knockback.getKnockbackProfiler().setKnockback(player.getPlayer(), getKit().getGameRules().getKbProfile());

		// If the player has no kits, apply the default kit, otherwise
		// give the player a list of kit books to choose from
		if (getKit().getGameRules().isParkour()) {
			player.getInventory().setItem(Hotbar.getItems().get(HotbarItem.PARKOUR_HIDE_PLAYERS).getSlot(), Hotbar.getItem(HotbarItem.PARKOUR_HIDE_PLAYERS));
			player.getInventory().setItem(Hotbar.getItems().get(HotbarItem.PARKOUR_LAST_CHECKPOINT).getSlot(), Hotbar.getItem(HotbarItem.PARKOUR_LAST_CHECKPOINT));
			player.getInventory().setItem(Hotbar.getItems().get(HotbarItem.PARKOUR_RESET).getSlot(), Hotbar.getItem(HotbarItem.PARKOUR_RESET));
		}
		else if (!getKit().getGameRules().isSumo()) {
			ProfileKitData kitData = profile.getKitData().get(getKit());

			if (kitData.getKitCount() > 0) {
				profile.getKitData().get(getKit()).giveBooks(player);
			} else {
				player.getInventory().setArmorContents(getKit().getKitLoadout().getArmor());
				player.getInventory().setContents(getKit().getKitLoadout().getContents());
				new MessageFormat(Locale.MATCH_GIVE_KIT.format(profile.getLocale()))
					.add("{kit_name}", "Default")
					.send(player);
			}
		}

		for (GameParticipant<MatchGamePlayer> participant : getParticipants()) {
			for (MatchGamePlayer participantPlayer : participant.getPlayers()) {
				if (participantPlayer.getPlayer() != null && !participantPlayer.getPlayer().equals(player)) {
					VisibilityLogic.handle(player, participantPlayer.getPlayer());
				}
			}
		}
	}

	public void start() {
		// Set state
		state = MatchState.STARTING_ROUND;

		// Start logic task
		logicTask = new MatchLogicTask(this);
		logicTask.runTaskTimer(Yang.get(), 0L, 20L);

		// Set arena as active
		arena.setBusy(true);

		// Send arena message
		if (getArena().getAuthor() != null && !getArena().getAuthor().isEmpty()) {
			sendMessage(Locale.MATCH_PLAYING_ARENA_AUTHOR, new MessageFormat()
				.add("{arena_name}", arena.getName())
				.add("{author}", arena.getAuthor()));
		} else
			sendMessage(Locale.MATCH_PLAYING_ARENA_NO_AUTHOR, new MessageFormat().add("{arena_name}", arena.getName()));

		// Setup players
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					Profile profile = Profile.get(player.getUniqueId());
					profile.setState(ProfileState.FIGHTING);
					profile.setMatch(this);

					setupPlayer(player);
					if (getKit().getGameRules().isShowHealth()) {
						for (GameParticipant<MatchGamePlayer> gameParticipantOther : getParticipants()) {
							for (MatchGamePlayer gamePlayerOther : gameParticipantOther.getPlayers()) {
								Player other = gamePlayerOther.getPlayer();
								Scoreboard scoreboard = player.getScoreboard();
								Objective objective = scoreboard.getObjective(DisplaySlot.BELOW_NAME);

								if (objective == null) {
									objective = scoreboard.registerNewObjective("showhealth", "health");
								}

								objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
								objective.setDisplayName(ChatColor.RED + StringEscapeUtils.unescapeJava("\u2764"));
								objective.getScore(other.getName()).setScore((int) Math.floor(other.getHealth() / 2));
							}
						}
					}
				}
			}
		}

		// Handle player visibility
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					VisibilityLogic.handle(player);
				}
			}
		}

		new MatchStartEvent(this).call();
	}

	public void end() {
		new MatchEndEvent(this).call();

		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						player.setFireTicks(0);
						player.updateInventory();

						Profile profile = Profile.get(player.getUniqueId());
						profile.setFishHit(0);
						profile.setState(ProfileState.LOBBY);
						profile.setMatch(null);
						profile.setEnderpearlCooldown(new Cooldown(0));
						profile.setSelectedKit(null);
						profile.updateCategory();

						if (getKit().getGameRules().isShowHealth()) {
							Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

							if (objective != null) objective.unregister();
						}
						if (getKit().getGameRules().isHcfTrap() && Yang.get().getScoreboardConfig().getBoolean("FIGHTS.PANDAABILITY"))
							PandaAbility.removeCooldowns(player);
						if (profile.getFocused() != null && Yang.get().isLunarClient()) {
							BukkitAPI.removeTeammates(player);
							profile.setFocused(null);
						}
					}
				}
			}
			gameParticipant.removeRallyWaypoints();
		}

		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						VisibilityLogic.handle(player);
						Hotbar.giveHotbarItems(player);
						Yang.get().getEssentials().teleportToSpawn(player);
					}
				}
			}
		}

		for (Player player : getSpectatorsAsPlayers()) {
			removeSpectator(player);
		}

		removeEntities();

		new MatchResetTask(this).runTask(Yang.get());

		matches.remove(this);
		logicTask.cancel();
	}

	public abstract boolean canEndMatch();

	public void onRoundStart() {
		// Reset snapshots
		snapshots.clear();

		timeData = System.currentTimeMillis();

		// Reset each game participant
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			gameParticipant.reset();
			for (MatchGamePlayer player : gameParticipant.getPlayers()) {
				if (player.getPlayer() != null) {
					// Allow movement if the kit is sumo
					if (getKit().getGameRules().isSumo() || getKit().getGameRules().isBridge() || getKit().getGameRules().isParkour() ||
							!Yang.get().getMainConfig().getBoolean("MATCH.MOVE_BEFORE_START_MATCH"))
						PlayerUtil.allowMovement(player.getPlayer());
				}
			}
		}

		TaskUtil.run(() -> {
			for (GameParticipant<MatchGamePlayer> participant : getParticipants()) {
				for (MatchGamePlayer gamePlayer : participant.getPlayers()) {
					Player player = gamePlayer.getPlayer();
					if (player != null) {
						for (PotionEffect effect : kit.getGameRules().getEffects()) {
							player.addPotionEffect(effect);
						}
					}
				}
			}
			}
		);
	}

	public abstract boolean canStartRound();

	public void onRoundEnd() {
		timeData = System.currentTimeMillis() - timeData;
		// Snapshot alive players' inventories
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						if (!gamePlayer.isDead()) {
							MatchSnapshot snapshot = new MatchSnapshot(player, false);
							snapshot.setPotionsThrown(gamePlayer.getPotionsThrown());
							snapshot.setPotionsMissed(gamePlayer.getPotionsMissed());
							snapshot.setLongestCombo(gamePlayer.getLongestCombo());
							snapshot.setTotalHits(gamePlayer.getHits());

							snapshots.add(snapshot);
						}
					}
				}
			}
		}

		if (this instanceof BasicTeamMatch) {
			BasicTeamMatch match = (BasicTeamMatch) this;
			// Set opponents in snapshots if solo
			if (match.getParticipantA().getPlayers().size() == 1 && match.getParticipantB().getPlayers().size() == 1) {
				for (MatchSnapshot snapshot : snapshots) {
					if (snapshot.getUuid().equals(match.getParticipantA().getLeader().getUuid())) {
						snapshot.setOpponent(match.getParticipantB().getLeader().getUuid());
					} else if (snapshot.getUuid().equals(match.getParticipantB().getLeader().getUuid())) {
						snapshot.setOpponent(match.getParticipantA().getLeader().getUuid());
					}
				}
			}
		}

		// Make all snapshots available
		for (MatchSnapshot snapshot : snapshots) {
			snapshot.setCreatedAt(System.currentTimeMillis());
			MatchSnapshot.getSnapshots().put(snapshot.getUuid(), snapshot);
		}

		sendEndMatchMessages();
	}

	public void sendEndMatchMessages() {
		// Send ending messages to game participants
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						for (BaseComponent[] components : generateEndComponents(player)) {
							player.spigot().sendMessage(components);
						}
					}
				}
			}
		}

		// Send ending messages to spectators
		for (Player player : getSpectatorsAsPlayers()) {
			for (BaseComponent[] components : generateEndComponents(player)) {
				player.spigot().sendMessage(components);
			}

			removeSpectator(player);
		}
	}

	public abstract boolean canEndRound();

	public void onDisconnect(Player dead) {
		if (this instanceof BasicTeamRoundMatch) {
			BasicTeamRoundMatch match = (BasicTeamRoundMatch) this;
			if (match.getParticipantA().containsPlayer(dead.getUniqueId())) match.setWinningParticipant(match.getParticipantB());
			else match.setWinningParticipant(match.getParticipantA());
			end();
			return;
		}

		// Don't continue if the match is already ending
		if (!(state == MatchState.STARTING_ROUND || state == MatchState.PLAYING_ROUND || state == MatchState.ENDING_ROUND)) return;

		MatchGamePlayer deadGamePlayer = getGamePlayer(dead);

		if (deadGamePlayer != null) {
			deadGamePlayer.setDisconnected(true);

			if (!deadGamePlayer.isDead()) onDeath(dead, true);
		}
	}

	public void onDeath(Player dead, boolean disconnected) {
		// Don't continue if the match is already ending
		if (!(state == MatchState.STARTING_ROUND || state == MatchState.PLAYING_ROUND)) return;

		MatchGamePlayer deadGamePlayer = getGamePlayer(dead);

		// Don't continue if the player is already dead
		if (deadGamePlayer.isDead()) return;

		// Get killer
		Player killer = PlayerUtil.getLastAttacker(dead);

		// Set player as dead
		if (getKit().getGameRules().isBridge()) {
			getParticipant(dead).getPlayers().forEach(gamePlayer -> gamePlayer.setDead(false));
		}
		else deadGamePlayer.setDead(true);

		Profile profile = Profile.get(dead.getUniqueId());

		if (killer != null) {
			MatchGamePlayer matchGamePlayer = getGamePlayer(killer);
			matchGamePlayer.incrementKills();
		}

		dead.setVelocity(new Vector());

		MatchSnapshot snapshot = new MatchSnapshot(dead, true);
		snapshot.setPotionsMissed(deadGamePlayer.getPotionsMissed());
		snapshot.setPotionsThrown(deadGamePlayer.getPotionsThrown());
		snapshot.setLongestCombo(deadGamePlayer.getLongestCombo());
		snapshot.setTotalHits(deadGamePlayer.getHits());

		snapshots.add(snapshot);

		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						if (this instanceof BasicTeamMatch) {
							/*if (!getKit().getGameRules().isSumo() && !getKit().getGameRules().isBridge())*/
							VisibilityLogic.handle(player, dead);
						}
						if (!getKit().getGameRules().isBridge())
							sendDeathMessage(player, dead, killer);
					}
				}
			}
		}

		// Handle visibility for spectators
		// Send death message
		for (Player player : getSpectatorsAsPlayers()) {
			if (!getKit().getGameRules().isSumo() && !getKit().getGameRules().isBridge()) {
				VisibilityLogic.handle(player, dead);
			}
			if (!getKit().getGameRules().isBridge()) {
				sendDeathMessage(player, dead, killer);
			}
		}

		if (canEndRound()) {
			state = MatchState.ENDING_ROUND;
			onRoundEnd();

			if (canEndMatch()) state = MatchState.ENDING_MATCH;
			logicTask.setNextAction(Yang.get().getMainConfig().getInteger("MATCH.END_ROUND_TIME"));
		} else {
			if (!(this instanceof BasicTeamRoundMatch)) {
				if (!disconnected) {
					TaskUtil.runLater(() -> {
						PlayerUtil.reset(dead);
						addSpectator(dead, killer, false);
					}, 10L);
				}
			} else {
				if (getKit().getGameRules().isBridge()) {
					BasicTeamRoundMatch teamRoundMatch = (BasicTeamRoundMatch) this;

					Location spawn = teamRoundMatch.getParticipantA().containsPlayer(dead.getUniqueId()) ?
						teamRoundMatch.getArena().getSpawnA() : teamRoundMatch.getArena().getSpawnB();
					dead.teleport(spawn.add(0, 2, 0));
					TaskUtil.runLater(() -> {
						PlayerUtil.reset(dead);
						if (profile.getSelectedKit() == null) {
							dead.getInventory().setContents(getKit().getKitLoadout().getContents());
						} else {
							dead.getInventory().setContents(profile.getSelectedKit().getContents());
						}
						giveBridgeKit(dead);
					}, 5L);
				}

				/*BasicTeamRoundMatch teamRoundMatch = (BasicTeamRoundMatch) this;

				Location spawn = teamRoundMatch.getParticipantA().containsPlayer(dead.getUniqueId()) ?
						teamRoundMatch.getArena().getSpawnA() : teamRoundMatch.getArena().getSpawnB();
				dead.teleport(spawn.add(0, 2, 0));
				TaskUtil.runLater(() -> {
					PlayerUtil.reset(dead);
					if (profile.getSelectedKit() == null) {
						dead.getInventory().setContents(getKit().getKitLoadout().getContents());
					} else {
						dead.getInventory().setContents(profile.getSelectedKit().getContents());
					}
				}, 5L);
				if (getKit().getGameRules().isBridge()) {
					giveBridgeKit(dead);
				}*/
			}
		}

		if (Tournament.getTournament() != null && Tournament.getTournament().getPlayers().contains(dead.getUniqueId())) {
			profile.setInTournament(false);
		}
	}

	public abstract boolean isOnSameTeam(Player first, Player second);

	public abstract List<GameParticipant<MatchGamePlayer>> getParticipants();

	public GameParticipant<MatchGamePlayer> getParticipant(Player player) {
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			if (gameParticipant.containsPlayer(player.getUniqueId())) {
				return gameParticipant;
			}
		}
		return null;
	}

	public MatchGamePlayer getGamePlayer(Player player) {
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (gamePlayer.getUuid().equals(player.getUniqueId())) {
					return gamePlayer;
				}
			}
		}
		return null;
	}

	public abstract ChatColor getRelationColor(Player viewer, Player target);

	public abstract List<String> getScoreboardLines(Player player);

	public abstract List<String> getSpectatorScoreboardLines();

	public void addSpectator(Player spectator, Player target, boolean follow) {
		Profile profile = Profile.get(spectator.getUniqueId());

		profile.setMatch(this);

		if (profile.getParty() == null) spectator.teleport(target.getLocation().clone().add(0, 2, 0));

		if (profile.getState() != ProfileState.STAFF_MODE) {
			spectators.add(spectator.getUniqueId());
			profile.setState(ProfileState.SPECTATING);
			if (!follow) {
				Hotbar.giveHotbarItems(spectator);
				spectator.updateInventory();
			}
		}

		VisibilityLogic.handle(spectator);

		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (GamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player bukkitPlayer = gamePlayer.getPlayer();

					if (bukkitPlayer != null && (!spectator.hasPermission("yang.staff") || !spectator.isOp())) {
						VisibilityLogic.handle(bukkitPlayer);
						new MessageFormat(Locale.MATCH_NOW_SPECTATING.format(Profile.get(bukkitPlayer.getUniqueId()).getLocale()))
							.add("{spectator_name}", spectator.getName())
							.send(bukkitPlayer);
					}
				}
			}
		}
		spectator.spigot().setCollidesWithEntities(false);
		TaskUtil.runLater(() -> {
			spectator.setGameMode(GameMode.CREATIVE);
			spectator.setAllowFlight(true);
			spectator.setFlying(true);
		}, 5L);
	}

	public void removeSpectator(Player spectator) {
		spectators.remove(spectator.getUniqueId());

		Profile profile = Profile.get(spectator.getUniqueId());
		profile.setState(ProfileState.LOBBY);
		profile.setMatch(null);

		boolean follow = profile.getFollow() != null;

		if (!follow) {
			PlayerUtil.reset(spectator);
			Hotbar.giveHotbarItems(spectator);
			Yang.get().getEssentials().teleportToSpawn(spectator);
		}
		else {
			profile.getFollow().detect();
		}
		VisibilityLogic.handle(spectator);

		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player bukkitPlayer = gamePlayer.getPlayer();

					if (bukkitPlayer != null && (!spectator.hasPermission("yang.staff") || !spectator.isOp())) {
						VisibilityLogic.handle(bukkitPlayer);

						if (state != MatchState.ENDING_MATCH) {
							new MessageFormat(Locale.MATCH_NO_LONGER_SPECTATING.format(Profile.get(bukkitPlayer.getUniqueId()).getLocale()))
								.add("{spectator_name}", spectator.getName())
								.send(bukkitPlayer);
						}
					}
				}
			}
		}
	}

	public String getDuration() {
		if (state == MatchState.STARTING_ROUND) return "Starting";
		if (state == MatchState.ENDING_MATCH) return "Ending";
		else return TimeUtil.millisToTimer(System.currentTimeMillis() - timeData);
	}

	public void sendMessage(String message) {
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			gameParticipant.sendMessage(CC.translate(message));
		}

		for (Player player : getSpectatorsAsPlayers()) {
			player.sendMessage(CC.translate(message));
		}
	}

	public void sendMessage(Locale lang, MessageFormat messageFormat) {
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			gameParticipant.sendMessage(lang, messageFormat);
		}

		for (Player player : getSpectatorsAsPlayers()) {
			messageFormat.setMessage(lang.format(Profile.get(player.getUniqueId()).getLocale()));
			messageFormat.send(player);
		}
	}

	public void sendSound(Sound sound, float volume, float pitch) {
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			gameParticipant.sendSound(sound, volume, pitch);
		}

		for (Player player : getSpectatorsAsPlayers()) {
			player.playSound(player.getLocation(), sound, volume, pitch);
		}
	}

	protected List<Player> getSpectatorsAsPlayers() {
		List<Player> players = new ArrayList<>();

		for (UUID uuid : spectators) {
			Player player = Bukkit.getPlayer(uuid);

			if (player != null) players.add(player);
		}

		return players;
	}

	public abstract List<BaseComponent[]> generateEndComponents(Player player);

	public void sendDeathMessage(Player player, Player dead, Player killer) {
		String deathMessage;
		Profile profile = Profile.get(player.getUniqueId());

		if (killer == null) {
			deathMessage = new MessageFormat(Locale.MATCH_PLAYER_DIED.format(profile.getLocale()))
				.add("{dead_name}", getRelationColor(player, dead) + dead.getName())
				.toString();
		} else {
			deathMessage = new MessageFormat(Locale.MATCH_PLAYER_KILLED.format(profile.getLocale()))
				.add("{dead_name}", getRelationColor(player, dead) + dead.getName())
				.add("{killer_name}", getRelationColor(player, killer) + killer.getName())
				.toString();
		}

		player.sendMessage(deathMessage);
	}

	public void sendDeathMessage(Player dead, Player killer) {
		String deathMessage;
		// Send death message
		for (GameParticipant<MatchGamePlayer> gameParticipant : this.getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player other = gamePlayer.getPlayer();
					Profile profile = Profile.get(other.getUniqueId());

					if (killer == null) {
						deathMessage = new MessageFormat(Locale.MATCH_PLAYER_DIED.format(profile.getLocale()))
							.add("{dead_name}", getRelationColor(other, dead) + dead.getName())
							.toString();
					} else {
						deathMessage = new MessageFormat(Locale.MATCH_PLAYER_KILLED.format(profile.getLocale()))
							.add("{dead_name}", getRelationColor(other, dead) + dead.getName())
							.add("{killer_name}", getRelationColor(other, killer) + killer.getName())
							.toString();
					}

					other.sendMessage(deathMessage);
				}
			}
		}

		// Handle visibility for spectators
		// Send death message
		for (Player other : this.getSpectatorsAsPlayers()) {
			Profile profile = Profile.get(other.getUniqueId());
			if (killer == null) {
				deathMessage = new MessageFormat(Locale.MATCH_PLAYER_DIED.format(profile.getLocale()))
					.add("{dead_name}", getRelationColor(other, dead) + dead.getName())
					.toString();
			} else {
				deathMessage = new MessageFormat(Locale.MATCH_PLAYER_KILLED.format(profile.getLocale()))
					.add("{dead_name}", getRelationColor(other, dead) + dead.getName())
					.add("{killer_name}", getRelationColor(other, killer) + killer.getName())
					.toString();
			}
			other.sendMessage(deathMessage);
		}
	}

	public static void init() {
		if (Yang.get().getMainConfig().getBoolean("MATCH.ENDERPEARL_XP_COOLDOWN"))
			new MatchPearlCooldownTask().runTaskTimerAsynchronously(Yang.get(), 2L, 2L);
		new MatchSnapshotCleanupTask().runTaskTimerAsynchronously(Yang.get(), 20L * 5, 20L * 5);
//		Yang.get().getServer().getScheduler().runTaskTimer(Yang.get(), new WaterCheckTask(), 20L, 8L);
	}

	public void removeEntities() {
		getDroppedItems().forEach(Entity::remove);
	}

	public static void cleanup() {
		for (Match match : matches) {
			match.getPlacedBlocks().forEach(location -> location.getBlock().setType(Material.AIR));
			match.getChangedBlocks().forEach((blockState) -> blockState.getLocation().getBlock().setType(blockState.getType()));
			match.getDroppedItems().forEach(Entity::remove);
		}
	}

	public static int getInFightsCount(Queue queue) {
		int i = 0;

		for (Match match : matches) {
			if (match.getQueue() != null &&
			    (match.getState() == MatchState.STARTING_ROUND || match.getState() == MatchState.PLAYING_ROUND)) {
				if (match.getQueue().equals(queue)) {
					for (GameParticipant<? extends GamePlayer> gameParticipant : match.getParticipants()) {
						i += gameParticipant.getPlayers().size();
					}
				}
			}
		}

		return i;
	}

	public static BaseComponent[] generateInventoriesComponents(String prefix, GameParticipant<MatchGamePlayer> participant) {
		return generateInventoriesComponents(prefix, Collections.singletonList(participant));
	}

	public static BaseComponent[] generateInventoriesComponents(String prefix, List<GameParticipant<MatchGamePlayer>> participants) {
		ChatComponentBuilder builder = new ChatComponentBuilder(prefix);

		int totalPlayers = 0;
		int processedPlayers = 0;

		for (GameParticipant<MatchGamePlayer> gameParticipant : participants) {
			totalPlayers += gameParticipant.getPlayers().size();
		}

		for (GameParticipant<MatchGamePlayer> gameParticipant : participants) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				processedPlayers++;

				ChatComponentBuilder current = new ChatComponentBuilder(
						CC.translate(
							new MessageFormat(Locale.MATCH_CLICK_TO_VIEW_NAME.format(Profile.get(gamePlayer.getUuid()).getLocale()))
								.add("{name}", CC.strip(gamePlayer.getUsername()))
								.add("{color}", gamePlayer.isDisconnected() ?
										CC.GRAY + CC.STRIKE_THROUGH :
										Profile.get(gamePlayer.getUuid()).getColor())
								.toString()))
						.attachToEachPart(ChatHelper.hover(CC.translate(
							new MessageFormat(Locale.MATCH_CLICK_TO_VIEW_HOVER.format(Profile.get(gamePlayer.getUuid()).getLocale()))
								.add("{name}", gamePlayer.getUsername())
								.add("{color}", gamePlayer.isDisconnected() ?
										CC.GRAY + CC.STRIKE_THROUGH :
										Profile.get(gamePlayer.getUuid()).getColor())
								.toString())))
						.attachToEachPart(ChatHelper.click("/viewinv " + gamePlayer.getUuid().toString()));

				builder.append(current.create());

				if (processedPlayers != totalPlayers) {
					builder.append(", ");
					builder.getCurrent().setClickEvent(null);
					builder.getCurrent().setHoverEvent(null);
				}
			}
		}

		return builder.create();
	}

	public String getMatchState() {
		return this.state.name();
	}

	public String getArenaName() {
		return this.arena.getName();
	}

	public String getArenaType() {
		return this.arena.getType().name();
	}

	public String getKitName() {
		return this.kit.getName();
	}

}

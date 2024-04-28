package me.tulio.yang.profile;

import me.tulio.yang.Locale;
import me.tulio.yang.Yang;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.essentials.event.SpawnTeleportEvent;
import me.tulio.yang.hotbar.Hotbar;
import me.tulio.yang.nametags.NameTag;
import me.tulio.yang.profile.visibility.VisibilityLogic;
import me.tulio.yang.profile.weight.Weight;
import me.tulio.yang.tablist.TabAdapter;
import me.tulio.yang.utilities.Cooldown;
import me.tulio.yang.utilities.Reflection;
import me.tulio.yang.utilities.TaskUtil;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.string.MessageFormat;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;
import java.util.Collections;

public class ProfileListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onSpawnTeleportEvent(SpawnTeleportEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if (!profile.isBusy() && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			Hotbar.giveHotbarItems(event.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if (profile.getState() != ProfileState.FIGHTING) {
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE || !event.getPlayer().isOp()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if (profile.getState() != ProfileState.FIGHTING) {
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE || !event.getPlayer().isOp()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if (profile.getState() == ProfileState.LOBBY) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) {
				event.setCancelled(true);

				if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
					Yang.get().getEssentials().teleportToSpawn(player);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLoginEvent(PlayerLoginEvent event) {
		if (!Yang.get().isServerLoaded()) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, CC.translate("&cPlease wait for the server to load completely"));
			return;
		}
		if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
			if (Profile.getProfiles().get(event.getPlayer().getUniqueId()) == null) {
				Profile profile = new Profile(event.getPlayer().getUniqueId());
				try {
					TaskUtil.runAsync(profile::load);
					Profile.getProfiles().put(event.getPlayer().getUniqueId(), profile);
				} catch (Exception e) {
					event.disallow(
							PlayerLoginEvent.Result.KICK_OTHER,
							CC.translate(Yang.get().getLangConfig().getString("GLOBAL_MESSAGES.FAILED_LOAD_PROFILE")));
					throw new IllegalArgumentException("Player Profile could not be created, contact the plugin author");
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player player = event.getPlayer();

		new MessageFormat(Locale.JOIN_MESSAGES
				.format(Profile.get(player.getUniqueId()).getLocale()))
				.send(event.getPlayer());

		PacketPlayOutScoreboardTeam a = new PacketPlayOutScoreboardTeam();
		Reflection.getField(a.getClass(), "a", String.class).set(a, "Yang");
		Reflection.getField(a.getClass(), "h", int.class).set(a, 3);
		Reflection.getField(a.getClass(), "b", String.class).set(a, "Yang");
		Reflection.getField(a.getClass(), "f", int.class).set(a, -1);
		Reflection.getField(a.getClass(), "g", Collection.class).set(a, Collections.singletonList(player.getName()));

		for (Player other : Bukkit.getOnlinePlayers()) {
			TaskUtil.runAsync(() -> ((CraftPlayer) other).getHandle().playerConnection.sendPacket(a));
		}

		Profile profile = Profile.get(player.getUniqueId());

		// This is like this to test my theory that the luckperms api being slow, that's why sometimes the colors do not appear
		Weight weight = new Weight(player.getUniqueId(), Yang.get().getRankManager().getRank().getWeight(player.getUniqueId()));
		TaskUtil.runLater(() -> {
			weight.setFormat(profile.getColor() + player.getName());
			profile.setWeight(weight);
			TabAdapter.getRanks().add(weight);
		}, 5L);

		profile.setName(player.getName());
		profile.setOnline(true);
		profile.setFishHit(0);
		profile.setState(ProfileState.LOBBY);
		profile.setMatch(null);
		profile.setEnderpearlCooldown(new Cooldown(0));
		profile.setSelectedKit(null);
		if (Yang.get().getColoredRanksConfig().getConfiguration().contains("groups." + Yang.get().getRankManager().getRank().getName(player.getUniqueId()))) {
			profile.setColor(Yang.get().getColoredRanksConfig().getString("groups." + Yang.get().getRankManager().getRank().getName(player.getUniqueId())));
		} else {
			profile.setColor("&r");
		}
		profile.updateCategory();

		Yang.get().getEssentials().teleportToSpawn(player);
		Hotbar.giveHotbarItems(player);

		if (profile.getClan() == null) {
			if (Clan.getByPlayer(player) != null) {
				profile.setClan(Clan.getByPlayer(player));
			}
		}

		TaskUtil.runAsync(()-> {
			if (NameTag.isInitiated()) {
				player.setMetadata("sl-LoggedIn", new FixedMetadataValue(Yang.get(), true));
				NameTag.initiatePlayer(player);
				NameTag.reloadPlayer(player);
				NameTag.reloadOthersFor(player);
			}
		});

		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			VisibilityLogic.handle(player, otherPlayer);
			VisibilityLogic.handle(otherPlayer, player);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage(null);

		Profile profile = Profile.get(event.getPlayer().getUniqueId());
		profile.setOnline(false);
		TaskUtil.runAsync(profile::save);

		if (profile.getFollow() != null) profile.setFollow(null);

		TabAdapter.getRanks().remove(profile.getWeight());

		if (profile.getRematchData() != null) profile.getRematchData().validate();
	}

	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event) {
		if (event.getReason() != null) {
			if (event.getReason().contains("Flying is not enabled")) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Profile profile = Profile.get(player.getUniqueId());
			if (player.getGameMode() == GameMode.SURVIVAL && player.hasPermission("*")) {
				if (profile.getState() == ProfileState.LOBBY && event.getClickedInventory() instanceof PlayerInventory
						&& !profile.getKitEditorData().isActive()) {
					event.setCancelled(true);
				}
			}
        }
	}
}

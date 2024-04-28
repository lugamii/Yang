package me.tulio.yang.event.game.map.impl;

import lombok.Getter;
import me.tulio.yang.Yang;
import me.tulio.yang.event.game.EventGame;
import me.tulio.yang.event.game.map.EventGameMap;
import me.tulio.yang.event.impl.spleef.SpleefGameLogic;
import me.tulio.yang.event.impl.sumo.SumoGameLogic;
import me.tulio.yang.event.impl.tntrun.TNTRunGameLogic;
import me.tulio.yang.event.impl.tnttag.TNTTagGameLogic;
import me.tulio.yang.profile.follow.Follow;
import me.tulio.yang.match.participant.GameParticipant;
import me.tulio.yang.match.participant.GamePlayer;
import me.tulio.yang.profile.visibility.VisibilityLogic;
import me.tulio.yang.utilities.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpreadEventGameMap extends EventGameMap {

	@Getter private final List<Location> spawnLocations = new ArrayList<>();

	public SpreadEventGameMap(String mapName) {
		super(mapName);
	}

	@Override
	public void teleportFighters(EventGame game) {
		int i = 0;

		Location[] locations = spawnLocations.toArray(new Location[0]);

		if (game.getGameLogic() instanceof SumoGameLogic) {
			Player participantA = ((SumoGameLogic) game.getGameLogic()).getParticipantA().getLeader().getPlayer();
			Player participantB = ((SumoGameLogic) game.getGameLogic()).getParticipantB().getLeader().getPlayer();

			if (participantA != null) {
				participantA.teleport(locations[0]);

				if (Follow.getByFollowed(participantA.getUniqueId()) != null) Follow.getByFollowed(participantA.getUniqueId()).detect();
			}
			if (participantB != null) {
				participantB.teleport(locations[1]);

				if (Follow.getByFollowed(participantB.getUniqueId()) != null) Follow.getByFollowed(participantB.getUniqueId()).detect();
			}

			VisibilityLogic.handle(participantA, participantB);
			VisibilityLogic.handle(participantB, participantA);
			return;
		}

		for (GameParticipant<GamePlayer> participant : game.getParticipants()) {
			if (game.getGameLogic() instanceof TNTTagGameLogic || game.getGameLogic() instanceof TNTRunGameLogic
			        || game.getGameLogic() instanceof SpleefGameLogic) {
				if (participant.isEliminated()) continue;
				for (GamePlayer gamePlayer : participant.getPlayers()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						player.teleport(locations[i]);

						if (Follow.getByFollowed(player.getUniqueId()) != null) Follow.getByFollowed(player.getUniqueId()).detect();

						i++;

						if (i == locations.length) i = 0;
					}
				}
			}
		}
	}

	@Override
	public boolean isSetup() {
		return spectatorPoint != null && !spawnLocations.isEmpty();
	}

	@Override
	public void save() {
		super.save();

		FileConfiguration config = Yang.get().getEventsConfig().getConfiguration();
		config.set("EVENT_MAPS." + getMapName() + ".TYPE", "SPREAD");
		config.set("EVENT_MAPS." + getMapName() + ".SPECTATOR_POINT", LocationUtil.serialize(spectatorPoint));
		List<String> list = new ArrayList<>();
		for (Location spawnLocation : spawnLocations) {
			list.add(LocationUtil.serialize(spawnLocation));
		}
		config.set("EVENT_MAPS." + getMapName() + ".SPAWN_LOCATIONS", list);

		try {
			config.save(Yang.get().getEventsConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

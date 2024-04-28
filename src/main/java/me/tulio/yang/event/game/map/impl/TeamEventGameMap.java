package me.tulio.yang.event.game.map.impl;

import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Yang;
import me.tulio.yang.event.game.EventGame;
import me.tulio.yang.event.game.map.EventGameMap;
import me.tulio.yang.event.impl.gulag.GulagGameLogic;
import me.tulio.yang.event.impl.sumo.SumoGameLogic;
import me.tulio.yang.profile.follow.Follow;
import me.tulio.yang.match.participant.GameParticipant;
import me.tulio.yang.match.participant.GamePlayer;
import me.tulio.yang.profile.visibility.VisibilityLogic;
import me.tulio.yang.utilities.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class TeamEventGameMap extends EventGameMap {

	@Getter @Setter private Location spawnPointA;
	@Getter @Setter private Location spawnPointB;

	public TeamEventGameMap(String mapName) {
		super(mapName);
	}

	@Override
	public void teleportFighters(EventGame game) {
		int locationIndex = 0;
		Location[] locations = new Location[]{ spawnPointA, spawnPointB };

		if (game.getGameLogic() instanceof SumoGameLogic) {
			GameParticipant<? extends GamePlayer>[] participants = new GameParticipant[] {
					((SumoGameLogic) game.getGameLogic()).getParticipantA(),
					((SumoGameLogic) game.getGameLogic()).getParticipantB()
			};

			for (GameParticipant<? extends GamePlayer> participant : participants) {
				int processed = 0;

				for (GamePlayer gamePlayer : participant.getPlayers()) {
					processed++;

					Player player = gamePlayer.getPlayer();

					if (player != null) {
						player.teleport(locations[locationIndex]);
						VisibilityLogic.handle(player);

						if (Follow.getByFollowed(player.getUniqueId()) != null) Follow.getByFollowed(player.getUniqueId()).detect();
					}

					if (processed == participant.getPlayers().size()) locationIndex++;
				}
			}
		} else if (game.getGameLogic() instanceof GulagGameLogic) {
			GameParticipant<GamePlayer>[] participants = new GameParticipant[] {
					((GulagGameLogic) game.getGameLogic()).getParticipantA(),
					((GulagGameLogic) game.getGameLogic()).getParticipantB()
			};

			for (GameParticipant<GamePlayer> participant : participants) {
				int processed = 0;

				for (GamePlayer gamePlayer : participant.getPlayers()) {
					processed++;

					Player player = gamePlayer.getPlayer();
					VisibilityLogic.handle(player);

					if (player != null) player.teleport(locations[locationIndex]);

					if (processed == participant.getPlayers().size()) locationIndex++;
				}
			}
		}
	}

	@Override
	public boolean isSetup() {
		return spectatorPoint != null && spawnPointA != null && spawnPointB != null;
	}

	@Override
	public void save() {
		super.save();

		FileConfiguration config = Yang.get().getEventsConfig().getConfiguration();
		config.set("EVENT_MAPS." + getMapName() + ".TYPE", "TEAM");
		config.set("EVENT_MAPS." + getMapName() + ".SPAWN_POINT_A", LocationUtil.serialize(spawnPointA));
		config.set("EVENT_MAPS." + getMapName() + ".SPAWN_POINT_B", LocationUtil.serialize(spawnPointB));

		try {
			config.save(Yang.get().getEventsConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

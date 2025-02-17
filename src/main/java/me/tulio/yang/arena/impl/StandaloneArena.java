package me.tulio.yang.arena.impl;

import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Yang;
import me.tulio.yang.arena.Arena;
import me.tulio.yang.arena.ArenaType;
import me.tulio.yang.arena.cuboid.Cuboid;
import me.tulio.yang.utilities.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class StandaloneArena extends Arena {

	private final List<Arena> duplicates = new ArrayList<>();
	@Setter private Cuboid spawnRed;
	@Setter private Cuboid spawnBlue;

	public StandaloneArena(String name, Location location1, Location location2) {
		super(name, location1, location2);
	}

	@Override
	public ArenaType getType() {
		return ArenaType.STANDALONE;
	}

	@Override
	public void save() {
//		System.out.println("STANDALONE ARENA SAVE");
		String path = "arenas." + getName();

		FileConfiguration configuration = Yang.get().getArenasConfig().getConfiguration();
		configuration.set(path, null);
		configuration.set(path + ".type", getType().name());
		configuration.set(path + ".spawnA", LocationUtil.serialize(spawnA));
		configuration.set(path + ".spawnB", LocationUtil.serialize(spawnB));
		configuration.set(path + ".cuboid.location1", LocationUtil.serialize(getLowerCorner()));
		configuration.set(path + ".cuboid.location2", LocationUtil.serialize(getUpperCorner()));
		configuration.set(path + ".kits", getKits());
		configuration.set(path + ".author", getAuthor());

		if (spawnRed != null) {
			configuration.set(path + ".spawnred.location1", LocationUtil.serialize(spawnRed.getLowerCorner()));
			configuration.set(path + ".spawnred.location2", LocationUtil.serialize(spawnRed.getUpperCorner()));
		}
		if (spawnBlue != null) {
			configuration.set(path + ".spawnblue.location1", LocationUtil.serialize(spawnBlue.getLowerCorner()));
			configuration.set(path + ".spawnblue.location2", LocationUtil.serialize(spawnBlue.getUpperCorner()));
		}

		if (!duplicates.isEmpty()) {
			int i = 0;

			for (Arena duplicate : duplicates) {
				i++;

				configuration.set(path + ".duplicates." + i + ".cuboid.location1", LocationUtil.serialize(duplicate.getLowerCorner()));
				configuration.set(path + ".duplicates." + i + ".cuboid.location2", LocationUtil.serialize(duplicate.getUpperCorner()));
				configuration.set(path + ".duplicates." + i + ".spawnA", LocationUtil.serialize(duplicate.getSpawnA()));
				configuration.set(path + ".duplicates." + i + ".spawnB", LocationUtil.serialize(duplicate.getSpawnB()));
			}
		}

		try {
			configuration.save(Yang.get().getArenasConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete() {
		super.delete();

		FileConfiguration configuration = Yang.get().getArenasConfig().getConfiguration();
		configuration.set("arenas." + getName(), null);

		try {
			configuration.save(Yang.get().getArenasConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

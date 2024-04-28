package me.tulio.yang.utilities.find.impl;

import lombok.experimental.UtilityClass;
import me.tulio.yang.Yang;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.file.type.BasicConfigurationFile;

import java.util.UUID;

@UtilityClass
public class FlatFileFinder {

    private final BasicConfigurationFile config = Yang.get().getPlayersConfig();

    public Profile findByName(String name) {
        for (String uuid : config.getConfiguration().getConfigurationSection("players").getKeys(false)) {
            if (config.getConfiguration().getString("players." + uuid + ".name").equalsIgnoreCase(name)) {
                Profile profile = new Profile(UUID.fromString(uuid));
                profile.load();
                return profile;
            }
        }
        return null;
    }

    public Profile findByUUID(UUID uuid) {
        if (config.getConfiguration().contains("players." + uuid.toString())) {
            Profile profile = new Profile(uuid);
            profile.load();
            return profile;
        }
        return null;
    }
}

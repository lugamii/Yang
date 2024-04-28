package me.tulio.yang.utilities.find;

import lombok.experimental.UtilityClass;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.file.impl.FlatFileIProfile;
import me.tulio.yang.profile.file.impl.MongoDBIProfile;
import me.tulio.yang.utilities.find.impl.FlatFileFinder;
import me.tulio.yang.utilities.find.impl.MongoDBFinder;
import org.bukkit.Bukkit;

import java.util.UUID;

@UtilityClass
public class ProfileFinder {

    public Profile findProfileByName(String name) {
        if (Bukkit.getPlayer(name) != null) {
            return Profile.get(Bukkit.getPlayer(name).getUniqueId());
        }
        Profile profile;
        if (Profile.getIProfile() instanceof MongoDBIProfile) return MongoDBFinder.findByName(name);
        else if (Profile.getIProfile() instanceof FlatFileIProfile) return FlatFileFinder.findByName(name);
        return null;
    }

    public Profile findProfileByUUID(UUID uuid) {
        if (Bukkit.getPlayer(uuid) != null) {
            return Profile.get(uuid);
        }
        if (Profile.getIProfile() instanceof MongoDBIProfile) return MongoDBFinder.findByUUID(uuid);
        else if (Profile.getIProfile() instanceof FlatFileIProfile) return FlatFileFinder.findByUUID(uuid);
        return null;
    }
}

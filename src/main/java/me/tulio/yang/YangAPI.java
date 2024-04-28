package me.tulio.yang;

import me.tulio.yang.knockback.Knockback;
import me.tulio.yang.knockback.KnockbackProfiler;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;

import java.util.UUID;

public class YangAPI {

    public static void setKnockbackProfile(KnockbackProfiler profile) {
        Knockback.setKnockbackProfiler(profile);
    }

    public boolean isInQueue(UUID uuid) {
        return Profile.get(uuid).getState() == ProfileState.QUEUEING;
    }

    public boolean isInMatch(UUID uuid) {
        return Profile.get(uuid).getState() == ProfileState.FIGHTING;
    }

    public boolean isInStaffMode(UUID uuid) {
        return Profile.get(uuid).getState() == ProfileState.STAFF_MODE;
    }

    public boolean isInSpectating(UUID uuid) {
        return Profile.get(uuid).getState() == ProfileState.SPECTATING;
    }

    public boolean isInEvent(UUID uuid) {
        return Profile.get(uuid).getState() == ProfileState.EVENT;
    }

    public boolean isInLobby(UUID uuid) {
        return Profile.get(uuid).getState() == ProfileState.LOBBY;
    }

}

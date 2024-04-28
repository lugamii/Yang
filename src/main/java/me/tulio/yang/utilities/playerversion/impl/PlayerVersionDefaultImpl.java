package me.tulio.yang.utilities.playerversion.impl;

import me.tulio.yang.utilities.playerversion.IPlayerVersion;
import me.tulio.yang.utilities.playerversion.PlayerVersion;
import org.bukkit.entity.Player;

public class PlayerVersionDefaultImpl implements IPlayerVersion {
    @Override
    public PlayerVersion getPlayerVersion(Player player) {
        return PlayerVersion.getVersionFromRaw(0);
    }
}

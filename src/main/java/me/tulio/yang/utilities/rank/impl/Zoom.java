// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.rank.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import club.frozed.core.ZoomAPI;
import java.util.UUID;
import me.tulio.yang.utilities.rank.IRank;

public class Zoom implements IRank
{
    @Override
    public String getRankSystem() {
        return "ZoomCore";
    }
    
    @Override
    public String getName(final UUID uuid) {
        return ZoomAPI.getRankName(this.getPlayer(uuid));
    }
    
    @Override
    public String getPrefix(final UUID uuid) {
        return ZoomAPI.getRankPrefix(this.getPlayer(uuid));
    }
    
    @Override
    public String getSuffix(final UUID uuid) {
        return ZoomAPI.getRankSuffix(this.getPlayer(uuid));
    }
    
    @Override
    public String getColor(final UUID uuid) {
        return ZoomAPI.getRankColor(this.getPlayer(uuid)) + ZoomAPI.getRankName(this.getPlayer(uuid));
    }
    
    @Override
    public int getWeight(final UUID uuid) {
        return 0;
    }
    
    public Player getPlayer(final UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }
}

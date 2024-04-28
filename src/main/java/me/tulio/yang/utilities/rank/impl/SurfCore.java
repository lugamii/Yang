// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.rank.impl;

import org.bukkit.ChatColor;
import com.skitbet.surfapi.SurfCoreAPI;
import java.util.UUID;
import me.tulio.yang.utilities.rank.IRank;

public class SurfCore implements IRank
{
    @Override
    public String getRankSystem() {
        return "SurfCore";
    }
    
    @Override
    public String getName(final UUID uuid) {
        return SurfCoreAPI.getInstance().getProfileManager().getProfileRankName(uuid);
    }
    
    @Override
    public String getPrefix(final UUID uuid) {
        return SurfCoreAPI.getInstance().getProfileManager().getProfileRankPrefix(uuid);
    }
    
    @Override
    public String getSuffix(final UUID uuid) {
        return SurfCoreAPI.getInstance().getProfileManager().getProfileSuffix(uuid);
    }
    
    @Override
    public String getColor(final UUID uuid) {
        return ChatColor.WHITE.toString();
    }
    
    @Override
    public int getWeight(final UUID uuid) {
        return SurfCoreAPI.getInstance().getProfileManager().getProfileRankPriority(uuid);
    }
}

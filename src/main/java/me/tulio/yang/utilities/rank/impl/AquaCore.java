// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.rank.impl;

import me.activated.core.api.player.GlobalPlayer;
import me.activated.core.api.player.PlayerData;
import me.activated.core.plugin.AquaCoreAPI;
import java.util.UUID;
import me.tulio.yang.utilities.rank.IRank;

public class AquaCore implements IRank
{
    @Override
    public String getRankSystem() {
        return "AquaCore";
    }
    
    @Override
    public String getName(final UUID uuid) {
        final PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(uuid);
        return (data == null) ? "No Data" : data.getHighestRank().getName();
    }
    
    @Override
    public String getPrefix(final UUID uuid) {
        final PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(uuid);
        return (data == null) ? "No Data" : data.getHighestRank().getPrefix();
    }
    
    @Override
    public String getSuffix(final UUID uuid) {
        final PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(uuid);
        return (data == null) ? "No Data" : data.getHighestRank().getSuffix();
    }
    
    @Override
    public String getColor(final UUID uuid) {
        final PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(uuid);
        return (data == null) ? "No Data" : (data.getHighestRank().getColor() + "");
    }
    
    @Override
    public int getWeight(final UUID uuid) {
        final GlobalPlayer data = AquaCoreAPI.INSTANCE.getGlobalPlayer(uuid);
        return (data == null) ? 0 : data.getRankWeight();
    }
}
